package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Attribute;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttribute is a Querydsl query type for Attribute
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAttribute extends EntityPathBase<Attribute> {

    private static final long serialVersionUID = -243529282L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttribute attribute = new QAttribute("attribute");

    public final BooleanPath exact = createBoolean("exact");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> imageHeight = createNumber("imageHeight", Integer.class);

    public final NumberPath<Integer> imageWidth = createNumber("imageWidth", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final BooleanPath scale = createBoolean("scale");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public final BooleanPath watermark = createBoolean("watermark");

    public final BooleanPath withImage = createBoolean("withImage");

    public QAttribute(String variable) {
        this(Attribute.class, forVariable(variable), INITS);
    }

    public QAttribute(Path<? extends Attribute> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttribute(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttribute(PathMetadata metadata, PathInits inits) {
        this(Attribute.class, metadata, inits);
    }

    public QAttribute(Class<? extends Attribute> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

