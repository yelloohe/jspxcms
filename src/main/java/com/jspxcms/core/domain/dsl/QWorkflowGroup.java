package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.WorkflowGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkflowGroup is a Querydsl query type for WorkflowGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWorkflowGroup extends EntityPathBase<WorkflowGroup> {

    private static final long serialVersionUID = -167105502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkflowGroup workflowGroup = new QWorkflowGroup("workflowGroup");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public QWorkflowGroup(String variable) {
        this(WorkflowGroup.class, forVariable(variable), INITS);
    }

    public QWorkflowGroup(Path<? extends WorkflowGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkflowGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkflowGroup(PathMetadata metadata, PathInits inits) {
        this(WorkflowGroup.class, metadata, inits);
    }

    public QWorkflowGroup(Class<? extends WorkflowGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

