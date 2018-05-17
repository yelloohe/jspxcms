package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.Ad;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAd is a Querydsl query type for Ad
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAd extends EntityPathBase<Ad> {

    private static final long serialVersionUID = -211983203L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAd ad = new QAd("ad");

    public final DateTimePath<java.util.Date> beginDate = createDateTime("beginDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final StringPath flash = createString("flash");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final StringPath script = createString("script");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final QAdSlot slot;

    public final StringPath text = createString("text");

    public final StringPath url = createString("url");

    public QAd(String variable) {
        this(Ad.class, forVariable(variable), INITS);
    }

    public QAd(Path<? extends Ad> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAd(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAd(PathMetadata metadata, PathInits inits) {
        this(Ad.class, metadata, inits);
    }

    public QAd(Class<? extends Ad> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
        this.slot = inits.isInitialized("slot") ? new QAdSlot(forProperty("slot"), inits.get("slot")) : null;
    }

}

