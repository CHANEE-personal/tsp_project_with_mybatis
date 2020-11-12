package com.chan.main;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration
@ComponentScan("com.*")
@Controller
public class indexController {
	
	/**
	  * @FileName : indexController.java
	  * @Project : chan_project
	  * @Date : 2020. 11. 5. 
	  * @작성자 : CHO
	  * @변경이력 :
	  * @프로그램 설명 : 관리자 메인
	  */
	@RequestMapping(value="/index")
	public ModelAndView index() throws Exception 
	{	
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("login.html");
		
		return mv;
	}
		
	@RequestMapping(value="/top")
	public ModelAndView top() throws Exception
	{
		ModelAndView mv = new ModelAndView();								
		
		mv.addObject("top.html");
		
		return mv;		
	}
	
}
