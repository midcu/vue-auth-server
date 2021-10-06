package com.midcu.authsystem.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ResponseBody
@Tag(name = "通用功能")
public class UserController {

	@Operation(
		summary = "验证码功能",
    	description = "获取验证码"
	)
	@RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaptchaUtil.out(130, 32, 5, request, response);
    }

}
