package com.jspxcms.core.service;

import com.jspxcms.core.domain.Site;

import javax.xml.bind.JAXBException;
import java.io.*;

/**
 * Created by PONY on 2017/7/17.
 */
public interface SitePortService {
    public void exportSite(Site site, Writer writer) throws JAXBException, IOException;

    public File exportAll(Site site) throws IOException, JAXBException;

    public Site importSite(Integer userId, Reader reader) throws JAXBException;

    public void importAll(File dir,Integer userId) throws IOException, JAXBException;
}
