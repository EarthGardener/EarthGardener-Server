package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stopclimatechange.earthgarden.util.RandomGenerator;
import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
public class Tree extends Timestamped{

    @Id
    @JsonIgnore
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.stopclimatechange.earthgarden.util.RandomGenerator")
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Integer level;

    @NotNull
    private Integer exp;

    @NotNull
    private Integer total_sum;

    @NotNull
    private Integer month_sum;

    public Tree(){
        name = "tree";
        level = 1;
        exp = 0;
        total_sum = 0;
        month_sum = 0;
    }

    public void updateTreeName(String name){
        this.name = name;
    }
}