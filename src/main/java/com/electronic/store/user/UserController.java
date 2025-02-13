package com.electronic.store.user;

import com.electronic.store.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;

import static java.io.File.separator;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getOneUser(Authentication connectedUser) {
        return userService.getUser(connectedUser.getName());
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String deleteUser(@PathVariable String userId) throws IOException {
        userService.deleteUser(userId);
        return "User Deleted Successfully";
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAll(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                @RequestParam(defaultValue = "5",required = false) int pageSize,
                                @RequestParam(defaultValue = "email",required = false) String field){
        return userService.getAllUsers(pageNumber,pageSize,field);
    }

    @GetMapping("/search-user/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> searchUserWithUsername(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                @RequestParam(defaultValue = "5",required = false) int pageSize,
                                                @RequestParam(defaultValue = "email",required = false) String field,
                                                @PathVariable String username){
        return userService.findByUserName(username,pageNumber,pageSize,field);
    }

    @GetMapping("/get-email/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserDto getByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/image/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String imageUpload(@RequestPart("file") MultipartFile file,
                              @PathVariable String userId) throws IOException {
        final String fileUploadSubPath = "users" + separator + userId;
        fileService.uploadFile(file,fileUploadSubPath);
        return "file uploaded successfully";
    }

    @GetMapping("/serve-image/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Resource> serveImage(@PathVariable String userId) throws IOException {
        final String fileUploadSubPath = "users" + separator + userId;
        Resource file = fileService.loadLatestFile(userId,fileUploadSubPath);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
