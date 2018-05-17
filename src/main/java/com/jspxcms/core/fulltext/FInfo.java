package com.jspxcms.core.fulltext;

import static org.apache.lucene.document.Field.Index.ANALYZED;
import static org.apache.lucene.document.Field.Index.NOT_ANALYZED;
import static org.apache.lucene.document.Field.Store.NO;
import static org.apache.lucene.document.Field.Store.YES;
import static org.apache.lucene.search.BooleanClause.Occur.MUST;
import static org.apache.lucene.search.BooleanClause.Occur.MUST_NOT;
import static org.apache.lucene.search.BooleanClause.Occur.SHOULD;
import static org.apache.lucene.util.Version.LUCENE_36;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.util.NumberUtils;

import com.jspxcms.common.fulltext.LuceneException;
import com.jspxcms.core.domain.Attribute;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoTag;
import com.jspxcms.core.domain.Node;

/**
 * 信息实体的全文检索转换类
 * 
 * @author liufang
 * 
 */
public class FInfo {
	public static final String ID = "id";
	public static final String SITE_ID = "siteId";
	public static final String NODE_ID = "nodeId";
	// 有权限阅读的用户
	public static final String USER_ID = "userId";
	public static final String ATTR_ID = "attrId";
	public static final String PUBLISH_DATE = "publishDate";
	public static final String STATUS = "status";
	public static final String TITLE = "title";
	public static final String KEYWORD = "keyword";
	public static final String DESCRIPTION = "description";
	public static final String CREATOR = "creator";
	public static final String AUTHOR = "author";
	public static final String TEXT = "text";

	public static List<Integer> idsFromDoc(List<Document> docList) {
		if (!docList.isEmpty()) {
			List<Integer> ids = new ArrayList<Integer>(docList.size());
			for (Document doc : docList) {
				ids.add(NumberUtils.parseNumber(doc.get(ID), Integer.class));
			}
			return ids;
		} else {
			return Collections.emptyList();
		}
	}

	public static List<Integer> idsFromString(List<String> idList) {
		if (!idList.isEmpty()) {
			List<Integer> ids = new ArrayList<Integer>(idList.size());
			for (String id : idList) {
				ids.add(NumberUtils.parseNumber(id, Integer.class));
			}
			return ids;
		} else {
			return Collections.emptyList();
		}
	}

	public static Term id(Integer id) {
		return new Term(FInfo.ID, String.valueOf(id));
	}

	public static Document doc(Info info) {
		Document doc = new Document();
		String id = String.valueOf(info.getId());
		doc.add(new Field(ID, id, YES, NOT_ANALYZED));

		String siteId = String.valueOf(info.getSite().getId());
		doc.add(new Field(SITE_ID, siteId, NO, NOT_ANALYZED));

		String nodeId;
		Node node = info.getNode();
		while (node != null) {
			nodeId = String.valueOf(node.getId());
			doc.add(new Field(NODE_ID, nodeId, NO, NOT_ANALYZED));
			node = node.getParent();
		}

		List<Attribute> attrs = info.getAttrs();
		if (CollectionUtils.isNotEmpty(attrs)) {
			String attrId;
			for (Attribute attr : attrs) {
				attrId = String.valueOf(attr.getId());
				doc.add(new Field(ATTR_ID, attrId, NO, NOT_ANALYZED));
			}
		}

		NumericField publishDate = new NumericField(PUBLISH_DATE);
		doc.add(publishDate.setLongValue(info.getPublishDate().getTime()));

		doc.add(new Field(STATUS, info.getStatus(), NO, NOT_ANALYZED));

		String title = info.getFullTitleOrTitle();
		if (StringUtils.isNotBlank(title)) {
			doc.add(new Field(TITLE, title, YES, ANALYZED));
		}

		Collection<InfoTag> infoTags = info.getInfoTags();
		if (CollectionUtils.isNotEmpty(infoTags)) {
			String tagName;
			for (InfoTag infoTag : infoTags) {
				tagName = infoTag.getTag().getName();
				doc.add(new Field(KEYWORD, tagName, NO, ANALYZED));
			}
		}

		String description = info.getMetaDescription();
		if (StringUtils.isNotBlank(description)) {
			doc.add(new Field(DESCRIPTION, description, NO, ANALYZED));
		}

		String text = info.getPlainText();
		if (StringUtils.isNotBlank(text)) {
			doc.add(new Field(TEXT, text, YES, ANALYZED));
		}

		String creator = info.getCreator().getUsername();
		if (StringUtils.isNotBlank(creator)) {
			doc.add(new Field(CREATOR, creator, NO, ANALYZED));
		}

		String author = info.getAuthor();
		if (StringUtils.isNotBlank(author)) {
			doc.add(new Field(AUTHOR, author, NO, ANALYZED));
		}

		return doc;
	}

	public static Query query(Analyzer analyzer, Integer[] siteIds,
			Integer[] nodeIds, Integer[] attrIds, Date beginDate, Date endDate,
			String[] status, Integer[] excludeId, String q, String title,
			String[] keywords, String description, String text,
			String[] creators, String[] authors) {
		try {
			BooleanQuery query = new BooleanQuery();
			if (ArrayUtils.isNotEmpty(siteIds)) {
				BooleanQuery qy = new BooleanQuery();
				for (Integer id : siteIds) {
					String s = String.valueOf(id);
					qy.add(new TermQuery(new Term(SITE_ID, s)), SHOULD);
				}
				query.add(qy, MUST);
			}
			if (ArrayUtils.isNotEmpty(nodeIds)) {
				BooleanQuery qy = new BooleanQuery();
				for (Integer id : nodeIds) {
					String s = String.valueOf(id);
					qy.add(new TermQuery(new Term(NODE_ID, s)), SHOULD);
				}
				query.add(qy, MUST);
			}
			if (ArrayUtils.isNotEmpty(attrIds)) {
				BooleanQuery qy = new BooleanQuery();
				for (Integer id : attrIds) {
					String s = String.valueOf(id);
					qy.add(new TermQuery(new Term(ATTR_ID, s)), SHOULD);
				}
				query.add(qy, MUST);
			}
			if (beginDate != null || endDate != null) {
				Long min = beginDate != null ? beginDate.getTime() : null;
				Long max = endDate != null ? endDate.getTime() : null;
				NumericRangeQuery<Long> qy = NumericRangeQuery.newLongRange(
						PUBLISH_DATE, min, max, true, true);
				query.add(qy, MUST);
			}
			if (ArrayUtils.isNotEmpty(status)) {
				BooleanQuery qy = new BooleanQuery();
				for (String s : status) {
					qy.add(new TermQuery(new Term(STATUS, s)), SHOULD);
				}
				query.add(qy, MUST);
			}
			if (ArrayUtils.isNotEmpty(excludeId)) {
				for (Integer id : excludeId) {
					query.add(new TermQuery(id(id)), MUST_NOT);
				}
			}
			if (StringUtils.isNotBlank(q)) {
				q = QueryParser.escape(q);
				Query qy = MultiFieldQueryParser.parse(LUCENE_36, q,
						new String[] { TITLE, KEYWORD, DESCRIPTION, TEXT },
						new Occur[] { SHOULD, SHOULD, SHOULD, SHOULD },
						analyzer);
				query.add(qy, MUST);
			}
			if (StringUtils.isNotBlank(title)) {
				title = QueryParser.escape(title);
				QueryParser p = new QueryParser(LUCENE_36, TITLE, analyzer);
				query.add(p.parse(title), MUST);
			}
			if (ArrayUtils.isNotEmpty(keywords)) {
				BooleanQuery qy = new BooleanQuery();
				for (String keyword : keywords) {
					qy.add(new TermQuery(new Term(KEYWORD, keyword)), SHOULD);
				}
				query.add(qy, MUST);
			}
			if (StringUtils.isNotBlank(description)) {
				description = QueryParser.escape(description);
				QueryParser p = new QueryParser(LUCENE_36, DESCRIPTION,
						analyzer);
				query.add(p.parse(description), MUST);
			}
			if (StringUtils.isNotBlank(text)) {
				text = QueryParser.escape(text);
				QueryParser p = new QueryParser(LUCENE_36, TEXT, analyzer);
				query.add(p.parse(text), MUST);
			}
			if (ArrayUtils.isNotEmpty(creators)) {
				BooleanQuery qy = new BooleanQuery();
				for (String creator : creators) {
					qy.add(new TermQuery(new Term(CREATOR, creator)), SHOULD);
				}
				query.add(qy, MUST);
			}
			if (ArrayUtils.isNotEmpty(authors)) {
				BooleanQuery qy = new BooleanQuery();
				for (String author : authors) {
					qy.add(new TermQuery(new Term(AUTHOR, author)), SHOULD);
				}
				query.add(qy, MUST);
			}
			return query;
		} catch (Exception e) {
			throw new LuceneException("Error during create query.", e);
		}
	}

	/*
	 * 不再使用这种查询方式。
	 * 
	 * public static Predicate query(Integer[] siteIds, Integer[] nodeIds,
	 * Integer[] attrIds, Date beginDate, Date endDate, String[] status,
	 * Integer[] excludeId, String q, String title, String[] keywords, String
	 * description, String text) { FInfo info = FInfo.info; BooleanBuilder exp =
	 * new BooleanBuilder(); if (ArrayUtils.isNotEmpty(siteIds)) { exp =
	 * exp.and(info.siteId.in(siteIds)); } if (ArrayUtils.isNotEmpty(nodeIds)) {
	 * exp = exp.and(info.nodeId.in(nodeIds)); } if
	 * (ArrayUtils.isNotEmpty(attrIds)) { exp =
	 * exp.and(info.attrId.in(attrIds)); } if (beginDate != null) { exp =
	 * exp.and(info.publishDate.goe(beginDate)); } if (endDate != null) { exp =
	 * exp.and(info.publishDate.loe(endDate)); } if
	 * (ArrayUtils.isNotEmpty(status)) { exp = exp.and(info.status.in(status));
	 * } if (ArrayUtils.isNotEmpty(excludeId)) { exp =
	 * exp.and(info.id.notIn(excludeId)); } if (StringUtils.isNotBlank(q)) { exp
	 * = exp.and(info.title.contains(q).or(info.text.contains(q))); } if
	 * (StringUtils.isNotBlank(title)) { exp =
	 * exp.and(info.title.contains(title)); } if
	 * (ArrayUtils.isNotEmpty(keywords)) { exp =
	 * exp.and(info.keyword.in(keywords)); } if
	 * (StringUtils.isNotBlank(description)) { exp =
	 * exp.and(info.description.contains(description)); } if
	 * (StringUtils.isNotBlank(text)) { exp = exp.and(info.text.contains(text));
	 * } return exp; }
	 */
}
