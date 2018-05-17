package com.jspxcms.common.fulltext;

import java.util.Collection;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;

/**
 * Lucene索引模板接口
 * 
 * @author liufang
 * 
 */
public interface LuceneIndexTemplate {
	public List<String> list(Query query, String field, Limitable limitable,
			Sort sort);

	public Page<String> page(Query query, String field, Pageable pageable,
			Sort sort);

	public void addDocument(Document document);

	public void addDocuments(Collection<Document> documents);

	public void updateDocument(Term term, Document document);

	public void deleteDocuments(Term... terms);

	public void deleteDocuments(Term term);

	public void deleteDocuments(Query... queries);

	public void deleteDocuments(Query query);

	public void deleteAll();
}
