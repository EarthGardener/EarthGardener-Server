package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CheckMent {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String ment;

    @AllArgsConstructor
    @Getter
    public static class CheckMentAndExp{

        @JsonProperty
        private Integer exp;

        @JsonProperty
        private String ment;
    }
}

