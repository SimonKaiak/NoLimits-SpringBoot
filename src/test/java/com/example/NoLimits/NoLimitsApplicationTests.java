package com.example.NoLimits;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.NoLimits.config.TestSecurityConfig;

@SpringBootTest(classes = NoLimitsApplication.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class NoLimitsApplicationTests {

    @Test
    public void contextLoads() {
    }
}