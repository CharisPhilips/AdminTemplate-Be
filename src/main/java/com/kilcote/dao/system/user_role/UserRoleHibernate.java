package com.kilcote.dao.system.user_role;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.UserRole;
 
/**
 * userRole dao
 */
@Repository("usermenuDAO")
public class UserRoleHibernate extends AbstractDao<Long, UserRole> {
 
    /**
     * @param userId
     * @return
     */
    public List<UserRole> findByUserId(long userId) {
    	List<UserRole> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<UserRole> cQuery = cb.createQuery(UserRole.class);
    		Root<UserRole> c = cQuery.from(UserRole.class);
    		cQuery.select(c).where(cb.equal(c.get("user").get("id"), userId));
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
	public List<UserRole> findByEmail(String email) {
		List<UserRole> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<UserRole> cQuery = cb.createQuery(UserRole.class);
			Root<UserRole> c = cQuery.from(UserRole.class);
			cQuery.select(c).where(cb.equal(c.get("user").get("email"), email));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
	}
	
    /**
     * @param userId
     * @param roleId
     * @return
     */
    public List<UserRole> findByUserIdAndRoleId(long userId, long roleId) {
    	List<UserRole> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<UserRole> cQuery = cb.createQuery(UserRole.class);
    		Root<UserRole> c = cQuery.from(UserRole.class);
    		cQuery.select(c).where(cb.and(cb.equal(c.get("user").get("id"), userId), cb.equal(c.get("role").get("id"), roleId)));
    		resultList = em.createQuery(cQuery).getResultList();
    	} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
    }
    
    /**
     * @param userId
     * @return
     */
    public boolean deleteByUserId(Long userId) {
    	Boolean result = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaDelete<UserRole> criteriaDelete = cb.createCriteriaDelete(UserRole.class);
    		Root<UserRole> c = criteriaDelete.from(UserRole.class);
    		criteriaDelete.where(cb.equal(c.get("user").get("id"), userId));
    		em.getTransaction().begin();
    		int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
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

	/**
	 * @param roleIds
	 * @return
	 */
	public boolean deleteByRoleIds(List<Long> roleIds) {
		Boolean result = null;
		EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaDelete<UserRole> criteriaDelete = cb.createCriteriaDelete(UserRole.class);
    		Root<UserRole> c = criteriaDelete.from(UserRole.class);
    		criteriaDelete.where(c.get("role").get("id").in(roleIds));
    		em.getTransaction().begin();
    		int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
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