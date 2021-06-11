package com.kye.iStudyManageApp.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/********************************************
* default로 뜨는 스프링 시큐리티 로그인 화면 대신 내가 설정한 로그인 폼으로 이동시키기 위한 설정
**********************************************/

@Configuration  // 빈 등록 : 스프링 컨테이너에서 객체를 생성 및 관리
@EnableWebSecurity  // 시큐리티 필터에 추가 = 스프링 시큐리티를 활성화하고 여기 설정을 추가함
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 주소로 접근했을 때 권한 및 인증을 미리 체크하겠다는 뜻
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/auth/**")   // auth이하는 인증(로그인) 없이 입장 가능
                .permitAll()
                .anyRequest()  // auth이외는 모두
                .authenticated()  // 인증이 되어야 함
            .and()
                .formLogin()
                .loginPage("/auth/loginForm") ; // 인증이 걸려 있는 페이지로 접속하면 로그인 화면으로 보냄

    }
}
