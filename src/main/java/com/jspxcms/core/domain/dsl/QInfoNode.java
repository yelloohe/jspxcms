package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoNode;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoNode is a Querydsl query type for InfoNode
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoNode extends EntityPathBase<InfoNode> {

    private static final long serialVersionUID = 1277867118L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoNode infoNode = new QInfoNode("infoNode");

    public final QInfo info;

    public final QNode node;

    public QInfoNode(String variable) {
        this(InfoNode.class, forVariable(variable), INITS);
    }

    public QInfoNode(Path<? extends InfoNode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoNode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoNode(PathMetadata metadata, PathInits inits) {
        this(InfoNode.class, metadata, inits);
    }

    public QInfoNode(Class<? extends InfoNode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.node = inits.isInitialized("node") ? new QNode(forProperty("node"), inits.get("node")) : null;
    }

}

