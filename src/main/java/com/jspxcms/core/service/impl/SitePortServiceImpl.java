package com.jspxcms.core.service.impl;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Created by PONY on 2017/7/17.
 */
@Service
public class SitePortServiceImpl implements SitePortService {
    public void exportSite(Site site, Writer writer) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Site.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        // output pretty printed
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(site, writer);
    }

    public Site importSite(Integer userId, Reader reader) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Site.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Site site = (Site) unmarshaller.unmarshal(reader);
        siteService.importSite(site, userId);
        return site;
    }

    public void importAll(File dir, Integer userId) throws IOException, JAXBException {
        Site site = site = importSite(userId, new FileReader(getChildFile(dir, SITE_XML)));
        modelPortService.importModel(site.getId(), new FileReader(getChildFile(dir, MODEL_XML)));
        nodePortService.importNode(site.getId(), userId, new FileReader(getChildFile(dir, NODE_XML)));
        attributePortService.importAttribute(site.getId(), userId, new FileReader(getChildFile(dir, ATTRIBUTE_XML)));
        infoPortService.importInfo(site.getId(), userId, new FileReader(getChildFile(dir, INFO_XML)));
        String siteBase = site.getSiteBase(null);
        // 导入模板文件
        File templateFile = getChildFile(dir, TEMPLATE_DIR);
        if (templateFile != null) {
            FileHandler templateFileHandler = FileHandler.getLocalFileHandler(pathResolver, Constants.TEMPLATE_STORE_PATH);
            File destTemplateFile = templateFileHandler.getFile(siteBase);
            FileUtils.copyDirectory(templateFile, destTemplateFile);
        }
        // 导入上传文件
        File uploadsFile = getChildFile(dir, UPLOADS_DIR);
        if (uploadsFile != null) {
            FileHandler uploadFileHandler = site.getUploadsPublishPoint().getFileHandler(pathResolver);
            File destUploadFile = uploadFileHandler.getFile("");
            FileUtils.copyDirectory(uploadsFile, destUploadFile);
        }
    }

    /**
     * 获取文件夹的子文件
     *
     * @param dir  父文件夹
     * @param name 子文件名
     * @return 子文件。不存在则返回null
     */
    private File getChildFile(File dir, String name) {
        for (File f : dir.listFiles()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    public File exportAll(Site site) throws IOException, JAXBException {
        // 创建临时文件目录，用于存放导出文件
        File dir = FilesEx.getTempFile();
        dir.mkdirs();
        File siteFile = new File(dir, SITE_XML);
        this.exportSite(site, new FileWriter(siteFile));
        File modelFile = new File(dir, MODEL_XML);
        modelPortService.exportModel(site, new FileWriter(modelFile));
        File nodeFile = new File(dir, NODE_XML);
        nodePortService.exportNode(site.getId(), new FileWriter(nodeFile));
        File attributeFile = new File(dir, ATTRIBUTE_XML);
        attributePortService.exportAttribute(site.getId(), new FileWriter(attributeFile));
        File infoFile = new File(dir, INFO_XML);
        infoPortService.exportInfo(site.getId(), new FileWriter(infoFile));
        // 导出模板文件
        String siteBase = site.getSiteBase(null);
        File templateFile = new File(dir, TEMPLATE_DIR);
        FileHandler templateFileHandler = FileHandler.getLocalFileHandler(pathResolver, Constants.TEMPLATE_STORE_PATH);
        File srcTemplateFile = templateFileHandler.getFile(siteBase);
        if (srcTemplateFile.exists()) {
            FileUtils.copyDirectory(srcTemplateFile, templateFile);
        }
        // 导出上传文件
        File uploadsFile = new File(dir, UPLOADS_DIR);
        FileHandler uploadFileHandler = site.getUploadsPublishPoint().getFileHandler(pathResolver);
        File srcUploadFile = uploadFileHandler.getFile(siteBase);
        if (srcUploadFile.exists()) {
            FileUtils.copyDirectory(srcUploadFile, new File(uploadsFile, srcUploadFile.getName()));
        }
        return dir;
    }

    public static final String SITE_XML = "site.xml";
    public static final String MODEL_XML = "model.xml";
    public static final String NODE_XML = "node.xml";
    public static final String ATTRIBUTE_XML = "attribute.xml";
    public static final String INFO_XML = "info.xml";
    public static final String TEMPLATE_DIR = "template";
    public static final String UPLOADS_DIR = "uploads";

    @Autowired
    protected PathResolver pathResolver;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ModelPortService modelPortService;
    @Autowired
    private NodePortService nodePortService;
    @Autowired
    private AttributePortService attributePortService;
    @Autowired
    private InfoPortService infoPortService;
}
