package com.kilcote.entity.system;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kilcote.entity._base.GenericEntity;

import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_role_menu")
public class RoleMenu extends GenericEntity {

	
	@Setter
	@ManyToOne(targetEntity=Role.class, cascade={CascadeType.ALL})
	@JoinColumn(name="role_id", nullable=false)
	private Role role;
	
	@Setter
	@ManyToOne(targetEntity=Menu.class, cascade={CascadeType.ALL})
	@JoinColumn(name="menu_id", nullable=false)
    private Menu menu;
	
	@Transient
	private String menuPermission;
	
	@JsonProperty
	@Transient
	public Role getRole() { return role; }
	
	@JsonProperty
	@Transient
	public Menu getMenu() { return menu; }
	
	@Transient
	public String getMenuPermission() {
		if (menu != null) {
			return menu.getPermission();
		}
		return null;
	}

	public Long getRoleId() {
		if (role != null) {
			return role.getId();
		}
		return null;
	}

	public Long getMenuId() {
		if (menu != null) {
			return menu.getId();
		}
		return null;
	}

}