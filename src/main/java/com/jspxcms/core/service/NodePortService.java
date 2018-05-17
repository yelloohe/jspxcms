package com.jspxcms.core.service;

import com.jspxcms.core.domain.Node;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by PONY on 2017/7/17.
 */
public interface NodePortService {

    public List<Node> importNode(Integer siteId, Integer userId, Reader reader) throws JAXBException;

    public void exportNode(Integer siteId, Writer writer) throws JAXBException, IOException;
}
