package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tbl_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag; // 삭제 여부

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>(); // 상품 이미지

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeDesc(String desc) {
        this.pdesc = desc;
    }

    public void changeName(String name) {
        this.pname = name;
    }

    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 상품 이미지 추가
     * @param image
     */
    public void addImage(ProductImage image) {
        image.setOrd(imageList.size() + 1);
        imageList.add(image);
    }

    /**
     * 상품 이미지 이름 추가
     * @param fileName
     */
    public void addImageString(String fileName) {
        ProductImage image = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(image);
    }

    /**
     * 상품 이미지 삭제
     */
    public void clearList() {
        this.imageList.clear();
    }

}
