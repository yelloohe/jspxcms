package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.WorkflowStep;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkflowStep is a Querydsl query type for WorkflowStep
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWorkflowStep extends EntityPathBase<WorkflowStep> {

    private static final long serialVersionUID = 1796083913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkflowStep workflowStep = new QWorkflowStep("workflowStep");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final ListPath<com.jspxcms.core.domain.WorkflowStepRole, QWorkflowStepRole> stepRoles = this.<com.jspxcms.core.domain.WorkflowStepRole, QWorkflowStepRole>createList("stepRoles", com.jspxcms.core.domain.WorkflowStepRole.class, QWorkflowStepRole.class, PathInits.DIRECT2);

    public final QWorkflow workflow;

    public QWorkflowStep(String variable) {
        this(WorkflowStep.class, forVariable(variable), INITS);
    }

    public QWorkflowStep(Path<? extends WorkflowStep> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkflowStep(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkflowStep(PathMetadata metadata, PathInits inits) {
        this(WorkflowStep.class, metadata, inits);
    }

    public QWorkflowStep(Class<? extends WorkflowStep> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workflow = inits.isInitialized("workflow") ? new QWorkflow(forProperty("workflow"), inits.get("workflow")) : null;
    }

}

