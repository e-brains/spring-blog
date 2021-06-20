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
     * 스프링부트 시큐리티 방식 회원 정보 수정
     * 회원정보 변경 후 세션을 다시 생성한다.
     ****************/
    @Transactional
    public void 회원정보수정(User user) {

        // 수정 시에는 영속성 컨텍스트에 User 오브젝트를 영속화 시키고 영속화된 User 오브렉트를 수정한다.
        // 1. User 테이블을 DB로 부터 Select 해서 영속화 시킨다.
        // 2. 영속화된 User 오브젝트를 변경하면 자동으로 DB에 update 문을 날려준다.
        User persistUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("회원정보 조회 실패 !!!");
        });

        // user 객체의 Oauth컬럼이 비어있는 카카오 나 구글 로그인 사용자는 패스워드 수정이 불가하다.
        if (persistUser.getOAuth() == null || persistUser.getOAuth().equals("")) {
            String rwPwd = user.getPassword();      // 수정된 패스워드
            String encPwd = encoder.encode(rwPwd);  // 해쉬 암호화
            persistUser.setPassword(encPwd);        // 수정된 암호화된 패스워드 셋팅
            persistUser.setEmail(user.getEmail());  // 수정된 메일 셋팅
        }

        // 본 함수가 종료될 때 (본 서비스가 종료될 때) 트랜잭션이 죵료되고 자동으로 commit 이 된다.
        // 이말은 영속화된 persistence 객체의 변호가 감지되면 더티체킹을 통해 자동으로 update 문을 날려준다는 말과 같음
    }

    /***************
     * 회원정보 조회 (단건)
     ****************/
    @Transactional(readOnly = true)
    public User 회원정보조회(String username) {

        User persistUser = userRepository.findByUsername(username).orElseGet(() -> {
            return new User(); // orElseGet()을 사용하여 회원을 못찾으면 빈 객체를 리턴하도록 한다.
        });
        return persistUser;
    }

    /***************
     * 스프링부트 시큐리티 방식 로그인 (인증 필요 없이 처리)
     ****************/


}
