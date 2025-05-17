package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.domain.Product;

import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert() {
        Product product = Product.builder()
                .pname("Test Product")
                .pdesc("Test Description")
                .price(1000)
                .build();

        product.addImageString(UUID.randomUUID().toString() + '_' + "IMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString() + '_' + "IMAGE2.jpg");

        productRepository.save(product);
    }
}
