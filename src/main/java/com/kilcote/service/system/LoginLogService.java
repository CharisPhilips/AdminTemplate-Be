package com.kilcote.service.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kilcote.dao.system.ip2location.Ip2LocationHibernate;
import com.kilcote.dao.system.login_log.LoginLogHibernate;
import com.kilcote.entity.system.Ip2Location;
import com.kilcote.entity.system.LoginLog;
import com.kilcote.utils.HttpUtil;
import com.kilcote.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Transactional
@Service("loginLogService")
public class LoginLogService {

	@Autowired
	private LoginLogHibernate loginLogHibernate;
	
	@Autowired
	private Ip2LocationHibernate ip2LocationHibernate;
	
	/**
	 * @param page
	 * @param size
	 * @param email
	 * @param dtFrom
	 * @param dtTo
	 * @return
	 */
	public Page<LoginLog> listLoginLogByPage(Integer  pageNo, Integer pageSize, String email, Date dtFrom, Date dtTo) {
		PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		Page<LoginLog> pageResult = loginLogHibernate.listPage(paging, email, dtFrom, dtTo);
		return pageResult;
	}
	

	/**
	 * @param loginLogId
	 * @return
	 */
	public LoginLog findById(long loginLogId) {
		//jpa
		return loginLogHibernate.getByKey(loginLogId);
	}

	/**
	 * @param loginLog
	 * @return
	 * @throws Exception 
	 */
	public boolean addLoginLog(LoginLog loginLog) throws Exception {
		loginLog.setLoginTime(LocalDateTime.now());
        String ip = HttpUtil.getHttpServletRequestIpAddress();
        loginLog.setIp(ip);
        
		List<Ip2Location> locationData = ip2LocationHibernate.findByIp(StringUtil.getValidIp(ip));
		if (locationData != null && locationData.size() == 1) {
			loginLog.setLocation(locationData.get(0).toString());
		}
		Boolean result = loginLogHibernate.add(loginLog);
		return result;
	}

	/**
	 * @param loginLog
	 * @return
	 * @throws Exception 
	 */
	public boolean updateLoginLog(LoginLog loginLog) throws Exception {
		Boolean result = loginLogHibernate.update(loginLog, loginLog.getId());
		return result;
	}

	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteById(Long id) {
		return loginLogHibernate.deleteByKey(id, "id");
	}

	/**
	 * @param ids
	 * @return
	 */
	public Boolean deleteByIds(String[] ids) {
		List<Long> logIds = new ArrayList<Long>();
		for(String s : ids) {
			logIds.add(Long.valueOf(s));
		}
		loginLogHibernate.deleteBatchIds(logIds);
		return true;
	}
}
