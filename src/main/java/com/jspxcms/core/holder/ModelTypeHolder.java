package com.jspxcms.core.holder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelTypeHolder {
	private List<String> types;
	private Map<String, String> paths;

	public Map<String, String> getPaths() {
		return paths;
	}

	public void setPaths(Map<String, String> paths) {
		this.paths = paths;
	}

	/**
	 * 根据站点获取模型列表。非主站点（id!=1）不显示用户user和系统模型global。
	 * 
	 * @param siteId
	 * @return
	 */
	public List<String> getTypesBySiteId(Integer siteId) {
		List<String> list = new ArrayList<String>();
		list.addAll(types);
		if (siteId != 1) {
			list.remove("user");
			list.remove("global");
		}
		return list;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

}
