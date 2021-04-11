package com.reddit.clone.mapper;


import com.reddit.clone.dto.SubredditDto;
import com.reddit.clone.model.Subreddit;
import org.springframework.stereotype.Service;

@Service
public class SubredditMapper {

    public SubredditDto mapSubredditToDto(Subreddit subreddit){
        return SubredditDto.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    public Subreddit mapDtoToSubreddit(SubredditDto subredditDto){
        return Subreddit.builder()
                .id(subredditDto.getId())
                .name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }
}
