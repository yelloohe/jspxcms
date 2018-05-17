package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.WorkflowLog;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkflowLog is a Querydsl query type for WorkflowLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWorkflowLog extends EntityPathBase<WorkflowLog> {

    private static final long serialVersionUID = -80616025L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkflowLog workflowLog = new QWorkflowLog("workflowLog");

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final StringPath from = createString("from");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath opinion = createString("opinion");

    public final QWorkflowProcess process;

    public final QSite site;

    public final StringPath to = createString("to");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final QUser user;

    public QWorkflowLog(String variable) {
        this(WorkflowLog.class, forVariable(variable), INITS);
    }

    public QWorkflowLog(Path<? extends WorkflowLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkflowLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkflowLog(PathMetadata metadata, PathInits inits) {
        this(WorkflowLog.class, metadata, inits);
    }

    public QWorkflowLog(Class<? extends WorkflowLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.process = inits.isInitialized("process") ? new QWorkflowProcess(forProperty("process"), inits.get("process")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

