package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoAttribute;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoAttribute is a Querydsl query type for InfoAttribute
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoAttribute extends EntityPathBase<InfoAttribute> {

    private static final long serialVersionUID = -1936734288L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoAttribute infoAttribute = new QInfoAttribute("infoAttribute");

    public final QAttribute attribute;

    public final StringPath image = createString("image");

    public final QInfo info;

    public QInfoAttribute(String variable) {
        this(InfoAttribute.class, forVariable(variable), INITS);
    }

    public QInfoAttribute(Path<? extends InfoAttribute> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoAttribute(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoAttribute(PathMetadata metadata, PathInits inits) {
        this(InfoAttribute.class, metadata, inits);
    }

    public QInfoAttribute(Class<? extends InfoAttribute> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attribute = inits.isInitialized("attribute") ? new QAttribute(forProperty("attribute"), inits.get("attribute")) : null;
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
    }

}

