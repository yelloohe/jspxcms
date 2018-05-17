package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.OperationLog;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOperationLog is a Querydsl query type for OperationLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOperationLog extends EntityPathBase<OperationLog> {

    private static final long serialVersionUID = -38552069L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOperationLog operationLog = new QOperationLog("operationLog");

    public final StringPath area = createString("area");

    public final StringPath country = createString("country");

    public final NumberPath<Integer> dataId = createNumber("dataId", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final StringPath name = createString("name");

    public final QSite site;

    public final StringPath text = createString("text");

    public final DateTimePath<java.util.Date> time = createDateTime("time", java.util.Date.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final QUser user;

    public QOperationLog(String variable) {
        this(OperationLog.class, forVariable(variable), INITS);
    }

    public QOperationLog(Path<? extends OperationLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOperationLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOperationLog(PathMetadata metadata, PathInits inits) {
        this(OperationLog.class, metadata, inits);
    }

    public QOperationLog(Class<? extends OperationLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

