package com.kye.iStudyManageApp.test;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncTest {

    @Test
    public void 해쉬_암호화(){
        String encPwd = new BCryptPasswordEncoder().encode("1234");
        System.out.printf("1234kye암호화 : "+encPwd+"\n");

    }
}
