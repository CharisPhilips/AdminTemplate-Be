package com.kilcote.service.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kilcote.dao.system.menu.MenuHibernate;
import com.kilcote.dao.system.role.RoleHibernate;
import com.kilcote.dao.system.role_menu.RoleMenuHibernate;
import com.kilcote.dao.system.user_role.UserRoleHibernate;
import com.kilcote.entity.system.Menu;
import com.kilcote.entity.system.Role;
import com.kilcote.entity.system.RoleMenu;
import com.kilcote.entity.system.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Transactional
@Service("roleService")
public class RoleService {

	@Autowired
	private RoleHibernate roleHibernate;
	
	@Autowired
	private RoleMenuHibernate roleMenuHibernate;
	
	@Autowired
	private UserRoleHibernate userRoleHibernate;
	
	@Autowired
	private MenuHibernate menuHibernate;

	/**
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Role> listRoleByPage(int pageNo, int pageSize) {
		PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		Page<Role> pageResult = roleHibernate.listPage(paging);
		return pageResult;
	}
	
	/**
	 * @return
	 */
	public List<Role> listRoles() {
		List<Role> result = roleHibernate.list();
		return result;
	}
	
	/**
	 * @param userId
	 * @return
	 */
	public List<UserRole> findRolesByUser(long userId) {
        List<UserRole> userRoles = userRoleHibernate.findByUserId(userId);
        return userRoles;
    }
	
	/**
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public boolean addRole(Role role) throws Exception {
		List<Role> roles = roleHibernate.findByRoleName(role.getRoleName());
		if (roles != null && roles.size() > 0) {
			throw new Exception("Your role exist already");
		}
		role.setCreateTime(LocalDateTime.now());
		Boolean result = roleHibernate.add(role);
		if (result) {
			SaveRoleMenu(role);
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public Boolean updateRole(Role role) throws Exception {
		List<Role> roles = roleHibernate.findByRoleNameWithoutSelf(role);
		if (roles != null && roles.size() > 0) {
			throw new Exception("Your role exist already");
		}
		role.setModifyTime(LocalDateTime.now());
		Boolean result = roleHibernate.update(role, role.getId());
		if (result) {
			SaveRoleMenu(role);
		}
		return result;
	}
	
	/**
	 * @param role
	 * @throws Exception
	 */
	private void SaveRoleMenu(Role role) throws Exception {
		List<Long> menuIds = role.getMenuIds();
		if (menuIds != null) {
			if(roleMenuHibernate.deleteByRoleId(role.getId())) {
				for (Long menuId: menuIds) {
					Role addRole = roleHibernate.getByKey(role.getId());
					Menu menu = menuHibernate.getByKey(menuId); 
					RoleMenu rm = new RoleMenu();
					rm.setRole(addRole);
					rm.setMenu(menu);
					addRoleMenu(rm);
				}
			}
		}
	}
	
	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteById(Long id) {
		return roleHibernate.deleteByKey(id, "id");
	}
	
	/**
	 * @param ids
	 * @return
	 */
	public Boolean deleteByIds(String[] ids) {
		List<Long> roleIds = new ArrayList<Long>();
		for(String s : ids) {
			roleIds.add(Long.valueOf(s));
		}
		roleHibernate.deleteBatchIds(roleIds);
		userRoleHibernate.deleteByRoleIds(roleIds);
		return true;
	}
	
	/**
	 * @param roleMenu
	 * @return
	 * @throws Exception
	 */
	public boolean addRoleMenu(RoleMenu roleMenu) throws Exception {
		List<RoleMenu> urList = roleMenuHibernate.findByRoleIdAndMenuId(roleMenu.getRoleId(), roleMenu.getMenuId());
		if (urList != null && urList.size() > 0) {
			return false;
		}
		return roleMenuHibernate.update(roleMenu, roleMenu.getId(), true);
	}

}

