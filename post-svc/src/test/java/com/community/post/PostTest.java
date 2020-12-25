package com.community.post;

import com.community.post.client.PostClient;
import com.community.post.controller.DiscussPostController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostApiApplication.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
@EnableFeignClients(basePackages = {"com.community.post.client"})
public class PostTest {

    @Autowired
    PostClient postClient;

    @Autowired
    DiscussPostController discussPostController;

    @Test
    public void testCache() {

        System.out.println(postClient.findDiscussPosts(0,0,10,1));

        System.out.println(discussPostController.findDiscussPosts(0,0,10,0));
    }

}
