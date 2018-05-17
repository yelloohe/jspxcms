package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.SpecialCategory;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpecialCategory is a Querydsl query type for SpecialCategory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSpecialCategory extends EntityPathBase<SpecialCategory> {

    private static final long serialVersionUID = -2019584391L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpecialCategory specialCategory = new QSpecialCategory("specialCategory");

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath metaKeywords = createString("metaKeywords");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QSpecialCategory(String variable) {
        this(SpecialCategory.class, forVariable(variable), INITS);
    }

    public QSpecialCategory(Path<? extends SpecialCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpecialCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpecialCategory(PathMetadata metadata, PathInits inits) {
        this(SpecialCategory.class, metadata, inits);
    }

    public QSpecialCategory(Class<? extends SpecialCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

