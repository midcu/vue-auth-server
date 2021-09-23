package com.midcu.authsystem.web;

import java.security.Principal;
import java.util.List;

import com.midcu.authsystem.web.qo.UserQuery;
import com.midcu.authsystem.web.rp.BaseResponse;
import com.midcu.authsystem.web.ro.UserRo;
import com.midcu.authsystem.web.vo.BaseVo;
import com.midcu.authsystem.web.vo.InfoVo;
import com.midcu.authsystem.web.vo.MenuVo;
import com.midcu.authsystem.web.vo.PermissionVo;
import com.midcu.authsystem.web.vo.UserVo;
import com.midcu.authsystem.service.MenusService;
import com.midcu.authsystem.service.PermissionService;
import com.midcu.authsystem.service.RolesService;
import com.midcu.authsystem.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "系统：用户管理")
public class UsersController {
    @Autowired
	private UsersService usersServiceImpl;

	@Autowired
	private RolesService rolesServiceImpl;

	@Autowired
	private MenusService menusServiceImpl;

	@Autowired
	private PermissionService permissionServiceImpl;

	@Operation(
		summary = "用户数据初始化",
    	description = "返回用户的角色，权限，菜单等内容，用于构建前端页面。"
	)
	@GetMapping("/init")
	public ResponseEntity<Object> init(Principal principal) {

		UserVo userVo = usersServiceImpl.findUserByUsername(principal.getName(), UserVo.class);

		List<MenuVo> menus = menusServiceImpl.findMenuByUserId(userVo.getId(), MenuVo.class);

		List<String> permissions = permissionServiceImpl.findPermissionByUserId(userVo.getId(), PermissionVo.class).stream().map(p -> p.name).toList();

		return new ResponseEntity<>(new InfoVo(menus, userVo, permissions), HttpStatus.OK);
	}

	@Operation(
		summary = "用户列表获取",
    	description = "获取所有的用户"
	)
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('users:list')")
	public ResponseEntity<Object> search(@Validated UserQuery query) {
		return new ResponseEntity<>(usersServiceImpl.findUserList(query), HttpStatus.OK);
	}

	@Operation(
		summary = "新增用户",
    	description = "新增用户"
	)
	@PostMapping
	@PreAuthorize("hasAuthority('users:add')")
	public ResponseEntity<Object> create(@Validated @RequestBody UserRo userRo) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userRo.setPassword(encoder.encode(userRo.getPassword()));

		return new ResponseEntity<>(usersServiceImpl.save(userRo, BaseVo.class), HttpStatus.OK);
	}

	@Operation(
		summary = "更新用户",
    	description = "更新用户的指定项::主要作用是更新用户密码，建议其他信息由用户自己更新！"
	)
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('users:edit')")
	public ResponseEntity<Object> update(@RequestBody UserRo userRo, @PathVariable("id") Long id) {

        if (userRo.getPassword() != null) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            userRo.setPassword(encoder.encode(userRo.getPassword()));
        }

		usersServiceImpl.update(userRo, id);
		return new ResponseEntity<>(new BaseResponse("保存成功！"), HttpStatus.OK);
	}
    
	@Operation(
		summary = "删除用户",
    	description = "根据用户ID删除用户"
	)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('users:del')")
	public ResponseEntity<Object> del(@PathVariable("id") Long id) {

		usersServiceImpl.deleteById(id);
		return new ResponseEntity<>(new BaseResponse("删除成功！"), HttpStatus.OK);
	}

	@Operation(
		summary = "获取用户角色",
    	description = "根据用户ID获取用户角色"
	)
	@GetMapping("/roles/{id}")
	@PreAuthorize("hasAuthority('users:roles:list')")
	public ResponseEntity<Object> userRoles(@PathVariable("id") Long id) {

		return new ResponseEntity<>(rolesServiceImpl.findRoleByUserId(id, UserVo.class), HttpStatus.OK);
	}

	@Operation(
		summary = "设置用户角色",
    	description = "根据用户ID设置用户角色"
	)
	@PostMapping("/roles/{id}")
	@PreAuthorize("hasAuthority('users:roles:reset')")
	public ResponseEntity<Object> setRoles(@PathVariable("id") Long id, @RequestBody List<Long> roleIds) {

		usersServiceImpl.setUserRoles(id, roleIds);
		return new ResponseEntity<>(new BaseResponse("角色设置成功！"), HttpStatus.OK);
	}
}
