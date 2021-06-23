package com.kilcote.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kilcote.common.data.theme.ColorData;
import com.kilcote.common.data.theme.PaletteData;
import com.kilcote.common.data.theme.RootData;
import com.kilcote.common.data.theme.ThemePaletteData;
import com.kilcote.dao.system.theme.ThemeHibernate;
import com.kilcote.entity.system.ThemePalette;

import lombok.Getter;
@Getter
@Transactional
@Service("themeService")
public class ThemeService {
	
	@Autowired
	private ThemeHibernate themeHibernate;

	public RootData listThemeBean() {
		List<ThemePalette> dbThemes = themeHibernate.list();
		RootData result = convertDb2RootData(dbThemes);
		return result;
	}
	
	public List<ThemePalette> listTheme() {
		List<ThemePalette> dbThemes = themeHibernate.list();
		return dbThemes;
	}
	

	public ThemePaletteData findById(Long id) {
		ThemePalette data = themeHibernate.getByKey(id);
		return convertDb2Data(data);
	}

	/**
	 * @param theme
	 * @return
	 * @throws Exception
	 */
	public boolean addTheme(ThemePalette theme) throws Exception {
		List<ThemePalette> themes = themeHibernate.findByThemePaletteKey(theme.getThemeKey());
		if (themes != null && themes.size() > 0) {
			throw new Exception("Your role exist already");
		}
		Boolean result = themeHibernate.add(theme);
		return result;
	}


	/**
	 * @param theme
	 * @return
	 * @throws Exception
	 */
	public Long addTheme(ThemePaletteData theme) throws Exception {
		ThemePalette dbInfo = convertData2Db(theme);
		if (addTheme(dbInfo)) {
			return dbInfo.getId();
		}
		return null;
	}

	/**
	 * @param theme
	 * @return
	 * @throws Exception
	 */
	public void addTheme(RootData theme) throws Exception {
		List<ThemePaletteData> themeDatas = theme.getTheme();
		for (int i = 0; i < themeDatas.size(); i++) {
			try {
				addTheme(convertData2Db((ThemePaletteData)themeDatas.get(i)));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param theme
	 * @return
	 * @throws Exception
	 */
	public boolean updateTheme(ThemePaletteData theme) throws Exception {
		return updateTheme(convertData2Db(theme));
	}

	
	/**
	 * @param theme
	 * @return
	 * @throws Exception
	 */
	public Boolean updateTheme(ThemePalette theme) throws Exception {
		List<ThemePalette> themes = themeHibernate.findByThemePaletteNameWithoutSelf(theme);
		if (themes != null && themes.size() > 0) {
			throw new Exception("The theme exist already");
		}
		Boolean result = themeHibernate.update(theme, theme.getId());
		return result;
	}

	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteTheme(Long id) {
		return themeHibernate.deleteByKey(id, "id");
	}
	
	/**
	 * @param themeIds
	 * @return
	 */
	public Boolean deleteByIds(String[] ids) {
		List<Long> themeIds = new ArrayList<Long>();
		for(String s : ids) {
			themeIds.add(Long.valueOf(s));
		}
		return themeHibernate.deleteBatchIds(themeIds);
	}
	
	/**
	 * @param dbThemes
	 * @return
	 */
	public RootData convertDb2RootData(List<ThemePalette> dbThemes) {
		RootData result = new RootData();
		for (int i = 0; i < dbThemes.size(); i++) {
			ThemePalette dbTheme = dbThemes.get(i);
			ThemePaletteData theme = convertDb2Data(dbTheme);
			result.addThemeData(theme);
		}
		return result;
	}
	
	
	private ThemePaletteData convertDb2Data(ThemePalette dbTheme) {
		ThemePaletteData theme = new ThemePaletteData();
		theme.setId(dbTheme.getId());
		theme.setKey(dbTheme.getThemeKey());
		theme.setName(dbTheme.getThemeName());
		
		PaletteData themePalette = new PaletteData();
		ColorData primary = new ColorData();
		primary.setLight(dbTheme.getThemePrimaryLight());
		primary.setMain(dbTheme.getThemePrimaryMain());
		primary.setDark(dbTheme.getThemePrimaryDark());
		primary.setContrastText(dbTheme.getThemePrimaryContrast());
		themePalette.setPrimary(primary);
		ColorData secondary = new ColorData();
		secondary.setLight(dbTheme.getThemeSecondaryLight());
		secondary.setMain(dbTheme.getThemeSecondaryMain());
		secondary.setDark(dbTheme.getThemeSecondaryDark());
		secondary.setContrastText(dbTheme.getThemeSecondaryContrast());
		themePalette.setSecondary(secondary);
		
		theme.setThemePalette(themePalette);
		
		PaletteData lightPalette = new PaletteData();
		primary = new ColorData();
		primary.setLight(dbTheme.getLightPrimaryLight());
		primary.setMain(dbTheme.getLightPrimaryMain());
		primary.setDark(dbTheme.getLightPrimaryDark());
		primary.setContrastText(dbTheme.getLightPrimaryContrast());
		lightPalette.setPrimary(primary);
		secondary = new ColorData();
		secondary.setLight(dbTheme.getLightSecondaryLight());
		secondary.setMain(dbTheme.getLightSecondaryMain());
		secondary.setDark(dbTheme.getLightSecondaryDark());
		secondary.setContrastText(dbTheme.getLightSecondaryContrast());
		lightPalette.setSecondary(secondary);
		
		theme.setLightPalette(lightPalette);
		
		PaletteData darkPalette = new PaletteData();
		primary = new ColorData();
		primary.setLight(dbTheme.getDarkPrimaryLight());
		primary.setMain(dbTheme.getDarkPrimaryMain());
		primary.setDark(dbTheme.getDarkPrimaryDark());
		primary.setContrastText(dbTheme.getDarkPrimaryContrast());
		darkPalette.setPrimary(primary);
		secondary = new ColorData();
		secondary.setLight(dbTheme.getDarkSecondaryLight());
		secondary.setMain(dbTheme.getDarkPrimaryMain());
		secondary.setDark(dbTheme.getDarkPrimaryDark());
		secondary.setContrastText(dbTheme.getDarkPrimaryContrast());
		darkPalette.setSecondary(secondary);
		
		theme.setDarkPalette(darkPalette);
		return theme;
	}


	/**
	 * @param theme
	 * @return
	 */
	public ThemePalette convertData2Db(ThemePaletteData theme) {
		
		ThemePalette result = new ThemePalette();
		
		result.setId(theme.getId());
		result.setThemeKey(theme.getKey());
		result.setThemeName(theme.getName());
		
		result.setThemePrimaryLight(theme.getThemePalette().getPrimary().getLight());
		result.setThemePrimaryMain(theme.getThemePalette().getPrimary().getMain());
		result.setThemePrimaryDark(theme.getThemePalette().getPrimary().getDark());
		result.setThemePrimaryContrast(theme.getThemePalette().getPrimary().getContrastText());
		
		result.setThemeSecondaryLight(theme.getThemePalette().getSecondary().getLight());
		result.setThemeSecondaryMain(theme.getThemePalette().getSecondary().getMain());
		result.setThemeSecondaryDark(theme.getThemePalette().getSecondary().getDark());
		result.setThemeSecondaryContrast(theme.getThemePalette().getSecondary().getContrastText());
		
		result.setLightPrimaryLight(theme.getLightPalette().getPrimary().getLight());
		result.setLightPrimaryMain(theme.getLightPalette().getPrimary().getMain());
		result.setLightPrimaryDark(theme.getLightPalette().getPrimary().getDark());
		result.setLightPrimaryContrast(theme.getLightPalette().getPrimary().getContrastText());
		
		result.setLightSecondaryLight(theme.getLightPalette().getSecondary().getLight());
		result.setLightSecondaryMain(theme.getLightPalette().getSecondary().getMain());
		result.setLightSecondaryDark(theme.getLightPalette().getSecondary().getDark());
		result.setLightSecondaryContrast(theme.getLightPalette().getSecondary().getContrastText());
		
		result.setDarkPrimaryLight(theme.getDarkPalette().getPrimary().getLight());
		result.setDarkPrimaryMain(theme.getDarkPalette().getPrimary().getMain());
		result.setDarkPrimaryDark(theme.getDarkPalette().getPrimary().getDark());
		result.setDarkPrimaryContrast(theme.getDarkPalette().getPrimary().getContrastText());

		result.setDarkSecondaryLight(theme.getDarkPalette().getSecondary().getLight());
		result.setDarkSecondaryMain(theme.getDarkPalette().getSecondary().getMain());
		result.setDarkSecondaryDark(theme.getDarkPalette().getSecondary().getDark());
		result.setDarkSecondaryContrast(theme.getDarkPalette().getSecondary().getContrastText());
		
		return result;
	}
}

