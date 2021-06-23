package com.kilcote.controller.system;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kilcote.common.constants.StringConstant;
import com.kilcote.controller._base.BaseController;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.entity.system.LoginLog;
import com.kilcote.service.system.LoginLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({"/api/loginLog"})
@RequiredArgsConstructor
public class LoginLogController extends BaseController {
	
	@Autowired
	private LoginLogService service;
	
	@GetMapping("/loginLogsByPage")
	@PreAuthorize("hasAuthority('loginlog:view')")
	public ResponseEntity<Page<LoginLog>> getLoginLogsByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size,
		@RequestParam(required = false) String email,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dtFrom,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dtTo) {
		
		Page<LoginLog> result = this.service.listLoginLogByPage(page, size, email, dtFrom, dtTo);
		return new ResponseEntity<Page<LoginLog>>(result, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('loginlog:delete')")
	@ControllerEndpoint(operation="Delete Login Log", exceptionMessage="Delete Login Log failed")
    public ResponseEntity<Boolean> deleteLoginLog(@PathVariable Long id) {
		Boolean result = this.service.deleteById(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
	
	@DeleteMapping("/deletes/{ids}")
	@PreAuthorize("hasAuthority('loginlog:delete')")
	@ControllerEndpoint(operation="Delete Login Logs", exceptionMessage="Delete Login Logs failed")
    public ResponseEntity<Boolean> deleteLoginLogs(@PathVariable String ids) {
		String[] loginLogIds = ids.split(StringConstant.COMMA);
		Boolean result = this.service.deleteByIds(loginLogIds);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
}
