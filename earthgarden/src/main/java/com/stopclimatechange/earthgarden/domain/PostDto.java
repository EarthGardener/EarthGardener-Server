package com.stopclimatechange.earthgarden.domain;

import com.stopclimatechange.earthgarden.util.CheckList;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostDto {
    @NotNull
    private String title;

    @NotNull
    private Integer exp;

    private List<String> checklist = new ArrayList<>();

    public PostDto(Post post){
        this.title = post.getTitle();
        this.exp = post.getExp();
        if(post.getChecklist_1()!=null)
            this.checklist.add(CheckList.checkList.get(post.getChecklist_1()));
        if(post.getChecklist_2()!=null)
            this.checklist.add(CheckList.checkList.get(post.getChecklist_2()));
        if(post.getChecklist_3()!=null)
            this.checklist.add(CheckList.checkList.get(post.getChecklist_3()));
    }
}
