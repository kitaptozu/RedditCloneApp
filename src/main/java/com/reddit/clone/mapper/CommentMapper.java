package com.reddit.clone.mapper;

import com.reddit.clone.dto.CommentsDto;
import com.reddit.clone.model.Comment;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;

import java.time.Instant;

public class CommentMapper {

    public Comment mapDtoToComment(CommentsDto commentsDto, Post post, User user) {

        return Comment.builder()
                .id(commentsDto.getId())
                .text(commentsDto.getText())
                .post(post)
                .createdDate(Instant.now())
                .user(user)
                .build();
    }

    public CommentsDto mapCommentToDto(Comment comment) {

        return CommentsDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getText())
                .username(comment.getUser().getUsername())
                .build();
    }
}
