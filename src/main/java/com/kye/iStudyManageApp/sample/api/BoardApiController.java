package com.kye.iStudyManageApp.sample.api;

import com.kye.iStudyManageApp.sample.auth.PrincipalDetail;
import com.kye.iStudyManageApp.sample.dto.ReplySaveRequestDto;
import com.kye.iStudyManageApp.sample.dto.ResponseDto;
import com.kye.iStudyManageApp.sample.model.Board;
import com.kye.iStudyManageApp.sample.model.Reply;
import com.kye.iStudyManageApp.sample.service.BoardService;
import com.kye.iStudyManageApp.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    public BoardService boardService;

    /**************
     * 글쓰기 저장
     * @AuthenticationPrincipal는 세션에서 user정보를 읽기 위해 사용
     **************/
    @PostMapping("/api/board/saveForm")
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {

        System.out.printf("============== api/board/saveForm save 처리 시작 ============== \n");

        // 서비스 호출
        boardService.글저장(board, principal.getUser());

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);  // 자바오브젝트를 JSON으로 변환해서 리턴
    }

    /**************
     * 글 수정
     **************/
    @PutMapping("/api/board/modify")
    public ResponseDto<Integer> update(@RequestBody Board board){  // json은 @RequestBody로 받는다.

        boardService.글수정하기(board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**************
     * 글 삭제
     **************/
    @DeleteMapping("/api/board/deleteById/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id) {

        System.out.printf("====> api/board/deleteById [id="+id+"] delete 처리 시작 !! \n");

        // 서비스 호출
        boardService.글삭제(id);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**************
     * 댓글 쓰기 저장
     * api/board/{boardId}/reply (REST API 규칙 준수)
     * 데이터를 받을 때 컨트롤러에서 dto를 만들어서 받는게 좋다.
     * 여기선 사용하지 않음
     **************/
    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> replySave(@PathVariable int boardId, @RequestBody Reply requestReply,
                                          @AuthenticationPrincipal PrincipalDetail principal) {

        System.out.printf("============== api/board/{id}/reply save 처리 시작 ============== \n");

        // 서비스 호출
        boardService.댓글저장(principal.getUser(), boardId, requestReply);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);  // 자바오브젝트를 JSON으로 변환해서 리턴
    }

    /**************
     * 댓글 쓰기 저장
     * DTO 사용
     **************/
    @PostMapping("/api/board/{boardId}/replyDto")
    public ResponseDto<Integer> replySaveDto(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
        boardService.댓글쓰기DTO(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }


}
