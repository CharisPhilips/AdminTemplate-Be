package com.kilcote.dao.system.role;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.Role;
 
/**
 * role DAO.
 */
@Repository("roleDAO")
public class RoleHibernate extends AbstractDao<Long, Role> {
 
	/**
	 * @param roleName
	 * @return
	 */
	public List<Role> findByRoleName(String roleName) {
		List<Role> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Role> cQuery = cb.createQuery(Role.class);
			Root<Role> c = cQuery.from(Role.class);
			cQuery.select(c).where(cb.equal(c.get("roleName"), roleName));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}

	/**
	 * @param menu
	 * @return
	 */
	public List<Role> findByRoleNameWithoutSelf(Role menu) {
		List<Role> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Role> cQuery = cb.createQuery(Role.class);
			Root<Role> c = cQuery.from(Role.class);
			cQuery.select(c).where(cb.and(cb.equal(c.get("roleName"), menu.getRoleName()), cb.notEqual(c.get("id"), menu.getId())));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}
	

}