package com.stopclimatechange.earthgarden.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeDto {

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

    public TreeDto(Tree tree){
        this.name = tree.getName();
        this.level = tree.getLevel();
        this.exp = tree.getExp();
        this.total_sum = tree.getTotal_sum();
        this.month_sum = tree.getMonth_sum();
    }

    @Getter
    @Setter
    public static class NameDto{

        @NotNull
        private String name;
    }
}
