package com.jspxcms.core.service.impl;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.service.InfoPortService;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PONY on 2017/7/17.
 */
@Service
public class InfoPortServiceImpl implements InfoPortService {
    @XmlRootElement
    private static class Infos {
        @XmlElement(name = "info")
        public List<Info> getInfos() {
            return infos;
        }

        public void setInfos(List<Info> infos) {
            this.infos = infos;
        }

        private List<Info> infos = new ArrayList<>();
    }

    public List<Info> importInfo(Integer siteId, Integer userId, Reader reader) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Infos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Infos infos = (Infos) unmarshaller.unmarshal(reader);
        List<Info> infoList = infos.getInfos();
        infoService.importInfo(infoList, userId, siteId);
        return infoList;
    }

    public void exportInfo(Integer siteId, Writer writer) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Infos.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        // output pretty printed
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        List<Info> infoList = infoQuery.findBySiteId(siteId);
        for (Info info : infoList) {
            Node node = new Node();
            Node srcNode = info.getNode();
            if (srcNode != null) {
                node.setName(srcNode.getName());
                node.setNumber(srcNode.getNumber());
            }
            info.setNode(node);
        }
        Infos xmlList = new Infos();
        xmlList.setInfos(infoList);
        marshaller.marshal(xmlList, writer);
    }

    @Autowired
    private InfoService infoService;
    @Autowired
    private InfoQueryService infoQuery;
}
