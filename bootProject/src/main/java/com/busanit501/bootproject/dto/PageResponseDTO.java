package com.busanit501.bootproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size; // 페이징당 보여줄 갯수
    private int total;

    private int start;
    private int end;

    // 이전페이지 존재여부
    private boolean prev;
    // 다음페이지 존재여부
    private boolean next;

    // 웹에 전달할 페이징 처리된 게시글 10개, 임의로
    private List<E> dtoList;

    //방법2,
    // 생성자를 특정이름으로 직접 정의해서, 호출해서 사용함.
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, int total,
                           PageRequestDTO pageRequestDTO) {
        // 기본 유효성,
        if(total <= 0){
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.end = ((int) Math.ceil(page / 10.0)) * 10;
        this.start = this.end - 9;
        int last = (int)(Math.ceil(total/10.0));
        this.end = end > last ? last :end;

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

    }
}
