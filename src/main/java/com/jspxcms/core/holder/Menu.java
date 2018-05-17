package com.jspxcms.core.holder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 后台菜单类
 * 
 * @author liufang
 *
 */
public class Menu implements Comparable<Menu> {
	/**
	 * ID。如100，100-200
	 */
	private String id;
	/**
	 * 名称。如文档管理、栏目管理
	 */
	private String name;
	/**
	 * 字体图标的class
	 */
	private String icon;
	/**
	 * 菜单的访问地址
	 */
	private String url;
	/**
	 * 菜单地址对应的权限，可以有多个
	 */
	private String perms;
	/**
	 * 菜单对应功能模块里的具体操作，比如栏目管理里面的新增、修改、删除等功能
	 */
	private List<Op> ops;
	/**
	 * 菜单的排列顺序，为ID的最后一个节点，如100-200，则为200
	 */
	private int seq = Integer.MAX_VALUE;
	/**
	 * 上级菜单ID
	 */
	private String parentId;
	/**
	 * 左侧链接地址。部分功能有会显示左侧页面，如文档管理、栏目管理。
	 */
	private String leftUrl;
	/**
	 * 中间链接地址。就是一个功能的连接地址，显示在页面的中间。
	 */
	private String centerUrl;
	/**
	 * 菜单权限的第一个权限
	 */
	private String perm;

	/**
	 * 上级菜单
	 */
	private Menu parent;
	/**
	 * 子菜单
	 */
	private Set<Menu> children;

	/**
	 * 根据ID排序，并处理父子关系
	 * 
	 * @param menus
	 * @return
	 */
	public static TreeSet<Menu> sort(Set<Menu> menus) {
		TreeSet<Menu> set = new TreeSet<Menu>();
		for (Menu menu : menus) {
			String parentId = menu.getParentId();
			if (StringUtils.isNotBlank(parentId)) {
				for (Menu parent : menus) {
					if (parentId.equals(parent.getId())) {
						parent.addChild(menu);
						menu.setParent(parent);
						break;
					}
				}
			} else {
				set.add(menu);
			}
		}
		return set;
	}

	/**
	 * 将map解析为Menu对象
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	public static Menu parse(Object id, Map<Object, Object> map) {
		Menu menu = new Menu();
		menu.setId(String.valueOf(id));
		Object name = map.get("name");
		if (name != null) {
			menu.setName(String.valueOf(name));
		}
		Object icon = map.get("icon");
		if (icon != null) {
			menu.setIcon(String.valueOf(icon));
		}
		Object url = map.get("url");
		if (url != null) {
			menu.setUrl(String.valueOf(url));
		}
		Object perms = map.get("perms");
		if (perms != null) {
			menu.setPerms(String.valueOf(perms));
		}

		@SuppressWarnings("unchecked")
		List<Object> ops = (List<Object>) map.get("ops");
		if (ops != null) {
			List<Op> menuOps = new ArrayList<Op>(ops.size());
			menu.setOps(menuOps);
			for (Object op : ops) {
				String[] pair = StringUtils.split(String.valueOf(op), '@');
				String n = pair[0];
				String p = null;
				if (pair.length > 1) {
					p = pair[1];
				}
				menuOps.add(new Op(n, p));
			}
		}
		return menu;
	}

	public Menu() {
	}

	public Menu(String id, String name, String url, String perms, List<Op> functions) {
		setId(id);
		this.name = name;
		setUrl(url);
		setPerms(perms);
		this.ops = functions;
	}

	public int compareTo(Menu o) {
		return this.getSeq() - o.getSeq();
	}

	public void addChild(Menu menu) {
		if (children == null) {
			children = new TreeSet<Menu>();
		}
		children.add(menu);
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Set<Menu> getChildren() {
		if (children == null) {
			children = new TreeSet<Menu>();
		}
		return children;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getPerms() {
		return perms;
	}

	public List<Op> getFunctions() {
		return ops;
	}

	public String getParentId() {
		return parentId;
	}

	public String getLeftUrl() {
		return leftUrl;
	}

	public void setLeftUrl(String leftUrl) {
		this.leftUrl = leftUrl;
	}

	public String getCenterUrl() {
		return centerUrl;
	}

	public void setCenterUrl(String centerUrl) {
		this.centerUrl = centerUrl;
	}

	public String getPerm() {
		return perm;
	}

	public List<Op> getOps() {
		return ops;
	}

	public void setOps(List<Op> ops) {
		this.ops = ops;
	}

	public void setId(String id) {
		this.id = id;
		String[] arr = StringUtils.split(id, '-');
		this.seq = NumberUtils.toInt(arr[arr.length - 1], Integer.MAX_VALUE);
		if (id.indexOf('-') != -1) {
			this.parentId = id.substring(0, id.lastIndexOf('-'));
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
		if (url != null) {
			String[] urlArr = StringUtils.split(url, ',');
			centerUrl = urlArr[0];
			if (urlArr.length > 1) {
				leftUrl = urlArr[1];
			}
		}
	}

	public void setPerms(String perms) {
		this.perms = perms;
		if (perms != null) {
			perm = StringUtils.split(perms, ',')[0];
		}
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "id:" + getId() + ";name:" + getName() + ";url:" + getUrl() + ";perms:" + getPerms();
	}

	/**
	 * 功能模块的具体操作，比如栏目管理里面的新增、修改、删除等功能。
	 * 
	 * @author liufang
	 *
	 */
	public static class Op {
		/**
		 * 操作名称
		 */
		private String name;
		/**
		 * 操作对应的权限，可能会有多个，用英文逗号`,`分开
		 */
		private String perms;
		/**
		 * 操作权限中的第一个权限
		 */
		private String perm;

		public Op(String name, String perms) {
			this.name = name;
			this.perms = perms;
			if (StringUtils.isNotBlank(perms)) {
				this.perm = StringUtils.split(perms, ',')[0];
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPerms() {
			return perms;
		}

		public void setPerms(String perms) {
			this.perms = perms;
		}

		public String getPerm() {
			return perm;
		}

		public void setPerm(String perm) {
			this.perm = perm;
		}
	}

}
