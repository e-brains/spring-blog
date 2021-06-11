package com.kye.iStudyManageApp.sample.controller;

import com.kye.iStudyManageApp.sample.dto.MemberForm;
import org.springframework.web.bind.annotation.*;

// @Controller : 사용자가 요청 -> Html파일이나 기타 view파일로 응답하는 경우
// @RestController : 사용자가 요청 -> Data로 응답하는 경우
@RestController
public class HttpRestControllerSample {

    // @RequestParam : get?뒤에 쿼리 스트링으로 오는 데이터 받기
    @GetMapping("/rest/get") // select
    public String getType(@RequestParam int id, @RequestParam String username){
        System.out.printf("id = %d , username = %s", id, username);
        return "get 요청 타입";
    }

    // DTO 객체를 통해서 쿼리 스트링 데이터 받기
    @GetMapping("/rest/getdto")  //http://localhost:8080/rest/dto?id=1&username=홍길동&email=aaa@gmail.com&password=aaa123
    public String getDto(MemberForm form){
        System.out.printf("id = %d, username = %s , password = %s , email = %s \n",
                form.getId(), form.getUsername(), form.getPassword(), form.getEmail());
        return "get DTO 성공!!  "+form.getId()+" / "+form.getUsername()+" / "+form.getPassword()+" / "+form.getEmail();
    }

    // @RequestParam 으로 데이터 받기
    @PostMapping("/rest/post") // insert
    public String postType(@RequestParam int id, @RequestParam String username){
        System.out.printf("id = %d , username = %s\n", id, username);
        return "post 요청 성공 !!";
    }

    // DTO 객체로  데이터 받기
    @PostMapping("/rest/postdto") // insert
    public String postDto(MemberForm form){

        // 빌더 패턴을 이용하여 객체 생성 ( id 자동생성 시 id 값이 없더라도 객체생성 가능, 파라미터 순서 필요 없음 )
        MemberForm m = MemberForm.builder().username(form.getUsername()).password(form.getPassword()).email(form.getEmail()).build();
        System.out.println("m.toString() : " + m.toString());

        // form DTO를 읽어서 클라이언트에서 온 데이터를 읽는다.
        System.out.printf("id = %d, username = %s , password = %s , email = %s \n",
                form.getId(), form.getUsername(), form.getPassword(), form.getEmail());
        return "post DTO 성공 !!"+form.getId()+" / "+form.getUsername()+" / "+form.getPassword()+" / "+form.getEmail();
    }

    // Json으로 데이터 받기
    // @RequestBody : body의 텍스트 데이터 나 json 데이터를 받을 때 사용
    // 자동으로 json데이터를 MemberForm에 맵핑 시켜준다.
    @PostMapping("/rest/postJson") // insert
    public String postJson(@RequestBody MemberForm form){
        System.out.printf("id = %d, username = %s , password = %s , email = %s \n",
                form.getId(), form.getUsername(), form.getPassword(), form.getEmail());
        return "post Json 성공 !!"+form.getId()+" / "+form.getUsername()+" / "+form.getPassword()+" / "+form.getEmail();
    }

    @PutMapping("/rest/put")  // update
    public String putType(){
        return "put 요청 타입";
    }

    @DeleteMapping("/rest/delete")  // delete
    public String deleteType(){
        return "delete 요청 타입";
    }

}
