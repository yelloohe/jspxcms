package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.MemberGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberGroup is a Querydsl query type for MemberGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberGroup extends EntityPathBase<MemberGroup> {

    private static final long serialVersionUID = -431674713L;

    public static final QMemberGroup memberGroup = new QMemberGroup("memberGroup");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final StringPath name = createString("name");

    public final SetPath<com.jspxcms.core.domain.NodeMemberGroup, QNodeMemberGroup> nodeGroups = this.<com.jspxcms.core.domain.NodeMemberGroup, QNodeMemberGroup>createSet("nodeGroups", com.jspxcms.core.domain.NodeMemberGroup.class, QNodeMemberGroup.class, PathInits.DIRECT2);

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public QMemberGroup(String variable) {
        super(MemberGroup.class, forVariable(variable));
    }

    public QMemberGroup(Path<? extends MemberGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberGroup(PathMetadata metadata) {
        super(MemberGroup.class, metadata);
    }

}

