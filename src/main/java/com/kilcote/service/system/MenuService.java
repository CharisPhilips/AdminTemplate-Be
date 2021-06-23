package com.kilcote.service.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.kilcote.common.data.MenuTree;
import com.kilcote.common.data.Tree;
import com.kilcote.dao.system.menu.MenuHibernate;
import com.kilcote.dao.system.role_menu.RoleMenuHibernate;
import com.kilcote.dao.system.user_role.UserRoleHibernate;
import com.kilcote.entity.system.Menu;
import com.kilcote.entity.system.RoleMenu;
import com.kilcote.utils.TreeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Transactional
@Service("menuService")
@Getter
public class MenuService {

	@Autowired
	private MenuHibernate menuHibernate;
	
	@Autowired
	private RoleMenuHibernate roleMenuHibernate;
	
	@Autowired
	private UserRoleHibernate userRoleHibernate;

	public List<Menu> findUserMenus(String email) {
		return menuHibernate.findByUserEmail(email);
	}

	/**
	 * @return
	 */
	public List<? extends Tree<?>> findMenus() {
		List<MenuTree> trees = new ArrayList<>();
		try {
			List<Menu> menus = menuHibernate.listSortOrder();
			buildTrees(trees, menus);
			List<? extends Tree<?>> menuTree = TreeUtil.build(trees);
			return menuTree;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return trees;
	}

	/**
	 * @param roleId
	 * @return
	 */
	public List<Menu> listMenuByRoleId(Long roleId) {
		List<RoleMenu> userRoles = roleMenuHibernate.findByRoleId(roleId);
		List<Menu> menuList = userRoles.stream().map(RoleMenu::getMenu).collect(Collectors.toList());
		return menuList;
	}
	
	/**
	 * @param trees
	 * @param menus
	 */
	private void buildTrees(List<MenuTree> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            MenuTree tree = new MenuTree();
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId());
            tree.setMenuKey(menu.getMenuKey());
            tree.setMenuName(menu.getMenuName());
            tree.setMenuNameEn(menu.getMenuNameEn());
            tree.setMenuNameDe(menu.getMenuNameDe());
            tree.setMenuNameCs(menu.getMenuNameCs());
            tree.setMenuNamePl(menu.getMenuNamePl());
            tree.setMenuNameRu(menu.getMenuNameRu());
            tree.setMenuNameFr(menu.getMenuNameFr());
            tree.setMenuPath(menu.getMenuPath());
            tree.setIcon(menu.getIcon());
            tree.setOrderNum(menu.getOrderNum());
            tree.setType(menu.getType());
            tree.setPermission(menu.getPermission());
            trees.add(tree);
        });
    }

	/**
	 * @param id
	 * @return
	 */
	public Menu findById(Long id) {
		return menuHibernate.getByKey(id);
	}
	
	/**
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public boolean addMenu(Menu menu) throws Exception {
		List<Menu> menus = menuHibernate.findByMenuKey(menu.getMenuKey());
		if (menus != null && menus.size() > 0) {
			throw new Exception("Your menu exist already");
		}
		menu.setCreateTime(LocalDateTime.now());
		Boolean result = menuHibernate.add(menu);
		return result;
	}

	/**
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public Boolean updateMenu(Menu menu) throws Exception {
		List<Menu> menus = menuHibernate.findByMenuKeyWithoutSelf(menu);
		if (menus != null && menus.size() > 0) {
			throw new Exception("Your menu exist already");
		}
		Boolean result = menuHibernate.update(menu, menu.getId());
		return result;
	}

	/**
	 * @param id
	 * @return
	 */
	public Boolean deleteById(Long id) {
		return menuHibernate.deleteByKey(id, "id");
	}

	/**
	 * @param ids
	 * @return
	 */
	public Boolean deleteByIds(String[] ids) {
		List<Long> mendIds = new ArrayList<Long>();
		for(String s : ids) {
			mendIds.add(Long.valueOf(s));
		}
		menuHibernate.deleteBatchIds(mendIds);
		this.roleMenuHibernate.deleteByMenuId(mendIds);
		return true;
	}
}

