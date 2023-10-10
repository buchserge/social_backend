package com.buchs.fullstack.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.support.PagedListHolder;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessagesWrapper {
    private String userName;
    private PagedListHolder messages;

}
