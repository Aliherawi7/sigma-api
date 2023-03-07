package com.herawi.sigma.controllers;

import com.herawi.sigma.models.Account;
import com.herawi.sigma.services.AccountService;
import com.herawi.sigma.services.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;
    private final AccountService accountService;

    public FileController(FileStorageService fileStorageService,
                          AccountService accountService){
        this.fileStorageService = fileStorageService;
        this.accountService = accountService;
    }

    @GetMapping("profile-picture/{username}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username){
        byte[] image = fileStorageService.getProfileImage(username);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

}
