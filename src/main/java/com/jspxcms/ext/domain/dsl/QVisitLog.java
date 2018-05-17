package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.VisitLog;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVisitLog is a Querydsl query type for VisitLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVisitLog extends EntityPathBase<VisitLog> {

    private static final long serialVersionUID = -232297741L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVisitLog visitLog = new QVisitLog("visitLog");

    public final StringPath area = createString("area");

    public final StringPath browser = createString("browser");

    public final StringPath cookie = createString("cookie");

    public final StringPath country = createString("country");

    public final StringPath device = createString("device");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final StringPath os = createString("os");

    public final StringPath referrer = createString("referrer");

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final StringPath source = createString("source");

    public final DateTimePath<java.util.Date> time = createDateTime("time", java.util.Date.class);

    public final StringPath timeString = createString("timeString");

    public final StringPath url = createString("url");

    public final com.jspxcms.core.domain.dsl.QUser user;

    public final StringPath userAgent = createString("userAgent");

    public QVisitLog(String variable) {
        this(VisitLog.class, forVariable(variable), INITS);
    }

    public QVisitLog(Path<? extends VisitLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVisitLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVisitLog(PathMetadata metadata, PathInits inits) {
        this(VisitLog.class, metadata, inits);
    }

    public QVisitLog(Class<? extends VisitLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jspxcms.core.domain.dsl.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

