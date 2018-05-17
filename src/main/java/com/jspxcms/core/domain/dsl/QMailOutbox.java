package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.MailOutbox;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMailOutbox is a Querydsl query type for MailOutbox
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMailOutbox extends EntityPathBase<MailOutbox> {

    private static final long serialVersionUID = -788728462L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMailOutbox mailOutbox = new QMailOutbox("mailOutbox");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final SetPath<com.jspxcms.core.domain.MailInbox, QMailInbox> inboxs = this.<com.jspxcms.core.domain.MailInbox, QMailInbox>createSet("inboxs", com.jspxcms.core.domain.MailInbox.class, QMailInbox.class, PathInits.DIRECT2);

    public final QMailText mailText;

    public final NumberPath<Integer> readNumber = createNumber("readNumber", Integer.class);

    public final NumberPath<Integer> receiverNumber = createNumber("receiverNumber", Integer.class);

    public final QUser sender;

    public final DateTimePath<java.util.Date> sendTime = createDateTime("sendTime", java.util.Date.class);

    public QMailOutbox(String variable) {
        this(MailOutbox.class, forVariable(variable), INITS);
    }

    public QMailOutbox(Path<? extends MailOutbox> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMailOutbox(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMailOutbox(PathMetadata metadata, PathInits inits) {
        this(MailOutbox.class, metadata, inits);
    }

    public QMailOutbox(Class<? extends MailOutbox> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mailText = inits.isInitialized("mailText") ? new QMailText(forProperty("mailText")) : null;
        this.sender = inits.isInitialized("sender") ? new QUser(forProperty("sender"), inits.get("sender")) : null;
    }

}

