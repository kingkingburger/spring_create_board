package com.example.blog.Board.entity;
//테이블 구조화 클래스 역할

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int hits;
    private char deleteYn;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;

    @Builder //빌더 패턴 이용
    public Board(String title, String content, String writer, int hits, char deleteYn){
        this.title=title;
        this.content=content;
        this.writer=writer;
        this.hits = hits;
        this.deleteYn = deleteYn;
    }

    /**
     * 트랜젝션이 종료(commit)될 때  자동으로 쿼리 실행
     */
    //게시글 수정
    public void update(String title, String content, String writer){
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.modifiedDate = LocalDateTime.now();
    }

    //조회수 증가
    public void increaseHits(){
        this.hits++;
    }

    //게시글 삭제
    public void delete(){
        this.deleteYn = 'Y';
    }

}
