package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.Question;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = 1312149728L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final DateTimePath<java.util.Date> beginDate = createDateTime("beginDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> interval = createNumber("interval", Integer.class);

    public final ListPath<com.jspxcms.ext.domain.QuestionItem, QQuestionItem> items = this.<com.jspxcms.ext.domain.QuestionItem, QQuestionItem>createList("items", com.jspxcms.ext.domain.QuestionItem.class, QQuestionItem.class, PathInits.DIRECT2);

    public final NumberPath<Integer> mode = createNumber("mode", Integer.class);

    public final SetPath<com.jspxcms.ext.domain.QuestionRecord, QQuestionRecord> records = this.<com.jspxcms.ext.domain.QuestionRecord, QQuestionRecord>createSet("records", com.jspxcms.ext.domain.QuestionRecord.class, QQuestionRecord.class, PathInits.DIRECT2);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> total = createNumber("total", Integer.class);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
    }

}

