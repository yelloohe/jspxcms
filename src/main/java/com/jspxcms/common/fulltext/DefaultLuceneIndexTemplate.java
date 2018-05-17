package com.jspxcms.common.fulltext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;

/**
 * 默认全文检索模板
 * 
 * 每次查询前执行SearcherManager.maybeRefresh();
 * 
 * @author liufang
 * 
 */
public class DefaultLuceneIndexTemplate implements LuceneIndexTemplate, BeanFactoryAware {
	private boolean isAutoCommit = false;
	private IndexWriter indexWriter;
	private SearcherManager searcherManager;
	private BeanFactory beanFactory;

	public DefaultLuceneIndexTemplate() {
	}

	public DefaultLuceneIndexTemplate(IndexWriter indexWriter, SearcherManager searcherManager) {
		this.indexWriter = indexWriter;
		this.searcherManager = searcherManager;
	}

	public List<String> list(Query query, String field, Limitable limitable, Sort sort) {
		try {
			IndexSearcher searcher = null;
			try {
				getSearcherManager().maybeRefresh();
				searcher = getSearcherManager().acquire();
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
				if (searcher != null) {
					getSearcherManager().release(searcher);
				}
			}
		} catch (Exception e) {
			throw new LuceneException("Error during searching.", e);
		}
	}

	public Page<String> page(Query query, String field, Pageable pageable, Sort sort) {
		try {
			IndexSearcher searcher = null;
			try {
				getSearcherManager().maybeRefresh();
				searcher = getSearcherManager().acquire();
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
				if (searcher != null) {
					getSearcherManager().release(searcher);
				}
			}
		} catch (Exception e) {
			throw new LuceneException("Error during searching.", e);
		}
	}

	//
	// public List<Document> list(Predicate predicate,
	// EntityPath<Document> entityPath, Limitable limitable) {
	// try {
	// IndexSearcher searcher = null;
	// try {
	// getSearcherManager().maybeRefresh();
	// searcher = getSearcherManager().acquire();
	// LuceneQuery query = new LuceneQuery(searcher);
	// query.where(predicate);
	// QuerydslUtils.applySorting(query, entityPath,
	// limitable.getSort());
	// Integer firstResult = limitable.getFirstResult();
	// if (firstResult != null && firstResult > 0) {
	// query.offset(firstResult);
	// }
	// Integer maxResults = limitable.getMaxResults();
	// if (maxResults != null && maxResults > 0) {
	// query.limit(maxResults);
	// }
	// return query.list();
	// } finally {
	// if (searcher != null) {
	// getSearcherManager().release(searcher);
	// }
	// }
	// } catch (Exception e) {
	// throw new LuceneException("Error during searching.", e);
	// }
	// }
	//
	// public Page<Document> page(Predicate predicate,
	// EntityPath<Document> entityPath, Pageable pageable) {
	// try {
	// IndexSearcher searcher = null;
	// try {
	// getSearcherManager().maybeRefresh();
	// searcher = getSearcherManager().acquire();
	// LuceneQuery query = new LuceneQuery(searcher);
	// long total = query.count();
	// List<Document> content;
	// if (total > pageable.getOffset()) {
	// query.offset(pageable.getOffset());
	// query.limit(pageable.getPageSize());
	// QuerydslUtils.applySorting(query, entityPath,
	// pageable.getSort());
	// content = query.list();
	// } else {
	// content = Collections.emptyList();
	// }
	// Page<Document> page = new PageImpl<Document>(content, pageable,
	// total);
	// return page;
	// } finally {
	// if (searcher != null) {
	// getSearcherManager().release(searcher);
	// }
	// }
	// } catch (Exception e) {
	// throw new LuceneException("Error during searching.", e);
	// }
	// }

	public void addDocument(Document document) {
		try {
			getIndexWriter().addDocument(document);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during adding a document.", e);
		}
	}

	public void addDocuments(Collection<Document> documents) {
		try {
			getIndexWriter().addDocuments(documents);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during adding a document.", e);
		}

	}

	public void updateDocument(Term term, Document document) {
		try {
			getIndexWriter().updateDocument(term, document);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during updating a document.", e);
		}
	}

	public void deleteDocuments(Term... terms) {
		try {
			getIndexWriter().deleteDocuments(terms);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteDocuments(Term term) {
		try {
			getIndexWriter().deleteDocuments(term);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteDocuments(Query... queries) {
		try {
			getIndexWriter().deleteDocuments(queries);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteDocuments(Query query) {
		try {
			getIndexWriter().deleteDocuments(query);
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	public void deleteAll() {
		try {
			getIndexWriter().deleteAll();
			if (isAutoCommit) {
				getIndexWriter().commit();
			}
		} catch (Exception e) {
			throw new LuceneException("Error during deleting a document.", e);
		}
	}

	// public void afterPropertiesSet() throws Exception {
	// if (indexWriter == null) {
	// throw new IllegalArgumentException("indexFactory is required");
	// }
	// if (searcherManager == null) {
	// throw new IllegalArgumentException("indexFactory is required");
	// }
	// }

	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	public void setSearcherManager(SearcherManager searcherManager) {
		this.searcherManager = searcherManager;
	}

	public IndexWriter getIndexWriter() {
		if (indexWriter == null) {
			indexWriter = beanFactory.getBean(IndexWriter.class);
		}
		return indexWriter;
	}

	public SearcherManager getSearcherManager() {
		if (searcherManager == null) {
			searcherManager = beanFactory.getBean(SearcherManager.class);
		}
		return searcherManager;
	}

	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
