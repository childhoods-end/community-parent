package com.community.user.client;

import com.community.user.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", path = "/v1/user", url = "http://localhost:8083")
public interface UserClient {

    //UserController-API
    @GetMapping(path = "/find_by_id")
    User findUserById(@RequestParam int id);

    @PostMapping(path = "/register")
    Map<String, Object> register(@RequestBody User user);

    @PostMapping(path = "/activation")
    int activation(@RequestBody ActivateDto activateDto);

    @PostMapping(path = "/login")
    Map<String, Object> login(@RequestBody LoginUserDto loginUserDto);

    @GetMapping(path = "/logout")
    void logout(@RequestParam String ticket);

    @GetMapping(path = "/find_ticket")
    LoginTicket findLoginTicket(@RequestParam String ticket);

    @PostMapping(path = "/update_header")
    int updateHeader(@RequestBody NewHeaderDto newHeaderDto);

    @GetMapping(path = "/find_by_name")
    User findUserByName(@RequestParam String username);

    @GetMapping(path = "/find_my_post_count")
    long findMyPostCount(@RequestParam int userId,@RequestParam int entityType);

    @GetMapping(path = "/find_my_post")
    List<Map<String, Object>> findMyPost(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit);

    @GetMapping(path = "/find_my_reply_count")
    long findMyReplyCount(@RequestParam int userId, @RequestParam int entityType);

    @GetMapping(path = "/find_my_reply")
    List<Map<String, Object>> findMyReply(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit);

    @GetMapping(path = "/find_by_email")
    User findUserByEmail(@RequestParam String email);

    @GetMapping(path = "/get_author")
    List<String> getAuthorities(@RequestParam int userId);

    // Message-Controller-API
    @GetMapping(path = "/message/find_conversations")
    List<Message> findConversations(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit);

    @GetMapping(path = "/message/find_conversations_count")
    int findConversationCount(@RequestParam int userId);

    @GetMapping(path = "/message/find_letters")
    List<Message> findLetters(@RequestParam String conversationId, @RequestParam int offset, int limit);

    @GetMapping(path = "/message/find_letter_count")
    int findLetterCount(@RequestParam String conversationId);

    @GetMapping(path = "/message/find_letter_unread_count")
    int findLetterUnreadCount(@RequestParam int userId, @RequestParam String conversationId);

    @PostMapping(path = "/message/add")
    int addMessage(@RequestBody Message message);

    @PostMapping(path = "/message/read")
    int readMessage(@RequestBody List<Integer> ids);

    @GetMapping(path = "/message/find_latest_notice")
    Message findLatestNotice(@RequestParam int userId, @RequestParam String topic);

    @GetMapping(path = "/message/find_notice_count")
    int findNoticeCount(@RequestParam int userId, @RequestParam String topic);

    @GetMapping(path = "/message/find_notice_unread_count")
    int findNoticeUnreadCount(@RequestParam int userId, @RequestParam String topic);

    @GetMapping(path = "/message/find_notices")
    List<Message> findNotices(@RequestParam int userId, @RequestParam String topic, @RequestParam int offset, @RequestParam int limit);



    // LikeController-API
    @PostMapping(path = "/like/like")
    void like(@RequestBody LikeDto likeDto);

    @GetMapping(path = "/like/find_count")
    long findEntityLikeCount(@RequestParam int entityType,@RequestParam  int entityId);

    @GetMapping(path = "/like/find_statue")
    int findEntityLikeStatus(@RequestParam int userId, @RequestParam int entityType,@RequestParam int entityId);

    @GetMapping(path = "/like/find_count_by_id")
    int findUserLikeCount(@RequestParam int userId);

    // FollowController-API
    @GetMapping(path = "/follow/follow")
    void follow(@RequestParam int userId, @RequestParam int entityType, @RequestParam int entityId);

    @GetMapping(path = "/follow/unfollow")
    void unfollow(@RequestParam int userId, @RequestParam int entityType, @RequestParam int entityId);

    @GetMapping(path = "/follow/find_followee_count")
    long findFolloweeCount(@RequestParam int userId, @RequestParam int entityType);

    @GetMapping(path = "/follow/find_follower_count")
    long findFollowerCount(@RequestParam int entityType, @RequestParam int entityId);

    @GetMapping(path = "/follow/has_followed")
    boolean hasFollowed(@RequestParam int userId, @RequestParam int entityType, @RequestParam int entityId);

    @GetMapping(path = "/follow/find_followees")
    List<Map<String, Object>> findFollowees(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit);

    @GetMapping(path = "/follow/find_followers")
    List<Map<String, Object>> findFollowers(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit);


}
