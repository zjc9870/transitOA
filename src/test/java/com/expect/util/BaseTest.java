package com.expect.util;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.expect.admin.SpringBootCommonApplication;


@RunWith(SpringRunner.class)
//@WebAppConfiguration
@SpringBootTest(classes = SpringBootCommonApplication.class)
public class BaseTest {

}
