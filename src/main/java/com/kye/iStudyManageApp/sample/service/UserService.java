package com.kye.iStudyManageApp.sample.service;

import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌, IoC해줌
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /***************
     * 회원가입
     ****************/
    @Transactional // 하나의 트랜잭션으로 묶는다.
    public void 회원가입(User user) {
        userRepository.save(user);
    }

    /***************
     * 로그인 (전통적인 방식)
     ****************/

    @Transactional(readOnly = true) // select할때 트랜잭션 시작 부터 트랜잭션 종료 시 까지 데이터 정합성 보장 (repeatable read)
    public User 로그인(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }


    /***************
     * 로그인 (스프링부트 시큐리티 방식)
     ****************/



}
