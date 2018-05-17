package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.NodeBuffer;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNodeBuffer is a Querydsl query type for NodeBuffer
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNodeBuffer extends EntityPathBase<NodeBuffer> {

    private static final long serialVersionUID = 1360898560L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNodeBuffer nodeBuffer = new QNodeBuffer("nodeBuffer");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QNode node;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QNodeBuffer(String variable) {
        this(NodeBuffer.class, forVariable(variable), INITS);
    }

    public QNodeBuffer(Path<? extends NodeBuffer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNodeBuffer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNodeBuffer(PathMetadata metadata, PathInits inits) {
        this(NodeBuffer.class, metadata, inits);
    }

    public QNodeBuffer(Class<? extends NodeBuffer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.node = inits.isInitialized("node") ? new QNode(forProperty("node"), inits.get("node")) : null;
    }

}

