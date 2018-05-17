package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.ScoreBoard;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScoreBoard is a Querydsl query type for ScoreBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QScoreBoard extends EntityPathBase<ScoreBoard> {

    private static final long serialVersionUID = -2103144302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScoreBoard scoreBoard = new QScoreBoard("scoreBoard");

    public final NumberPath<Integer> fid = createNumber("fid", Integer.class);

    public final StringPath ftype = createString("ftype");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QScoreItem item;

    public final NumberPath<Integer> votes = createNumber("votes", Integer.class);

    public QScoreBoard(String variable) {
        this(ScoreBoard.class, forVariable(variable), INITS);
    }

    public QScoreBoard(Path<? extends ScoreBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScoreBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScoreBoard(PathMetadata metadata, PathInits inits) {
        this(ScoreBoard.class, metadata, inits);
    }

    public QScoreBoard(Class<? extends ScoreBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QScoreItem(forProperty("item"), inits.get("item")) : null;
    }

}

