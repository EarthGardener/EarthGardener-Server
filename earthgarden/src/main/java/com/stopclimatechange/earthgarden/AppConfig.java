package com.stopclimatechange.earthgarden;

import com.stopclimatechange.earthgarden.repository.UserRepository;
import com.stopclimatechange.earthgarden.service.ImageUploadService;
import com.stopclimatechange.earthgarden.service.UserService;
import com.stopclimatechange.earthgarden.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploadService imageUploadService;

    @Bean
    public UserService signUpService(){
        return new UserServiceImpl(userRepository, passwordEncoder, imageUploadService);
    }

}
