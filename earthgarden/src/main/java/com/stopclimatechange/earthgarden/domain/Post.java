package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stopclimatechange.earthgarden.util.RandomGenerator;
import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Getter
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



}
