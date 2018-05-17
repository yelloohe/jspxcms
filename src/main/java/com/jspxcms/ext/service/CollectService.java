package com.jspxcms.ext.service;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoDetail;
import com.jspxcms.ext.domain.Collect;

public interface CollectService {
	public Page<Collect> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable);

	public RowSide<Collect> findSide(Integer siteId,
			Map<String, String[]> params, Collect bean, Integer position,
			Sort sort);

	public List<Collect> findList(Integer siteId);

	public Collect get(Integer id);

	public Collect save(Collect bean, Integer nodeId, Integer userId,
			Integer siteId);

	public Collect update(Collect bean, Integer nodeId);

	public Collect delete(Integer id);

	public List<Collect> delete(Integer[] ids);

	public void running(Integer id);

	public void ready(Integer id);

	public boolean isRunning(Integer id);

	public boolean collcetItem(CloseableHttpClient httpclient, URI uri,
			Integer collectId, String charset, Integer nodeId,
			Integer creatorId, Info info, InfoDetail detail,
			Map<String, String> customs, Map<String, String> clobs)
			throws ClientProtocolException, IOException;

}
