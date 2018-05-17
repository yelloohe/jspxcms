package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoOrg;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoOrg is a Querydsl query type for InfoOrg
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoOrg extends EntityPathBase<InfoOrg> {

    private static final long serialVersionUID = -1759892744L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoOrg infoOrg = new QInfoOrg("infoOrg");

    public final QInfo info;

    public final QOrg org;

    public final BooleanPath viewPerm = createBoolean("viewPerm");

    public QInfoOrg(String variable) {
        this(InfoOrg.class, forVariable(variable), INITS);
    }

    public QInfoOrg(Path<? extends InfoOrg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoOrg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoOrg(PathMetadata metadata, PathInits inits) {
        this(InfoOrg.class, metadata, inits);
    }

    public QInfoOrg(Class<? extends InfoOrg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.org = inits.isInitialized("org") ? new QOrg(forProperty("org"), inits.get("org")) : null;
    }

}

