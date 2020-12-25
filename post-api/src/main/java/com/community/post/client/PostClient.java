package com.community.post.client;

import com.community.post.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "post-service", path = "/v1/post", url = "http://localhost:8082")
public interface PostClient {

    // Comment-Controller-api
    @GetMapping(path = "/comment/find_by_entity")
    List<Comment> findCommentsByEntity(@RequestParam int entityType, @RequestParam int userId, @RequestParam int offset, @RequestParam int limit);

    @GetMapping(path = "/comment/find_count")
    int findCommentCount(@RequestParam int entityType, @RequestParam int entityId);

    @PostMapping(path = "/comment/add")
    int addComment(@RequestBody Comment comment);

    @GetMapping(path = "/comment/find_by_id")
    Comment findCommentById(@RequestParam int id);

    @GetMapping(path = "/comment/find")
    List<Comment> findComments(@RequestParam int entityType, @RequestParam int userId, @RequestParam int offset, @RequestParam int limit);

    // DiscussPost-Controller-api
    @GetMapping(path = "/find")
    List<DiscussPost> findDiscussPosts(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit, @RequestParam int orderMode);

    @PostMapping(path = "/find_rows")
    int findDiscussPostRows(@RequestBody int userId);

    @PostMapping(path = "/add")
    int addDiscussPost(@RequestBody DiscussPost post);

    @GetMapping(path = "/find_by_id")
    DiscussPost findDiscussPostById(@RequestParam int id);

    @PostMapping(path = "/update_comment_count")
    int updateCommentCount(@RequestBody UpdatePost updatePost);

    @PostMapping(path = "/update_type")
    int updateType(@RequestBody UpdatePost updatePost);

    @PostMapping(path = "/update_status")
    int updateStatus(@RequestBody UpdatePost updatePost);

    @PostMapping(path = "/update_score")
    int updateScore(@RequestBody UpdatePost updatePost);

}
