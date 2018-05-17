package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Notification;

public interface NotificationService {
	public Page<Notification> findAll(Integer receiverId, Map<String, String[]> params, Pageable pageable);

	public List<Notification> findAll(Integer receiverId, Map<String, String[]> params, Limitable limitable);

	public int countByReceiverId(Integer receiverId);

	public Notification send(String type, Integer key, Integer receiverId, String contentTpl, String sourceName,
			String sourceUrl, String message, String targetName, String targetUrl, String url, String backendUrl);

	public Notification get(Integer id);

	public Notification save(Notification bean);

	public Notification update(Notification bean);

	public Notification delete(Integer id);

	public List<Notification> delete(Integer[] ids);
}
