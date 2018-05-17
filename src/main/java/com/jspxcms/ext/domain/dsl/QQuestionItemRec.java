package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.QuestionItemRec;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionItemRec is a Querydsl query type for QuestionItemRec
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQuestionItemRec extends EntityPathBase<QuestionItemRec> {

    private static final long serialVersionUID = -150826115L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionItemRec questionItemRec = new QQuestionItemRec("questionItemRec");

    public final StringPath answer = createString("answer");

    public final QQuestionItem item;

    public final QQuestionRecord record;

    public QQuestionItemRec(String variable) {
        this(QuestionItemRec.class, forVariable(variable), INITS);
    }

    public QQuestionItemRec(Path<? extends QuestionItemRec> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionItemRec(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionItemRec(PathMetadata metadata, PathInits inits) {
        this(QuestionItemRec.class, metadata, inits);
    }

    public QQuestionItemRec(Class<? extends QuestionItemRec> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QQuestionItem(forProperty("item"), inits.get("item")) : null;
        this.record = inits.isInitialized("record") ? new QQuestionRecord(forProperty("record"), inits.get("record")) : null;
    }

}

