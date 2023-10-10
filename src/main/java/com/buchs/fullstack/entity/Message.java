package com.buchs.fullstack.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@JsonIdentityReference(alwaysAsId = true)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String name;
    private Long likeCount = 0L;
    @OneToMany(mappedBy = "message", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
    @ManyToMany
    @JoinTable(name = "message_user", joinColumns = {@JoinColumn(name = "message_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<UserInfo> userLikes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserInfo user;



    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", text='" + text + '\'' + ", name='" + name + '\'' + ", comments=" + comments + '}';
    }
}
