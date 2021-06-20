package com.kye.iStudyManageApp.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kye.iStudyManageApp.sample.auth.PrincipalDetail;
import com.kye.iStudyManageApp.sample.model.User;
import com.kye.iStudyManageApp.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

// 화면 컨트롤러 (나중에 명칭 수정 예로 UiController 등)
// 인증이 필요없는 곳은 auth를 추가
// 그냥 주소가 / 이면 index.html 허용
// static 디렉토리 아래에 있는 /js/**, /css/**, /image/** 는 허용해야 함
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // yaml에 설정된 kye의 key값을 가져오기 위한 어노테이션
    // 카카오 로그인 시 사용
    @Value("${kye.key}")
    private String kyeKey;

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
     *  스프링 시큐리티를 이용한 회원가입 화면 호출
     *  (인증이 필요 없이 접근 가능해야 함)
     *********************/
    @GetMapping("auth/joinForm2")
    public String authJoinForm() {
        return "auth/joinForm2";
    }

    /********************
     *  스프링 시큐리티를 이용한 로그인 화면 호출
     *  (인증이 필요 없이 접근 가능해야 함)
     *********************/
    @GetMapping("auth/loginForm2")
    public String authLoginForm() {
        return "auth/loginForm2";
    }

    /********************
     *  스프링 시큐리티를 이용한 회원정보수정 화면 호출
     *  (인증이 필요 없이 접근 가능해야 함)
     *********************/
    @GetMapping("/user/updateForm")
    public String userUpdateForm(HttpSession session, @AuthenticationPrincipal PrincipalDetail principal){
/*
        // 로그인 세션 id로 회원정보를 DB로 부터 읽는다
        // 세션정보를 재활용하면 회원정보 수정 후 화면에 보여줄때 변경되지 않은 정보를 보여 줄 수 있기 때문임
        int id = principal.getUser().getId();
        User user = userService.회원정보조회(id);
*/
        session.setAttribute("user", principal.getUser());
        return "/user/updateForm";
    }

    /********************
     *  로그인 실패 시 에러 메시지 창
     *********************/
    @GetMapping("fail")
    public String messageForm() {
        return "sample/message";
    }


    /********************
     *  카카오 로그인
     *  OAuth를 이용한 로그인
     *  OAuth 로그인(카카오 로그인) 사용자는 회원정보 수정을 못하게 막는 루틴이 필요함 (나중에 추가할 것)
     *********************/
/*    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수

        // POST방식으로 key=value 데이터를 요청 (카카오쪽으로)
        // Retrofit2
        // OkHttp
        // RestTemplate

        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "b344701c3ff69917f13cd47bb45df871");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // response 데이터를 오브젝트를 만들어서 받아보자
        // Gson, Json Simple, ObjectMapper 라이브러리  사용
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());

        // 엑세스 토큰을 이용하여 유저 정보를 가져와 보자
        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        System.out.println(response2.getBody());

        // 가져온 유저 데이터를 맵퍼를 이용하여 오브젝트에 담아보자
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // User 오브젝트 : username, password, email 확인
        System.out.println("카카오 아이디(번호) : "+kakaoProfile.getId());
        System.out.println("카카오 이메일 : "+kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그서버 유저네임 : "+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그서버 이메일 : "+kakaoProfile.getKakao_account().getEmail());

        // UUID란 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘 (임시 패스워드)
        // UUID imsiPwd = UUID.randomUUID(); // 기존 사용자의 패스워드가 바꾸기 때문에 사용 불가.

        System.out.println("블로그서버 패스워드 : "+kyeKey); // kyeKey라는 고정 패스워드 사용 (보안 위험이 매우큼)

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                .password(kyeKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")  // OAuth 로그인 인지 구분하는 구분자.
                .build();

        // 가입자 혹은 비가입자 체크 해서 처리
        User originUser = userService.회원찾기(kakaoUser.getUsername());

        if(originUser.getUsername() == null) {
            System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다");
            userService.회원가입(kakaoUser);
        }

        System.out.println("자동 로그인을 진행합니다.");

        // 로그인 처리
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kyeKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }*/



    // 클라이언트에서 ajax 호출 시
    // @Restcontroller로 UserApiController를 별도로 만들지 않더라도 @ResponseBody를 이용하여 여기서 JSON 반환 가능
    @PostMapping("api/user/test")
    @ResponseBody // json 텍스트 반환하게 해준다.
    public int save(@RequestBody User user) {
        System.out.printf("UserController 의 save() 호출 성공 ");
        return 1;
    }
}
