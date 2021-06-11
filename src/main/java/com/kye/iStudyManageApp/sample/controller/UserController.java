package com.kye.iStudyManageApp.sample.controller;

import com.kye.iStudyManageApp.sample.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

// 인증이 필요없는 곳은 auth를 추가
// 그냥 주소가 / 이면 index.html 허용
// static 디렉토리 아래에 있는 /js/**, /css/**, /image/** 는 허용해야 함
@Controller
public class UserController {

    /********************
     * 전통적인 방식 로그인
     *********************/
/*

    @GetMapping("user/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("user/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }
*/

    /********************
     *  스프링 시큐리티를 이용한 로그인
     *********************/
/*

    @GetMapping("user/security/loginForm")
    public String securityLoginForm() {
        return "user/loginForm";
    }

    @GetMapping("user/security/joinForm")
    public String securityJoinForm() {
        return "user/joinForm";
    }
*/

    /********************
     *  스프링 시큐리티를 이용한 로그인 화면 (인증이 필요 없이 접근 가능해야 함)
     *********************/
    @GetMapping("auth/loginForm")
    public String authLoginForm() {
        return "user/loginForm";
    }

    /********************
     *  스프링 시큐리티를 이용한 회원가입 화면 (인증이 필요 없이 접근 가능해야 함)
     *********************/
    @GetMapping("auth/joinForm")
    public String authJoinForm() {
        return "user/joinForm";
    }




    // 클라이언트에서 ajax 호출 시
    // @Restcontroller로 UserApiController를 별도로 만들지 않더라도 @ResponseBody를 이용하여 여기서 JSON 반환 가능
    @PostMapping("api/user/test")
    @ResponseBody // json 텍스트 반환하게 해준다.
    public int save(@RequestBody User user) {
        System.out.printf("UserController 의 save() 호출 성공 ");
        return 1;
    }
}
