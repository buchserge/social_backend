package com.buchs.fullstack.repo;

import com.buchs.fullstack.entity.Message;
import com.buchs.fullstack.entity.Token;
import com.buchs.fullstack.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
    Optional<UserInfo> findByName(String username);

}
