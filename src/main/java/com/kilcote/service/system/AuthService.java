package com.kilcote.service.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.kilcote.common.constants.StringConstant;
import com.kilcote.common.data.Router;
import com.kilcote.dao.system.login_log.LoginLogHibernate;
import com.kilcote.dao.system.menu.MenuHibernate;
import com.kilcote.dao.system.role_menu.RoleMenuHibernate;
import com.kilcote.dao.system.user.UserHibernate;
import com.kilcote.dao.system.user_role.UserRoleHibernate;
import com.kilcote.entity.system.Menu;
import com.kilcote.entity.system.User;
import com.kilcote.entity.system.UserRole;
import com.kilcote.utils.TokenProviderUtil;
import com.kilcote.utils.TreeUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Transactional
@Service("authService")
public class AuthService implements UserDetailsService {

	@Autowired
	private UserHibernate userHibernate;

	@Autowired
	private UserRoleHibernate userRoleHibernate;

	@Autowired
	private RoleMenuHibernate roleMenuHibernate;

	@Autowired
	private MenuHibernate menuHibernate;
	
	@Autowired
	private LoginLogHibernate loginLogHibernate;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	/**
	 * @param email
	 * @param password
	 * @param authenticationManager 
	 * @return
	 * @throws Exception
	 */
	public User login(String email, String password, AuthenticationManager authenticationManager, String userAgent) throws Exception {
		List<User> users = userHibernate.findByEmail(email);
		if (users!=null && users.size() > 1) {
			throw new Exception("Your account duplicate in db");
		}
		else if (users.size() == 0) { 
			throw new Exception("The account doesn't exist in db"); 
		}
		else {
			User user = users.get(0);
			if (!user.isEnabled()) {
				throw new Exception("The account is not enabled");
			}
			if (bcryptEncoder.matches(password, user.getPassword())) {
				Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = TokenProviderUtil.generateToken(authentication);
				user.setAccessToken(jwt);
				user.setAuthorities(authentication.getAuthorities());
				user.setRoutes(this.getUserRouters(email));

				//update last login time
		        LocalDateTime dtLastLogin = LocalDateTime.now();
				Boolean result = userHibernate.updateLoginTime(email, dtLastLogin);
				if (result) {
				}
				return user;
			}
			else {
				return null;
			}
		}
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean signup(User user) throws Exception {
		List<User>userList = userHibernate.findByEmail(user.getEmail());
		if (userList.size() > 0) {
			throw new Exception("Your email exist already");
		}
		String encodedPassword = bcryptEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setStatus(User.STATUS_DISABLED);
		user.setCreateTime(LocalDateTime.now());
		return userHibernate.add(user);
	}

	/**
	 * @param email
	 */
	public boolean setEnableUser(String email) {
		return userHibernate.updateEnabled(email);
	}

	/**
	 * @param userId
	 * @return
	 */
	public String findUserPermissions(long userId) {
        List<UserRole> userRoles = userRoleHibernate.findByUserId(userId);
        List<Long> roleIdList = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<String> roleMenus = roleMenuHibernate.distinctPermissionByRoleIds(roleIdList);
        return roleMenus.stream().map(p -> p).collect(Collectors.joining(StringConstant.COMMA));
    }

	/**
	 * @param email
	 * @return
	 */
	public List<Router<Menu>> getUserRouters(String email) {
		List<Router<Menu>> routes = new ArrayList<>();
		List<UserRole> userRoles = userRoleHibernate.findByEmail(email);
		List<Long> roleIdList = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
		List<Menu> roleMenus = roleMenuHibernate.distinctRoleMenuIdByRoleIds(roleIdList);
        
		roleMenus.forEach(menu -> {
			Router<Menu> route = new Router<>();
			route.setId(menu.getId());
			route.setKey(menu.getMenuKey());
			route.setParentId(menu.getParentId());
			route.setLink(menu.getMenuPath());
			route.setName(menu.getMenuName());
			route.setNameEn(menu.getMenuNameEn());
			route.setNameDe(menu.getMenuNameDe());
			route.setNameCs(menu.getMenuNameCs());
			route.setNamePl(menu.getMenuNamePl());
			route.setNameRu(menu.getMenuNameRu());
			route.setNameFr(menu.getMenuNameFr());
			route.setIcon(menu.getIcon());
			routes.add(route);
		});
		return TreeUtil.buildVueRouter(routes);
	}
	
	/**
	 *
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		List<User> userList = userHibernate.findByEmail(email);
		if (userList == null || userList.size() <= 0) {
			throw new UsernameNotFoundException("The account not exist in db.");
		}
		User user = userList.get(0);
		String permissions = findUserPermissions(user.getId());
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
		if (StringUtils.isNotBlank(permissions)) {
			grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
		}
		user.setAuthorities(grantedAuthorities);
		user.setRoutes(this.getUserRouters(email));
		return user;
	}

	/**
	 * @param user
	 * @param authenticationManager
	 * @return
	 * @throws Exception 
	 */
	public boolean resetPassword(User user, AuthenticationManager authenticationManager) throws Exception {
		boolean result = false;
		List<User> users = userHibernate.findByEmail(user.getEmail());
		if (users!=null && users.size() > 1) {
			throw new Exception("Your account duplicate in db");
		}
		else if (users.size() == 0) { 
			throw new Exception("The account doesn't exist in db"); 
		}
		else {
			User userDB = users.get(0);
			String encodedPassword = bcryptEncoder.encode(user.getPassword());
			userDB.setPassword(encodedPassword);
			userDB.setModifyTime(LocalDateTime.now());
			result = userHibernate.update(userDB, userDB.getId());
		}
		return result;
	}
	
	/**
	 * @param user
	 * @param authenticationManager
	 * @return
	 * @throws Exception
	 */
	public User changePassword(User user, AuthenticationManager authenticationManager) throws Exception {
		List<User> users = userHibernate.findByEmail(user.getEmail());
		if (users!=null && users.size() > 1) {
			throw new Exception("Your account duplicate in db");
		}
		else if (users.size() == 0) { 
			throw new Exception("The account doesn't exist in db"); 
		}
		else {
			User userDB = users.get(0);
			if (bcryptEncoder.matches(user.getPassword(), userDB.getPassword())) {
				String encodedPassword = bcryptEncoder.encode(user.getNewPassword());
				userDB.setPassword(encodedPassword);
				userDB.setModifyTime(LocalDateTime.now());
				Boolean result = userHibernate.update(userDB, userDB.getId());
				if(result) {
					Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getNewPassword()));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = TokenProviderUtil.generateToken(authentication);
					userDB.setAccessToken(jwt);
					userDB.setAuthorities(authentication.getAuthorities());
					userDB.setRoutes(this.getUserRouters(user.getEmail()));
					return userDB;
				}
			}
		}
		return null;
	}

	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteById(Long id) {
		return userHibernate.deleteByKey(id, "id");
	}

}
