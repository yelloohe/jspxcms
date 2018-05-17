package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.GuestbookType;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGuestbookType is a Querydsl query type for GuestbookType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGuestbookType extends EntityPathBase<GuestbookType> {

    private static final long serialVersionUID = -1734086943L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGuestbookType guestbookType = new QGuestbookType("guestbookType");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public QGuestbookType(String variable) {
        this(GuestbookType.class, forVariable(variable), INITS);
    }

    public QGuestbookType(Path<? extends GuestbookType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGuestbookType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGuestbookType(PathMetadata metadata, PathInits inits) {
        this(GuestbookType.class, metadata, inits);
    }

    public QGuestbookType(Class<? extends GuestbookType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
    }

}

