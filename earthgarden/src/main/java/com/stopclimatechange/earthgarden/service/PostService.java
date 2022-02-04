package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.*;
import com.stopclimatechange.earthgarden.repository.PostRepository;
import com.stopclimatechange.earthgarden.util.CheckList;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private  final TreeService treeService;
    private final ImageUploadService imageUploadService;
    private final PostRepository postRepository;

    public List<PostDto> findFitPost(User user, String date){
        Integer year = Integer.parseInt(handlingDate(date)[0]);
        Integer month = Integer.parseInt(handlingDate(date)[1]);
        List<Post> allPost = user.getPosts();
        List<PostDto> fitPosts = new ArrayList<PostDto>();
        for(Post post : allPost){
            if(post.getCreatedAt().getYear() == year && post.getCreatedAt().getMonthValue() == month){
                PostDto postDto = new PostDto(post);
                fitPosts.add(postDto);
            }
        }
        //##정렬해야대?
        return fitPosts;
    }

    public List<CheckMent> chooseMents(){
        List<CheckMent> checkMents = new ArrayList<>();
        for(int i = 0 ; i < 5; i++){
            Integer num = Integer.valueOf(((int)(Math.random() * 18) % 6) +1);
            String ment = CheckList.checkList.get(i*6+num);
            CheckMent checkMent = new CheckMent(i*6+num, ment);
            checkMents.add(checkMent);
        }
        return checkMents;
    }

    public void posting(User user, String title,
                        Integer checklist_1, Integer checklist_2, Integer checklist_3,
                        MultipartFile image_1, MultipartFile image_2, MultipartFile image_3) {
        Integer[] checklist = new Integer[] {
                checklist_1, checklist_2,checklist_3
        };
        MultipartFile[] images = new MultipartFile[]{
                image_1,image_2,image_3
        };
        Integer exp = countExp(checklist);
        treeService.updateExp(user, exp);

        List<Post> posts = user.getPosts();
        PostDto postDto = new PostDto(title, exp, checklist_1, checklist_2, checklist_3);
        Post post = new Post(postDto, checklist_1, checklist_2,checklist_3, user);
        posts.add(post);
        postRepository.save(post);
        savePostImages(post, images);
        userService.saveUpdatedUser(user);


    }

    private String[] handlingDate(String date){
        return  date.split("-");
    }

    private Integer countExp(Integer[] checklist){
        Integer exp = 0;

        for(Integer i : checklist){

            if(!(i == null || i == 0)){
                exp+= ((i-1)/6 +2)*50;
            }
        }
        return exp;
    }

    private void savePostImages(Post post, MultipartFile[] images){
        List<PostImage> postImages = post.getPostImages();
        for(MultipartFile image : images){
            if(!image.isEmpty()){
                PostImage postImage = new PostImage(imageUploadService.restore(image),post);
                postImages.add(postImage);
            }
        }
    }

}
