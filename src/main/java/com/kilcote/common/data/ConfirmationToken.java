package com.kilcote.common.data;

import java.util.Date;
import java.util.UUID;

import com.kilcote.entity.system.User;

import lombok.Data;

@Data
public class ConfirmationToken {

    private Long id;
    private String confirmationToken;
    private Date createdDate;
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }
}