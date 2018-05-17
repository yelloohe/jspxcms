package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.QuestionOptRec;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionOptRec is a Querydsl query type for QuestionOptRec
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQuestionOptRec extends EntityPathBase<QuestionOptRec> {

    private static final long serialVersionUID = -113457699L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionOptRec questionOptRec = new QQuestionOptRec("questionOptRec");

    public final QQuestionOption option;

    public final QQuestionRecord record;

    public QQuestionOptRec(String variable) {
        this(QuestionOptRec.class, forVariable(variable), INITS);
    }

    public QQuestionOptRec(Path<? extends QuestionOptRec> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionOptRec(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionOptRec(PathMetadata metadata, PathInits inits) {
        this(QuestionOptRec.class, metadata, inits);
    }

    public QQuestionOptRec(Class<? extends QuestionOptRec> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.option = inits.isInitialized("option") ? new QQuestionOption(forProperty("option"), inits.get("option")) : null;
        this.record = inits.isInitialized("record") ? new QQuestionRecord(forProperty("record"), inits.get("record")) : null;
    }

}

