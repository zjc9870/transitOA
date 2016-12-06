package com.expect.util;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.expect.admin.SpringBootCommonApplication;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = SpringBootCommonApplication.class)
@WebAppConfiguration
public class BaseTest {

}
