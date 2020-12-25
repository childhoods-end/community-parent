package com.community.post.dao;

import com.community.post.dto.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 返回帖子的评论，offset和limit用于分页，
    // entityType代表是帖子还是评论还是题的评论，entityId代表具体类别下的那个数据
    // offset为页号，limit为每页回帖数
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 返回评论的数量
    int selectCountByEntity(int entityType, int entityId);

    // 增加评论
    int insertComment(Comment comment);

    Comment selectCommentById(int id);

    List<Comment> selectComments(int entityType, int userId, int offset, int limit);
}
