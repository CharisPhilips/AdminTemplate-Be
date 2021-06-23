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
import com.kilcote.entity.system.Log;
import com.kilcote.service.system.LogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({"/api/log"})
@RequiredArgsConstructor
public class LogController extends BaseController {
	
	@Autowired
	private LogService service;
	
	@GetMapping("/logsByPage")
	@PreAuthorize("hasAuthority('log:view')")
	public ResponseEntity<Page<Log>> getLogsByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(required = false) String email, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dtFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dtTo) {
		Page<Log> result = this.service.listLogByPage(page, size, email, dtFrom, dtTo);
		return new ResponseEntity<Page<Log>>(result, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('log:delete')")
    public ResponseEntity<Boolean> deleteLog(@PathVariable Long id) {
		Boolean result = this.service.deleteById(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
	
	@DeleteMapping("/deletes/{ids}")
	@PreAuthorize("hasAuthority('log:delete')")
    public ResponseEntity<Boolean> deleteLogs(@PathVariable String ids) {
		String[] logIds = ids.split(StringConstant.COMMA);
		Boolean result = this.service.deleteByIds(logIds);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
}
