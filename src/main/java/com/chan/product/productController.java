package com.chan.product;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration
@ComponentScan("com.*")
@Controller
public class productController {
		
	/**
	  * @FileName : productController.java
	  * @Project : chan_project
	  * @Date : 2020. 11. 4. 
	  * @작성자 : CHO
	  * @변경이력 :
	  * @프로그램 설명 : 상품목록
	 */
	@RequestMapping("/am/product/basicProductList")
	public ModelAndView basicProductList (HttpServletRequest request, @RequestParam Map<String, Object> commandMap) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("/product/basicProductList.html");			
				
		return mv;
	}
	
	/**
	  * @FileName : productController.java
	  * @Project : chan_project
	  * @Date : 2020. 11. 4. 
	  * @작성자 : CHO
	  * @변경이력 :
	  * @프로그램 설명 : 상품등록
	  */
	@RequestMapping("/am/product/productRegist")
	public String productRegist (HttpServletRequest request, @RequestParam Map<String, Object> commandMap) throws Exception{		
		
		String result = "";
		
		return result;
	}
		
	/**
	  * @FileName : productController.java
	  * @Project : chan_project
	  * @Date : 2020. 11. 4. 
	  * @작성자 : CHO
	  * @변경이력 :
	  * @프로그램 설명 : 상품수정
	  */
	@RequestMapping("/am/product/productModify")
	public String productModify (HttpServletRequest request, @RequestParam Map<String, Object> commandMap) throws Exception{
		String result = "";
		
		return result;
	}
}
