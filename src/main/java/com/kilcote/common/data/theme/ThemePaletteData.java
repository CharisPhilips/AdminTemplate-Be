package com.kilcote.common.data.theme;

import lombok.Data;

@Data
public class ThemePaletteData {
	
	private Long id;
	private String key;
	private String name;
	
	private PaletteData themePalette;
	private	PaletteData	lightPalette;
	private	PaletteData	darkPalette;
}
