package com.kye.iStudyManageApp.sample.handler;

import com.kye.iStudyManageApp.sample.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/****************************
메시지 처리 공통 클래스
*****************************/

@ControllerAdvice  // 모든 exception 이 걸리면 이곳을 호출하도록 한다.
@RestController
public class GlobalExceptionHandler {

/*

    // 아래처럼 개별적으로 구분해서 호출하도록 할 수 있다.
    @ExceptionHandler(value = IllegalArgumentException.class)  // IllegalArgumentException이 발생하면 스프링이 이 메서드 호출
    public String handleArgumentException(IllegalArgumentException e){
        return "<h1>"+e.getMessage()+"</h1>";
    }

    // 모든 Exception이 발생하면 스프링이 이 메서드 호출
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e){
        return "<h1>"+e.getMessage()+"</h1>";
    }
*/

    // 모든 Exception이 발생하면 스프링이 이 메서드 호출
    @ExceptionHandler(value = Exception.class)
    public ResponseDto<String> handleDtoException(Exception e){
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

}
