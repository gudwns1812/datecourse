package com.datecourse.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.datecourse.web.constrant.SessionConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTestWithSpringBootTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void springBootControllerTest() throws Exception {
        mockMvc.perform(get("/")
                        .sessionAttr(SessionConst.MEMBER_ID, 1L)) // "로그인 된 상태임"을 강제로 주입
                .andExpect(status().isOk())
                .andExpect(view().name("loginHome")); // 결과 뷰 이름 확인
    }
}
