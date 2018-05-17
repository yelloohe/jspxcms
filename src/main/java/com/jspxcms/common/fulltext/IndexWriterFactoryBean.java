package com.jspxcms.common.fulltext;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * IndexWriter工厂类
 * 
 * @author liufang
 * 
 */
public class IndexWriterFactoryBean implements FactoryBean<IndexWriter>,
		InitializingBean, DisposableBean {
	private IndexWriter indexWriter;

	private Directory directory;
	private IndexWriterConfig indexWriterConfig;

	public IndexWriter getObject() throws Exception {
		if (indexWriter == null) {
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		}
		return indexWriter;
	}

	public Class<IndexWriter> getObjectType() {
		return IndexWriter.class;
	}

	public void afterPropertiesSet() throws Exception {
		if (directory == null) {
			throw new BeanInitializationException(
					"Must specify a directory property");
		}
		if (indexWriterConfig == null) {
			throw new BeanInitializationException(
					"Must specify a indexWriterConfig property");
		}
	}

	public void destroy() throws Exception {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}

	public boolean isSingleton() {
		return true;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	public void setIndexWriterConfig(IndexWriterConfig indexWriterConfig) {
		this.indexWriterConfig = indexWriterConfig;
	}
}
