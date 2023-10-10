package com.buchs.fullstack.service;

import com.buchs.fullstack.entity.UserInfo;
import com.buchs.fullstack.entity.UserInfoDetails;
import com.buchs.fullstack.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoService implements UserDetailsService {

    public UserInfoService() {
    }


    @Autowired
    private UserInfoRepository repository;


    public UserInfo getUserByName(String name) {
        return repository.findByName(name).orElseThrow();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = repository.findByName(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }


}
