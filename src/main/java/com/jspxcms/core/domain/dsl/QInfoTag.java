package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoTag;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoTag is a Querydsl query type for InfoTag
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoTag extends EntityPathBase<InfoTag> {

    private static final long serialVersionUID = -1759888466L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoTag infoTag = new QInfoTag("infoTag");

    public final QInfo info;

    public final QTag tag;

    public QInfoTag(String variable) {
        this(InfoTag.class, forVariable(variable), INITS);
    }

    public QInfoTag(Path<? extends InfoTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoTag(PathMetadata metadata, PathInits inits) {
        this(InfoTag.class, metadata, inits);
    }

    public QInfoTag(Class<? extends InfoTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag"), inits.get("tag")) : null;
    }

}

