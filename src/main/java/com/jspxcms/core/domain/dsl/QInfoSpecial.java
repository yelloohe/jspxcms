package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoSpecial;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoSpecial is a Querydsl query type for InfoSpecial
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoSpecial extends EntityPathBase<InfoSpecial> {

    private static final long serialVersionUID = -1478651187L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoSpecial infoSpecial = new QInfoSpecial("infoSpecial");

    public final QInfo info;

    public final QSpecial special;

    public QInfoSpecial(String variable) {
        this(InfoSpecial.class, forVariable(variable), INITS);
    }

    public QInfoSpecial(Path<? extends InfoSpecial> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoSpecial(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoSpecial(PathMetadata metadata, PathInits inits) {
        this(InfoSpecial.class, metadata, inits);
    }

    public QInfoSpecial(Class<? extends InfoSpecial> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.special = inits.isInitialized("special") ? new QSpecial(forProperty("special"), inits.get("special")) : null;
    }

}

