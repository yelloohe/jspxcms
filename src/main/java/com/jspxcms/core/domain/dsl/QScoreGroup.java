package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.ScoreGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScoreGroup is a Querydsl query type for ScoreGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QScoreGroup extends EntityPathBase<ScoreGroup> {

    private static final long serialVersionUID = -2098423765L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScoreGroup scoreGroup = new QScoreGroup("scoreGroup");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.jspxcms.core.domain.ScoreItem, QScoreItem> items = this.<com.jspxcms.core.domain.ScoreItem, QScoreItem>createList("items", com.jspxcms.core.domain.ScoreItem.class, QScoreItem.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public QScoreGroup(String variable) {
        this(ScoreGroup.class, forVariable(variable), INITS);
    }

    public QScoreGroup(Path<? extends ScoreGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScoreGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScoreGroup(PathMetadata metadata, PathInits inits) {
        this(ScoreGroup.class, metadata, inits);
    }

    public QScoreGroup(Class<? extends ScoreGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

