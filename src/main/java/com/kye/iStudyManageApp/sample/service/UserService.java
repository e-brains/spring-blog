package com.kye.iStudyManageApp.sample.service;

import com.kye.iStudyManageApp.sample.model.RoleType;
import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌, IoC해줌
@Service
public class UserService {

    @Autowired  // DI로 주입됨
    private UserRepository userRepository;

    @Autowired // DI로 주입됨
    private BCryptPasswordEncoder encoder;

    /***************
     * 회원가입 (전통적인 방식)
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
     * 스프링부트 시큐리티 방식 회원 가입 (인증 필요 없이 처리)
     ****************/
    @Transactional // 하나의 트랜잭션으로 묶는다.
    public void 시큐리티_회원가입(User user) {

        String rawPwd = user.getPassword();  // 원본 패스워드
        String encPwd = encoder.encode(rawPwd);  // 해쉬 암호화

        // 암호화된 패스워드 셋팅
        user.setPassword(encPwd);

        // 관리자 권한은 서버에서 직접 넣어 줘야 하니 여기서 셋팅
        user.setRole(RoleType.USER);

        userRepository.save(user);
    }



    /***************
     * 스프링부트 시큐리티 방식 로그인 (인증 필요 없이 처리)
     ****************/







}
