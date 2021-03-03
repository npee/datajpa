package com.npee.myproject.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UserNameOnly {

    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
