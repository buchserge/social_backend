package com.buchs.fullstack.repo;

import com.buchs.fullstack.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TokenRepo extends JpaRepository<Token, Integer> {

    @Query("select t from Token t where t.user.id=:id")
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}