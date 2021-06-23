package com.kilcote.service.system;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilcote.dao.system.ip2location.Ip2LocationHibernate;
import com.kilcote.dao.system.log.LogHibernate;
import com.kilcote.entity.system.Ip2Location;
import com.kilcote.entity.system.Log;
import com.kilcote.utils.StringUtil;

import lombok.Getter;

@Getter
@Transactional
@Service("logService")
public class LogService {

	@Autowired
	private LogHibernate logHibernate;
	@Autowired
	private Ip2LocationHibernate ip2LocationHibernate;
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @param page
	 * @param size
	 * @param email
	 * @param dtFrom
	 * @param dtTo
	 * @return
	 */
	public Page<Log> listLogByPage(Integer pageNo, Integer pageSize, String email, Date dtFrom, Date dtTo) {
		PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		Page<Log> pageResult = logHibernate.listPage(paging, email, dtFrom, dtTo);
		return pageResult;
	}


	/**
	 * @param point
	 * @param method
	 * @param ip
	 * @param operation
	 * @param email
	 * @param start
	 * @throws Exception
	 */
	public void addLog(ProceedingJoinPoint point, Method method, String ip, String operation, String email, long start) throws Exception {
		Log log = new Log();
		log.setIp(ip);

		log.setEmail(email);
		log.setTime(System.currentTimeMillis() - start);
		log.setOperation(operation);

		String className = point.getTarget().getClass().getName();
		String methodName = method.getName();
		log.setMethod(className + "." + methodName + "()");

		Object[] args = point.getArgs();
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paramNames = u.getParameterNames(method);
		if (args != null && paramNames != null) {
			StringBuilder params = new StringBuilder();
			params = handleParams(params, args, Arrays.asList(paramNames));
			log.setParams(params.toString());
		}
		log.setCreateTime(LocalDateTime.now());
		
		List<Ip2Location> locationData = ip2LocationHibernate.findByIp(StringUtil.getValidIp(ip));
		if (locationData != null && locationData.size() == 1) {
			log.setLocation(locationData.get(0).toString());
		}
		addLog(log);		
	}

	@SuppressWarnings("all")
	private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Map) {
					Set set = ((Map) args[i]).keySet();
					List<Object> list = new ArrayList<>();
					List<Object> paramList = new ArrayList<>();
					for (Object key : set) {
						list.add(((Map) args[i]).get(key));
						paramList.add(key);
					}
					return handleParams(params, list.toArray(), paramList);
				} else {
					if (args[i] instanceof Serializable) {
						Class<?> aClass = args[i].getClass();
						try {
							aClass.getDeclaredMethod("toString", new Class[]{null});
							// If NoSuchMethodException is not thrown, there is toString method, safe writeValueAsString, otherwise, toString method of Object is used
							params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
						} catch (NoSuchMethodException e) {
							params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
						}
					} else if (args[i] instanceof MultipartFile) {
						MultipartFile file = (MultipartFile) args[i];
						params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
					} else {
						params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
					}
				}
			}
		} catch (Exception ignore) {
			params.append("Parameter parsing failed");
		}
		return params;
	}

	/**
	 * @param log
	 * @return
	 * @throws Exception
	 */
	public boolean addLog(Log log) throws Exception {
		log.setCreateTime(LocalDateTime.now());
		Boolean result = logHibernate.add(log);
		return result;
	}

	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteById(Long id) {
		return logHibernate.deleteByKey(id, "id");
	}

	/**
	 * @param logIds
	 * @return
	 */
	public Boolean deleteByIds(String[] ids) {
		List<Long> logIds = new ArrayList<Long>();
		for(String s : ids) {
			logIds.add(Long.valueOf(s));
		}
		logHibernate.deleteBatchIds(logIds);
		return true;
	}
}
