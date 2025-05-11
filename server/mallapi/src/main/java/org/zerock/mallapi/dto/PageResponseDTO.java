package org.zerock.mallapi.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> dtoList; // DTO 리스트

    private List<Integer> pageNumList; // 페이지 번호 리스트

    private PageRequestDTO pageRequestDTO;  // 검색 조건

    private boolean prev, next; // 이전, 다음 버튼

    private int totalCount, prevPage, nextPage, totalPage, current; // 전체 페이지 수, 이전 페이지, 다음 페이지, 전체 페이지 수, 현재 페이지

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) total;

        // 끝페이지 end
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        // 시작페이지 start
        int start = end - 9;

        // 진짜 마지막
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        end = end > last ? last : end;

        this.prev = start > 1; // 이전 버튼

        this.next = totalCount > end * pageRequestDTO.getSize(); // 다음 버튼

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList()); // 페이지 번호 리스트

        this.prevPage = prev ? start - 1 : 0; // 이전 페이지

        this.nextPage = next ? end + 1 : 0; // 다음 페이지

//        this.totalPage = (int) Math.ceil(totalCount / (double) pageRequestDTO.getSize());
//
//        this.current = pageRequestDTO.getPage();
    }

}
