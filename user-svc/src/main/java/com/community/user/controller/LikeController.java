package com.community.user.controller;

import com.community.user.dto.LikeDto;
import com.community.user.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping(path = "/like/like")
    public void like(@RequestBody LikeDto likeDto){
        likeService.like(likeDto.getUserId(),likeDto.getEntityType(),likeDto.getEntityId(),likeDto.getEntityUserId());
    }

    @GetMapping(path = "/like/find_count")
    public long findEntityLikeCount(@RequestParam int entityType,@RequestParam  int entityId){
        return likeService.findEntityLikeCount(entityType,entityId);
    }

    @GetMapping(path = "/like/find_statue")
    public int findEntityLikeStatus(@RequestParam int userId, @RequestParam int entityType,@RequestParam int entityId){
        return likeService.findEntityLikeStatus(userId,entityType,entityId);
    }

    @GetMapping(path = "/like/find_count_by_id")
    public int findUserLikeCount(@RequestParam int userId){
        return likeService.findUserLikeCount(userId);
    }
}
