package com.kye.iStudyManageApp.sample.auth;

import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service  // bean등록
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    // 스프링이 로그인 요청을 가로챌 때 username, password 변수 2개를 가로채는데
    // password 부분은 알아서 처리해 줌
    // 나는 username이 DB에 있는지 확인해서 리턴해 주는 메서드를 구현해 주면
    // 자동으로 알아서 메서드가 로드되서 작동하게 된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User principal = userRepository.findByUsername(username).orElseThrow(()->{
            return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        });

        // 시큐리티 세션이 아닌 일반 세션에 데이터를 넣어서 UI에서 쉽게 활용하기 위한 코드
        if (principal != null) {
            session.setAttribute("principal", principal);
        }

        return new PrincipalDetail(principal);  // 시큐리티 세션에 User정보가 UserDetails 타입으로 저장됨
    }
}
