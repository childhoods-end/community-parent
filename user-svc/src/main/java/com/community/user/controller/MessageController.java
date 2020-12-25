package com.community.user.controller;

import com.community.user.dto.Message;
import com.community.user.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping(path = "/message/find_conversations")
    public List<Message> findConversations(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return messageService.findConversations(userId,offset,limit);
    }

    @GetMapping(path = "/message/find_conversations_count")
    public int findConversationCount(@RequestParam int userId){
        return messageService.findConversationCount(userId);
    }

    @GetMapping(path = "/message/find_letters")
    public List<Message> findLetters(@RequestParam String conversationId, @RequestParam int offset, int limit){
        return messageService.findLetters(conversationId,offset,limit);
    }

    @GetMapping(path = "/message/find_letter_count")
    public int findLetterCount(@RequestParam String conversationId){
        return messageService.findLetterCount(conversationId);
    }

    @GetMapping(path = "/message/find_letter_unread_count")
    public int findLetterUnreadCount(@RequestParam int userId, @RequestParam String conversationId){
        return messageService.findLetterUnreadCount(userId,conversationId);
    }

    @PostMapping(path = "/message/add")
    public int addMessage(@RequestBody Message message){
        return messageService.addMessage(message);
    }

    @PostMapping(path = "/message/read")
    public int readMessage(@RequestBody List<Integer> ids){
        return messageService.readMessage(ids);
    }

    @GetMapping(path = "/message/find_latest_notice")
    public Message findLatestNotice(@RequestParam int userId, @RequestParam String topic){
        return messageService.findLatestNotice(userId,topic);
    }

    @GetMapping(path = "/message/find_notice_count")
    public int findNoticeCount(@RequestParam int userId, @RequestParam String topic){
        return messageService.findNoticeCount(userId,topic);
    }

    @GetMapping(path = "/message/find_notice_unread_count")
    public int findNoticeUnreadCount(@RequestParam int userId, @RequestParam String topic){
        return messageService.findNoticeUnreadCount(userId,topic);
    }

    @GetMapping(path = "/message/find_notices")
    public List<Message> findNotices(@RequestParam int userId, @RequestParam String topic, @RequestParam int offset, @RequestParam int limit){
        return messageService.findNotices(userId,topic,offset,limit);
    }

}
