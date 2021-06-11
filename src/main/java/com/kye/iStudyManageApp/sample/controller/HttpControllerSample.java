package com.kye.iStudyManageApp.sample.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @Controller : 사용자가 요청 -> Html파일이나 기타 view파일로 응답하는 경우
// @RestController : 사용자가 요청 -> Data로 응답하는 경우
@Controller
@Slf4j
public class HttpControllerSample {

    // post, put, delete 형식을 테스트 하기 위한 파일 호출
    // @RequestParam : get?뒤에 쿼리 스트링으로 오는 데이터 받기
    @GetMapping("/test")
    public String test(){ // 브라우저 주소창에서 요청하는 것은 GET방식만 지원
        return "/sample/dummyController";
    }

    @GetMapping("/sample/get") // select
    public String getType(Model model){ // 브라우저 주소창에서 요청하는 것은 GET방식만 지원
        model.addAttribute("message", "Get Typt 호출 성공");
        return "sample/message";
    }

    @PostMapping("/sample/post") // insert
    public String postType(Model model){
        log.info("Post Typt 호출 성공");
        model.addAttribute("message", "Post Typt 호출 성공");
        return "sample/message";
    }

    @PutMapping("/sample/put")  // update
    public String putType(){
        log.info("put Typt 호출 성공");
        return null;
    }

    @DeleteMapping("/sample/delete")  // delete
    public String deleteType(){
        log.info("delete Typt 호출 성공");
        return null;
    }

}
