package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.NodeMemberGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNodeMemberGroup is a Querydsl query type for NodeMemberGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNodeMemberGroup extends EntityPathBase<NodeMemberGroup> {

    private static final long serialVersionUID = -2070835643L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNodeMemberGroup nodeMemberGroup = new QNodeMemberGroup("nodeMemberGroup");

    public final BooleanPath commentPerm = createBoolean("commentPerm");

    public final BooleanPath contriPerm = createBoolean("contriPerm");

    public final QMemberGroup group;

    public final QNode node;

    public final BooleanPath viewPerm = createBoolean("viewPerm");

    public QNodeMemberGroup(String variable) {
        this(NodeMemberGroup.class, forVariable(variable), INITS);
    }

    public QNodeMemberGroup(Path<? extends NodeMemberGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNodeMemberGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNodeMemberGroup(PathMetadata metadata, PathInits inits) {
        this(NodeMemberGroup.class, metadata, inits);
    }

    public QNodeMemberGroup(Class<? extends NodeMemberGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QMemberGroup(forProperty("group")) : null;
        this.node = inits.isInitialized("node") ? new QNode(forProperty("node"), inits.get("node")) : null;
    }

}

