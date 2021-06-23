package com.kilcote.common.data.theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RootData{
	
	private List<ThemePaletteData> theme = new ArrayList<ThemePaletteData>();
	
	public void addThemeData(ThemePaletteData data) {
		theme.add(data);
	}
}