package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;

//    @PostMapping("/")
//    public Map<String, String> register(ProductDTO productDTO) {
//        log.info("register: " + productDTO);
//
//        List<MultipartFile> files = productDTO.getFiles();
//
//        List<String> uploadedFileNames = fileUtil.saveFiles(files);
//
//        productDTO.setUploadFileNames(uploadedFileNames);
//
//        log.info("Uploaded File Names: " + uploadedFileNames);
//
//        return Map.of("RESULT", "SUCCESS");
//    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("PageRequestDTO: " + pageRequestDTO);
        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        log.info("pno: " + pno);
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {
        log.info("pno: " + pno);
        log.info("productDTO: " + productDTO);

        productDTO.setPno(pno);

        // old product Database saved Product
        ProductDTO oldProductDTO = productService.get(pno);

        // file upload - 새로운 파일 업로드
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // 기존 업로드된 파일 이름 목록 (DB에 저장된)
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        // add new files
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames); // 기존중 삭제하지 않을 파일 + 새로운 파일
        }

        // 업데이트된 파일 목록을 DTO에 설정
        productDTO.setUploadFileNames(uploadedFileNames);

        productService.modify(productDTO);

        /**
         * 삭제해야 할 파일 찾기 (클라이언트가 전송한 목록에 없는 기존 파일)
         * 기존의 파일 목록에서 현재 업로드된 파일 목록에 없는 파일을 찾아서 삭제
         */
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        if(oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFiles =
                    oldFileNames.stream().
                            filter(fileName -> uploadedFileNames.indexOf(fileName) == -1)
                            .collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

}
