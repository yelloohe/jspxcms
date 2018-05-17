package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.FriendlinkType;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendlinkType is a Querydsl query type for FriendlinkType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFriendlinkType extends EntityPathBase<FriendlinkType> {

    private static final long serialVersionUID = -925817748L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendlinkType friendlinkType = new QFriendlinkType("friendlinkType");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public QFriendlinkType(String variable) {
        this(FriendlinkType.class, forVariable(variable), INITS);
    }

    public QFriendlinkType(Path<? extends FriendlinkType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendlinkType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendlinkType(PathMetadata metadata, PathInits inits) {
        this(FriendlinkType.class, metadata, inits);
    }

    public QFriendlinkType(Class<? extends FriendlinkType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
    }

}

