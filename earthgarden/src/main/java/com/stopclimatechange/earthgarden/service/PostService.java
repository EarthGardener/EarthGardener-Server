package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.Post;
import com.stopclimatechange.earthgarden.domain.PostDto;
import com.stopclimatechange.earthgarden.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    public List<PostDto> findFitPost(User user, String date){
        Integer year = Integer.parseInt(handlingDate(date)[0]);
        Integer month = Integer.parseInt(handlingDate(date)[1]);
        List<Post> allPost = user.getPosts();
        List<PostDto> fitPosts = new ArrayList<PostDto>();
        for(Post post : allPost){
            if(post.getCreatedAt().getDayOfYear() == year && post.getCreatedAt().getDayOfMonth() == month){
                PostDto postDto = new PostDto(post);
                fitPosts.add(postDto);
            }
        }
        //##정렬해야대?
        return fitPosts;
    }

    private String[] handlingDate(String date){
        return  date.split("-");

    }
}
