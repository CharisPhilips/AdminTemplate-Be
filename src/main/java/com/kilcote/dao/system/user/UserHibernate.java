package com.kilcote.dao.system.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.User;
 
/**
 * user DAO.
 */
@Repository("userDAO")
public class UserHibernate extends AbstractDao<Long, User> {
 
	/**
     * @param email
     * @param password
     * @return
     */
    public List<User> findByEmailAndPassword(String email, String password) {
    	List<User> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<User> cQuery = cb.createQuery(User.class);
			Root<User> c = cQuery.from(User.class);
			cQuery.select(c).where(cb.and(cb.equal(c.get("email"), email), cb.equal(c.get("password"), password)));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}
    
    /**
	 * @param email
	 * @return
	 */
	public List<User> findByEmailAndEnabled(String email) {
		List<User> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<User> cQuery = cb.createQuery(User.class);
			Root<User> c = cQuery.from(User.class);
			cQuery.select(c).where(cb.and(cb.equal(c.get("email"), email), cb.equal(c.get("status"), User.STATUS_ENABLED)));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}

	/**
	 * @param email
	 * @return
	 */
	public List<User> findByEmail(String email) {
		List<User> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<User> cQuery = cb.createQuery(User.class);
			Root<User> c = cQuery.from(User.class);
			cQuery.select(c).where(cb.equal(c.get("email"), email));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}
	
	/**
	 * @param email
	 * @return
	 */
	public List<User> findByEmailWithoutSelf(User user) {
		List<User> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<User> cQuery = cb.createQuery(User.class);
			Root<User> c = cQuery.from(User.class);
			cQuery.select(c).where(cb.and(cb.equal(c.get("email"), user.getEmail()), cb.notEqual(c.get("id"), user.getId())));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}

	public Boolean updateEnabled(String email) {
		Boolean result = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaUpdate<User> cQuery = cb.createCriteriaUpdate(User.class);
			Root<User> c = cQuery.from(User.class);
			cQuery.set(c.get("status"), User.STATUS_ENABLED).where(cb.equal(c.get("email"), email));
			em.getTransaction().begin();
			int rowsUpdated = em.createQuery(cQuery).executeUpdate();
			em.getTransaction().commit();
			result = true;
		} catch (HibernateException e) {
    		e.printStackTrace();
    		result = false;
		} finally {
			em.close();
		}
		return result;
	}
	
	public Boolean updateLoginTime(String email, LocalDateTime dtLastLogin) {
		Boolean result = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaUpdate<User> cQuery = cb.createCriteriaUpdate(User.class);
			Root<User> c = cQuery.from(User.class);
			cQuery.set(c.get("lastTime"), dtLastLogin).where(cb.equal(c.get("email"), email));
			em.getTransaction().begin();
			int rowsUpdated = em.createQuery(cQuery).executeUpdate();
			em.getTransaction().commit();
			result = true;
		} catch (HibernateException e) {
    		e.printStackTrace();
    		result = false;
		} finally {
			em.close();
		}
		return result;
	}
}