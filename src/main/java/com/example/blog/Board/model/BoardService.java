package com.example.blog.Board.model;

import com.example.blog.Board.dto.BoardRequestDto;
import com.example.blog.Board.dto.BoardResponseDto;
import com.example.blog.Board.entity.Board;
import com.example.blog.Board.entity.BoardRepository;
import com.example.blog.Board.paging.CommonParams;
import com.example.blog.Board.paging.Pagination;
import com.example.blog.exception.CustomException;
import com.example.blog.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    //final을 붙혀야지 RequiredArgsConstructor가 생성자 만들면서 주입해줌
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;


    //게시글 생성
    @Transactional//일반적으로 메서드 레벨에 선언하게 되며, 메서드의 실행, 종료, 예외를 기준으로
    //각각 실행(begin), 종료(commit), 예외(rollback)를 자동으로 처리해 줍니다.
    public Long save(final BoardRequestDto params) {
        //entity 객체에 생성된 게시글 정보가 담김
        Board entity = boardRepository.save(params.toEntity());
        return entity.getId();
    }

    //게시글 리스트 조회
    public List<BoardResponseDto> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
        List<Board> list = boardRepository.findAll(sort);
        //list 변수에는 게시글 Entity가 담겨 있고,각각의 Entity를 BoardResponseDto 타입으로 변경(생성)해서 리턴
        return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
        //list 변수에는 게시글 Entity가 담겨 있고,
        //각각의 Entity를 BoardResponseDto 타입으로 변경(생성)해서 리턴해 준다고 생각해 주시면 되겠습니다.

    }

    //게시글 수정
    @Transactional
    public Long update(final Long id, @RequestBody final BoardRequestDto params) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.update(params.getTitle(), params.getWriter(), params.getContent());
        return id;
        //해당 메서드의 실행이 종료(commit)되면 update 쿼리가 자동으로 실행
        //Entity가 생성되거나, Entity를 조회하는 시점에
        //영속성 컨텍스트에 Entity를 보관 및 관리
        //영속성 컨텍스트에 포함된 Entity 객체의 값이 변경되면
        //트랜잭션(Transaction)이 종료(commit)되는 시점에 update 쿼리를 실행
    }

    //게시글 삭제
    @Transactional
    public Long delete(final Long id){
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.delete();
        return id;
    }

    //게시글 상세정보 조회
    @Transactional
    public BoardResponseDto findById(final Long id){
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.increaseHits();
        return new BoardResponseDto(entity);
    }

    /**
     * 게시글 리스트 조회 - (삭제 여부 기준)
     */
//    public List<BoardResponseDto> findAllByDeleteYn(final char deleteYn) {
//
//        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
//        List<Board> list = boardRepository.findAllByDeleteYn(deleteYn, sort);
//        return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
//    }

    /**
     * 게시글 리스트 조회 - (With. pagination information)
     */
    public Map<String, Object> findAll(CommonParams params) {

        // 게시글 수 조회
        int count = boardMapper.count(params);

        // 등록된 게시글이 없는 경우, 로직 종료
        if (count < 1) {
            return Collections.emptyMap();
        }

        // 페이지네이션 정보 계산
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 게시글 리스트 조회
        List<BoardResponseDto> list = boardMapper.findAll(params);

        // 데이터 반환
        Map<String, Object> response = new HashMap<>();
        response.put("params", params);
        response.put("list", list);
        return response;
    }
}
