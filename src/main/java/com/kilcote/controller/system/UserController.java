package com.kilcote.controller.system;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kilcote.common.Global;
import com.kilcote.controller._base.BaseController;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.entity.system.Role;
import com.kilcote.entity.system.User;
import com.kilcote.service.system.RoleService;
import com.kilcote.service.system.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@RestController
@RequestMapping({"/api/user"})
@RequiredArgsConstructor
public class UserController extends BaseController {

	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 0;
	
	@Autowired
	private UserService service;
	@Autowired
	private RoleService roleService;

	@GetMapping("/usersByPage")
	@PreAuthorize("hasAuthority('user:view')")
	public ResponseEntity<Page<User>> getUsersByPage(@RequestParam(defaultValue="0") Integer page, @RequestParam(defaultValue="10") Integer size) {
		Page<User> result = this.service.listUserByPage(page, size);
		log.info(this.getClass().getName() + "getUsersByPage");
		return new ResponseEntity<Page<User>>(result, HttpStatus.OK);
	}
	
	@GetMapping("/rolesByList")
	@PreAuthorize("hasAuthority('user:view')")
	public ResponseEntity<List<Role>> getRolesByList() {
		List<Role> result = this.roleService.listRoles();
		return new ResponseEntity<List<Role>>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/add")
	@PreAuthorize("hasAuthority('user:add')")
	@ControllerEndpoint(operation="Add user", exceptionMessage="Add user failed")
	public ResponseEntity<User> addUser(@RequestPart("user") User user, @RequestParam(value="avatar", required=false) MultipartFile avatar) throws Exception {
		User userAdd = null;
		user.setId(null);
		if(this.service.addUser(user, avatar)) {
			userAdd = this.service.findById(user.getId());
		}
		return new ResponseEntity<User>(userAdd, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('user:update')")
	@ControllerEndpoint(operation="Update user", exceptionMessage="Update user failed")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestPart(value="user", required=true) User user, @RequestParam(value="avatar", required=false) MultipartFile avatar) throws Exception {
		User userUpdate = null;
		user.setId(id);
		if(this.service.updateUser(user, avatar)) {
			userUpdate = this.service.findById(user.getId());
		}
		return new ResponseEntity<User>(userUpdate, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('user:delete')")
	@ControllerEndpoint(operation="Delete user", exceptionMessage="Delete user failed")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        Boolean result = this.service.deleteUser(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
	
	@GetMapping(value = "/avatar/{id}")
	public ResponseEntity<Resource> getAvatar(@PathVariable Long id) throws Exception {
		File file = Global.getAvatarFilePath(id);
		Path path = Paths.get(file.getPath());			
		byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=avatar.png");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(file.length())
				.body(resource);
	}
}