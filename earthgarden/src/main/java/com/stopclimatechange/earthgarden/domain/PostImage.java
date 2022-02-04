package com.stopclimatechange.earthgarden.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    private String image_url;

    @ManyToOne
    @JoinColumn(name ="post_id")
    private Post post;

    public PostImage (String image_url, Post post){
        this.image_url = image_url;
        this.post = post;
    }
}
