package com.example.blog.Board.entity;
//board 테이블의 CRUD 쿼리를 자동으로 생성해주는 레파지토리 인터페이스

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * 게시글 리스트 조회 - (삭제 여부 기준)
     */
    List<Board> findAllByDeleteYn(final char deleteYn, final Sort sort);
}
