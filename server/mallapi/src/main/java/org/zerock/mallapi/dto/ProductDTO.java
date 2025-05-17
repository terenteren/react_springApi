package org.zerock.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno; // 상품 번호

    private String pname; // 상품 이름

    private int price; // 상품 가격

    private String pdesc; // 상품 설명

    private boolean delFlag; // 삭제 여부

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>(); // 파일 데이터

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>(); // 업로드된 파일 이름 리스트 (db에 저장된 파일 이름)


}
