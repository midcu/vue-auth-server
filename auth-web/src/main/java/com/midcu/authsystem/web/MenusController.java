package com.midcu.authsystem.web;

import com.midcu.authsystem.web.qo.MenuQuery;
import com.midcu.authsystem.web.rp.BaseResponse;
import com.midcu.authsystem.web.ro.MenuRo;
import com.midcu.authsystem.web.vo.BaseVo;
import com.midcu.authsystem.web.vo.MenuLiteVo;
import com.midcu.authsystem.web.vo.MenuVo;
import com.midcu.authsystem.service.MenusService;

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
@RequestMapping("/menus")
@Tag(name = "系统：菜单管理")
public class MenusController {

	@Autowired
	private MenusService menusServiceImpl;

	@Operation(
		summary = "菜单列表获取",
    	description = "获取所有的菜单"
	)
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('menus:list')")
	public ResponseEntity<Object> search(@Validated MenuQuery query) {
		return new ResponseEntity<>(menusServiceImpl.findList(query, MenuVo.class), HttpStatus.OK);
	}

	@Operation(
		summary = "菜单列表获取",
    	description = "获取所有菜单的ID和名称"
	)
	@GetMapping("/list/all")
	@PreAuthorize("hasAuthority('menus:list')")
	public ResponseEntity<Object> searchAll(@Validated MenuQuery query) {
		return new ResponseEntity<>(menusServiceImpl.findList(query, MenuLiteVo.class), HttpStatus.OK);
	}

	@Operation(
		summary = "新增菜单",
    	description = "添加新的菜单"
	)
	@PostMapping
	@PreAuthorize("hasAuthority('menus:add')")
	public ResponseEntity<Object> create(@Validated @RequestBody MenuRo menu) {

		return new ResponseEntity<>(new BaseResponse("保存成功！", menusServiceImpl.save(menu, BaseVo.class)), HttpStatus.OK);
	}

	@Operation(
		summary = "更新菜单",
    	description = "更新菜单的指定项"
	)
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('menus:edit')")
	public ResponseEntity<Object> update(@RequestBody MenuRo menu, @PathVariable("id") Long id) {

		menusServiceImpl.update(menu, id);
		return new ResponseEntity<>(new BaseResponse("保存成功！"), HttpStatus.OK);
	}
    
	@Operation(
		summary = "删除菜单",
    	description = "根据菜单ID删除菜单"
	)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('menus:del')")
	public ResponseEntity<Object> del(@PathVariable("id") Long id) {

		menusServiceImpl.deleteMenusById(id);
		return new ResponseEntity<>(new BaseResponse("删除成功！"), HttpStatus.OK);
	}

}
