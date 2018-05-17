package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.ScoreItem;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScoreItem is a Querydsl query type for ScoreItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QScoreItem extends EntityPathBase<ScoreItem> {

    private static final long serialVersionUID = 1872032743L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScoreItem scoreItem = new QScoreItem("scoreItem");

    public final QScoreGroup group;

    public final StringPath icon = createString("icon");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public QScoreItem(String variable) {
        this(ScoreItem.class, forVariable(variable), INITS);
    }

    public QScoreItem(Path<? extends ScoreItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScoreItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScoreItem(PathMetadata metadata, PathInits inits) {
        this(ScoreItem.class, metadata, inits);
    }

    public QScoreItem(Class<? extends ScoreItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QScoreGroup(forProperty("group"), inits.get("group")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

