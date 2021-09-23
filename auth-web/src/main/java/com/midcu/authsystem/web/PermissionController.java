package com.midcu.authsystem.web;

import com.midcu.authsystem.web.qo.PermissionQuery;
import com.midcu.authsystem.web.rp.BaseResponse;
import com.midcu.authsystem.web.vo.BaseVo;
import com.midcu.authsystem.web.vo.PermissionVo;
import com.midcu.authsystem.web.ro.PermissionRo;
import com.midcu.authsystem.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/permissions")
@Tag(name = "系统：权限管理")
public class PermissionController {
    
    @Autowired
	private PermissionService permissionServiceImpl;

    @Operation(
		summary = "权限列表获取",
    	description = "获取所有的权限"
	)
	@GetMapping("/list")
	public ResponseEntity<Object> search(@Validated PermissionQuery query) {
		return new ResponseEntity<>(permissionServiceImpl.findList(query, PermissionVo.class), HttpStatus.OK);
	}

	@Operation(
		summary = "新增权限",
    	description = "新增权限"
	)
	@PostMapping
	public ResponseEntity<Object> create(@Validated @RequestBody PermissionRo permissionRo) {

		return new ResponseEntity<>(new BaseResponse("保存成功！", permissionServiceImpl.save(permissionRo, BaseVo.class)), HttpStatus.OK);
	}

	@Operation(
		summary = "更新权限",
    	description = "更新权限的指定描述和标识"
	)
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@RequestBody PermissionRo permissionRo, @PathVariable("id") Long id) {

		permissionServiceImpl.update(permissionRo, id);
		return new ResponseEntity<>(new BaseResponse("修改成功！"), HttpStatus.OK);
	}
    
	@Operation(
		summary = "删除权限",
    	description = "根据权限ID删除权限"
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> del(@PathVariable("id") Long id) {

		permissionServiceImpl.deleteById(id);
		return new ResponseEntity<>(new BaseResponse("删除成功！"), HttpStatus.OK);
	}
}
