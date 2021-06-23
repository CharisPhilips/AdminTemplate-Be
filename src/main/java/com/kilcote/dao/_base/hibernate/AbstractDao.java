package com.kilcote.dao._base.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.kilcote.utils.OrmUtils;

/**
 * Abstract DAO.
 */
public abstract class AbstractDao<PK extends Serializable, T> {
    
    private final Class<T> persistentClass;
    
    @Autowired
    EntityManagerFactory entityManagerFactory;
    
    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
 
    /**
     * @return
     */
    protected EntityManager getEntityManager() {
    	return this.entityManagerFactory.createEntityManager();
    }
    
    /**
     * @return
     */
    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManagerFactory.getCriteriaBuilder();
    }
    
	/**
	 * @return
	 */
	protected CriteriaQuery<T> createEntityCriteria() {
		return getCriteriaBuilder().createQuery(this.persistentClass);
	}    
 
    /**
     * @param key
     * @return
     */
    public T getByKey(PK key) {
    	EntityManager em = getEntityManager();
        T result = em.find(this.persistentClass, key);
        em.detach(result);
        em.close();
        return result;
    }
    
    /**
     * @return
     */
    public List<T> list() {
    	List<T> resultList = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaQuery<T> cQuery = cb.createQuery(persistentClass);
    		Root<T> c = cQuery.from(persistentClass);
    		cQuery.select(c);
    		resultList = getEntityManager().createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
    	return resultList;
    }
    
    /**
     * @param entity
     * @return
     */
    public Boolean add(T entity) {
    	Boolean result = null;
    	EntityManager em = getEntityManager();
    	try {
    		em.getTransaction().begin();
    		em.persist(entity);
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
     * @param entity
     * @param key
     * @return
     */
    public Boolean update(T entity, PK key) {
    	return update(entity, key, false);
    }
    
    /**
     * @param entity
     * @param key
     * @param isSetNull
     * @return
     */
    public Boolean update(T entity, PK key, boolean isSetNull) {
    	Boolean result = null;
    	T entityEdit = null; 
    	if (!isSetNull) {
    		entityEdit = getByKey(key);
    		OrmUtils.copyNonNullProperties(entity, entityEdit);
    	} else {
    		entityEdit = entity;
    	}
		
    	EntityManager em = getEntityManager();
    	try {
    		em.getTransaction().begin();
    		em.merge(entityEdit);
    		em.getTransaction().commit();
    		result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			em.close();
		}
    	return result;
    }
    
    /**
     * @param key
     * @param pkField
     * @return
     */
    public Boolean deleteByKey(PK key, String pkField) {
    	Boolean result = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(persistentClass);
    		Root<T> root = criteriaDelete.from(persistentClass);
    		criteriaDelete.where(cb.equal(root.get("id"), key));
    		em.getTransaction().begin();
    		em.createQuery(criteriaDelete).executeUpdate();
    		em.getTransaction().commit();
    		result = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
    	} finally {
			em.close();
		}
    	return result;
    }
    
    /**
     * @param key
     * @return
     */
    public Boolean delete(PK key) {
    	Boolean result = null;
    	EntityManager em = getEntityManager();
    	try {
    		em.getTransaction().begin();
    		T entity = em.find(persistentClass, key);
    		em.remove(entity);
    		em.getTransaction().commit();
    		result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			em.close();
		}
    	return result;
    }
    
    /**
     * @param keys
     * @return
     */
    public Boolean deleteBatchIds(List<PK> keys) {
    	Boolean result = null;
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = getCriteriaBuilder();
    		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(persistentClass);
    		Root<T> root = criteriaDelete.from(persistentClass);
    		criteriaDelete.where(root.get("id").in(keys));
    		em.getTransaction().begin();
    		int rowsDeleted = em.createQuery(criteriaDelete).executeUpdate();
    		System.out.println("entities deleted: " + rowsDeleted);
    		em.getTransaction().commit();
    		result = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
		} finally {
			em.close();
		}
    	return result;
    }
    
	/**
	 * @param page
	 * @return
	 */
	public Page<T> listPage(PageRequest page) {
		Page<T> result = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQueryCount = cb.createQuery(Long.class);
			Root<T> root = criteriaQueryCount.from(persistentClass);
			criteriaQueryCount.select(cb.count(root));
			long count = em.createQuery(criteriaQueryCount).getSingleResult();
			
			CriteriaQuery<T> criteriaQueryClient = cb.createQuery(persistentClass);
			root = criteriaQueryClient.from(persistentClass);
			criteriaQueryClient.select(root);
			
			page = AbstractDao.getPageRequest(page, count);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(root.get("id")));
			criteriaQueryClient.orderBy(orderList);
			
			TypedQuery<T> query = em.createQuery(criteriaQueryClient);
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
			result = new PageImpl<T>(query.getResultList(), page, count);
		} catch (Exception e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return result;
	}
	
	/**
	 * calcuate pagesize page number from count newly
	 * @param page
	 * @param count
	 * @return
	 */
	public static PageRequest getPageRequest(PageRequest page, long count) {
		PageRequest result = null;
		int pageLimit = (int) ((count + page.getPageSize() - 1) / page.getPageSize()) + 1;
		if (page.getPageNumber() >= pageLimit - 1) {
			int nPage = pageLimit = 1;
			if (nPage < 1) {
				nPage = 1;
			}
			result = PageRequest.of(pageLimit - 1, page.getPageSize(), page.getSort());
		} else {
			result = page;
		}
		return result;
	}
}