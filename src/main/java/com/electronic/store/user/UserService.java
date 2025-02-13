package com.electronic.store.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    UserDto getUser(String userId);
    Page<UserDto> getAllUsers(int pageNumber, int pageSize, String field);
    Page<UserDto> findByUserName(String userName, int pageNumber, int pageSize, String field);
    UserDto getUserByEmail(String email);
    void deleteUser(String userId) throws IOException;
}
