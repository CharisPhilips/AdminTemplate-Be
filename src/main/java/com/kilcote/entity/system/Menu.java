package com.kilcote.entity.system;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kilcote.entity._base.GenericEntity;
import com.kilcote.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_menu")
public class Menu extends GenericEntity{
	
	public static final Long TOP_MENU_ID = 0L;
	
	public static final String TYPE_MENU = "0";
    public static final String TYPE_BUTTON = "1";
	/**
	 * parent menu ID
	 */
	@Column(name="parent_id")
	private Long parentId;

	/**
	 * menu/button key
	 */
	@Column(name="menu_key", nullable=false, length=64)
	@JsonProperty(value="key")
	private String menuKey;

	/**
	 * menu/button name
	 */
	@Column(name="menu_name", nullable=false, length=64)
	@JsonProperty(value="name")
	private String menuName;
	
	/**
	 * menu/button name(Germany)
	 */
	@Column(name="menu_name_de", nullable=true, length=64)
	@JsonProperty(value="name_de")
	private String menuNameDe;
	
	/**
	 * menu/button name(Czech)
	 */
	@Column(name="menu_name_cs", nullable=true, length=64)
	@JsonProperty(value="name_cs")
	private String menuNameCs;

	/**
	 * menu/button name(Polish)
	 */
	@Column(name="menu_name_pl", nullable=true, length=64)
	@JsonProperty(value="name_pl")
	private String menuNamePl;
	
	/**
	 * menu/button name(Russian)
	 */
	@Column(name="menu_name_ru", nullable=true, length=64)
	@JsonProperty(value="name_ru")
	private String menuNameRu;
	
	/**
	 * menu/button name(English)
	 */
	@Column(name="menu_name_en", nullable=true, length=64)
	@JsonProperty(value="name_en")
	private String menuNameEn;
	
	/**
	 * menu/button name(France)
	 */
	@Column(name="menu_name_fr", nullable=true, length=64)
	@JsonProperty(value="name_fr")
	private String menuNameFr;
	
	/**
	 * menuURL
	 */
	@Column(name="menu_link", length=128)
	@JsonProperty(value="link")
	private String menuPath;

	/**
	 * permsission
	 */
	@Column(name="permission", length=64)
	@JsonProperty(value="permission")
	private String permission;

	/**
	 * icon
	 */
	@Column(name="icon", length=64)
	@JsonProperty(value="icon")
	private String icon;

    /**
     * type
     */
	@Column(name="menu_type", nullable=false, length=4)
	@JsonProperty(value="type")
    private String type;
    
	/**
	 *orderNum
	 */
	@Column(name="order_num")
	@JsonProperty(value="orderNum")
	private Integer orderNum;

	/**
	 * createTime
	 */
	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)
	@Column(name="create_time")
	private LocalDateTime createTime;

	/**
	 * modifyTime
	 */
	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)	
	@Column(name="modify_time")
	private LocalDateTime modifyTime;

	private transient String createTimeFrom;
	private transient String createTimeTo;

}