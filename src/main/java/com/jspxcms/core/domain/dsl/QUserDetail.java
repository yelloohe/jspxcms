package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.UserDetail;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserDetail is a Querydsl query type for UserDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserDetail extends EntityPathBase<UserDetail> {

    private static final long serialVersionUID = 1783771418L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserDetail userDetail = new QUserDetail("userDetail");

    public final StringPath bio = createString("bio");

    public final StringPath comeFrom = createString("comeFrom");

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final StringPath creationIp = createString("creationIp");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> lastLoginDate = createDateTime("lastLoginDate", java.util.Date.class);

    public final StringPath lastLoginIp = createString("lastLoginIp");

    public final NumberPath<Integer> loginErrorCount = createNumber("loginErrorCount", Integer.class);

    public final DateTimePath<java.util.Date> loginErrorDate = createDateTime("loginErrorDate", java.util.Date.class);

    public final NumberPath<Integer> logins = createNumber("logins", Integer.class);

    public final StringPath msn = createString("msn");

    public final DateTimePath<java.util.Date> prevLoginDate = createDateTime("prevLoginDate", java.util.Date.class);

    public final StringPath prevLoginIp = createString("prevLoginIp");

    public final StringPath qq = createString("qq");

    public final QUser user;

    public final DateTimePath<java.util.Date> validationDate = createDateTime("validationDate", java.util.Date.class);

    public final StringPath weixin = createString("weixin");

    public final BooleanPath withAvatar = createBoolean("withAvatar");

    public QUserDetail(String variable) {
        this(UserDetail.class, forVariable(variable), INITS);
    }

    public QUserDetail(Path<? extends UserDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserDetail(PathMetadata metadata, PathInits inits) {
        this(UserDetail.class, metadata, inits);
    }

    public QUserDetail(Class<? extends UserDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

