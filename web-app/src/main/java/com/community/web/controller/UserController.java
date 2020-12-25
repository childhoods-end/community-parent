package com.community.web.controller;


import com.community.post.client.PostClient;
import com.community.post.dto.Comment;
import com.community.post.dto.DiscussPost;
import com.community.user.client.UserClient;
import com.community.user.dto.NewHeaderDto;
import com.community.web.annotation.LoginRequired;
import com.community.user.dto.User;
import com.community.web.entity.Page;
import com.community.web.util.CommunityConstant;
import com.community.web.util.CommunityUtil;
import com.community.web.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserClient userClient;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private PostClient postClient;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${quniu.bucket.header.url}")
    private String headerBucketUrl;


    // 需要拦截标志
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model) {
        // 上传文件名称
        String fileName = CommunityUtil.generateUUID();
        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);
        return "/site/setting";
    }

    // 更新头像路径
    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "文件名不能为空!");
        }

        String url = headerBucketUrl + "/" + fileName;
        NewHeaderDto newHeaderDto = new NewHeaderDto();
        newHeaderDto.setUserId(hostHolder.getUser().getId());
        newHeaderDto.setHeaderUrl(url);
        userClient.updateHeader(newHeaderDto);

        return CommunityUtil.getJSONString(0);
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        // 从最后一个点截取字符串，.png
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        NewHeaderDto newHeaderDto = new NewHeaderDto();
        newHeaderDto.setUserId(user.getId());
        newHeaderDto.setHeaderUrl(headerUrl);
        userClient.updateHeader(newHeaderDto);

        return "redirect:/index";
    }



    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }

    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userClient.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = userClient.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = userClient.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = userClient.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = userClient.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);


        return "/site/profile";
    }

    @RequestMapping(path = "/my-post/{userId}", method = RequestMethod.GET)
    public String getMyPosts(@PathVariable("userId") int userId, Page page, Model model,
                             @RequestParam(name = "orderMode", defaultValue = "0") int orderMode) {
        User user = userClient.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);


        page.setLimit(5);
        page.setPath("/my-post/" + userId);
        page.setRows((int) userClient.findMyPostCount(userId, ENTITY_TYPE_POST));

        List<DiscussPost> list = postClient.findDiscussPosts(userId, page.getOffset(), page.getLimit() , orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                // 装入帖子
                map.put("post", post);


                // 用于首页查找帖子信息时顺便查赞的信息
                long likeCount = userClient.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        // 装入模型，能调用
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);

        return "/site/my-post";
    }

    @RequestMapping(path = "/my-reply/{userId}", method = RequestMethod.GET)
    public String getMyReplies(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userClient.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);


        page.setLimit(5);
        page.setPath("/my-reply/" + userId);
        page.setRows((int) userClient.findMyReplyCount(userId, ENTITY_TYPE_POST));
        // 回复列表
        //List<Comment> replyList = commentService.findCommentsById(
                //ENTITY_TYPE_COMMENT, userId, 0, Integer.MAX_VALUE);
        FindComments findComments = new FindComments();
        findComments.setEntityType(ENTITY_TYPE_POST);
        findComments.setUserId(userId);
        findComments.setOffset(page.getOffset());
        findComments.setLimit(page.getLimit());

        List<Comment> list = postClient.findComments(findComments );
        List<Map<String, Object>> comments = new ArrayList<>();
        if (list != null) {
            for (Comment comment : list) {
                Map<String, Object> map = new HashMap<>();
                // 装入回复
                map.put("comment", comment);

                DiscussPost post = postClient.findDiscussPostById(comment.getEntityId());
                map.put("post", post);
                // 用于首页查找帖子信息时顺便查赞的信息
                long likeCount = userClient.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                map.put("likeCount", likeCount);

                comments.add(map);
            }
        }
        // 装入模型，能调用
        model.addAttribute("comments", comments);


        return "/site/my-reply";
    }

}
