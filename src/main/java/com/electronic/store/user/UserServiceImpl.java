package com.electronic.store.user;

import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.file.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final FileService fileService;

    @Override
    public UserDto getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public Page<UserDto> getAllUsers(int pageNumber, int pageSize, String field) {
        try {
            Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
            return userRepository.findAll(pageRequest)
                    .map(user -> mapper.map(user, UserDto.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @Override
    public Page<UserDto> findByUserName(String userName, int pageNumber, int pageSize, String field) {
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return userRepository.findByFirstname(userName,pageRequest)
                .map(user -> mapper.map(user, UserDto.class));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        fileService.deleteUserFolder(userId);
        userRepository.delete(user);
    }

}
