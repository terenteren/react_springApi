package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.mallapi.domain.Todo;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1() {
        Assertions.assertNotNull(todoRepository);

        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert() {

        for (int i = 0; i < 100; i++) {

            Todo todo = Todo.builder()
                    .title("Test Title" + i)
                    .content("Test Content" + i)
                    .completed(false)
                    .dueDate(LocalDate.now())
                    .build();

            Todo result = todoRepository.save(todo);

            log.info(result);
        }
    }

    @Test
    public void testRead() {
        Long id = 1L;

        Todo todo = todoRepository.findById(id).orElse(null);

        log.info(todo);
    }

    @Test
    public void testUpdate() {
        Long id = 1L;

        Todo todo = todoRepository.findById(id).orElse(null);

        if (todo != null) {
            todo.changeTitle("Updated Title");
            todo.changeContent("Updated Content");
            todo.changeCompleted(true);
            todo.changeDueDate(LocalDate.now().plusDays(7));

            Todo updatedTodo = todoRepository.save(todo);

            log.info(updatedTodo);
        } else {
            log.warn("Todo with ID " + id + " not found.");
        }
    }

    @Test
    public void testPaging() {
        // 페이지 번호는 0부터 시작
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        log.info("Total Elements: " + result.getTotalElements());
        log.info("Total getcontent: " + result.getTotalPages());
    }

//    @Test
//    public void testSearch1() {
//        Page<Todo> result = todoRepository.search1();
//
//    }
}
