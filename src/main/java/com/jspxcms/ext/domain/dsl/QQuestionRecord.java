package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.QuestionRecord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionRecord is a Querydsl query type for QuestionRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQuestionRecord extends EntityPathBase<QuestionRecord> {

    private static final long serialVersionUID = -38207151L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionRecord questionRecord = new QQuestionRecord("questionRecord");

    public final StringPath cookie = createString("cookie");

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final SetPath<com.jspxcms.ext.domain.QuestionItemRec, QQuestionItemRec> itemRecs = this.<com.jspxcms.ext.domain.QuestionItemRec, QQuestionItemRec>createSet("itemRecs", com.jspxcms.ext.domain.QuestionItemRec.class, QQuestionItemRec.class, PathInits.DIRECT2);

    public final SetPath<com.jspxcms.ext.domain.QuestionOptRec, QQuestionOptRec> optRecs = this.<com.jspxcms.ext.domain.QuestionOptRec, QQuestionOptRec>createSet("optRecs", com.jspxcms.ext.domain.QuestionOptRec.class, QQuestionOptRec.class, PathInits.DIRECT2);

    public final QQuestion question;

    public final com.jspxcms.core.domain.dsl.QUser user;

    public QQuestionRecord(String variable) {
        this(QuestionRecord.class, forVariable(variable), INITS);
    }

    public QQuestionRecord(Path<? extends QuestionRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionRecord(PathMetadata metadata, PathInits inits) {
        this(QuestionRecord.class, metadata, inits);
    }

    public QQuestionRecord(Class<? extends QuestionRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question"), inits.get("question")) : null;
        this.user = inits.isInitialized("user") ? new com.jspxcms.core.domain.dsl.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

