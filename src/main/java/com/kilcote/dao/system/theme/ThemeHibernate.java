package com.kilcote.dao.system.theme;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.kilcote.dao._base.hibernate.AbstractDao;
import com.kilcote.entity.system.ThemePalette;
 
/**
 * themeName DAO.
 */
@Repository("themeDAO")
public class ThemeHibernate extends AbstractDao<Long, ThemePalette> {
 
	/**
	 * @param themeName
	 * @return
	 */
	public List<ThemePalette> findByThemePaletteKey(String themeName) {
		List<ThemePalette> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<ThemePalette> cQuery = cb.createQuery(ThemePalette.class);
			Root<ThemePalette> c = cQuery.from(ThemePalette.class);
			cQuery.select(c).where(cb.equal(c.get("themeKey"), themeName));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}

	/**
	 * @param theme
	 * @return
	 */
	public List<ThemePalette> findByThemePaletteNameWithoutSelf(ThemePalette theme) {
		List<ThemePalette> resultList = null;
		EntityManager em = getEntityManager();
		try {
			CriteriaBuilder cb = getCriteriaBuilder();
			CriteriaQuery<ThemePalette> cQuery = cb.createQuery(ThemePalette.class);
			Root<ThemePalette> c = cQuery.from(ThemePalette.class);
			cQuery.select(c).where(cb.and(cb.equal(c.get("themeKey"), theme.getThemeKey()), cb.notEqual(c.get("id"), theme.getId())));
			resultList = em.createQuery(cQuery).getResultList();
		} catch (HibernateException e) {
    		e.printStackTrace();
		} finally {
			em.close();
		}
		return resultList;
	}

}