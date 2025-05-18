package org.zerock.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() { // 폴더를 만드는 용도
        File tempFolder = new File(uploadPath);

        if(!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        uploadPath = tempFolder.getAbsolutePath();
        log.info("------------------------------------------");
        log.info("Upload Path: " + uploadPath);
    }

    /**
     * 파일을 저장하는 메서드
     * @param files
     * @return List<String> 업로드된 파일 이름 리스트
     */
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        if (files == null || files.isEmpty()) {
            return null;
        }
        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file: files) {

            if (file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) {
                continue; // 파일 이름이 없으면 건너뜀
            }

            String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, savedName);

            try {
                Files.copy(file.getInputStream(), savePath); // 원본 파일을 업로드

                String contentType = file.getContentType(); // 파일의 MIME 타입을 가져옴

                // 이미지 파일이라면
                if (contentType != null && contentType.startsWith("image/")) {
                    // 이미지 파일에 대한 추가 처리 (예: 썸네일 생성 등)
                    log.info("Image file uploaded: " + savedName);
                    Path thumbnailPath = Paths.get(uploadPath, "thumb_" + savedName);
                    Thumbnails.of(savePath.toFile()).size(200, 200).toFile(thumbnailPath.toFile()); // 200,200 사이즈 썸네일 생성
                } else {
                    // 이미지 파일이 아닌 경우
                    log.info("Non-image file uploaded: " + savedName);
                }

                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } // for end

        return uploadNames;
    }

    /**
     * 파일을 읽어오는 메서드
     * @param fileName
     * @return ResponseEntity<Resource>
     */
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        if (!resource.isReadable()) { // 파일이 존재하지 않거나 읽을 수 없는 경우
            resource = new FileSystemResource(uploadPath + File.separator + "default-placeholder.png");
        }

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath())); // MIME 타입을 가져옴
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource); // ResponseEntity를 반환
    }

    /**
     * 파일 삭제 메서드
     * @param fileNames
     */
    public void deleteFiles(List<String> fileNames) {

        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }

        fileNames.forEach(fileName -> {
            String thumbnailName = "thumb_" + fileName; // 썸네일 파일 이름 생성

            Path thumbnailPath = Paths.get(uploadPath, thumbnailName); // 썸네일 파일 경로
            Path filePath = Paths.get(uploadPath, fileName); // 원본 파일 경로

            try {
                Files.deleteIfExists(filePath); // 원본 파일 삭제
                Files.deleteIfExists(thumbnailPath); // 썸네일 파일 삭제
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }


}
