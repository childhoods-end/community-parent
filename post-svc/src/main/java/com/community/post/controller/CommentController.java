package com.community.post.controller;

import com.community.post.dto.Comment;
import com.community.post.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/post/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping(path = "/find_by_entity")
    public List<Comment> findCommentsByEntity(@RequestParam int entityType, @RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return commentService.findCommentsByEntity(entityType,userId,offset,limit);
    }

    @GetMapping(path = "/find_count")
    public int findCommentCount(@RequestParam int entityType, @RequestParam int entityId){
        return commentService.findCommentCount(entityType,entityId);
    }

    @PostMapping(path = "/add")
    public int addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping(path = "/find_by_id")
    public Comment findCommentById(@RequestParam int id){
        return commentService.findCommentById(id);
    }

    @GetMapping(path = "/find")
    public List<Comment> findComments(@RequestParam int entityType, @RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return commentService.findComments(entityType,userId,offset,limit);
    }
}
