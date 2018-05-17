package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.UserMemberGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserMemberGroup is a Querydsl query type for UserMemberGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserMemberGroup extends EntityPathBase<UserMemberGroup> {

    private static final long serialVersionUID = -745469732L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserMemberGroup userMemberGroup = new QUserMemberGroup("userMemberGroup");

    public final QMemberGroup group;

    public final QUser user;

    public QUserMemberGroup(String variable) {
        this(UserMemberGroup.class, forVariable(variable), INITS);
    }

    public QUserMemberGroup(Path<? extends UserMemberGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserMemberGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserMemberGroup(PathMetadata metadata, PathInits inits) {
        this(UserMemberGroup.class, metadata, inits);
    }

    public QUserMemberGroup(Class<? extends UserMemberGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QMemberGroup(forProperty("group")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

