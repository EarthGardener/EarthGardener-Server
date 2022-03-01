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
        this.checklist.add(CheckList.checkList.get(post.getChecklist_1()));
        this.checklist.add(CheckList.checkList.get(post.getChecklist_2()));
        this.checklist.add(CheckList.checkList.get(post.getChecklist_3()));
    }

    public PostDto(String title, Integer exp, Integer[] checklist){
        this.title = title;
        this.exp = exp;
        this.checklist.add(CheckList.checkList.get(checklist[0]));
        this.checklist.add(CheckList.checkList.get(checklist[1]));
        this.checklist.add(CheckList.checkList.get(checklist[2]));
    }
}
