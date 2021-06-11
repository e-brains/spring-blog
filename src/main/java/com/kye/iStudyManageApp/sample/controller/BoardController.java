package com.kye.iStudyManageApp.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping({"","/"}) // 아무것도 없거나 / 일때
    public String index(){
        return "sample/index"; // sample/index.html 호출
    }


}
