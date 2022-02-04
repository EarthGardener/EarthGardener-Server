package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stopclimatechange.earthgarden.util.RandomGenerator;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped{

    @Id
    @JsonIgnore
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.stopclimatechange.earthgarden.util.RandomGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @NotNull
    private String title;

    @NotNull
    private Integer exp;

    private Integer checklist_1;

    private Integer checklist_2;

    private Integer checklist_3;

    @NotNull
    @OneToMany(mappedBy="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    public Post(PostDto postDto, Integer checklist_1, Integer checklist_2, Integer checklist_3, User user){
        this.title = postDto.getTitle();
        this.exp = postDto.getExp();
        this.checklist_3 = checklist_3;
        this.checklist_2 = checklist_2;
        this.checklist_1 = checklist_1;
        this.user = user;
    }

}
