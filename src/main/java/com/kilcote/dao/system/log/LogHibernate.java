package com.kilcote.dao.system.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.Log;
import com.kilcote.utils.DateUtil;
 
/**
 * Log DAO.
 */
@Repository("logDAO")
public class LogHibernate extends AbstractDao<Long, Log> {
	
	public Page<Log> listPage(PageRequest page, String email, Date dtFrom, Date dtTo) {
		Page<Log> result = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQueryCount = cb.createQuery(Long.class);
			Root<Log> c = criteriaQueryCount.from(Log.class);
			
			Predicate cond = null;
			Predicate condDate = null;
			if (dtFrom != null || dtTo != null) {
				if (dtFrom != null && dtTo != null) {
					condDate = cb.and(cb.greaterThanOrEqualTo(c.get("createTime"), DateUtil.convertToLocalDateTimeViaInstant(dtFrom)), cb.lessThanOrEqualTo(c.get("createTime"), DateUtil.convertToLocalDateTimeViaInstant(dtTo)));
				} else if (dtFrom != null) {
					condDate = cb.greaterThanOrEqualTo(c.get("createTime"), DateUtil.convertToLocalDateTimeViaInstant(dtFrom));
				} else {
					condDate = cb.lessThanOrEqualTo(c.get("createTime"), DateUtil.convertToLocalDateTimeViaInstant(dtTo));
				}
			}
			if (email != null && email.trim().length() > 0) {
				Predicate condEmail = null;
				condEmail = cb.like(c.get("email"), "%" + email.trim() + "%");
				if (condDate != null) {
					cond = cb.and(condEmail, condDate);
				} else {
					cond = condEmail;
				}
			} else {
				if (condDate != null) {
					cond = condDate;
				}
			}
			
			if (cond != null) {
				criteriaQueryCount.select(cb.count(c)).where(cond);
			} else {
				criteriaQueryCount.select(cb.count(c));
			}
			long count = em.createQuery(criteriaQueryCount).getSingleResult();
			
			CriteriaQuery<Log> criteriaQueryClient = cb.createQuery(Log.class);
			c = criteriaQueryClient.from(Log.class);
			
			if (cond != null) {
				criteriaQueryClient.select(c).where(cond);
			} else {
				criteriaQueryClient.select(c);
			}
			
			page = AbstractDao.getPageRequest(page, count);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(c.get("id")));
			criteriaQueryClient.orderBy(orderList);
			
			TypedQuery<Log> query = em.createQuery(criteriaQueryClient);
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
			result = new PageImpl<Log>(query.getResultList(), page, count);
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return result;
	}
}