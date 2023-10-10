package com.buchs.fullstack.repo;

import com.buchs.fullstack.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
//    @Query("select u from UserInfo u where u.message.id=:id")
//    List<Message> findAllMessagesByUser(Long id);

}
