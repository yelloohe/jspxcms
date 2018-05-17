package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoMemberGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoMemberGroup is a Querydsl query type for InfoMemberGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoMemberGroup extends EntityPathBase<InfoMemberGroup> {

    private static final long serialVersionUID = 190919705L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoMemberGroup infoMemberGroup = new QInfoMemberGroup("infoMemberGroup");

    public final QMemberGroup group;

    public final QInfo info;

    public final BooleanPath viewPerm = createBoolean("viewPerm");

    public QInfoMemberGroup(String variable) {
        this(InfoMemberGroup.class, forVariable(variable), INITS);
    }

    public QInfoMemberGroup(Path<? extends InfoMemberGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoMemberGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoMemberGroup(PathMetadata metadata, PathInits inits) {
        this(InfoMemberGroup.class, metadata, inits);
    }

    public QInfoMemberGroup(Class<? extends InfoMemberGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QMemberGroup(forProperty("group")) : null;
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
    }

}

