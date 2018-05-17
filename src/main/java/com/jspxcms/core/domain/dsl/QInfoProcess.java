package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoProcess;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoProcess is a Querydsl query type for InfoProcess
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoProcess extends EntityPathBase<InfoProcess> {

    private static final long serialVersionUID = 220295299L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoProcess infoProcess = new QInfoProcess("infoProcess");

    public final QWorkflowProcess _super;

    //inherited
    public final DateTimePath<java.util.Date> beginDate;

    //inherited
    public final NumberPath<Integer> dataId;

    //inherited
    public final NumberPath<Integer> dataType;

    //inherited
    public final BooleanPath end;

    //inherited
    public final DateTimePath<java.util.Date> endDate;

    //inherited
    public final NumberPath<Integer> id;

    public final QInfo info;

    //inherited
    public final SetPath<com.jspxcms.core.domain.WorkflowLog, QWorkflowLog> logs;

    //inherited
    public final BooleanPath rejection;

    // inherited
    public final QSite site;

    // inherited
    public final QWorkflowStep step;

    // inherited
    public final QUser user;

    // inherited
    public final QWorkflow workflow;

    public QInfoProcess(String variable) {
        this(InfoProcess.class, forVariable(variable), INITS);
    }

    public QInfoProcess(Path<? extends InfoProcess> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoProcess(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoProcess(PathMetadata metadata, PathInits inits) {
        this(InfoProcess.class, metadata, inits);
    }

    public QInfoProcess(Class<? extends InfoProcess> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QWorkflowProcess(type, metadata, inits);
        this.beginDate = _super.beginDate;
        this.dataId = _super.dataId;
        this.dataType = _super.dataType;
        this.end = _super.end;
        this.endDate = _super.endDate;
        this.id = _super.id;
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.logs = _super.logs;
        this.rejection = _super.rejection;
        this.site = _super.site;
        this.step = _super.step;
        this.user = _super.user;
        this.workflow = _super.workflow;
    }

}

