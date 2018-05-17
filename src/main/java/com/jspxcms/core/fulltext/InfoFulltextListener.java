package com.jspxcms.core.fulltext;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.listener.AbstractInfoListener;

/**
 * InfoFulltextListener
 * 
 * @author liufang
 * 
 */
@Component
public class InfoFulltextListener extends AbstractInfoListener {
	@Override
	public void postInfoSave(Info bean) {
		generator.addDocument(new Info[] { bean });
	}

	@Override
	public void postInfoUpdate(Info bean) {
		generator.updateDocument(new Info[] { bean });
	}

	@Override
	public void postInfoPass(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));
	}

	@Override
	public void postInfoReject(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));
	}

	@Override
	public void postInfoMove(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));
	}

	@Override
	public void postInfoDelete(List<Info> beans) {
		generator.deleteDocuments(beans.toArray(new Info[beans.size()]));
	}

	public void postInfoArchive(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));
	}

	public void postInfoLogicDelete(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));

	}

	public void postInfoRecall(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));
	}

	public void postInfoRecycle(List<Info> beans) {
		generator.updateDocument(beans.toArray(new Info[beans.size()]));
	}

	private InfoFulltextGenerator generator;

	@Autowired
	public void setInfoFulltextGenerator(InfoFulltextGenerator generator) {
		this.generator = generator;
	}
}
