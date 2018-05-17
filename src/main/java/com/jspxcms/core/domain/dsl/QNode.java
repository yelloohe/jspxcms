package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Node;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNode is a Querydsl query type for Node
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNode extends EntityPathBase<Node> {

    private static final long serialVersionUID = 719643040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNode node = new QNode("node");

    public final SetPath<com.jspxcms.core.domain.NodeBuffer, QNodeBuffer> buffers = this.<com.jspxcms.core.domain.NodeBuffer, QNodeBuffer>createSet("buffers", com.jspxcms.core.domain.NodeBuffer.class, QNodeBuffer.class, PathInits.DIRECT2);

    public final ListPath<Node, QNode> children = this.<Node, QNode>createList("children", Node.class, QNode.class, PathInits.DIRECT2);

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final QUser creator;

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final SetPath<com.jspxcms.core.domain.NodeDetail, QNodeDetail> details = this.<com.jspxcms.core.domain.NodeDetail, QNodeDetail>createSet("details", com.jspxcms.core.domain.NodeDetail.class, QNodeDetail.class, PathInits.DIRECT2);

    public final BooleanPath hidden = createBoolean("hidden");

    public final StringPath htmlStatus = createString("htmlStatus");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QModel infoModel;

    public final StringPath name = createString("name");

    public final SetPath<com.jspxcms.core.domain.NodeMemberGroup, QNodeMemberGroup> nodeGroups = this.<com.jspxcms.core.domain.NodeMemberGroup, QNodeMemberGroup>createSet("nodeGroups", com.jspxcms.core.domain.NodeMemberGroup.class, QNodeMemberGroup.class, PathInits.DIRECT2);

    public final QModel nodeModel;

    public final SetPath<com.jspxcms.core.domain.NodeOrg, QNodeOrg> nodeOrgs = this.<com.jspxcms.core.domain.NodeOrg, QNodeOrg>createSet("nodeOrgs", com.jspxcms.core.domain.NodeOrg.class, QNodeOrg.class, PathInits.DIRECT2);

    public final SetPath<com.jspxcms.core.domain.NodeRole, QNodeRole> nodeRoles = this.<com.jspxcms.core.domain.NodeRole, QNodeRole>createSet("nodeRoles", com.jspxcms.core.domain.NodeRole.class, QNodeRole.class, PathInits.DIRECT2);

    public final StringPath number = createString("number");

    public final NumberPath<Integer> p0 = createNumber("p0", Integer.class);

    public final NumberPath<Integer> p1 = createNumber("p1", Integer.class);

    public final NumberPath<Integer> p2 = createNumber("p2", Integer.class);

    public final NumberPath<Integer> p3 = createNumber("p3", Integer.class);

    public final NumberPath<Integer> p4 = createNumber("p4", Integer.class);

    public final NumberPath<Integer> p5 = createNumber("p5", Integer.class);

    public final NumberPath<Integer> p6 = createNumber("p6", Integer.class);

    public final QNode parent;

    public final BooleanPath realNode = createBoolean("realNode");

    public final NumberPath<Integer> refers = createNumber("refers", Integer.class);

    public final QSite site;

    public final NumberPath<Integer> treeLevel = createNumber("treeLevel", Integer.class);

    public final StringPath treeMax = createString("treeMax");

    public final StringPath treeNumber = createString("treeNumber");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final QWorkflow workflow;

    public QNode(String variable) {
        this(Node.class, forVariable(variable), INITS);
    }

    public QNode(Path<? extends Node> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNode(PathMetadata metadata, PathInits inits) {
        this(Node.class, metadata, inits);
    }

    public QNode(Class<? extends Node> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new QUser(forProperty("creator"), inits.get("creator")) : null;
        this.infoModel = inits.isInitialized("infoModel") ? new QModel(forProperty("infoModel"), inits.get("infoModel")) : null;
        this.nodeModel = inits.isInitialized("nodeModel") ? new QModel(forProperty("nodeModel"), inits.get("nodeModel")) : null;
        this.parent = inits.isInitialized("parent") ? new QNode(forProperty("parent"), inits.get("parent")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.workflow = inits.isInitialized("workflow") ? new QWorkflow(forProperty("workflow"), inits.get("workflow")) : null;
    }

}

