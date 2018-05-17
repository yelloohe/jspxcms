package com.jspxcms.core.service.impl;

import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.service.NodePortService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.NodeService;
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
public class NodePortServiceImpl implements NodePortService {
    @XmlRootElement
    private static class Nodes {
        @XmlElement(name = "node")
        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        private List<Node> nodes = new ArrayList<>();
    }

    public List<Node> importNode(Integer siteId, Integer userId, Reader reader) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Nodes.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Nodes nodes = (Nodes) unmarshaller.unmarshal(reader);
        List<Node> nodeList = nodes.getNodes();
        nodeService.importNode(nodeList, userId, siteId);
        return nodeList;
    }

    public void exportNode(Integer siteId, Writer writer) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Nodes.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        // output pretty printed
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        List<Node> nodeList = nodeQuery.findList(siteId);
        for (Node node : nodeList) {
            // 文档模型只导出名称和编码
            Model infoModel = new Model();
            Model srcInfoModel = node.getInfoModel();
            if (srcInfoModel != null) {
                infoModel.setName(srcInfoModel.getName());
                infoModel.setNumber(srcInfoModel.getNumber());
                infoModel.setType(srcInfoModel.getType());
                node.setInfoModel(infoModel);
            }
            // 栏目模型只导出名称和编码
            Model nodeModel = new Model();
            Model srcNodeModel = node.getNodeModel();
            if (srcNodeModel != null) {
                nodeModel.setName(srcNodeModel.getName());
                nodeModel.setNumber(srcNodeModel.getNumber());
                nodeModel.setType(srcNodeModel.getType());
                node.setNodeModel(nodeModel);
            }
            // 父栏目只导出名称和编码
            Node parent = new Node();
            Node srcParent = node.getParent();
            if (srcParent != null) {
                parent.setName(srcParent.getName());
                parent.setNumber(srcParent.getNumber());
                node.setParent(parent);
            }
        }
        Nodes xmlList = new Nodes();
        xmlList.setNodes(nodeList);
        marshaller.marshal(xmlList, writer);
    }

    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeQueryService nodeQuery;
}
