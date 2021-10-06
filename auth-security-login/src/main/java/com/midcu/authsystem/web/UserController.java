package com.midcu.authsystem.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.midcu.authsystem.dto.UserDetailsImpl;
import com.midcu.authsystem.dto.UserDto;
import com.midcu.authsystem.jwt.JwtTokenProvider;
import com.midcu.authsystem.web.ro.UserLogin;
import com.midcu.authsystem.web.rp.BaseResponse;
import com.midcu.authsystem.web.vo.InfoVo;
import com.midcu.authsystem.web.vo.MenuVo;
import com.midcu.authsystem.web.vo.UserVo;
import com.midcu.authsystem.service.MenusService;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private MenusService menusServiceImpl;

	@Operation(
		summary = "用户数据初始化",
    	description = "返回用户的角色，权限，菜单等内容，用于构建前端页面。"
	)
	@GetMapping("/init")
	public ResponseEntity<Object> init(Authentication  authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();

		UserVo userVo = usersServiceImpl.findUserByUsername(userDetails.getUsername(), UserVo.class);

		List<MenuVo> menus = menusServiceImpl.findMenuByUserId(userVo.getId(), MenuVo.class);

		List<String> permissions = userDetails.getAuthorities().stream().map(p -> p.getAuthority()).collect(Collectors.toList());

		return new ResponseEntity<>(new InfoVo(menus, userVo, permissions), HttpStatus.OK);
	}

    @Operation( summary = "用户登录")
	@PostMapping("/login")
	public ResponseEntity<Object> login(@Validated @RequestBody UserLogin userLogin) {
        UserDto userDto = usersServiceImpl.findUserByUsername(userLogin.getUsername(), UserDto.class);

        if (userDto == null) {
            return new ResponseEntity<>(new BaseResponse("用户名或密码错误！"), HttpStatus.BAD_REQUEST);
        } else if (userDto.getState() != 1) {
            return new ResponseEntity<>(new BaseResponse("账户异常！"), HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(userLogin.getPassword(), userDto.getPassword())) {
            String token = jwtTokenProvider.generateJwtToken(userDto);
            Map<String, Object> authInfo = new HashMap<String, Object>();
            authInfo.put("token", token);
            return new ResponseEntity<>(authInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new BaseResponse("用户名或密码错误"), HttpStatus.BAD_REQUEST);
        }
        
	}

    @Operation( summary = "用户注销")
	@GetMapping("/logout")
	public ResponseEntity<Object> logout() {

        jwtTokenProvider.releaseJwtToken();

        return new ResponseEntity<>(new BaseResponse("退出登录！"), HttpStatus.OK);
	}
    
    @Operation( summary = "token刷新")
	@GetMapping("/refreshtoken")
	public ResponseEntity<Object> refresh(Principal principal) {

        UserDto userDto = new UserDto(null, principal.getName(), null, null);

        String token = jwtTokenProvider.generateJwtToken(userDto);

        Map<String, Object> authInfo = new HashMap<String, Object>();
        
        authInfo.put("token", token);

        return new ResponseEntity<>(authInfo, HttpStatus.OK);
	}
}
