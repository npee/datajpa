package com.npee.myproject.service;

import com.npee.myproject.domain.entity.User;
import com.npee.myproject.domain.entity.dto.ResponseUserDto;

import java.util.List;

public interface UserService {
    // TODO: Entity가 준비되면 해당 Entity의 List를 반환하도록 수정
    List<ResponseUserDto> findAllUsers();
    ResponseUserDto findUser(Long id);
    Long save(User user);
    void deleteUser(Long id);

    List<ResponseUserDto> findAllUsersV2();
    ResponseUserDto findUserV2(Long id);
    Long saveV2(User user);
    void deleteUserV2(Long id);
}
