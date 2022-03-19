package com.stopclimatechange.earthgarden.domain;

import com.stopclimatechange.earthgarden.util.CheckList;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    public static class PostInfoDto{

        public String id;
        public String title;
        public Integer date;
        public Integer exp;
        public Integer checklist_1;
        public Integer checklist_2;
        public Integer checklist_3;

        public PostInfoDto(Post post){
            id = post.getId();
            title = post.getTitle();
            checklist_1 = post.getChecklist_1();
            checklist_2 = post.getChecklist_2();
            checklist_3 = post.getChecklist_3();
        }

        public void setDate(Integer date) {
            this.date = date;
        }


        public void setExp(Integer exp) {
            this.exp = exp;
        }
    }
}
