package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.*;
import com.stopclimatechange.earthgarden.repository.PostRepository;
import com.stopclimatechange.earthgarden.util.CheckList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private  final TreeService treeService;
    private final ImageUploadService imageUploadService;
    private final PostRepository postRepository;

    public HashMap<Integer, PostDto> findFitPost(User user, String date){

        Integer year = Integer.parseInt(handlingDate(date)[0]);
        Integer month = Integer.parseInt(handlingDate(date)[1]);

        LocalDateTime startDateTime = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDateTime = startDateTime.plusMonths(1);

        List<Post> allPost = postRepository.findAllByCreatedAtBetween(startDateTime, endDateTime);

        HashMap<Integer, PostDto> fitPosts = new HashMap<Integer, PostDto>();
        for(Post post : allPost){
            PostDto postDto = new PostDto(post);
            Integer postedDate = post.getCreatedAt().getDayOfMonth();
            fitPosts.put(postedDate, postDto);
        }
        //##정렬해야대?
        return fitPosts;
    }

    public Boolean checkTodayWrited(User user){
        List<Post> allPost = postRepository.findAllByCreatedAtBetween(LocalDate.now().atStartOfDay(), LocalDateTime.now());
        if(allPost.size() == 0){
            return false;
        }
        else
            return true;
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
                        String checklist_1, String checklist_2, String checklist_3,
                        MultipartFile image_1, MultipartFile image_2, MultipartFile image_3) {

        Integer[] checklist = changeChecklistIdToString(checklist_1, checklist_2, checklist_3);
        MultipartFile[] images = new MultipartFile[]{
                image_1,image_2,image_3
        };
        Integer exp = countExp(checklist);
        treeService.updateExp(user, exp);

        List<Post> posts = user.getPosts();
        PostDto postDto = new PostDto(title, exp, checklist);
        Post post = new Post(postDto, checklist, user);
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

    private Integer[] changeChecklistIdToString(String checkList_1, String checkList_2, String checkList_3){
        Integer checklist_int_1;
        Integer checklist_int_2;
        Integer checklist_int_3;

        if(checkList_1.length() != 0)
            checklist_int_1=Integer.parseInt(checkList_1);
        else
            checklist_int_1= 0;

        if(checkList_2.length() != 0)
            checklist_int_2=Integer.parseInt(checkList_2);
        else
            checklist_int_2= 0;

        if(checkList_3.length() != 0)
            checklist_int_3=Integer.parseInt(checkList_3);
        else
            checklist_int_3= 0;

        Integer[] checklist_Int = new Integer[]{
                checklist_int_1, checklist_int_2, checklist_int_3
        };
        return checklist_Int;
    }

}
