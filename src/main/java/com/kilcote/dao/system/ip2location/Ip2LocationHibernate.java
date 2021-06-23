package com.kilcote.dao.system.ip2location;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.Ip2Location;
 
/**
 * Log DAO.
 */
@Repository("ip2locationDAO")
public class Ip2LocationHibernate extends AbstractDao<Long, Ip2Location> {
	
	/**
	 * @param filePath
	 * @return
	 */
	public Boolean importDb(String filePath) {
		Boolean result = null;
		EntityManager em = getEntityManager();
		String tableName = Ip2Location.class.getAnnotation(Table.class).name(); //"t_ip2location"
    	try {
    		em.getTransaction().begin();
    		em.createNativeQuery(
				"LOAD DATA LOCAL\n " + 
				" INFILE '" + filePath + "'\n " + 
				"INTO TABLE\n " + 
				"`" + tableName + "" + "`\n " + 
				"FIELDS TERMINATED BY ','\n " + 
				"ENCLOSED BY '\"'\n " + 
				"LINES TERMINATED BY '\\r\\n'\n " + 
				"IGNORE 0 LINES \n" +
				"(ip_from, ip_to, country_code, country_name, region_name, city_name, latitude, longitude)" +
				";"
			).executeUpdate();
    		em.getTransaction().commit();
    		result = true;
    	} catch (HibernateException e) {
    		e.printStackTrace();
    		return false;
    	} finally {
			em.close();
		}
    	return result;
	}

	/**
	 * @param ipFrom
	 * @param ipTo
	 * @return
	 */
	public List<Ip2Location> findBy2Ip(Long ipFrom, Long ipTo) {
		List<Ip2Location> resultList = null;
		EntityManager em = getEntityManager();
		try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<Ip2Location> cQuery = cb.createQuery(Ip2Location.class);
    		Root<Ip2Location> c = cQuery.from(Ip2Location.class);
    		cQuery.select(c).where(cb.and(cb.equal(c.get("ipFrom"), ipFrom), cb.equal(c.get("ipTo"), ipTo)));
    		em.getTransaction().begin();
    		resultList = em.createQuery(cQuery).getResultList();
    		em.getTransaction().commit();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}
	
	/**
	 * @param ipFrom
	 * @param ipTo
	 * @return
	 */
	public List<Ip2Location> findByIp(Long ip) {
		List<Ip2Location> resultList = null;
		EntityManager em = getEntityManager();
		try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<Ip2Location> cQuery = cb.createQuery(Ip2Location.class);
    		Root<Ip2Location> c = cQuery.from(Ip2Location.class);
    		cQuery.select(c).where(cb.and(cb.lessThanOrEqualTo(c.get("ipFrom"), ip), cb.greaterThanOrEqualTo(c.get("ipTo"), ip)));
    		em.getTransaction().begin();
    		resultList = em.createQuery(cQuery).getResultList();
    		em.getTransaction().commit();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}
	
	/**
	 * @param ipFrom
	 * @param ipTo
	 * @return
	 */
	public boolean deleteBy2Ip(Long ipFrom, Long ipTo) {
		Boolean result = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaDelete<Ip2Location> criteriaDelete = cb.createCriteriaDelete(Ip2Location.class);
			Root<Ip2Location> c = criteriaDelete.from(Ip2Location.class);
			criteriaDelete.where(cb.and(cb.equal(c.get("ipFrom"), ipFrom), cb.equal(c.get("ipTo"), ipTo)));
			em.getTransaction().begin();
			int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
			result = true;
			em.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			result = false;
		} finally {
			em.close();
		}
		return result;
	}

}