package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.MailInbox;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMailInbox is a Querydsl query type for MailInbox
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMailInbox extends EntityPathBase<MailInbox> {

    private static final long serialVersionUID = -585398735L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMailInbox mailInbox = new QMailInbox("mailInbox");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QMailText mailText;

    public final QMailOutbox outbox;

    public final QUser receiver;

    public final DateTimePath<java.util.Date> receiveTime = createDateTime("receiveTime", java.util.Date.class);

    public final QUser sender;

    public final BooleanPath unread = createBoolean("unread");

    public QMailInbox(String variable) {
        this(MailInbox.class, forVariable(variable), INITS);
    }

    public QMailInbox(Path<? extends MailInbox> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMailInbox(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMailInbox(PathMetadata metadata, PathInits inits) {
        this(MailInbox.class, metadata, inits);
    }

    public QMailInbox(Class<? extends MailInbox> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mailText = inits.isInitialized("mailText") ? new QMailText(forProperty("mailText")) : null;
        this.outbox = inits.isInitialized("outbox") ? new QMailOutbox(forProperty("outbox"), inits.get("outbox")) : null;
        this.receiver = inits.isInitialized("receiver") ? new QUser(forProperty("receiver"), inits.get("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new QUser(forProperty("sender"), inits.get("sender")) : null;
    }

}

