package com.kilcote.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kilcote.common.data.Tree;
import com.kilcote.controller._base.BaseController;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.entity.system.Menu;
import com.kilcote.entity.system.Role;
import com.kilcote.service.system.MenuService;
import com.kilcote.service.system.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@RestController
@RequestMapping({"/api/role"})
@RequiredArgsConstructor
public class RoleController extends BaseController {

	@Autowired
	private RoleService service;
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/rolesByPage")
	@PreAuthorize("hasAuthority('role:view')")
	public ResponseEntity<Page<Role>> getRolesByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		Page<Role> result = this.service.listRoleByPage(page, size);
		return new ResponseEntity<Page<Role>>(result, HttpStatus.OK);
	}
	
	@GetMapping("/getMenuTrees")
	@PreAuthorize("hasAuthority('role:view')")
	public ResponseEntity<List<? extends Tree<?>>> getMenuTree() throws Exception {
		List<? extends Tree<?>> rootMenu = this.menuService.findMenus();
		return new ResponseEntity<List<? extends Tree<?>>>(rootMenu, HttpStatus.OK);
	}
	
	@GetMapping("/getMenusByRoleId/{roleId}")
	@PreAuthorize("hasAuthority('role:view')")
	public ResponseEntity<List<Menu>> getMenuById(@PathVariable("roleId") Long roleId) {
		List<Menu> result = this.menuService.listMenuByRoleId(roleId);
		return new ResponseEntity<List<Menu>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value = "/add")
	@PreAuthorize("hasAuthority('role:add')")
	@ControllerEndpoint(operation="Add role", exceptionMessage="Add role failed")
	public ResponseEntity<Role> addRole(@RequestBody Role role) throws Exception {
		role.setId(null);
		this.service.addRole(role);
		return new ResponseEntity<Role>(role, HttpStatus.CREATED);
	}

	@PutMapping(value = "/update/{id}")
	@PreAuthorize("hasAuthority('role:update')")
	@ControllerEndpoint(operation="Update role", exceptionMessage="Update role failed")
	public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) throws Exception {
		role.setId(id);
		this.service.updateRole(role);
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('role:delete')")
	@ControllerEndpoint(operation="Delete role", exceptionMessage="Delete role failed")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Long id) {
		Boolean result = this.service.deleteById(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
}