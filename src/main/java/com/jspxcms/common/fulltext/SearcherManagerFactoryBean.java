package com.jspxcms.common.fulltext;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 搜索Manager工厂类
 * 
 * @author liufang
 * 
 */
public class SearcherManagerFactoryBean implements
		FactoryBean<SearcherManager>, InitializingBean, DisposableBean {
	private SearcherManager searcherManger;

	private IndexWriter indexWriter;

	public SearcherManager getObject() throws Exception {
		if (searcherManger == null) {
			searcherManger = new SearcherManager(indexWriter, true,
					new SearcherFactory());
		}
		return searcherManger;
	}

	public Class<SearcherManager> getObjectType() {
		return SearcherManager.class;
	}

	public void afterPropertiesSet() throws Exception {
		if (indexWriter == null) {
			throw new BeanInitializationException(
					"Must specify a indexWriter property");
		}
	}

	public void destroy() throws Exception {
		if (searcherManger != null) {
			searcherManger.close();
		}
	}

	public boolean isSingleton() {
		return true;
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}
}
