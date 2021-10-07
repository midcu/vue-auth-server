package com.midcu.authsystem.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.midcu.authsystem.dto.UserDetailsImpl;
import com.midcu.authsystem.web.qo.MenuQuery;
import com.midcu.authsystem.web.vo.InfoVo;
import com.midcu.authsystem.web.vo.MenuVo;
import com.midcu.authsystem.web.vo.UserVo;
import com.wf.captcha.utils.CaptchaUtil;
import com.midcu.authsystem.service.MenusService;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@ResponseBody
@Tag(name = "用户：登录注册注销，前端信息初始化！")
public class UserController {
    
    @Autowired
    private UsersService usersServiceImpl;

	@Autowired
	private MenusService menusServiceImpl;

	// 是否开启管理员账号
	@Value("${midcu.authsystem.security.admin:false}") private Boolean admin;

	// 管理员账号名称
	@Value("${midcu.authsystem.security.user.username:admin}") private String adminUsername;

	@Operation(
		summary = "用户数据初始化",
    	description = "返回用户的角色，权限，菜单等内容，用于构建前端页面。"
	)
	@GetMapping("/init")
	
	public ResponseEntity<Object> init(Authentication  authentication) {

		
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		
		if (admin && userDetails.getUsername().equals(adminUsername)) {
			// 判断是否开启管理员账号功能 且是否为管理员账号
			UserVo userVo = new UserVo(1L, adminUsername, "系统管理员", "系统管理员账号", null, null, 1, null);
	
			// 拥有所有的菜单
			List<MenuVo> menus = menusServiceImpl.findList(new MenuQuery(), MenuVo.class).getData();
	
			List<String> permissions = userDetails.getAuthorities().stream().map(p -> p.getAuthority()).collect(Collectors.toList());
	
			return new ResponseEntity<>(new InfoVo(menus, userVo, permissions), HttpStatus.OK);
		} else {

			UserVo userVo = usersServiceImpl.findUserByUsername(userDetails.getUsername(), UserVo.class);
	
			List<MenuVo> menus = menusServiceImpl.findMenuByUserId(userVo.getId(), MenuVo.class);
	
			List<String> permissions = userDetails.getAuthorities().stream().map(p -> p.getAuthority()).collect(Collectors.toList());
	
			return new ResponseEntity<>(new InfoVo(menus, userVo, permissions), HttpStatus.OK);
		}
	}

	@Operation(
		summary = "验证码功能",
    	description = "获取验证码"
	)
	@RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaptchaUtil.out(130, 32, 5, request, response);
    }

	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String pwd = "{bcrypt}";

		pwd = "{bcrypt}".concat(encoder.encode("eea45e1507c2140a7abaed6c475e8b3e"));

		System.out.println(pwd);
	}
}
