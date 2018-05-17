package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.QuestionItem;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionItem is a Querydsl query type for QuestionItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQuestionItem extends EntityPathBase<QuestionItem> {

    private static final long serialVersionUID = -1421520877L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionItem questionItem = new QQuestionItem("questionItem");

    public final BooleanPath essay = createBoolean("essay");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final SetPath<com.jspxcms.ext.domain.QuestionItemRec, QQuestionItemRec> itemRecs = this.<com.jspxcms.ext.domain.QuestionItemRec, QQuestionItemRec>createSet("itemRecs", com.jspxcms.ext.domain.QuestionItemRec.class, QQuestionItemRec.class, PathInits.DIRECT2);

    public final NumberPath<Integer> maxSelected = createNumber("maxSelected", Integer.class);

    public final ListPath<com.jspxcms.ext.domain.QuestionOption, QQuestionOption> options = this.<com.jspxcms.ext.domain.QuestionOption, QQuestionOption>createList("options", com.jspxcms.ext.domain.QuestionOption.class, QQuestionOption.class, PathInits.DIRECT2);

    public final QQuestion question;

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final StringPath title = createString("title");

    public QQuestionItem(String variable) {
        this(QuestionItem.class, forVariable(variable), INITS);
    }

    public QQuestionItem(Path<? extends QuestionItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionItem(PathMetadata metadata, PathInits inits) {
        this(QuestionItem.class, metadata, inits);
    }

    public QQuestionItem(Class<? extends QuestionItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

