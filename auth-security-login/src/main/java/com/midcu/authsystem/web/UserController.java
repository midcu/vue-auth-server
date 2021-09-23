package com.midcu.authsystem.web;

import java.util.HashMap;
import java.util.Map;

import com.midcu.authsystem.dto.UserDto;
import com.midcu.authsystem.jwt.JwtTokenProvider;
import com.midcu.authsystem.web.ro.UserLogin;
import com.midcu.authsystem.web.rp.BaseResponse;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "用户：登录注册注销")
public class UserController {
    
    @Autowired
    private UsersService usersServiceImpl;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation( summary = "用户登录")
	@PostMapping("/login")
	public ResponseEntity<Object> login(@Validated @RequestBody UserLogin userLogin) {
        UserDto userDto = usersServiceImpl.findUserByUsername(userLogin.getUsername(), UserDto.class);

        if (userDto == null) {
            return new ResponseEntity<>(new BaseResponse("用户名或密码错误"), HttpStatus.BAD_REQUEST);
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
    
}
