package com.kilcote.service.system;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kilcote.common.ConstantAdminTemplate;
import com.kilcote.common.Global;
import com.kilcote.common.constants.StringConstant;
import com.kilcote.dao.system.menu.MenuHibernate;
import com.kilcote.dao.system.role.RoleHibernate;
import com.kilcote.dao.system.role_menu.RoleMenuHibernate;
import com.kilcote.dao.system.user.UserHibernate;
import com.kilcote.dao.system.user.UserJpaRepository;
import com.kilcote.dao.system.user.UserMapper;
import com.kilcote.dao.system.user_role.UserRoleHibernate;
import com.kilcote.entity.system.Role;
import com.kilcote.entity.system.User;
import com.kilcote.entity.system.UserRole;
import com.kilcote.utils.FileUtil;

import lombok.Getter;

@Getter
@Transactional
@Service("userService")
public class UserService {

	@Autowired
	private UserHibernate userHibernate;
	@Autowired
	private UserJpaRepository userJpa;
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserRoleHibernate userRoleHibernate;

	@Autowired
	private RoleHibernate roleHibernate;
	
	@Autowired
	private RoleMenuHibernate roleMenuHibernate;
	
	@Autowired
	private MenuHibernate menuHibernate;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	/**
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<User> listUserByPage(int pageNo, int pageSize) {
		PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		Page<User> pageResult = userHibernate.listPage(paging);
		for (User user: pageResult.getContent()) {
			List<UserRole> urList = userRoleHibernate.findByUserId(user.getId());
			user.setRoles(urList);
			if (ConstantAdminTemplate.isTestMode) {
				String rolesStr = urList.stream().map(p -> p.getRole().getRoleName()).collect(Collectors.joining(StringConstant.COMMA));
				user.setRolesStr(rolesStr);
			}
		}
		return pageResult;
	}

	/**
	 * @param userId
	 * @return
	 */
	public User findById(long userId) {
		//jpa
		return userHibernate.getByKey(userId);
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public boolean addUser(User user, MultipartFile fileAvatar) throws Exception {
		List<User>userList = userHibernate.findByEmail(user.getEmail());
		if (userList.size() > 0) {
			throw new Exception("Your email exist already");
		}
		String encodedPassword = bcryptEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		List<UserRole> urList = user.getRoles();
		user.setRoles(null);
		user.setCreateTime(LocalDateTime.now());
		
		Boolean result = userHibernate.add(user);
		if (fileAvatar != null && FileUtil.saveAvatarImage(fileAvatar, user.getId())) {
			user.setAvatar(Global.getAvatarHttpUrl(user.getId()));
			result = userHibernate.update(user, user.getId());
		}
		if (result) {
			user.setRoles(urList);
			SaveUserRole(user);
		}
		return result;
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public boolean updateUser(User user, MultipartFile fileAvatar) throws Exception {
		List<User> users = userHibernate.findByEmailWithoutSelf(user);
		if (users != null && users.size() > 0) {
			throw new Exception("Your account exist already");
		}
		if (user.getPassword() != null && user.getPassword().trim().length() > 0) {
			String encodedPassword = bcryptEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
		}
		List<UserRole> urList = user.getRoles();
		user.setRoles(null);
		user.setModifyTime(LocalDateTime.now());
		
		if (fileAvatar != null && FileUtil.saveAvatarImage(fileAvatar, user.getId())) {
			user.setAvatar(Global.getAvatarHttpUrl(user.getId()));
		}
		Boolean result = userHibernate.update(user, user.getId());
		if (result) {
			user.setRoles(urList);
			SaveUserRole(user);
		}
		return result;
	}

	/**
	 * @param user
	 * @throws Exception
	 */
	private void SaveUserRole(User user) throws Exception {
		List<Long> roleIds = user.getRoleIds();
		if (roleIds != null) {
			if(userRoleHibernate.deleteByUserId(user.getId())) {
				for (Long roleId: roleIds) {
					User addUser = userHibernate.getByKey(user.getId());
					Role role = roleHibernate.getByKey(roleId); 
					UserRole ur = new UserRole();
					ur.setUser(addUser);
					ur.setRole(role);
					addUserRole(ur);
				}
			}
		}
	}

	/**
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public boolean addUserRole(UserRole userRole) throws Exception {
		List<UserRole> urList = userRoleHibernate.findByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());
		if (urList != null && urList.size() > 0) {
			return false;
		}
		Boolean result = userRoleHibernate.update(userRole, userRole.getId(), true);
		return result;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteUser(Long id) {
		return userHibernate.deleteByKey(id, "id");
	}

}
