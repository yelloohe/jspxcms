package com.jspxcms.core.holder;

import java.util.List;
import java.util.Map;

public class ScheduleJobHolder {
	private List<String> codes;
	private Map<String, String> paths;

	public Map<String, String> getPaths() {
		return paths;
	}

	public void setPaths(Map<String, String> paths) {
		this.paths = paths;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
}
