package com.community.web.controller;

import com.community.user.client.UserClient;
import com.community.user.dto.LikeDto;
import com.community.user.dto.User;
import com.community.web.entity.Event;
import com.community.web.event.EventProducer;
import com.community.web.util.CommunityConstant;
import com.community.web.util.CommunityUtil;
import com.community.web.util.HostHolder;
import com.community.web.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private UserClient userClient;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(path = "/like", method = RequestMethod.POST)
    // 异步请求标识
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        // 获取当前用户
        User user = hostHolder.getUser();

        // 点赞
        LikeDto likeDto = LikeDto.builder()
                .userId(user.getId())
                .entityType(entityType)
                .entityId(entityId)
                .entityUserId(entityUserId)
                .build();
        userClient.like(likeDto);

        // 数量
        long likeCount = userClient.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = userClient.findEntityLikeStatus(user.getId(), entityType, entityId);
        // 返回的结果，通过map封装。包含赞的总数，和赞的状态
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }

        if(entityType == ENTITY_TYPE_POST) {
            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, postId);
        }

        // 以json格式返回
        return CommunityUtil.getJSONString(0, null, map);
    }

}
