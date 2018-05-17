package com.jspxcms.core.fulltext;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;

public interface InfoFulltextGenerator {

	public void addDocument(Info[] beans);

	public void updateDocument(Info[] beans);

	public void deleteDocuments(Info[] beans);

	public void addDocument(Integer siteId, final Node node, Integer userId);
}
