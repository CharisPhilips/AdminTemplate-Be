package com.kilcote.bootstrap;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilcote.common.data.theme.RootData;
import com.kilcote.entity.system.Ip2Location;
import com.kilcote.entity.system.Menu;
import com.kilcote.entity.system.Role;
import com.kilcote.entity.system.RoleMenu;
import com.kilcote.entity.system.User;
import com.kilcote.entity.system.UserRole;
import com.kilcote.service._BootstrapService;
import com.kilcote.utils.StringUtil;

public class BootstrapThread extends Thread{
	
	private _BootstrapService service;
	
	public void setService(_BootstrapService apiService) {
		this.service = apiService;
	}
	
	@Override
	public void run() {
//		String ip = "112.42.231.181";
//		String ip = "209.58.133.152";
//		List<Ip2Location> ips = service.getIp2LocationService().getIp2LocationHibernate().findByIp(StringUtil.getValidIp(ip));
		
		boolean isInitDatabase = false;
		boolean isInitTheme = false;
		boolean isInitIp2Location = false;
		if (isInitDatabase) {
			//apend test user
			User admin = addAdminUser();
			User tester = addTestUser();
			//append init role
			Role roleAdmin = addAdminRole();
			Role roleTester = addTestRole();
			//append init menu
			Menu menuSystem = addSystemMenu(roleAdmin);
			Menu menuHome= addHomeMenu(roleAdmin);
			
			addRole2User(admin, roleAdmin, roleTester);
			addRole2User(tester, roleTester);
		}
		if (isInitTheme) {
			readThemeJSonWriteDB();
		}
		if(isInitIp2Location) {
			readIp2LocationJSonWriteDB();
		}
	}

	private User addAdminUser() {
		try {
			List<User> users = service.getAuthService().getUserHibernate().findByEmail("admin@gmail.com");
			if (users != null && users.size() > 0 ) {
				return users.get(0);
			}
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setPassword("test");
			user.setStatus(user.STATUS_ENABLED);
			service.getUserService().addUser(user, null);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private User addTestUser() {
		try {
			List<User> users = service.getAuthService().getUserHibernate().findByEmail("test@gmail.com");
			if (users != null && users.size() > 0 ) {
				return users.get(0);
			}
			User user = new User();
			user.setEmail("test@gmail.com");
			user.setPassword("test");
			user.setStatus(user.STATUS_ENABLED);
			service.getUserService().addUser(user, null);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Menu addSystemMenu(Role role) {
		try {
			List<Menu> menus = service.getMenuService().getMenuHibernate().findByMenuKey("admin-system");
			if (menus != null && menus.size() > 0 ) {
				return menus.get(0);
			}
			
			Menu systemMenu = new Menu();
			systemMenu.setMenuKey("admin-system");
			systemMenu.setMenuName("System");
			systemMenu.setMenuNameDe("System");
			systemMenu.setMenuNameCs("Systém");
			systemMenu.setMenuNamePl("System");
			systemMenu.setMenuNameRu("Система");
			systemMenu.setMenuNameEn("System");
			systemMenu.setMenuNameFr("Système");
			
			systemMenu.setIcon("ios-settings");
			systemMenu.setPermission("system:view");
			systemMenu.setType(Menu.TYPE_MENU);
			systemMenu.setOrderNum(11);
			service.getMenuService().addMenu(systemMenu);
			
			//menu
			Menu menuMenu = new Menu();
			menuMenu.setMenuKey("admin-system-menu");
			menuMenu.setMenuName("Menu Manage");
			menuMenu.setMenuNameDe("Menü Verwalten");
			menuMenu.setMenuNameCs("Nabídka Spravovat");
			menuMenu.setMenuNamePl("Menu Zarządzaj.");
			menuMenu.setMenuNameRu("Управление Меню");
			menuMenu.setMenuNameEn("Menu Manage");
			menuMenu.setMenuNameFr("Menu Gérer");
			menuMenu.setMenuPath("/app/admin/system/menu");
			menuMenu.setIcon("ios-menu");
			menuMenu.setType(Menu.TYPE_MENU);
			menuMenu.setPermission("menu:view");
			menuMenu.setOrderNum(1);
			menuMenu.setParentId(systemMenu.getId());
			service.getMenuService().addMenu(menuMenu);
			
			Menu menuAddButton = new Menu();
			menuAddButton.setMenuKey("admin-system-menu-add");
			menuAddButton.setMenuName("Add Menu");
			menuAddButton.setMenuNameDe("Menü hinzufügen");
			menuAddButton.setMenuNameCs("Přidat Nabídku");
			menuAddButton.setMenuNamePl("Dodaj Menu");
			menuAddButton.setMenuNameRu("Добавить меню");
			menuAddButton.setMenuNameEn("Add Menu");
			menuAddButton.setMenuNameFr("Ajouter un Menu");
			menuAddButton.setPermission("menu:add");
			menuAddButton.setType(Menu.TYPE_BUTTON);
			menuAddButton.setOrderNum(1);
			menuAddButton.setParentId(menuMenu.getId());
			service.getMenuService().addMenu(menuAddButton);
			
			Menu menuUpdateButton = new Menu();
			menuUpdateButton.setMenuKey("admin-system-menu-update");
			menuUpdateButton.setMenuName("Update Menu");
			menuUpdateButton.setMenuNameDe("Menü aktualisieren");
			menuUpdateButton.setMenuNameCs("Aktualizujte nabídku");
			menuUpdateButton.setMenuNamePl("Menu aktualizacji");
			menuUpdateButton.setMenuNameRu("Обновить меню");
			menuUpdateButton.setMenuNameEn("Update Menu");
			menuUpdateButton.setMenuNameFr("Menu de mise à jour");
			menuUpdateButton.setPermission("menu:update");
			menuUpdateButton.setType(Menu.TYPE_BUTTON);
			menuUpdateButton.setOrderNum(2);
			menuUpdateButton.setParentId(menuMenu.getId());
			service.getMenuService().addMenu(menuUpdateButton);
			
			Menu menuDeleteButton = new Menu();
			menuDeleteButton.setMenuKey("admin-system-menu-delete");
			menuDeleteButton.setMenuName("Delete Menu");
			menuDeleteButton.setMenuNameDe("Menü Löschen");
			menuDeleteButton.setMenuNameCs("Smazat nabídku");
			menuDeleteButton.setMenuNamePl("Usuń menu");
			menuDeleteButton.setMenuNameRu("Удалить Меню");
			menuDeleteButton.setMenuNameEn("Delete Menu");
			menuDeleteButton.setMenuNameFr("Supprimer le Menu");
			menuDeleteButton.setPermission("menu:delete");
			menuDeleteButton.setType(Menu.TYPE_BUTTON);
			menuDeleteButton.setOrderNum(3);
			menuDeleteButton.setParentId(menuMenu.getId());
			service.getMenuService().addMenu(menuDeleteButton);
			
			//role
			Menu roleMenu = new Menu();
			roleMenu.setMenuKey("admin-system-role");
			roleMenu.setMenuName("Role Manage");
			roleMenu.setMenuNameDe("Rollenverwaltung");
			roleMenu.setMenuNameCs("Správa rolí");
			roleMenu.setMenuNamePl("Zarządzanie rolami");
			roleMenu.setMenuNameRu("Роль Управление");
			roleMenu.setMenuNameEn("Role Manage");
			roleMenu.setMenuNameFr("Gestion des rôles");
			roleMenu.setMenuPath("/app/admin/system/role");
			roleMenu.setIcon("ios-build");
			roleMenu.setType(Menu.TYPE_MENU);
			roleMenu.setPermission("role:view");
			roleMenu.setParentId(systemMenu.getId());
			roleMenu.setOrderNum(2);
			service.getMenuService().addMenu(roleMenu);
			
			Menu roleAddButton = new Menu();
			roleAddButton.setMenuKey("admin-system-role-add");
			roleAddButton.setMenuName("Add Role");
			roleAddButton.setMenuNameDe("Rolle hinzufügen");
			roleAddButton.setMenuNameCs("Přidat roli");
			roleAddButton.setMenuNamePl("Dodaj rolę");
			roleAddButton.setMenuNameRu("Добавить роль");
			roleAddButton.setMenuNameEn("Add Role");
			roleAddButton.setMenuNameFr("Ajouter un rôle");
			roleAddButton.setPermission("role:add");
			roleAddButton.setType(Menu.TYPE_BUTTON);
			roleAddButton.setOrderNum(1);
			roleAddButton.setParentId(roleMenu.getId());
			service.getMenuService().addMenu(roleAddButton);
			
			Menu roleUpdateButton = new Menu();
			roleUpdateButton.setMenuKey("admin-system-role-update");
			roleUpdateButton.setMenuName("Update Role");
			roleUpdateButton.setMenuNameDe("Rolle aktualisieren");
			roleUpdateButton.setMenuNameCs("Aktualizujte roli");
			roleUpdateButton.setMenuNamePl("Zaktualizuj rolę");
			roleUpdateButton.setMenuNameRu("Обновить роль");
			roleUpdateButton.setMenuNameEn("Update Role");
			roleUpdateButton.setMenuNameFr("Mettre à jour le rôle");
			roleUpdateButton.setPermission("role:update");
			roleUpdateButton.setType(Menu.TYPE_BUTTON);
			roleUpdateButton.setOrderNum(2);
			roleUpdateButton.setParentId(roleMenu.getId());
			service.getMenuService().addMenu(roleUpdateButton);
			
			Menu roleDeleteButton = new Menu();
			roleDeleteButton.setMenuKey("admin-system-role-delete");
			roleDeleteButton.setMenuName("Delete Role");
			roleDeleteButton.setMenuNameDe("Rolle löschen");
			roleDeleteButton.setMenuNameCs("Smazat roli");
			roleDeleteButton.setMenuNamePl("Usuń rolę");
			roleDeleteButton.setMenuNameRu("Удалить роль");
			roleDeleteButton.setMenuNameEn("Delete Role");
			roleDeleteButton.setMenuNameFr("Supprimer le rôle");
			roleDeleteButton.setPermission("role:delete");
			roleDeleteButton.setType(Menu.TYPE_BUTTON);
			roleDeleteButton.setOrderNum(3);
			roleDeleteButton.setParentId(roleMenu.getId());
			service.getMenuService().addMenu(roleDeleteButton);
			
			//user
			Menu userMenu = new Menu();
			userMenu.setMenuKey("admin-system-user");
			userMenu.setMenuName("User Manage");
			userMenu.setMenuNameDe("Benutzer verwalten");
			userMenu.setMenuNameCs("Správa uživatelů");
			userMenu.setMenuNamePl("Zarządzaj użytkownikami");
			userMenu.setMenuNameRu("Управление пользователями");
			userMenu.setMenuNameEn("User Manage");
			userMenu.setMenuNameFr("Gérer les utilisateurs");
			userMenu.setMenuPath("/app/admin/system/user");
			userMenu.setIcon("ios-person");
			userMenu.setPermission("user:view");
			userMenu.setType(Menu.TYPE_MENU);
			userMenu.setOrderNum(3);
			userMenu.setParentId(systemMenu.getId());
			service.getMenuService().addMenu(userMenu);
			
			Menu userAddButton = new Menu();
			userAddButton.setMenuKey("admin-system-user-add");
			userAddButton.setMenuName("Add User");
			userAddButton.setMenuNameDe("Nutzer hinzufügen");
			userAddButton.setMenuNameCs("Přidat uživatele");
			userAddButton.setMenuNamePl("Dodaj użytkownika");
			userAddButton.setMenuNameRu("Добавить пользователя");
			userAddButton.setMenuNameEn("Add User");
			userAddButton.setMenuNameFr("Ajouter un utilisateur");
			userAddButton.setPermission("user:add");
			userAddButton.setType(Menu.TYPE_BUTTON);
			userAddButton.setOrderNum(1);
			userAddButton.setParentId(userMenu.getId());
			service.getMenuService().addMenu(userAddButton);
					
			Menu userUpdateButton = new Menu();
			userUpdateButton.setMenuKey("admin-system-user-update");
			userUpdateButton.setMenuName("Update User");
			userUpdateButton.setMenuNameDe("Benutzer aktualisieren");
			userUpdateButton.setMenuNameCs("Aktualizujte uživatele");
			userUpdateButton.setMenuNamePl("Zaktualizuj użytkownika");
			userUpdateButton.setMenuNameRu("Обновить пользователя");
			userUpdateButton.setMenuNameEn("Update User");
			userUpdateButton.setMenuNameFr("Mettre à jour l'utilisateur");
			userUpdateButton.setPermission("user:update");
			userUpdateButton.setType(Menu.TYPE_BUTTON);
			userUpdateButton.setOrderNum(2);
			userUpdateButton.setParentId(userMenu.getId());
			service.getMenuService().addMenu(userUpdateButton);
			
			Menu userDeleteButton = new Menu();
			userDeleteButton.setMenuKey("admin-system-user-delete");
			userDeleteButton.setMenuName("Delete User");
			userDeleteButton.setMenuNameDe("Benutzer löschen");
			userDeleteButton.setMenuNameCs("Smazat uživatele");
			userDeleteButton.setMenuNamePl("Usuń użytkownika");
			userDeleteButton.setMenuNameRu("Удалить пользователя");
			userDeleteButton.setMenuNameEn("Delete User");
			userDeleteButton.setMenuNameFr("Supprimer l'utilisateur");
			userDeleteButton.setPermission("user:delete");
			userDeleteButton.setType(Menu.TYPE_BUTTON);
			userDeleteButton.setOrderNum(3);
			userDeleteButton.setParentId(userMenu.getId());
			service.getMenuService().addMenu(userDeleteButton);
			
			//theme
			Menu themeMenu = new Menu();
			themeMenu.setMenuKey("admin-system-theme");
			themeMenu.setMenuName("Theme Manage");
			themeMenu.setMenuNameDe("Thema verwalten");
			themeMenu.setMenuNameCs("Téma Spravovat");
			themeMenu.setMenuNamePl("Zarządzanie motywam");
			themeMenu.setMenuNameRu("Управление темами");
			themeMenu.setMenuNameEn("Theme Manage");
			themeMenu.setMenuNameFr("Thème Gérer");
			themeMenu.setMenuPath("/app/admin/system/theme");
			themeMenu.setIcon("ios-color-palette");
			themeMenu.setPermission("theme:view");
			themeMenu.setType(Menu.TYPE_MENU);
			themeMenu.setOrderNum(4);
			themeMenu.setParentId(systemMenu.getId());
			service.getMenuService().addMenu(themeMenu);
			
			Menu themeAddButton = new Menu();
			themeAddButton.setMenuKey("admin-system-theme-add");
			themeAddButton.setMenuName("Add Theme");
			themeAddButton.setMenuNameDe("Thema hinzufügen");
			themeAddButton.setMenuNameCs("Přidat motiv");
			themeAddButton.setMenuNamePl("Dodaj motyw");
			themeAddButton.setMenuNameRu("Добавить тему");
			themeAddButton.setMenuNameEn("Add Theme");
			themeAddButton.setMenuNameFr("Ajouter un thème");
			themeAddButton.setPermission("theme:add");
			themeAddButton.setType(Menu.TYPE_BUTTON);
			themeAddButton.setOrderNum(1);
			themeAddButton.setParentId(themeMenu.getId());
			service.getMenuService().addMenu(themeAddButton);
					
			Menu themeUpdateButton = new Menu();
			themeUpdateButton.setMenuKey("admin-system-theme-update");
			themeUpdateButton.setMenuName("Update Theme");
			themeUpdateButton.setMenuNameDe("Thema aktualisieren");
			themeUpdateButton.setMenuNameCs("Aktualizace motivu");
			themeUpdateButton.setMenuNamePl("Zaktualizuj motyw");
			themeUpdateButton.setMenuNameRu("Обновить тему");
			themeUpdateButton.setMenuNameEn("Update Theme");
			themeUpdateButton.setMenuNameFr("Mettre à jour le thème");
			themeUpdateButton.setPermission("theme:update");
			themeUpdateButton.setType(Menu.TYPE_BUTTON);
			themeUpdateButton.setOrderNum(2);
			themeUpdateButton.setParentId(themeMenu.getId());
			service.getMenuService().addMenu(themeUpdateButton);
			
			Menu themeDeleteButton = new Menu();
			themeDeleteButton.setMenuKey("admin-system-theme-delete");
			themeDeleteButton.setMenuName("Delete Theme");
			themeDeleteButton.setMenuNameDe("Thema löschen");
			themeDeleteButton.setMenuNameCs("Smazat motiv");
			themeDeleteButton.setMenuNamePl("Usuń motyw");
			themeDeleteButton.setMenuNameRu("Удалить тему");
			themeDeleteButton.setMenuNameEn("Delete Theme");
			themeDeleteButton.setMenuNameFr("Supprimer le thème");
			themeDeleteButton.setPermission("theme:delete");
			themeDeleteButton.setType(Menu.TYPE_BUTTON);
			themeDeleteButton.setOrderNum(3);
			themeDeleteButton.setParentId(themeMenu.getId());
			service.getMenuService().addMenu(themeDeleteButton);
			
			//log
			Menu logMenu = new Menu();
			logMenu.setMenuKey("admin-system-log");
			logMenu.setMenuName("Log Manage");
			logMenu.setMenuNameDe("Protokoll verwalten");
			logMenu.setMenuNameCs("Správa protokolu");
			logMenu.setMenuNamePl("Zarządzaj dziennikiem");
			logMenu.setMenuNameRu("Управление журналом");
			logMenu.setMenuNameEn("Log Manage");
			logMenu.setMenuNameFr("Gérer le journal");
			logMenu.setMenuPath("/app/admin/system/log");
			logMenu.setIcon("md-time");
			logMenu.setPermission("log:view");
			logMenu.setType(Menu.TYPE_MENU);
			logMenu.setOrderNum(5);
			logMenu.setParentId(systemMenu.getId());
			service.getMenuService().addMenu(logMenu);
			
			Menu logDeleteButton = new Menu();
			logDeleteButton.setMenuKey("admin-system-log-delete");
			logDeleteButton.setMenuName("Delete Log");
			logDeleteButton.setMenuNameDe("Protokoll löschen");
			logDeleteButton.setMenuNameCs("Smazat protokol");
			logDeleteButton.setMenuNamePl("Usuń dziennik");
			logDeleteButton.setMenuNameRu("Удалить журнал");
			logDeleteButton.setMenuNameEn("Delete Log");
			logDeleteButton.setMenuNameFr("Supprimer le journal");
			logDeleteButton.setPermission("log:delete");
			logDeleteButton.setType(Menu.TYPE_BUTTON);
			logDeleteButton.setOrderNum(1);
			logDeleteButton.setParentId(logMenu.getId());
			service.getMenuService().addMenu(logDeleteButton);
			
			//login log
			Menu loginLogMenu = new Menu();
			loginLogMenu.setMenuKey("admin-system-loginglog");
			loginLogMenu.setMenuName("Login Log Manage");
			loginLogMenu.setMenuNameDe("Login Log Verwalten");
			loginLogMenu.setMenuNameCs("Správa protokolu přihlášení");
			loginLogMenu.setMenuNamePl("Zaloguj Log Zarządzaj");
			loginLogMenu.setMenuNameRu("Логин Управление");
			loginLogMenu.setMenuNameEn("Login Log Manage");
			loginLogMenu.setMenuNameFr("Gérer le journal de connexion");
			loginLogMenu.setMenuPath("/app/admin/system/loginlog");
			loginLogMenu.setIcon("ios-people-outline");
			loginLogMenu.setPermission("loginlog:view");
			loginLogMenu.setType(Menu.TYPE_MENU);
			loginLogMenu.setOrderNum(6);
			loginLogMenu.setParentId(systemMenu.getId());
			service.getMenuService().addMenu(loginLogMenu);
			
			Menu loginlogDeleteButton = new Menu();
			loginlogDeleteButton.setMenuKey("admin-system-loginglog-delete");
			loginlogDeleteButton.setMenuName("Delete Login log");
			loginlogDeleteButton.setMenuNameDe("Login-Log löschen");
			loginlogDeleteButton.setMenuNameCs("Smazat přihlašovací protokol");
			loginlogDeleteButton.setMenuNamePl("Usuń dziennik logowania");
			loginlogDeleteButton.setMenuNameRu("Удалить журнал входа");
			loginlogDeleteButton.setMenuNameEn("Delete Login log");
			loginlogDeleteButton.setMenuNameFr("Supprimer le journal de connexion");
			loginlogDeleteButton.setPermission("log:delete");
			loginlogDeleteButton.setType(Menu.TYPE_BUTTON);
			loginlogDeleteButton.setOrderNum(1);
			loginlogDeleteButton.setParentId(loginLogMenu.getId());
			service.getMenuService().addMenu(loginlogDeleteButton);
			
			addRoleMenu(role, systemMenu, 
				menuMenu, menuAddButton, menuUpdateButton, menuDeleteButton, 
				roleMenu, roleAddButton, roleUpdateButton, roleDeleteButton,
				userMenu, userAddButton, userUpdateButton, userDeleteButton,
				themeMenu, themeAddButton, themeUpdateButton, themeDeleteButton,
				logMenu, logDeleteButton, 
				loginLogMenu, loginlogDeleteButton
			);
			return systemMenu;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Menu addHomeMenu(Role role) {

		try {
			List<Menu> menus = service.getMenuService().getMenuHibernate().findByMenuKey("admin-home");
			if (menus != null && menus.size() > 0 ) {
				return menus.get(0);
			}
			
			Menu homeMenu = new Menu();
			homeMenu.setMenuKey("admin-home");
			homeMenu.setMenuName("Home");
			homeMenu.setMenuNameDe("Home");
			homeMenu.setMenuNameCs("Domov");
			homeMenu.setMenuNamePl("Dom");
			homeMenu.setMenuNameRu("Домой");
			homeMenu.setMenuNameEn("Home");
			homeMenu.setMenuNameFr("Accueil");
			homeMenu.setMenuPath("/app/admin/system/home");
			homeMenu.setIcon("ios-home-outline");
			homeMenu.setPermission("home:view");
			homeMenu.setType(Menu.TYPE_MENU);
			homeMenu.setOrderNum(1);
			service.getMenuService().addMenu(homeMenu);
			
			addRoleMenu(role, homeMenu);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Role addAdminRole() {
		try {
			List<Role> roles = service.getRoleService().getRoleHibernate().findByRoleName("admin");
			if (roles != null && roles.size() > 0) {
				return roles.get(0);
			}
			Role adminRole = new Role();
			adminRole.setRemark("admin is role what manage all pages");
			adminRole.setRoleName("admin");
			service.getRoleService().addRole(adminRole);
			return adminRole;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Role addTestRole() {
		try {
			List<Role> roles = service.getRoleService().getRoleHibernate().findByRoleName("tester");
			if (roles != null && roles.size() > 0) {
				return roles.get(0);
			}
			Role testRole = new Role();
			testRole.setRemark("tester is general test role");
			testRole.setRoleName("tester");
			service.getRoleService().addRole(testRole);
			return testRole;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addRoleMenu(Role role, Menu... menus) {
		for (Menu menu : menus) {
			try {
				RoleMenu rm = new RoleMenu();
				rm.setRole(role);
				rm.setMenu(menu);
				service.getRoleService().addRoleMenu(rm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addRole2User(User user, Role... roles) {
		for (Role role : roles) {
			try {
				UserRole ur = new UserRole();
				ur.setUser(user);
				ur.setRole(role);
				service.getUserService().addUserRole(ur);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String testFindUserPermissions(Long id) {
		try {
			return service.getAuthService().findUserPermissions(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void readThemeJSonWriteDB() {
		try {
			String path = System.getProperty("user.dir") + File.separator + "theme.json";
			byte[] mapData = Files.readAllBytes(Paths.get(path));
			ObjectMapper objectMapper = new ObjectMapper();
			RootData root = objectMapper.readValue(mapData, RootData.class);
			this.service.getThemeService().addTheme(root);
			System.out.println("===finsh parse Theme JSon===");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	private void readIp2LocationJSonWriteDB() {
		try {
			String filename = "ip2location.csv";
			String pathCsv = System.getProperty("user.dir") + File.separator + filename;
			
			if (!this.service.getIp2LocationService().importDBFromCSV(filename)) {
				File csvFile = new File(pathCsv);
				if (csvFile.isFile()) {
					BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
					String row = null;
					while ((row = csvReader.readLine()) != null) {
						String[] data = row.split(",");
						if (data.length >= 8) {
							Ip2Location ildata = new Ip2Location();
							ildata.setIpFrom(Long.parseLong(StringUtil.remove2End(data[0])));
							ildata.setIpTo(Long.parseLong(StringUtil.remove2End(data[1])));
							ildata.setCountryCode(StringUtil.remove2End(data[2]));
							ildata.setCountryName(StringUtil.remove2End(data[3]));
							ildata.setRegionName(StringUtil.remove2End(data[4]));
							ildata.setCityName(StringUtil.remove2End(data[5]));
							ildata.setLatitude(Double.parseDouble(StringUtil.remove2End(data[6])));
							ildata.setLongitude(Double.parseDouble(StringUtil.remove2End(data[7])));
							this.service.getIp2LocationService().saveIp2Location(ildata);
						}
					}
					csvReader.close();
				}
			}
			System.out.println("===finsh parse Ip2location JSon===");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

}
