package com.jspxcms.core.service.impl;

import com.jspxcms.core.domain.Attribute;
import com.jspxcms.core.service.AttributePortService;
import com.jspxcms.core.service.AttributeService;
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
public class AttributePortServiceImpl implements AttributePortService {
    @XmlRootElement
    private static class Attributes {
        @XmlElement(name = "attribute")
        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }

        private List<Attribute> attributes = new ArrayList<>();
    }

    public List<Attribute> importAttribute(Integer siteId, Integer userId, Reader reader) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Attributes.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Attributes attributes = (Attributes) unmarshaller.unmarshal(reader);
        List<Attribute> attributeList = attributes.getAttributes();
        attributeService.importAttribute(attributeList, siteId);
        return attributeList;
    }

    public void exportAttribute(Integer siteId, Writer writer) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Attributes.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        // output pretty printed
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        List<Attribute> attributeList = attributeService.findList(siteId);
        Attributes xmlList = new Attributes();
        xmlList.setAttributes(attributeList);
        marshaller.marshal(xmlList, writer);
    }

    @Autowired
    private AttributeService attributeService;
}
