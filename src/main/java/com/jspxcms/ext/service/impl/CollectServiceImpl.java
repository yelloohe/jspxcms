package com.jspxcms.ext.service.impl;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.common.upload.UploadResult;
import com.jspxcms.common.upload.Uploader;
import com.jspxcms.core.domain.*;
import com.jspxcms.core.listener.NodeDeleteListener;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.core.support.UploadHandler;
import com.jspxcms.ext.domain.Collect;
import com.jspxcms.ext.repository.CollectDao;
import com.jspxcms.ext.service.CollectService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class CollectServiceImpl implements CollectService, NodeDeleteListener,
		UserDeleteListener, SiteDeleteListener {
	private Logger logger = LoggerFactory.getLogger(CollectServiceImpl.class);

	public Page<Collect> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<Collect> findSide(Integer siteId,
			Map<String, String[]> params, Collect bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<Collect>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Collect> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Collect> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Collect> fsp = SearchFilter.spec(filters,
				Collect.class);
		Specification<Collect> sp = new Specification<Collect>() {
			public Predicate toPredicate(Root<Collect> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site")
							.<Integer> get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Collect> findList(Integer siteId) {
		return dao.findList(siteId);
	}

	public Collect get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Collect save(Collect bean, Integer nodeId, Integer userId,
			Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		Node node = nodeQuery.get(nodeId);
		bean.setNode(node);
		User user = userService.get(userId);
		bean.setUser(user);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Collect update(Collect bean, Integer nodeId) {
		Node node = nodeQuery.get(nodeId);
		bean.setNode(node);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Collect delete(Integer id) {
		Collect bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Transactional
	public List<Collect> delete(Integer[] ids) {
		List<Collect> beans = new ArrayList<Collect>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Transactional
	public void running(Integer id) {
		Collect bean = get(id);
		bean.setStatus(Collect.RUNNING);
	}

	@Transactional
	public void ready(Integer id) {
		Collect bean = get(id);
		bean.setStatus(Collect.READY);
	}

	public boolean isRunning(Integer id) {
		Collect bean = get(id);
		if (bean == null) {
			return false;
		}
		return bean.isRunning();
	}

	public boolean collcetItem(CloseableHttpClient httpclient, URI uri,
			Integer collectId, String charset, Integer nodeId,
			Integer creatorId, Info info, InfoDetail detail,
			Map<String, String> customs, Map<String, String> clobs)
			throws ClientProtocolException, IOException {
		logger.debug("collcet item url: {}", uri);
		Collect collect = get(collectId);
		Site site = collect.getSite();
		Integer siteId = site.getId();
		Integer userId = collect.getUser().getId();
		try {
			HttpGet httpget = new HttpGet(uri);
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String html = EntityUtils.toString(entity, charset);
			// TODO item值
			String item = null;
			String id = collect.getFieldId(html, item, uri, httpclient,
					uploadHandler);
			String title = collect.getFieldTitle(html, item, id, uri,
					httpclient, uploadHandler);
			if (StringUtils.isBlank(title)) {
				return false;
			}
			if (!collect.getAllowDuplicate()
					&& infoQuery.containsTitle(title, siteId)) {
				return false;
			}
			StringBuilder textBuilder = new StringBuilder();
			String text = collect.getFieldText(html, item, id, uri, httpclient,
					uploadHandler);
			if (text != null) {
				// 采集图片
				text = collectImage(text, uri, site, userId,
						collect.getDownloadImage());
				textBuilder.append(text);
			}

			collect.infoField(info, html, item, id, uri, httpclient,
					uploadHandler);
			collect.detailField(detail, html, item, id, uri, httpclient,
					uploadHandler);
			detail.setTitle(title);
			collect.customsField(customs, html, item, id, uri, httpclient,
					uploadHandler);
			collect.clobsField(clobs, html, item, id, uri, httpclient,
					uploadHandler);
			String next = collect.getFieldNext(html, item, id, uri, httpclient,
					uploadHandler);
			response.close();
			while (StringUtils.isNotBlank(next)) {
				uri = uri.resolve(StringEscapeUtils.unescapeHtml4(next));
				httpget = new HttpGet(uri);
				response = httpclient.execute(httpget);
				entity = response.getEntity();
				html = EntityUtils.toString(entity, charset);
				text = collect.getFieldText(html, item, id, uri, httpclient,
						uploadHandler);
				if (text != null) {
					textBuilder.append(Info.PAGEBREAK_OPEN);
					textBuilder.append(Info.PAGEBREAK_CLOSE);
					// 采集图片
					text = collectImage(text, uri, site, userId,
							collect.getDownloadImage());
					textBuilder.append(text);
				}
				next = collect.getFieldNext(html, item, id, uri, httpclient,
						uploadHandler);
				response.close();
			}
			if (StringUtils.isNotBlank(textBuilder)) {
				clobs.put(Info.INFO_TEXT, textBuilder.toString());
			}
			return true;
		} catch (IOException e) {
			logger.error(null, e);
			return false;
		}
	}

	private String collectImage(String html, URI uri, Site site,
			Integer userId, boolean isDownload) {
		if (StringUtils.isBlank(html)) {
			return html;
		}
		StringBuilder buff = new StringBuilder(html.length());
		try {
			Parser parser = new Parser(new Lexer(html));
			NodeFilter filter = new TagNameFilter("img");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);
			SimpleNodeIterator it = nodes.elements();
			int begin = 0, end = 0;
			while (it.hasMoreNodes()) {
				ImageTag tag = (ImageTag) it.nextNode();
				String src = tag.getAttribute("src");
				src = StringUtils.trim(src);
				if (StringUtils.isBlank(src)) {
					continue;
				}
				String srcUrl = uri.resolve(StringEscapeUtils.unescapeHtml4(src)).toString();
				if (isDownload) {
					UploadResult result = new UploadResult();
					uploadHandler.upload(srcUrl, Uploader.IMAGE, site, userId,
							null, result);
					if (result.isSuccess()) {
						srcUrl = result.getFileUrl();
					}
				}
				tag.setAttribute("src", srcUrl);
				end = tag.getStartPosition();
				buff.append(html.subSequence(begin, end));
				buff.append(tag.toHtml());
				begin = tag.getEndPosition();
			}
			buff.append(html.subSequence(begin, html.length()));
		} catch (Exception e) {
			logger.error(null, e);
		}
		return buff.toString();
	}

	public void preNodeDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countByNodeId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("collect.management");
			}
		}
	}

	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countByUserId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("collect.management");
			}
		}
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("collect.management");
			}
		}
	}

	private UploadHandler uploadHandler;
	private InfoQueryService infoQuery;

	private NodeQueryService nodeQuery;
	private UserService userService;
	private SiteService siteService;

	@Autowired
	public void setUploadHandler(UploadHandler uploadHandler) {
		this.uploadHandler = uploadHandler;
	}

	@Autowired
	public void setInfoQueryService(InfoQueryService infoQuery) {
		this.infoQuery = infoQuery;
	}

	@Autowired
	public void setNodeQuery(NodeQueryService nodeQuery) {
		this.nodeQuery = nodeQuery;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private CollectDao dao;

	@Autowired
	public void setDao(CollectDao dao) {
		this.dao = dao;
	}
}
