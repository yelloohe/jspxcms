package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.SensitiveWord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSensitiveWord is a Querydsl query type for SensitiveWord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSensitiveWord extends EntityPathBase<SensitiveWord> {

    private static final long serialVersionUID = -65188638L;

    public static final QSensitiveWord sensitiveWord = new QSensitiveWord("sensitiveWord");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath replacement = createString("replacement");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QSensitiveWord(String variable) {
        super(SensitiveWord.class, forVariable(variable));
    }

    public QSensitiveWord(Path<? extends SensitiveWord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSensitiveWord(PathMetadata metadata) {
        super(SensitiveWord.class, metadata);
    }

}

