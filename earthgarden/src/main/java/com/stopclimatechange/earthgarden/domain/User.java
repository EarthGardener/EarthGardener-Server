package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stopclimatechange.earthgarden.util.RandomGenerator;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Timestamped implements UserDetails {

    @Id
    @JsonIgnore
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.stopclimatechange.earthgarden.util.RandomGenerator")
    private String id;

    @NotNull
    private String email;

    @JsonIgnore
    @NotNull
    private String pw;

    @NotNull
    private String nickname;

    @Column(nullable = true)
    private String image_url;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="tree_id")
    private Tree tree;

    @NotNull
    @OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<Post>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    public User(UserDto requestDto, Tree tree) {
        this.email = requestDto.getEmail();
        this.pw = requestDto.getPw();
        this.nickname = requestDto.getNickname();
        this.image_url = requestDto.getImage_url();
        this.roles = requestDto.getRoles();
        this.tree = tree;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}