package com.jspxcms.core.web.directive;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.support.ForeContext;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 节点列表标签、节点分页标签公共父类
 * 
 * @author liufang
 * 
 */
public abstract class AbstractNodeListPageDirective {
	/**
	 * 站点ID。整型。
	 */
	public static final String SITE_ID = "siteId";
	/**
	 * 父节点ID。整型。
	 */
	public static final String PARENT_ID = "parentId";
	/**
	 * 父节点编码。字符串。
	 */
	public static final String PARENT = "parent";
	/**
	 * 是否前台隐藏。布尔型。
	 */
	public static final String IS_HIDDEN = "isHidden";
	/**
	 * 是否真实节点（是否有信息的节点）。布尔型。
	 */
	public static final String IS_REAL_NODE = "isRealNode";
	/**
	 * 是否包含子节点。布尔型。
	 */
	public static final String IS_INCLUDE_CHILDREN = "isIncludeChildren";

	public static final String P0 = "p0";
	public static final String P1 = "p1";
	public static final String P2 = "p2";
	public static final String P3 = "p3";
	public static final String P4 = "p4";
	public static final String P5 = "p5";
	public static final String P6 = "p6";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doExecute(Environment env, Map params,
			TemplateModel[] loopVars, TemplateDirectiveBody body, boolean isPage)
			throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}
		Integer parentId = Freemarkers.getInteger(params, PARENT_ID);
		String parent = Freemarkers.getString(params, PARENT);
		Integer[] siteId = Freemarkers.getIntegers(params, SITE_ID);
		if (parentId == null) {
			Integer sid;
			if (ArrayUtils.isNotEmpty(siteId)) {
				sid = siteId[0];
			} else {
				sid = ForeContext.getSiteId(env);
			}
			if (StringUtils.isNotBlank(parent)) {
				Node pnode = service.findByNumber(sid, parent);
				if (pnode != null) {
					parentId = pnode.getId();
				} else {
					ForeContext.setEmptyResult(isPage, env, loopVars);
					return;
				}
			} else {
				// parentId和parent都为null，获取当前站点或指定站点的一级栏目，即首页栏目的子栏目。
				Node root = service.findRoot(sid);
				if (root != null) {
					parentId = root.getId();
				} else {
					ForeContext.setEmptyResult(isPage, env, loopVars);
					return;
				}
			}
		}
		Boolean isHidden = Freemarkers.getBoolean(params, IS_HIDDEN);
		if (isHidden == null && params.get(IS_HIDDEN) == null) {
			isHidden = false;
		}
		Boolean isRealNode = Freemarkers.getBoolean(params, IS_REAL_NODE);

		boolean isIncludeChildren = Freemarkers.getBoolean(params,
				IS_INCLUDE_CHILDREN, false);
		String treeNumber = null;
		if (isIncludeChildren && parentId != null) {
			Node pnode = service.get(parentId);
			if (pnode != null) {
				treeNumber = pnode.getTreeNumber();
			} else {
				ForeContext.setEmptyResult(isPage, env, loopVars);
				return;
			}
			parentId = null;
		}

		if (siteId == null && parentId == null
				&& StringUtils.isBlank(treeNumber)) {
			// 所有确定站点的参数都不存在，默认为当前站点。
			siteId = new Integer[] { ForeContext.getSiteId(env) };
		}

		Integer[] p0 = Freemarkers.getIntegers(params, P0);
		Integer[] p1 = Freemarkers.getIntegers(params, P1);
		Integer[] p2 = Freemarkers.getIntegers(params, P2);
		Integer[] p3 = Freemarkers.getIntegers(params, P3);
		Integer[] p4 = Freemarkers.getIntegers(params, P4);
		Integer[] p5 = Freemarkers.getIntegers(params, P5);
		Integer[] p6 = Freemarkers.getIntegers(params, P6);

		Sort defSort = new Sort("treeNumber");
		if (isPage) {
			Pageable pageable = Freemarkers.getPageable(params, env, defSort);
			Page<Node> pagedList = service.findPage(siteId, parentId,
					treeNumber, isRealNode, isHidden,p0, p1, p2, p3, p4, p5, p6,
					pageable);
			ForeContext.setTotalPages(pagedList.getTotalPages());
			loopVars[0] = env.getObjectWrapper().wrap(pagedList);
		} else {
			Limitable limitable = Freemarkers.getLimitable(params, defSort);
			List<Node> list = service.findList(siteId, parentId, treeNumber,
					isRealNode, isHidden,p0, p1, p2, p3, p4, p5, p6, limitable);
			loopVars[0] = env.getObjectWrapper().wrap(list);
		}
		body.render(env.getOut());
	}

	@Autowired
	private NodeQueryService service;
}
