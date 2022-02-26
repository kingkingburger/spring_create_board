package com.example.blog.Board.paging;

//웹에서는 화면 하단에 페이지 번호를 출력하는 기능의 이름을 페이지네이션이라고 부르는데요.
//페이지네이션 계산을 위해서는 앞에서 생성한 CommonParams의 파라미터들이 필요합니다.
//LIMIT 0, 15로 지정한다면 1페이지의 첫 번째 게시글부터 15개까지를 조회
import lombok.Getter;

@Getter
public class Pagination {
    private int totalRecordCount;   //전체 데이터 수
    private int totalPageCount;     //전체 페이지 수
    private int startPage;          //첫 페이지 번호
    private int endPage;            //끝 페이지 번호
    private int limitStart;         //LIMIT 시작 위치
    private boolean existPrevPage;  //이전 페이지 존재 여부
    private boolean existNextPage;  //다음 페이지 존재 여부

    public Pagination(int totalRecordCount, CommonParams params){
        if(totalRecordCount > 0){
            this.totalRecordCount = totalRecordCount;
            this.calculation(params);
        }
    }

    private void calculation(CommonParams params){
        //전체 페이지 수 계산
        totalPageCount = ((totalRecordCount - 1) / params.getRecordPerPage()) + 1;
        //테이블에 1,000개의 레코드가 있고, 페이지당 출력할 데이터 개수가 10개라고 가정했을 때
        //(1,000 / 10)의 결과인 100이 됩니다.



        //현제 페이지 번호가 전체 페이지 수보다 큰 경우. 현재 페이지 번호에 전체 페이지 수 저장
        if(params.getPage() > totalPageCount){
            params.setPage(totalPageCount);
        }

        //첫 페이지 번호 계산
        startPage = ((params.getPage() - 1) / params.getPageSize()) * params.getPageSize() + 1;

        //페이지 하단에 출력할 페이지 수(pageSize)가 10이고,
        //현재 페이지 번호(page)가 5라고 가정했을 때 1을 의미합니다.
        //다른 예로,  페이지 번호가 15라면, startPage는 11이 됩니다.
        //4/10 = 0 ,14/10 = 1


        //끝 페이지 번호 계산
        endPage = startPage + params.getPageSize() - 1;

        //페이지 하단에 출력할 페이지 수(pageSize)가 10이고,
        //현재 페이지 번호(page)가 5라고 가정했을 때 10을 의미합니다.
        //다른 예로,  페이지 번호가 15라면, endPage는 20이 됩니다.


        //끝 페이지가 전체 페이지 수보다 큰 경우. 끝 페이지 전체 페이지 수 저장
        if(endPage > totalPageCount){
            endPage = totalPageCount;
        }

        //LIMIT 시작 위치 계산
        limitStart = (params.getPage()-1) * params.getRecordPerPage();

        //MySQL의 LIMIT 구문에 사용되는 멤버 변수입니다.
        //LIMIT의 첫 번째 파라미터에는 시작 위치, 즉 몇 번째 데이터부터 조회할지를 지정하고,
        //두 번째 파라미터에는 시작 limitStart를 기준으로 조회할 데이터의 개수를 지정합니다.
        //예를 들어, 현재 페이지 번호가 1이고, 페이지당 출력할 데이터 개수가 10이라고 가정했을 때
        //(1 - 1) * 10 = 0이라는 결과가 나오게 되고, LIMIT 0, 10으로 쿼리가 실행됩니다.
        //다른 예로, 페이지 번호가 5라면, LIMIT 40, 10으로 쿼리가 실행됩니다.





        //이전 페이지 존재 여부 확인
        existPrevPage = startPage != 1;
        //startPage가 1이 아니라면, 이전 페이지는 무조건적으로 존재하게 됩니다.


        //다음 페이지 존재 여부 확인
        existNextPage = (endPage * params.getRecordPerPage()) < totalPageCount;
        //예를 들어, 페이지당 출력할 데이터 개수가 10개, 끝 페이지 번호가 10이라고 가정했을 때
        //(10 * 10) = 100이라는 결과가 나오게 되는데요.
        //만약, 전체 데이터 개수가 105개라면, 다음 페이지 존재 여부는 true가 됩니다.

    }
}
