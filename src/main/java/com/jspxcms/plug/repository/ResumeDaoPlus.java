package com.jspxcms.plug.repository;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.plug.domain.Resume;

public interface ResumeDaoPlus {

	public List<Resume> getList(Integer[] siteId, Limitable limitable);

}
