package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.MessageText;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageText is a Querydsl query type for MessageText
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMessageText extends EntityPathBase<MessageText> {

    private static final long serialVersionUID = 1628339318L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageText messageText = new QMessageText("messageText");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QMessage message;

    public final StringPath subject = createString("subject");

    public final StringPath text = createString("text");

    public QMessageText(String variable) {
        this(MessageText.class, forVariable(variable), INITS);
    }

    public QMessageText(Path<? extends MessageText> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageText(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageText(PathMetadata metadata, PathInits inits) {
        this(MessageText.class, metadata, inits);
    }

    public QMessageText(Class<? extends MessageText> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.message = inits.isInitialized("message") ? new QMessage(forProperty("message"), inits.get("message")) : null;
    }

}

