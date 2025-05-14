package org.zerock.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
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

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        if (files == null || files.isEmpty()) {
            return null;
        }
        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file: files) {
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
}
