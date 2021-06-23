package com.kilcote.service.system;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kilcote.dao.system.ip2location.Ip2LocationHibernate;
import com.kilcote.entity.system.Ip2Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
@Getter
@Transactional
@Service("ip2LocationService")
public class Ip2LocationService {
	
	@Autowired
	private Ip2LocationHibernate ip2LocationHibernate;
	
	public Boolean importDBFromCSV(String path) {
		return ip2LocationHibernate.importDb(path);
	}
	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteTheme(Long id) {
		return ip2LocationHibernate.deleteByKey(id, "id");
	}
	/**
	 * @param ildata
	 * @return
	 */
	public Boolean saveIp2Location(Ip2Location ildata) {
		Boolean result = null;
		List<Ip2Location> dbIldata = ip2LocationHibernate.findBy2Ip(ildata.getIpFrom(), ildata.getIpTo());
		if (dbIldata != null && dbIldata.size() > 0) {
			if (dbIldata.size() > 1) {
				//delete
				result = ip2LocationHibernate.deleteBy2Ip(ildata.getIpFrom(), ildata.getIpTo());
				if (result) {
					result = ip2LocationHibernate.add(ildata);
				}
			} else {
				//update
				result = ip2LocationHibernate.update(ildata, dbIldata.get(0).getId());
			}
		} else {
			//add
			result = ip2LocationHibernate.add(ildata);
		}
		return result;
	}
}

