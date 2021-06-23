package com.kilcote.entity.system;

import java.util.*;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kilcote.entity._base.GenericEntity;
import com.kilcote.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.kilcote.common.data.Router;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="t_user")
public class User extends GenericEntity implements UserDetails
{
	private static final long serialVersionUID = -6226762743823910935L;
	
	public static final int STATUS_DISABLED = 0;
	public static final int STATUS_ENABLED = 1;
	
	@JsonProperty(value="email")
	@NotNull(message = "{error.email.required}")
	@Email(message = "{user.email.invalid}")
    @Column(name="email", nullable=false, length=128)
	private String email;
	
    @JsonIgnore
	@JsonProperty(value="password", access=Access.WRITE_ONLY)
    @Column(name="password", nullable=false, length=256)
    private String password;
    
    @Min(0)
	@Max(1)
    @Column(name="status", nullable=true, length=4)
	private Integer status; //null, 0=no active, 1: active(basic), ... 
    
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<UserRole> roles;
    
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
	
	/**
	 * lastLoginTime
	 */
	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)	
	@Column(name="last_time")
	private LocalDateTime lastTime;
	
	/**
	 * avatar
	 */
	@JsonProperty(value="avatar")
    @Column(name="avatar", nullable=true, length=256)
	private String avatar;
	
	/**
	 * avatar
	 */
	@JsonProperty(value="description")
    @Column(name="description", nullable=true, length=256)
	private String description;
    
    @Transient
    private String rolesStr;
    
    @Transient
    private List<Long> roleIds;
    
    @Transient
    private String newPassword;
    
    @Transient
    private Collection<? extends GrantedAuthority> authorities;
    
    @Transient
    private String accessToken;
    
    @Transient
    private List<Router<Menu>> routes;
    
//    @Transient
//    private MultipartFile imgAvatar;
    
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return this.authorities; }

    @Transient
    @JsonIgnore
    @Override
    public String getUsername() { return this.email; }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() { return true; }
    
    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() { return true; }

    @Transient
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Transient
    @JsonIgnore
    @Override
    public boolean isEnabled() { return true; }
}