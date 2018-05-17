package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.WorkflowProcess;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkflowProcess is a Querydsl query type for WorkflowProcess
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWorkflowProcess extends EntityPathBase<WorkflowProcess> {

    private static final long serialVersionUID = 2017425234L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkflowProcess workflowProcess = new QWorkflowProcess("workflowProcess");

    public final DateTimePath<java.util.Date> beginDate = createDateTime("beginDate", java.util.Date.class);

    public final NumberPath<Integer> dataId = createNumber("dataId", Integer.class);

    public final NumberPath<Integer> dataType = createNumber("dataType", Integer.class);

    public final BooleanPath end = createBoolean("end");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final SetPath<com.jspxcms.core.domain.WorkflowLog, QWorkflowLog> logs = this.<com.jspxcms.core.domain.WorkflowLog, QWorkflowLog>createSet("logs", com.jspxcms.core.domain.WorkflowLog.class, QWorkflowLog.class, PathInits.DIRECT2);

    public final BooleanPath rejection = createBoolean("rejection");

    public final QSite site;

    public final QWorkflowStep step;

    public final QUser user;

    public final QWorkflow workflow;

    public QWorkflowProcess(String variable) {
        this(WorkflowProcess.class, forVariable(variable), INITS);
    }

    public QWorkflowProcess(Path<? extends WorkflowProcess> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkflowProcess(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkflowProcess(PathMetadata metadata, PathInits inits) {
        this(WorkflowProcess.class, metadata, inits);
    }

    public QWorkflowProcess(Class<? extends WorkflowProcess> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.step = inits.isInitialized("step") ? new QWorkflowStep(forProperty("step"), inits.get("step")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
        this.workflow = inits.isInitialized("workflow") ? new QWorkflow(forProperty("workflow"), inits.get("workflow")) : null;
    }

}

