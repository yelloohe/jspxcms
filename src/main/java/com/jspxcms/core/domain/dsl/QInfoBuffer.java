package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoBuffer;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoBuffer is a Querydsl query type for InfoBuffer
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoBuffer extends EntityPathBase<InfoBuffer> {

    private static final long serialVersionUID = -668291156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoBuffer infoBuffer = new QInfoBuffer("infoBuffer");

    public final NumberPath<Integer> burys = createNumber("burys", Integer.class);

    public final NumberPath<Integer> comments = createNumber("comments", Integer.class);

    public final NumberPath<Integer> diggs = createNumber("diggs", Integer.class);

    public final NumberPath<Integer> downloads = createNumber("downloads", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QInfo info;

    public final NumberPath<Integer> involveds = createNumber("involveds", Integer.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QInfoBuffer(String variable) {
        this(InfoBuffer.class, forVariable(variable), INITS);
    }

    public QInfoBuffer(Path<? extends InfoBuffer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoBuffer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoBuffer(PathMetadata metadata, PathInits inits) {
        this(InfoBuffer.class, metadata, inits);
    }

    public QInfoBuffer(Class<? extends InfoBuffer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
    }

}

