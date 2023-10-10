package com.buchs.fullstack.entity.dto;

import com.buchs.fullstack.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String text;
    private String name;
    private Long likeCount;
    private  UserDto userDto;
    private Set<UserDto> userLikes;

}
