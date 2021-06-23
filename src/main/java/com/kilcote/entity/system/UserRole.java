package com.kilcote.entity.system;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kilcote.entity._base.GenericEntity;

import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_user_role")
public class UserRole extends GenericEntity {

	@Setter
	@JsonIgnore
	@ManyToOne(targetEntity=User.class, cascade={CascadeType.ALL})
	@JoinColumn(name="user_id", nullable=true)
	private User user;
	
	@Setter
	@JsonIgnore
	@ManyToOne(targetEntity=Role.class, cascade={CascadeType.ALL})
	@JoinColumn(name="role_id", nullable=false)
    private Role role;

	@Transient
	@JsonIgnore
	public Role getRole() { return role; }
	
	@Transient
	@JsonIgnore
	public User getUser() { return user; }
	
	@Transient
	public Long getUserId() {
		if (user != null) {
			return user.getId();
		}
		return null;
	}
	@Transient
	public Long getRoleId() {
		if (role != null) {
			return role.getId();
		}
		return null;
	}
	
	@Transient
	public String getUserEmail() {
		if (user != null) {
			return user.getEmail();
		}
		return null;
	}
	
	@Transient
	public String getRoleName() {
		if (role != null) {
			return role.getRoleName();
		}
		return null;
	}
}