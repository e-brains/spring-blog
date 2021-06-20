package com.kye.iStudyManageApp.sample.controller;

import com.kye.iStudyManageApp.sample.auth.PrincipalDetail;
import com.kye.iStudyManageApp.sample.model.Board;
import com.kye.iStudyManageApp.sample.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller  // viewResolver 작동
public class BoardController {

    @Autowired
    private BoardService boardService;

    /**************
     * 서버단에서 스프링 세션 읽기
     **************/
/*

    // 아무것도 없거나 '/' 일때
    @GetMapping({"","/"})
    // @AuthenticationPrincipal로 스프링이 만든 세션에 접근한다.
    public String index(@AuthenticationPrincipal PrincipalDetail principal){

        System.out.printf("로그인 후 스프링 세션 읽기 : username : " + principal.getUsername());

        return "sample/index"; // sample/index.html 호출
    }
*/
/*

    // 첫화면으로 이동 (게시판 목록_페이징 처리 안함)
    @GetMapping({"","/"})
    public String index(Model model){

        model.addAttribute("boards", boardService.글목록());
        return "/sample/index"; // sample/index.html 호출, viewResolver 작동, prefix,subfix를 붙여줌
    }
*/
    /**************
     * 첫화면으로 이동 (게시판 목록_페이징 처리 적용)
     **************/
    @GetMapping({"","/"})
    public String index(Model model, @PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){

        model.addAttribute("boards", boardService.글목록_페이지(pageable));

        // 클라이언트가 mustache인 경우 서버단에서 처리해서 보내야 한다.
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        return "/sample/index"; // sample/index.html 호출, viewResolver 작동, prefix,subfix를 붙여줌
    }

    /**************
     * 상세보기 화면으로 이동
     * board는 reply정보를 같이 가지고 있다.
     **************/
    @GetMapping("/board/detail/{id}")  // 나중에 "/board/{id}/detail" 로 표준 방식으로 고칠 것
    public String findById(@PathVariable int id, Model model, @AuthenticationPrincipal PrincipalDetail principal){

        Board board = boardService.글상세보기(id);
        model.addAttribute("board", board);
        //model.addAttribute("user", principal.getUser());

        // mustache를 사용하면 클라이언트에 로직을 둘 수 없기 때문에 서버에서 작성해서 결과만 보내야 함
        // 작성자가 로그인한 상태이면 글 삭제 / 수정 버튼 을 보여주기 위한 체크 처리
        // 글작성자 user id 와 로그인한 user id 비교
        if (board.getUser().getId() == principal.getUser().getId()){
            model.addAttribute("chk", true);
        }else {
            model.addAttribute("chk", false);
        }

        return "/board/detailForm";
    }

    /**************
     * 첫글쓰기 화면으로 이동 (USER권한이 필요)
     **************/
    @GetMapping({"/board/saveForm"}) // 나중에 "/board/save" 로 표준 방식으로 고칠 것
    public String saveForm(){
        return "/board/saveForm"; // sample/saveForm.html 호출
    }


    /**************
     * 수정 화면으로 이동 (USER권한이 필요)
     **************/
    @GetMapping("/board/updateForm/{id}")  // 나중에 "/board/{id}/update" 로 표준 방식으로 고칠 것
    public String updateForm(@PathVariable int id, Model model){
        // 글상세 보기를 재활용해서 그대로 들고 간다.
        model.addAttribute("board", boardService.글상세보기(id));
        return "/board/updateForm";
    }

}
