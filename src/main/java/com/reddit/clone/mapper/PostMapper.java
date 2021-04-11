package com.reddit.clone.mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.reddit.clone.dto.PostRequest;
import com.reddit.clone.dto.PostResponse;
import com.reddit.clone.model.*;
import com.reddit.clone.repository.CommentRepository;
import com.reddit.clone.repository.VoteRepository;
import com.reddit.clone.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostMapper {

    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final VoteRepository voteRepository;

    public Post mapDtoToPost(PostRequest postRequest, Subreddit subreddit, User user) {

        return Post.builder()
                .postId(postRequest.getPostId())
                .postName(postRequest.getName())
                .description(postRequest.getDescription())
                .subreddit(subreddit)
                .user(user)
                .createdDate(Instant.now())
                .url(postRequest.getUrl())
                .voteCount(0)
                .build();
    }

    public PostResponse mapPostToDto(Post post){
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .voteCount(post.getVoteCount())
                .commentCount(commentRepository.findByPost(post).size())
                .duration(post.getCreatedDate().toEpochMilli()+"")
                .upVote(isPostUpVoted(post))
                .downVote(isPostDownVoted(post))
                .build();
    }


    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authenticationService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authenticationService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
