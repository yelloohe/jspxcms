package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Global;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGlobal is a Querydsl query type for Global
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGlobal extends EntityPathBase<Global> {

    private static final long serialVersionUID = -115619903L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGlobal global = new QGlobal("global");

    public final NumberPath<Integer> captchaErrors = createNumber("captchaErrors", Integer.class);

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final StringPath contextPath = createString("contextPath");

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final StringPath dataVersion = createString("dataVersion");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> port = createNumber("port", Integer.class);

    public final StringPath protocol = createString("protocol");

    public final ListPath<com.jspxcms.core.domain.Site, QSite> sites = this.<com.jspxcms.core.domain.Site, QSite>createList("sites", com.jspxcms.core.domain.Site.class, QSite.class, PathInits.DIRECT2);

    public final QPublishPoint uploadsPublishPoint;

    public QGlobal(String variable) {
        this(Global.class, forVariable(variable), INITS);
    }

    public QGlobal(Path<? extends Global> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGlobal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGlobal(PathMetadata metadata, PathInits inits) {
        this(Global.class, metadata, inits);
    }

    public QGlobal(Class<? extends Global> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.uploadsPublishPoint = inits.isInitialized("uploadsPublishPoint") ? new QPublishPoint(forProperty("uploadsPublishPoint"), inits.get("uploadsPublishPoint")) : null;
    }

}

