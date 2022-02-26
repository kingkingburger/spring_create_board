package com.example.blog.Board.paging;

import lombok.Getter;
import lombok.Setter;

//페이징과 검색 처리에는 필수적으로 전달받아야 하는 파라미터가 있습니다.
@Getter
@Setter
public class CommonParams {

    private int page;           // 현재 페이지 번호
    private int recordPerPage;  // 페이지당 출력할 데이터 개수
    private int pageSize;       // 화면 하단에 출력할 페이지 개수
    private String keyword;     // 검색 키워드
    private String searchType;  // 검색 유형
    private Pagination pagination;  // 페이지네이션 정보
}
