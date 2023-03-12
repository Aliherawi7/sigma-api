package com.herawi.sigma.services;

import com.herawi.sigma.exceptions.ProfileImageNotFoundException;
import com.herawi.sigma.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths
                .get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        System.out.println(fileStorageProperties.getUploadDir());
        try{
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            System.out.println("Couldn't create the directory where the upload files will be saved." + e);
        }
    }

    /*
    * store the file in the profile picture directory
    * */
    public void storeFile(MultipartFile multipartFile, String username) throws IOException {
        if(multipartFile == null || username == null) return;
        // Normalize file name
        String fileName = StringUtils.getFilename(multipartFile.getOriginalFilename());

        // Check if the file's name contains valid  characters or not
        assert fileName != null;
        if (fileName.contains("..")) {
            throw new RuntimeException("Sorry! File name which contains invalid path sequence " + fileName);
        }
        String extension = multipartFile.getOriginalFilename().split("\\.")[1];
        Path targetLocation = this.fileStorageLocation.resolve(username+"."+extension);
        Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    /*
    * get the file by user id from the profile picture directory
    *
    * */
    public byte[] getProfileImage(String username) throws ProfileImageNotFoundException {
        File image = Stream.of(Objects.requireNonNull(new File(fileStorageLocation.toUri()).listFiles()))
                .filter(item -> item.getName().split("\\.")[0].equalsIgnoreCase(username))
                .findFirst().orElse(null);

        if(image == null){
            throw new ProfileImageNotFoundException("file not found with provided username");
        }
        byte[] imageBytes = new byte[(int)image.length()];
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(image);
            inputStream.read(imageBytes);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    public String storeFile(File file , String userId) throws IOException {
        assert file != null;
        String fileName = StringUtils.getFilename(file.getName());
        FileInputStream fis = new FileInputStream(file);

        // Check if the file's name contains valid  characters or not
        if (fileName.contains("..")) {
            throw new RuntimeException("Sorry! File name which contains invalid path sequence " + fileName);
        }
        String extension = fileName.split("\\.")[1];
        Path targetLocation = this.fileStorageLocation.resolve(userId+"."+extension);
        Files.copy(fis, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return userId;
    }


}
