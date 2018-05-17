package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.InfoFavorite;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoFavorite is a Querydsl query type for InfoFavorite
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoFavorite extends EntityPathBase<InfoFavorite> {

    private static final long serialVersionUID = -1189507996L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoFavorite infoFavorite = new QInfoFavorite("infoFavorite");

    public final QFavorite _super;

    //inherited
    public final DateTimePath<java.util.Date> created;

    //inherited
    public final NumberPath<Integer> id;

    public final com.jspxcms.core.domain.dsl.QInfo info;

    // inherited
    public final com.jspxcms.core.domain.dsl.QUser user;

    public QInfoFavorite(String variable) {
        this(InfoFavorite.class, forVariable(variable), INITS);
    }

    public QInfoFavorite(Path<? extends InfoFavorite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoFavorite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoFavorite(PathMetadata metadata, PathInits inits) {
        this(InfoFavorite.class, metadata, inits);
    }

    public QInfoFavorite(Class<? extends InfoFavorite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QFavorite(type, metadata, inits);
        this.created = _super.created;
        this.id = _super.id;
        this.info = inits.isInitialized("info") ? new com.jspxcms.core.domain.dsl.QInfo(forProperty("info"), inits.get("info")) : null;
        this.user = _super.user;
    }

}

