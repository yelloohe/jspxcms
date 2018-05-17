package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.Friendlink;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendlink is a Querydsl query type for Friendlink
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFriendlink extends EntityPathBase<Friendlink> {

    private static final long serialVersionUID = 209222162L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendlink friendlink = new QFriendlink("friendlink");

    public final StringPath description = createString("description");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath logo = createString("logo");

    public final StringPath name = createString("name");

    public final BooleanPath recommend = createBoolean("recommend");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final QFriendlinkType type;

    public final StringPath url = createString("url");

    public final BooleanPath withLogo = createBoolean("withLogo");

    public QFriendlink(String variable) {
        this(Friendlink.class, forVariable(variable), INITS);
    }

    public QFriendlink(Path<? extends Friendlink> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendlink(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendlink(PathMetadata metadata, PathInits inits) {
        this(Friendlink.class, metadata, inits);
    }

    public QFriendlink(Class<? extends Friendlink> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
        this.type = inits.isInitialized("type") ? new QFriendlinkType(forProperty("type"), inits.get("type")) : null;
    }

}

