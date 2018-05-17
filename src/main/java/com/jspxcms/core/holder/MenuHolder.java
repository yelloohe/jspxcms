package com.jspxcms.core.holder;

import java.util.Set;

public class MenuHolder {
	private Set<Menu> menus;

	public MenuHolder() {
	}

	public MenuHolder(Set<Menu> menus) {
		this.menus = menus;
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
}
