package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.WorkflowStepRole;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkflowStepRole is a Querydsl query type for WorkflowStepRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWorkflowStepRole extends EntityPathBase<WorkflowStepRole> {

    private static final long serialVersionUID = 549288159L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkflowStepRole workflowStepRole = new QWorkflowStepRole("workflowStepRole");

    public final QRole role;

    public final NumberPath<Integer> roleIndex = createNumber("roleIndex", Integer.class);

    public final QWorkflowStep step;

    public QWorkflowStepRole(String variable) {
        this(WorkflowStepRole.class, forVariable(variable), INITS);
    }

    public QWorkflowStepRole(Path<? extends WorkflowStepRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkflowStepRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkflowStepRole(PathMetadata metadata, PathInits inits) {
        this(WorkflowStepRole.class, metadata, inits);
    }

    public QWorkflowStepRole(Class<? extends WorkflowStepRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role"), inits.get("role")) : null;
        this.step = inits.isInitialized("step") ? new QWorkflowStep(forProperty("step"), inits.get("step")) : null;
    }

}

