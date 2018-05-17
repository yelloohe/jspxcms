package com.jspxcms.core.service;

import java.util.Collection;
import java.util.Set;

public interface AttachmentRefService {
	public void update(Set<String> urls, String ftype, Integer fid);

	public void delete(String ftype, Integer fid);

	public int deleteBySiteId(Collection<Integer> siteIds);
}
