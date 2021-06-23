package com.kilcote.dao.system.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.Menu;
 
/**
 * menu DAO.
 */
@Repository("menuDAO")
public class MenuHibernate extends AbstractDao<Long, Menu> {
 
    /**
     * @param menuKey
     * @return
     */
    public List<Menu> findByMenuKey(String menuKey) {
    	List<Menu> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<Menu> cQuery = cb.createQuery(Menu.class);
    		Root<Menu> c = cQuery.from(Menu.class);
    		cQuery.select(c).where(cb.equal(c.get("menuKey"), menuKey));
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
    public List<Menu> findByMenuKeyWithoutSelf(Menu menu) {
    	List<Menu> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<Menu> cQuery = cb.createQuery(Menu.class);
    		Root<Menu> c = cQuery.from(Menu.class);
    		cQuery.select(c).where(cb.and(cb.equal(c.get("menuKey"), menu.getMenuKey()), cb.notEqual(c.get("id"), menu.getId())));
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
	public List<Menu> findByUserEmail(String email) {
		List<Menu> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Menu> cQuery = cb.createQuery(Menu.class);
			Root<Menu> c = cQuery.from(Menu.class);
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
	 * @return
	 */
	public List<Menu> listSortOrder() {
		List<Menu> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Menu> cQuery = cb.createQuery(Menu.class);
			Root<Menu> c = cQuery.from(Menu.class);
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("orderNum")));
			cQuery.select(c).orderBy(orderList);
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}

	public List<Menu> findByParent(Long parentid) {
		List<Menu> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Menu> cQuery = cb.createQuery(Menu.class);
			Root<Menu> c = cQuery.from(Menu.class);
			cQuery.select(c).where(cb.equal(c.get("parentId"), parentid));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}
}