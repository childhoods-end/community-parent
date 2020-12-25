package com.community.user.service;


import com.community.mail.client.MailClient;
import com.community.mail.dto.EmailRequest;
import com.community.user.dao.UserMapper;
import com.community.user.dto.LoginTicket;
import com.community.user.dto.User;
import com.community.user.util.CommunityConstant;
import com.community.user.util.CommunityUtil;
import com.community.user.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

@ComponentScan("com.community")
@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;



    @Autowired
    private UserService userService;

    // 域名 储存在 application.properties 中
    @Value("http://localhost:8083")
    private String domain;



    //@Autowired
    //private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public User findUserById(int id) {
        // return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            // 往 map 中装入提示信息
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        // 创建默认头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8083/v1/user/activation/101/code
        String url = domain+ "/v1/user/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        TemplateEngine templateEngine = new TemplateEngine();
        String content = templateEngine.process("/mail/activation", context);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("激活账号");
        emailRequest.setContext(content);
        mailClient.send(emailRequest);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        // 进行加密覆盖成md5密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        // 设定过期时间
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        // 插入登录凭证
        // loginTicketMapper.insertLoginTicket(loginTicket);

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        // loginTicket对象被自动转化为 json字符串
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        // 准备返回给客户端，让客户端用来保持登录状态
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        // loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        // 取出 ticket再修改状态。只是通过修改状态来设置有效，一直保存数据，以便以后查询日期等功能的实现。
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);

    }

    public LoginTicket findLoginTicket(String ticket) {
        // return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);

    }

    public int updateHeader(int userId, String headerUrl) {
        // return userMapper.updateHeader(userId, headerUrl);
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    public int updatePassword(int userId, String password) {
        return userMapper.updatePassword(userId, password);
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    // 查询发布帖子的实体的数量
    public long findMyPostCount(int userId, int entityType) {
        String myPostKey = RedisKeyUtil.getMyPostKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(myPostKey);
    }

    // 查询某用户发布的帖子
    public List<Map<String, Object>> findMyPost(int userId, int offset, int limit) {
        String myPostKey = RedisKeyUtil.getMyPostKey(userId, ENTITY_TYPE_POST);
        // 倒序查找
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(myPostKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user", user);
            // 时间查询，需要转化
            Double score = redisTemplate.opsForZSet().score(myPostKey , targetId);
            map.put("postTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

    // 查询发布帖子的实体的数量
    public long findMyReplyCount(int userId, int entityType) {
        String myReplyKey = RedisKeyUtil.getMyReplyKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(myReplyKey);
    }

    // 查询某用户发布的帖子
    public List<Map<String, Object>> findMyReply(int userId, int offset, int limit) {
        String myReplyKey = RedisKeyUtil.getMyReplyKey(userId, ENTITY_TYPE_COMMENT);
        // 倒序查找
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(myReplyKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user", user);
            // 时间查询，需要转化
            Double score = redisTemplate.opsForZSet().score(myReplyKey , targetId);
            map.put("replyTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

    public List<String> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<String> list = new ArrayList<>();
        switch (user.getType()) {
            case 1:
                list.add("admin");
            case 2:
                list.add("moderator");
            default:
                list.add("user");
        }

        return list;
    }

}
