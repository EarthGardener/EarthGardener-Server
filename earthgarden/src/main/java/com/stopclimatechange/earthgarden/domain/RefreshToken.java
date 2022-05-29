package com.stopclimatechange.earthgarden.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "refresh_token")
    private String refreshToken;

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
