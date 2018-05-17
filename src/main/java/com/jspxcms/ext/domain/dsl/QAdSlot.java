package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.AdSlot;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdSlot is a Querydsl query type for AdSlot
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAdSlot extends EntityPathBase<AdSlot> {

    private static final long serialVersionUID = -2032718789L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdSlot adSlot = new QAdSlot("adSlot");

    public final ListPath<com.jspxcms.ext.domain.Ad, QAd> ads = this.<com.jspxcms.ext.domain.Ad, QAd>createList("ads", com.jspxcms.ext.domain.Ad.class, QAd.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final StringPath template = createString("template");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QAdSlot(String variable) {
        this(AdSlot.class, forVariable(variable), INITS);
    }

    public QAdSlot(Path<? extends AdSlot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdSlot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdSlot(PathMetadata metadata, PathInits inits) {
        this(AdSlot.class, metadata, inits);
    }

    public QAdSlot(Class<? extends AdSlot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
    }

}

