package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    public void testGet() {
        Long tno = 50L;

        log.info(todoService.get(tno));
    }

    @Test
    public void testRegister() {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Test Title")
                .content("Test Content")
                .completed(false)
                .dueDate(LocalDate.now().toString())
                .build();

        Long tno = todoService.register(todoDTO);

        log.info("Created Todo with TNO: " + tno);
    }

    @Test
    public void testGetList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(11)
                .build();

        log.info(todoService.getList(pageRequestDTO));
    }

}
