package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.NodeRole;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNodeRole is a Querydsl query type for NodeRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNodeRole extends EntityPathBase<NodeRole> {

    private static final long serialVersionUID = -2071853514L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNodeRole nodeRole = new QNodeRole("nodeRole");

    public final BooleanPath infoPerm = createBoolean("infoPerm");

    public final QNode node;

    public final BooleanPath nodePerm = createBoolean("nodePerm");

    public final QRole role;

    public QNodeRole(String variable) {
        this(NodeRole.class, forVariable(variable), INITS);
    }

    public QNodeRole(Path<? extends NodeRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNodeRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNodeRole(PathMetadata metadata, PathInits inits) {
        this(NodeRole.class, metadata, inits);
    }

    public QNodeRole(Class<? extends NodeRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.node = inits.isInitialized("node") ? new QNode(forProperty("node"), inits.get("node")) : null;
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role"), inits.get("role")) : null;
    }

}

