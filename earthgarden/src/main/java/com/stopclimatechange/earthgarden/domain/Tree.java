package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stopclimatechange.earthgarden.util.RandomGenerator;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
public class Tree extends Timestamped{

    @Id
    @JsonIgnore
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.stopclimatechange.earthgarden.util.RandomGenerator")
    private String id;

    @NotEmpty
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

    public void updateByTreeDto(TreeDto treeDto){
        name = treeDto.getName();
        level = treeDto.getLevel();
        exp = treeDto.getExp();
        total_sum = treeDto.getTotal_sum();
        month_sum = treeDto.getMonth_sum();
    }
}
