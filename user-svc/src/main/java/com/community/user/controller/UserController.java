package com.community.user.controller;

import com.community.user.dao.UserMapper;
import com.community.user.dto.*;
import com.community.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping(path = "/find_by_id")
    public User findUserById(@RequestParam int id){
        return userService.findUserById(id);
    }

    @GetMapping(path = "/find_by_email")
    public User findUserByEmail(@RequestParam String email){
        return userMapper.selectByEmail(email);
    }

    @PostMapping(path = "/register")
    public Map<String, Object> register(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping(path = "/activation")
    public int activation(@RequestBody ActivateDto activateDto){
        return userService.activation(activateDto.getUserId(),activateDto.getCode());
    }

    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestBody LoginUserDto loginUserDto){
        return userService.login(loginUserDto.getUsername(),loginUserDto.getPassword(),loginUserDto.getExpiredSeconds());
    }

    @GetMapping(path = "/logout")
    public void logout(@RequestParam String ticket){
        userService.logout(ticket);
    }

    @GetMapping(path = "/find_my_post_count")
    long findMyPostCount(@RequestParam int userId,@RequestParam int entityType){
        return userService.findMyPostCount(userId,entityType);
    }

    @GetMapping(path = "/find_my_post")
    List<Map<String, Object>> findMyPost(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return userService.findMyPost(userId,offset,limit);
    }

    @GetMapping(path = "/find_my_reply_count")
    long findMyReplyCount(@RequestParam int userId, @RequestParam int entityType){
        return userService.findMyReplyCount(userId,entityType);
    }

    @GetMapping(path = "/find_my_reply")
    List<Map<String, Object>> findMyReply(@RequestParam int userId, @RequestParam int offset, @RequestParam int limit){
        return userService.findMyReply(userId,offset,limit);
    }


    @GetMapping(path = "/find_ticket")
    public LoginTicket findLoginTicket(@RequestParam String ticket){
        return userService.findLoginTicket(ticket);
    }

    @PostMapping(path = "/update_header")
    public int updateHeader(@RequestBody NewHeaderDto newHeaderDto){
        return userService.updateHeader(newHeaderDto.getUserId(),newHeaderDto.getHeaderUrl());
    }

    @GetMapping(path = "/find_by_name")
    public User findUserByName(@RequestParam String username){
        return userService.findUserByName(username);
    }

    @GetMapping(path = "/get_author")
    public List<String> getAuthorities(@RequestParam int userId){
        return userService.getAuthorities(userId);
    }
}
