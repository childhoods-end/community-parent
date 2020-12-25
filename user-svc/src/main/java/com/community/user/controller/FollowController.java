package com.community.user.controller;

import com.community.user.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping(path = "/follow/follow")
    public void follow(@RequestParam int userId, @RequestParam int entityType, @RequestParam int entityId){
        followService.follow(userId,entityType,entityId);
    }

    @GetMapping(path = "/follow/unfollow")
    public void unfollow(@RequestParam int userId, @RequestParam int entityType, @RequestParam int entityId){
        followService.unfollow(userId,entityType,entityId);
    }

    @GetMapping(path = "/follow/find_followee_count")
    public long findFolloweeCount(@RequestParam int userId, @RequestParam int entityType){
        return followService.findFolloweeCount(userId,entityType);
    }

    @GetMapping(path = "/follow/find_follower_count")
    public long findFollowerCount(@RequestParam int entityType, @RequestParam int entityId){
        return followService.findFollowerCount(entityType,entityId);
    }

    @GetMapping(path = "/follow/has_followed")
    public boolean hasFollowed(@RequestParam int userId, @RequestParam int entityType, @RequestParam int entityId){
        return followService.hasFollowed(userId,entityType,entityId);
    }

    @GetMapping(path = "/follow/find_followees")
    public List<Map<String, Object>> findFollowees(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return followService.findFollowees(userId,offset,limit);
    }

    @GetMapping(path = "/follow/find_followers")
    public List<Map<String, Object>> findFollowers(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return followService.findFollowers(userId,offset,limit);
    }

}
