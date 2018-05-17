package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoPush;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoPush is a Querydsl query type for InfoPush
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoPush extends EntityPathBase<InfoPush> {

    private static final long serialVersionUID = 1277932934L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoPush infoPush = new QInfoPush("infoPush");

    public final DateTimePath<java.util.Date> created = createDateTime("created", java.util.Date.class);

    public final QSite fromSite;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QInfo info;

    public final QSite toSite;

    public final QUser user;

    public QInfoPush(String variable) {
        this(InfoPush.class, forVariable(variable), INITS);
    }

    public QInfoPush(Path<? extends InfoPush> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoPush(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoPush(PathMetadata metadata, PathInits inits) {
        this(InfoPush.class, metadata, inits);
    }

    public QInfoPush(Class<? extends InfoPush> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromSite = inits.isInitialized("fromSite") ? new QSite(forProperty("fromSite"), inits.get("fromSite")) : null;
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.toSite = inits.isInitialized("toSite") ? new QSite(forProperty("toSite"), inits.get("toSite")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

