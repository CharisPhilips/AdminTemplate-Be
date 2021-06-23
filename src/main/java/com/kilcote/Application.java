package com.kilcote;

import javax.annotation.PostConstruct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kilcote.service._BootstrapService;

@SpringBootApplication
@MapperScan(basePackages = "com.kilcote.dao")
public class Application {

	@Autowired
	private _BootstrapService apiService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void init() {
		apiService.startThread();
	}

}