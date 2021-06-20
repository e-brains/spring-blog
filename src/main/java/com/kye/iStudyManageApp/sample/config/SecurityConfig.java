package com.kye.iStudyManageApp.sample.config;

import com.kye.iStudyManageApp.sample.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/********************************************
* default로 뜨는 스프링 시큐리티 로그인 화면 대신 내가 설정한 로그인 폼으로 이동시키기 위한 설정
**********************************************/

@Configuration  // 빈 등록 : 스프링 컨테이너에서 객체를 생성 및 관리
@EnableWebSecurity  // 시큐리티 필터에 추가 = 스프링 시큐리티를 활성화하고 여기 설정을 추가함
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 주소로 접근했을 때 권한 및 인증을 미리 체크하겠다는 뜻
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // PrincipalDetailService를 호출하면 시큐리티 세션 정보에 내가 만든 user객체 정보가 담긴다.
    @Autowired
    private PrincipalDetailService principalDetailService;

    // 패스워드 해쉬 암호화 모듈
    @Bean // IoC가 되서 스프링이 관리한다. (@Autowired 로 DI해서 어디서든 사용 가능)
    public BCryptPasswordEncoder encoderPWD(){
        return new BCryptPasswordEncoder();
    }

    // 스프링 시큐리티가 가료채서 대신 로그인 해주는데 password를 가로채기를 하는데
    // 해당 password가 뭘로 해쉬가 되서 회원가입이 되었는지 알아야 같은 해쉬로
    // 암호화 해서 DB에 있는 해쉬랑 비교할 수 있음
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // principalDetailService에서 user정보를 세션에 생성해 주고 그 정볼를 여기서 넘겨주면
        // 해쉬값을 여기서 알아서 비교해 준다.
        auth.userDetailsService(principalDetailService).passwordEncoder(encoderPWD());
    }

    @Bean // IoC가 되서 스프링이 관리한다. (@Autowired 로 DI해서 어디서든 사용 가능)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 시큐리티 로그인 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // csrf 토큰 비활성화 (테스트 시 걸어 두는게 좋음)
            .authorizeRequests()
                .antMatchers("/","/api/**","/auth/**","/js/**","/css/**","/image/**")  // 인증(로그인) 없이 입장 가능 URI
                .permitAll()
                .anyRequest()  // auth이외는 모두
                .authenticated()  // 인증이 되어야 함
            .and()
                .formLogin()
                .loginPage("/auth/loginForm2")  // 인증이 걸려 있는 페이지로 접속하면 로그인 화면으로 보내는 URI
                .loginProcessingUrl("/api/auth/login")  // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인을 해준다.

                // 로그인을 정상적으로 끝내면 여기 설정된 주소로 이동하게 한다
                // 성공하면 해당주소로 이동 하지만 (Ajax와 같이 쓰지말 것 : 응답이 두번되기 때문에 꼬임)
                // 로그인만! Ajax 쓰지말자 (회원가입은 써도됨).
                .defaultSuccessUrl("/")
                .failureUrl("/fail"); // 실패하면 여기로 설정된 주소로 이동하게 한다.

    }
}
