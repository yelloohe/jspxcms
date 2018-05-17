package com.jspxcms.common.fulltext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;

/**
 * 近实时索引模板实现
 * 
 * @author liufang
 * 
 */
public class NRTLuceneIndexTemplate implements LuceneIndexTemplate,
		InitializingBean {
	private TrackingIndexWriter trackingIndexWriter;

	private NRTManager nrtManager;

	public NRTLuceneIndexTemplate() {
	}

	public NRTLuceneIndexTemplate(TrackingIndexWriter trackingIndexWriter,
			NRTManager nrtManager) {
		this.trackingIndexWriter = trackingIndexWriter;
		this.nrtManager = nrtManager;
	}

	public List<String> list(Query query, String field, Limitable limitable,
			Sort sort) {
		IndexSearcher searcher = nrtManager.acquire();
		try {
			try {
				int n = limitable.getLastResult();
				n = n <= 0 ? 2000 : n;
				if (sort == null) {
					sort = new Sort();
				}
				TopDocs results = searcher.search(query, n, sort);
				int length = results.scoreDocs.length;
				List<String> list = new ArrayList<String>(length);
				for (ScoreDoc hit : results.scoreDocs) {
					list.add(searcher.doc(hit.doc).get(field));
				}
				return list;
			} finally {
				nrtManager.release(searcher);
			}
		} catch (Exception e) {
			throw new LuceneException("Error during searching.", e);
		}
	}

	public Page<String> page(Query query, String field, Pageable pageable,
			Sort sort) {
		IndexSearcher searcher = nrtManager.acquire();
		try {
			try {
				int n = pageable.getOffset() + pageable.getPageSize();
				if (sort == null) {
					sort = new Sort();
				}
				TopDocs results = searcher.search(query, n, sort);
				int length = results.scoreDocs.length;
				int size = length - pageable.getOffset();
				List<String> content;
				if (size > 0) {
					content = new ArrayList<String>(size);
					ScoreDoc hit;
					for (int i = pageable.getOffset(); i < length; i++) {
						hit = results.scoreDocs[i];
						content.add(searcher.doc(hit.doc).get(field));
					}
				} else {
					content = Collections.emptyList();
				}
				int total = results.totalHits;
				return new PageImpl<String>(content, pageable, total);
			} finally {
				nrtManager.release(searcher);
			}
		} catch (Exception e) {
			throw new LuceneException("Error during searching.", e);
		}
	}
//
//	public List<Document> list(Predicate predicate,
//			EntityPath<Document> entityPath, Limitable limitable) {
//		IndexSearcher searcher = nrtManager.acquire();
//		try {
//			try {
//				LuceneQuery query = new LuceneQuery(searcher);
//				query.where(predicate);
//				QuerydslUtils.applySorting(query, entityPath,
//						limitable.getSort());
//				Integer firstResult = limitable.getFirstResult();
//				if (firstResult != null && firstResult > 0) {
//					query.offset(firstResult);
//				}
//				Integer maxResults = limitable.getMaxResults();
//				if (maxResults != null && maxResults > 0) {
//					query.limit(maxResults);
//				}
//				return query.list();
//			} finally {
//				if (searcher != null) {
//					nrtManager.release(searcher);
//				}
//			}
//		} catch (Exception e) {
//			throw new LuceneException("Error during searching.", e);
//		}
//	}
//
//	public Page<Document> page(Predicate predicate,
//			EntityPath<Document> entityPath, Pageable pageable) {
//		IndexSearcher searcher = nrtManager.acquire();
//		try {
//			try {
//				LuceneQuery query = new LuceneQuery(searcher);
//				long total = query.count();
//				List<Document> content;
//				if (total > pageable.getOffset()) {
//					query.offset(pageable.getOffset());
//					query.limit(pageable.getPageSize());
//					QuerydslUtils.applySorting(query, entityPath,
//							pageable.getSort());
//					content = query.list();
//				} else {
//					content = Collections.emptyList();
//				}
//				Page<Document> page = new PageImpl<Document>(content, pageable,
//						total);
//				return page;
//			} finally {
//				if (searcher != null) {
//					nrtManager.release(searcher);
//				}
//			}
//		} catch (Exception e) {
//			throw new LuceneException("Error during searching.", e);
//		}
//	}

	public void addDocument(Document document) {
		try {
			trackingIndexWriter.addDocument(document);
		} catch (Exception e) {
			throw new LuceneException("Error during adding a document.", e);
		}
	}

	public void addDocuments(Collection<Document> documents) {
		try {
			trackingIndexWriter.addDocuments(documents);
		} catch (Exception e) {
			throw new LuceneException("Error during adding a document.", e);
		}

	}

	public void updateDocument(Term term, Document document) {
		try {
			trackingIndexWriter.updateDocument(term, document);
		} catch (Exception e) {
			throw new LuceneException("Error during updating a document.", e);
		}
	}

	public void deleteDocuments(Term... terms) {
		try {
			trackingIndexWriter.deleteDocuments(terms);
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteDocuments(Term term) {
		try {
			trackingIndexWriter.deleteDocuments(term);
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteDocuments(Query... queries) {
		try {
			trackingIndexWriter.deleteDocuments(queries);
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteDocuments(Query query) {
		try {
			trackingIndexWriter.deleteDocuments(query);
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteAll() {
		try {
			trackingIndexWriter.deleteAll();
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void afterPropertiesSet() throws Exception {
		if (trackingIndexWriter == null) {
			throw new IllegalArgumentException(
					"trackingIndexWriter is required");
		}
		if (nrtManager == null) {
			throw new IllegalArgumentException("nrtManager is required");
		}
	}

	public void setTrackingIndexWriter(TrackingIndexWriter trackingIndexWriter) {
		this.trackingIndexWriter = trackingIndexWriter;
	}

	public void setNrtManager(NRTManager nrtManager) {
		this.nrtManager = nrtManager;
	}
}
