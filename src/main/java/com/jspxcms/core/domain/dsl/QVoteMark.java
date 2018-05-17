package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.VoteMark;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteMark is a Querydsl query type for VoteMark
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVoteMark extends EntityPathBase<VoteMark> {

    private static final long serialVersionUID = -556368523L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteMark voteMark = new QVoteMark("voteMark");

    public final StringPath cookie = createString("cookie");

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final NumberPath<Integer> fid = createNumber("fid", Integer.class);

    public final StringPath ftype = createString("ftype");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final QUser user;

    public QVoteMark(String variable) {
        this(VoteMark.class, forVariable(variable), INITS);
    }

    public QVoteMark(Path<? extends VoteMark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteMark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteMark(PathMetadata metadata, PathInits inits) {
        this(VoteMark.class, metadata, inits);
    }

    public QVoteMark(Class<? extends VoteMark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

