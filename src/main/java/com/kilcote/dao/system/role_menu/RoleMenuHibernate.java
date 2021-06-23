package com.kilcote.dao.system.role_menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.Menu;
import com.kilcote.entity.system.RoleMenu;
 
/**
 * rolemenu DAO
 */
@Repository("rolemenuDAO")
public class RoleMenuHibernate extends AbstractDao<Long, RoleMenu> {

    /**
     * @param roleId
     * @return
     */
    public List<RoleMenu> findByRoleId(long roleId) {
    	List<RoleMenu> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<RoleMenu> cQuery = cb.createQuery(RoleMenu.class);
    		Root<RoleMenu> c = cQuery.from(RoleMenu.class);
    		cQuery.select(c).where(cb.equal(c.get("role").get("id"), roleId));
    		resultList = em.createQuery(cQuery).getResultList();
    	} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
    }
    
    /**
     * @param roleId
     * @param menuId
     * @return
     */
    public List<RoleMenu> findByRoleIdAndMenuId(long roleId, long menuId) {
    	List<RoleMenu> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<RoleMenu> cQuery = cb.createQuery(RoleMenu.class);
    		Root<RoleMenu> c = cQuery.from(RoleMenu.class);
    		cQuery.select(c).where(cb.and(cb.equal(c.get("role").get("id"), roleId), cb.equal(c.get("menu").get("id"), menuId)));
    		resultList = em.createQuery(cQuery).getResultList();
    	} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
    }

    /**
     * @param roleIds
     * @return
     */
    public List<String> distinctPermissionByRoleIds(List<Long> roleIds) {
    	List<String> resultList = null;
    	if (roleIds == null || roleIds.size() <= 0) {
    		return new ArrayList<String>(); 
    	}
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<String> cQuery = cb.createQuery(String.class);
    		Root<RoleMenu> c = cQuery.from(RoleMenu.class);
    		cQuery.select(c.get("menu").get("permission")).where(cb.and(c.get("role").get("id").in(roleIds), c.get("menu").get("permission").isNotNull())).distinct(true);
    		resultList = em.createQuery(cQuery).getResultList();
    	} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
    }

    /**
     * @param roleIds
     * @return
     */
    public List<Menu> distinctRoleMenuIdByRoleIds(List<Long> roleIds) {
    	List<Menu> resultList = null;
    	if (roleIds == null || roleIds.size() <= 0) {
    		return new ArrayList<Menu>();
    	}
    	EntityManager em = getEntityManager();
    	try {
	    	CriteriaBuilder cb = getCriteriaBuilder();
	    	CriteriaQuery<Menu> cQuery = cb.createQuery(Menu.class);
	    	Root<RoleMenu> c = cQuery.from(RoleMenu.class);
	    	List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("menu").get("orderNum")));
	    	cQuery.select(c.get("menu")).where(cb.and(c.get("role").get("id").in(roleIds), cb.notEqual(c.get("menu").get("type"), Menu.TYPE_BUTTON))).orderBy(orderList).distinct(true);
	    	resultList = em.createQuery(cQuery).getResultList();
    	} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
    }

    /**
     * @param roleId
     * @return
     */
    public boolean deleteByRoleId(Long roleId) {
    	Boolean result = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaDelete<RoleMenu> criteriaDelete = cb.createCriteriaDelete(RoleMenu.class);
    		Root<RoleMenu> c = criteriaDelete.from(RoleMenu.class);
    		criteriaDelete.where(cb.equal(c.get("role").get("id"), roleId));
    		em.getTransaction().begin();
    		em.createQuery(criteriaDelete).executeUpdate();
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
    		CriteriaDelete<RoleMenu> criteriaDelete = cb.createCriteriaDelete(RoleMenu.class);
    		Root<RoleMenu> c = criteriaDelete.from(RoleMenu.class);
    		criteriaDelete.where(c.get("role").get("id").in(roleIds));
    		em.getTransaction().begin();
    		int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
    		em.getTransaction().commit();
    		result = (rowsDeleted > 0);
    	} catch (HibernateException e) {
    		e.printStackTrace();
    		result = false;
		} finally {
			em.close();
		}
    	return result;
	}
	
	/**
	 * @param menuIds
	 * @return
	 */
	public boolean deleteByMenuId(List<Long> menuIds) {
		Boolean result = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaDelete<RoleMenu> criteriaDelete = cb.createCriteriaDelete(RoleMenu.class);
			Root<RoleMenu> c = criteriaDelete.from(RoleMenu.class);
			criteriaDelete.where(c.get("menu").get("id").in(menuIds));
			em.getTransaction().begin();
			int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
			em.getTransaction().commit();
			result = (rowsDeleted > 0);
		} catch (HibernateException e) {
			e.printStackTrace();
			result = false;
		} finally {
			em.close();
		}
		return result;
	}
}