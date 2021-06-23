package com.kilcote.common.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kilcote.entity.system.Menu;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends Tree<Menu> {
	@JsonProperty(value="link")
    private String menuPath;
	@JsonProperty(value="name")
    private String menuName;
	@JsonProperty(value="name_en")
	private String menuNameEn;
	@JsonProperty(value="name_de")
	private String menuNameDe;
	@JsonProperty(value="name_cs")
	private String menuNameCs;
	@JsonProperty(value="name_pl")
	private String menuNamePl;
	@JsonProperty(value="name_ru")
	private String menuNameRu;
	@JsonProperty(value="name_fr")
	private String menuNameFr;
	@JsonProperty(value="key")
	private String menuKey;
	@JsonProperty(value="permission")
    private String permission;
	@JsonProperty(value="icon")
    private String icon;
	@JsonProperty(value="type")
    private String type;
	@JsonProperty(value="orderNum")
    private Integer orderNum;
}
