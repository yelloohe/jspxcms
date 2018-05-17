package com.jspxcms.core.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Attachment;
import com.jspxcms.core.domain.AttachmentRef;
import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.repository.AttachmentRefDao;
import com.jspxcms.core.service.AttachmentRefService;
import com.jspxcms.core.service.AttachmentService;
import com.jspxcms.core.service.GlobalService;

@Service
@Transactional(readOnly = true)
public class AttachmentRefServiceImpl implements AttachmentRefService {
	public void update(Set<String> urls, String ftype, Integer fid) {
		if (urls == null) {
			urls = Collections.emptySet();
		}
		Global global = globalService.findUnique();
		PublishPoint point = global.getUploadsPublishPoint();
		String prefix = point.getUrlPrefix();
		Set<String> names = new HashSet<String>(urls.size());
		int lenth = prefix.length();
		for (String url : urls) {
			if (StringUtils.startsWith(url, prefix)) {
				names.add(url.substring(lenth));
			}
		}
		List<AttachmentRef> list = dao.findByFtypeAndFid(ftype, fid);
		// 先删除
		Set<AttachmentRef> tobeDelete = new HashSet<AttachmentRef>();
		for (AttachmentRef ref : list) {
			Attachment attachment = ref.getAttachment();
			String name = attachment.getName();
			if (!names.contains(name)) {
				Set<AttachmentRef> refs = attachment.getRefs();
				refs.remove(ref);
				// attachment.setUserd(!refs.isEmtpy());
				tobeDelete.add(ref);
			}
		}
		dao.delete(tobeDelete);
		list.removeAll(tobeDelete);
		// 再新增
		for (String name : names) {
			if (StringUtils.isBlank(name)) {
				continue;
			}
			boolean contains = false;
			for (AttachmentRef ref : list) {
				String n = ref.getAttachment().getName();
				if (n.equals(name)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				Attachment attachment = attachmentService.findByName(name);
				if (attachment != null) {
					save(ftype, fid, attachment);
				}
			}
		}
	}

	@Transactional
	public void delete(String ftype, Integer fid) {
		Set<String> urls = Collections.emptySet();
		update(urls, ftype, fid);
	}

	@Transactional
	public int deleteBySiteId(Collection<Integer> siteIds) {
		return dao.deleteBySiteId(siteIds);
	}

	@Transactional
	private AttachmentRef save(String ftype, Integer fid, Attachment attachment) {
		AttachmentRef bean = new AttachmentRef();
		bean.setFtype(ftype);
		bean.setFid(fid);
		bean.setAttachment(attachment);
		bean.setSite(attachment.getSite());
		bean.applyDefaultValue();
		bean = dao.save(bean);
		Set<AttachmentRef> refs = attachment.getRefs();
		refs.add(bean);
		// attachment.setUsed(!refs.isEmpty());
		return bean;
	}

	private GlobalService globalService;
	private AttachmentService attachmentService;

	@Autowired
	public void setGlobalService(GlobalService globalService) {
		this.globalService = globalService;
	}

	@Autowired
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	private AttachmentRefDao dao;

	@Autowired
	public void setDao(AttachmentRefDao dao) {
		this.dao = dao;
	}
}
