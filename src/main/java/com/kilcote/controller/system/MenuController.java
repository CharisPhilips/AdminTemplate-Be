package com.kilcote.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.kilcote.common.constants.StringConstant;
import com.kilcote.common.data.Tree;
import com.kilcote.controller._base.BaseController;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.entity.system.Menu;
import com.kilcote.service.system.MenuService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@RestController
@RequestMapping({"/api/menu"})
@RequiredArgsConstructor
public class MenuController extends BaseController {

	@Autowired
	private MenuService service;
	
	@GetMapping("/getMenuTrees")
	@PreAuthorize("hasAuthority('menu:view')")
	public ResponseEntity<List<? extends Tree<?>>> getMenuTree() throws Exception {
		List<? extends Tree<?>> rootMenu = service.findMenus();
		return new ResponseEntity<List<? extends Tree<?>>>(rootMenu, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMenuById/{id}")
	@PreAuthorize("hasAuthority('menu:view')")
	public ResponseEntity<Menu> getMenuById(@PathVariable("id") Long id) {
		Menu menu = this.service.findById(id);
		if (menu == null) {
			return new ResponseEntity<Menu>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Menu>(menu, HttpStatus.OK);
		}
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('menu:add')")
	@ControllerEndpoint(operation="Add menu", exceptionMessage="Add menu failed")
	public ResponseEntity<Menu> addMenu(@RequestBody Menu menu) throws Exception {
		menu.setId(null);
		this.service.addMenu(menu);
		return new ResponseEntity<Menu>(menu, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('menu:update')")
	@ControllerEndpoint(operation="Update menu", exceptionMessage="Update menu failed")
	public ResponseEntity<Menu> updateMenu(@PathVariable Long id, @RequestBody Menu menu) throws Exception {
		menu.setId(id);
		this.service.updateMenu(menu);
		return new ResponseEntity<Menu>(menu, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('menu:delete')")
	@ControllerEndpoint(operation="Delete menu", exceptionMessage="Delete menu failed")
    public ResponseEntity<Boolean> deleteMenu(@PathVariable Long id) {
		Boolean result = this.service.deleteById(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
	
	@DeleteMapping("/deletes/{ids}")
	@PreAuthorize("hasAuthority('menu:delete')")
	@ControllerEndpoint(operation="Delete menus", exceptionMessage="Delete menus failed")
    public ResponseEntity<Boolean> deleteMenus(@PathVariable String ids) {
		String[] menuIds = ids.split(StringConstant.COMMA);
		Boolean result = this.service.deleteByIds(menuIds);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
}