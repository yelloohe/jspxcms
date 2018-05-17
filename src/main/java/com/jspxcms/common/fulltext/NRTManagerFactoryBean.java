package com.jspxcms.common.fulltext;

import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 近实时Manager工厂类
 * 
 * @author liufang
 * 
 */
public class NRTManagerFactoryBean implements FactoryBean<NRTManager>,
		InitializingBean, DisposableBean {
	private NRTManager nrtManager;

	private TrackingIndexWriter trackingIndexWriter;

	private NRTManagerReopenThread reopenThread;

	public NRTManager getObject() throws Exception {
		if (nrtManager == null) {
			nrtManager = new NRTManager(trackingIndexWriter,
					new SearcherFactory());
			reopenThread = new NRTManagerReopenThread(nrtManager, 5.0, 0.1);
			reopenThread.setName("NRT Reopen Thread");
			reopenThread.setPriority(Math.min(Thread.currentThread()
					.getPriority() + 2, Thread.MAX_PRIORITY));
			reopenThread.setDaemon(true);
			reopenThread.start();
		}
		return nrtManager;
	}

	public Class<NRTManager> getObjectType() {
		return NRTManager.class;
	}

	public void afterPropertiesSet() throws Exception {
		if (trackingIndexWriter == null) {
			throw new BeanInitializationException(
					"Must specify a trackingIndexWriter property");
		}
	}

	public void destroy() throws Exception {
		if (reopenThread != null) {
			reopenThread.close();
		}
		if (nrtManager != null) {
			nrtManager.close();
		}
	}

	public boolean isSingleton() {
		return true;
	}

	public void setTrackingIndexWriter(TrackingIndexWriter trackingIndexWriter) {
		this.trackingIndexWriter = trackingIndexWriter;
	}
}
