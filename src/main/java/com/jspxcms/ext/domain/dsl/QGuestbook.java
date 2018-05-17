package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.Guestbook;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGuestbook is a Querydsl query type for Guestbook
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGuestbook extends EntityPathBase<Guestbook> {

    private static final long serialVersionUID = -1378564345L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGuestbook guestbook = new QGuestbook("guestbook");

    public final StringPath creationArea = createString("creationArea");

    public final StringPath creationCountry = createString("creationCountry");

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final StringPath creationIp = createString("creationIp");

    public final com.jspxcms.core.domain.dsl.QUser creator;

    public final StringPath email = createString("email");

    public final BooleanPath gender = createBoolean("gender");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mobile = createString("mobile");

    public final StringPath phone = createString("phone");

    public final StringPath qq = createString("qq");

    public final BooleanPath recommend = createBoolean("recommend");

    public final BooleanPath reply = createBoolean("reply");

    public final StringPath replyArea = createString("replyArea");

    public final StringPath replyCountry = createString("replyCountry");

    public final DateTimePath<java.util.Date> replyDate = createDateTime("replyDate", java.util.Date.class);

    public final com.jspxcms.core.domain.dsl.QUser replyer;

    public final StringPath replyIp = createString("replyIp");

    public final StringPath replyText = createString("replyText");

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath text = createString("text");

    public final StringPath title = createString("title");

    public final QGuestbookType type;

    public final StringPath username = createString("username");

    public QGuestbook(String variable) {
        this(Guestbook.class, forVariable(variable), INITS);
    }

    public QGuestbook(Path<? extends Guestbook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGuestbook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGuestbook(PathMetadata metadata, PathInits inits) {
        this(Guestbook.class, metadata, inits);
    }

    public QGuestbook(Class<? extends Guestbook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new com.jspxcms.core.domain.dsl.QUser(forProperty("creator"), inits.get("creator")) : null;
        this.replyer = inits.isInitialized("replyer") ? new com.jspxcms.core.domain.dsl.QUser(forProperty("replyer"), inits.get("replyer")) : null;
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
        this.type = inits.isInitialized("type") ? new QGuestbookType(forProperty("type"), inits.get("type")) : null;
    }

}

