package com.kilcote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kilcote.bootstrap.BootstrapThread;
import com.kilcote.service.system.AuthService;
import com.kilcote.service.system.Ip2LocationService;
import com.kilcote.service.system.MenuService;
import com.kilcote.service.system.RoleService;
import com.kilcote.service.system.ThemeService;
import com.kilcote.service.system.UserService;

import lombok.Getter;
 
/**
 * Bootstrap service.
 */
@Getter
@Transactional
@Service("bootstrapService")
public class _BootstrapService {
	
	private BootstrapThread serviceThread;
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ThemeService themeService;
	
	@Autowired
	private Ip2LocationService ip2LocationService;
	
	public _BootstrapService() {
		super();
	}

    public void startThread() {
    	this.serviceThread = new BootstrapThread();
    	this.serviceThread.setService(this);
    	this.serviceThread.start();
    }
}