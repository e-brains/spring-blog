package com.kye.iStudyManageApp.sample.auth;


import com.kye.iStudyManageApp.sample.model.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면
// UserDetails타입의 오브젝트를 스프링 시큐리티의 고유한 세션 저장소에 저장을 한다.
@Data
public class PrincipalDetail implements UserDetails {

    // 내가 만든 또는 사용할 user 오브젝트를 적용하기 위해서 꼭 필요함
    private User user;  // 오브젝트를 품고 있는 경우 컴포지션이라고 한다.

    // PrincipalDetailService클래스의 loadUserByUsername()메서드에서 UserDetails타입으로
    // 리턴해야 하므로 user의 내용이 있어야 하므로 여기서 생성자를 통해 user의 내용을 채운다.
    public PrincipalDetail(User user){
        this.user = user;
    }

    // user 오브젝트의 password를 리턴하도록 수정
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // user 오브젝트의 username을 리턴하도록 수정
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료 여부를 리턴한다.(true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 게정이 잠겨있는지 여부 (true: 안 잠겨 있음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부 (true : 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 게정 활성화 여부 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 계정의 권한목록을 리턴 (내가 만든 권한으로 커스터마이징)
    // 권한이 여러개인 경우 루프를 돌아야 하지만 현재는 한개이므로 루프 없이 코딩함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collections = new ArrayList<>();

        // 자바는 메서드를 파라미터로 넘길 수 없기 때문에 객체를 만들어서 넣는다.
/*
        collections.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_"+user.getRole(); // 'ROLE_'은 스프링 naming 규칙임 (반드시 붙여함)
            }
        });
*/

        // 파라미터의 타입이 정해져 있고 파라미터로 넘기려는 객체의 메서드가
        // 하나만 존재하면 람다식으로 표현 가능하다.
        collections.add(()->{return "ROLE_"+user.getRole();});

        return collections;
    }
}
