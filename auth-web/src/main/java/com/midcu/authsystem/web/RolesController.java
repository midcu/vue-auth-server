package com.midcu.authsystem.web;

import java.util.List;
import java.util.stream.Collectors;

import com.midcu.authsystem.web.qo.RoleQuery;
import com.midcu.authsystem.web.rp.BaseResponse;
import com.midcu.authsystem.web.rp.ListResponse;
import com.midcu.authsystem.web.ro.RoleRo;
import com.midcu.authsystem.web.vo.BaseVo;
import com.midcu.authsystem.web.vo.MenuLiteVo;
import com.midcu.authsystem.web.vo.PermissionLiteVo;
import com.midcu.authsystem.web.vo.RoleVo;
import com.midcu.authsystem.service.PermissionService;
import com.midcu.authsystem.service.RolesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/roles")
@Tag(name = "系统：角色管理")
public class RolesController {
    
    @Autowired
	private RolesService rolesServiceImpl;

	@Autowired
	private PermissionService permissionServiceImpl;

    @Operation(
		summary = "角色列表获取",
    	description = "获取所有的角色"
	)
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('roles:list')")
	public ResponseEntity<Object> search(@Validated RoleQuery query) {
		return new ResponseEntity<>(rolesServiceImpl.findList(query, RoleVo.class), HttpStatus.OK);
	}

	@Operation(
		summary = "新增角色",
		description = "新增角色"
	)
	@PostMapping
	@PreAuthorize("hasAuthority('roles:add')")
	public ResponseEntity<Object> create(@Validated @RequestBody RoleRo role) {

		return new ResponseEntity<>(new BaseResponse("保存成功！", rolesServiceImpl.save(role, BaseVo.class)), HttpStatus.OK);
	}

	@Operation(
		summary = "更新角色",
    	description = "更新角色的指定项！"
	)
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('roles:edit')")
	public ResponseEntity<Object> update(@RequestBody RoleRo roleRo, @PathVariable("id") Long id) {

		rolesServiceImpl.update(roleRo, id);
		return new ResponseEntity<>(new BaseResponse("保存成功！"), HttpStatus.OK);
	}

	@Operation(
		summary = "根据角色获取菜单",
    	description = "根据角色获取菜单"
	)
	@GetMapping("/menus/{roleId}")
	@PreAuthorize("hasAuthority('roles:menus')")
	public ResponseEntity<Object> roleId(@PathVariable("roleId") Long roleId) {

		return new ResponseEntity<>(new ListResponse<Long>(rolesServiceImpl.findMenuVoByRoleId(roleId, MenuLiteVo.class).stream().map(p -> p.id).collect(Collectors.toList())), HttpStatus.OK);
	}

	@Operation(
		summary = "删除角色",
    	description = "删除角色 同时删除角色对应的菜单和权限"
	)
	@DeleteMapping("/{roleId}")
	@PreAuthorize("hasAuthority('roles:del')")
	public ResponseEntity<Object> del(@PathVariable("roleId") Long roleId) {

		rolesServiceImpl.deleteRoleById(roleId);

		return new ResponseEntity<>(new BaseResponse("删除成功！"), HttpStatus.OK);
	}

	@Operation(
		summary = "根据角色获取权限",
    	description = "根据角色获取权限"
	)
	@GetMapping("/permissions/{roleId}")
	@PreAuthorize("hasAuthority('roles:permissions')")
	public ResponseEntity<Object> permission(@PathVariable("roleId") Long roleId) {

		return new ResponseEntity<>(new ListResponse<Long>(permissionServiceImpl.findPermissionByRoleId(roleId, PermissionLiteVo.class).stream().map(p -> p.id).collect(Collectors.toList())), HttpStatus.OK);
	}

	@Operation(
		summary = "设置角色权限",
    	description = "设置角色权限"
	)
	@PostMapping("/permissions/{roleId}")
	@PreAuthorize("hasAuthority('roles:permissions:reset')")
	public ResponseEntity<Object> setPermission(@PathVariable("roleId") Long roleId, @RequestBody List<Long> roleIds) {

		rolesServiceImpl.setRolePermissions(roleId, roleIds);

		return new ResponseEntity<>(new BaseResponse("权限设置成功！"), HttpStatus.OK);
	}

	@Operation(
		summary = "设置角色菜单",
    	description = "设置角色菜单"
	)
	@PostMapping("/menus/{roleId}")
	@PreAuthorize("hasAuthority('roles:menus:reset')")
	public ResponseEntity<Object> setMenus(@PathVariable("roleId") Long roleId, @RequestBody List<Long> menuIds) {

		rolesServiceImpl.setRoleMenus(roleId, menuIds);

		return new ResponseEntity<>(new BaseResponse("菜单设置成功！"), HttpStatus.OK);
	}

}
