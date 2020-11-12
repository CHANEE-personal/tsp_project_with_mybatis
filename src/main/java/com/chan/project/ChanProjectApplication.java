package com.chan.project;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration
@ComponentScan("com.*")	//현재 패키지의 빈, 설정, 서비스 자동스캔

@Configuration	//현재 파일이 컨텍스트의 설정 파일임을 의미

@SpringBootApplication
public class ChanProjectApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ChanProjectApplication.class, args);
	}	
}

