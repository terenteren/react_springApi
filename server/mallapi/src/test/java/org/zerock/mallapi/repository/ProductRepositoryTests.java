package org.zerock.mallapi.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.mallapi.domain.Product;

import java.util.Optional;
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

    @Transactional
    @Test
    public void testRead() {
        Long pno = 1L;

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());
    }

    @Test
    public void testRead2() {
        Long pno = 1L;

        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());
    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {
        Long pno = 1L;

        productRepository.updateToDelete(pno, true);
    }

    @Test
    public void testUpdate() {
        Product product = productRepository.selectOne(1L).get();

        product.changePrice(10000);

        product.clearList();

        product.addImageString(UUID.randomUUID().toString() + '_' + "IMAGE1.jpg");

        product.addImageString(UUID.randomUUID().toString() + '_' + "IMAGE2.jpg");

        product.addImageString(UUID.randomUUID().toString() + '_' + "IMAGE3.jpg");

        productRepository.save(product);
    }

}
