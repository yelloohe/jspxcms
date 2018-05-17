package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.UserOrg;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserOrg is a Querydsl query type for UserOrg
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserOrg extends EntityPathBase<UserOrg> {

    private static final long serialVersionUID = 442528443L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserOrg userOrg = new QUserOrg("userOrg");

    public final QOrg org;

    public final QUser user;

    public QUserOrg(String variable) {
        this(UserOrg.class, forVariable(variable), INITS);
    }

    public QUserOrg(Path<? extends UserOrg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserOrg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserOrg(PathMetadata metadata, PathInits inits) {
        this(UserOrg.class, metadata, inits);
    }

    public QUserOrg(Class<? extends UserOrg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.org = inits.isInitialized("org") ? new QOrg(forProperty("org"), inits.get("org")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

