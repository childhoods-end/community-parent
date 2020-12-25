package com.community.post.controller;

import com.community.post.dto.DiscussPost;
import com.community.post.dto.UpdatePost;
import com.community.post.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/post")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @PostMapping(path = "/find")
    public List<DiscussPost> findDiscussPosts(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit, @RequestParam int orderMode){
        return discussPostService.findDiscussPosts(userId,offset,limit,orderMode);
    }

    @PostMapping(path = "/find_rows")
    public int findDiscussPostRows(@RequestBody int userId){
        return discussPostService.findDiscussPostRows(userId);
    }

    @PostMapping(path = "/add")
    public int addDiscussPost(@RequestBody DiscussPost post){
        return discussPostService.addDiscussPost(post);
    }

    @GetMapping(path = "/find_by_id")
    public DiscussPost findDiscussPostById(@RequestParam int id){
        return discussPostService.findDiscussPostById(id);
    }

    @PostMapping(path = "/update_comment_count")
    public int updateCommentCount(@RequestBody UpdatePost updatePost){
        return discussPostService.updateCommentCount(updatePost.getId(),updatePost.getCommentCount());
    }

    @PostMapping(path = "/update_type")
    public int updateType(@RequestBody UpdatePost updatePost){
        return discussPostService.updateType(updatePost.getId(),updatePost.getType());
    }

    @PostMapping(path = "/update_status")
    public int updateStatus(@RequestBody UpdatePost updatePost){
        return discussPostService.updateStatus(updatePost.getId(),updatePost.getStatus());
    }

    @PostMapping(path = "/update_score")
    public int updateScore(@RequestBody UpdatePost updatePost){
        return discussPostService.updateScore(updatePost.getId(),updatePost.getScore());
    }
}
