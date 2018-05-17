package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.CollectField;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCollectField is a Querydsl query type for CollectField
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCollectField extends EntityPathBase<CollectField> {

    private static final long serialVersionUID = 354484714L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCollectField collectField = new QCollectField("collectField");

    public final StringPath code = createString("code");

    public final QCollect collect;

    public final StringPath dataAreaPattern = createString("dataAreaPattern");

    public final BooleanPath dataAreaReg = createBoolean("dataAreaReg");

    public final StringPath dataPattern = createString("dataPattern");

    public final BooleanPath dataReg = createBoolean("dataReg");

    public final StringPath dateFormat = createString("dateFormat");

    public final StringPath downloadType = createString("downloadType");

    public final StringPath filter = createString("filter");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath imageParam = createString("imageParam");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final StringPath sourceText = createString("sourceText");

    public final NumberPath<Integer> sourceType = createNumber("sourceType", Integer.class);

    public final StringPath sourceUrl = createString("sourceUrl");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public QCollectField(String variable) {
        this(CollectField.class, forVariable(variable), INITS);
    }

    public QCollectField(Path<? extends CollectField> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCollectField(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCollectField(PathMetadata metadata, PathInits inits) {
        this(CollectField.class, metadata, inits);
    }

    public QCollectField(Class<? extends CollectField> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.collect = inits.isInitialized("collect") ? new QCollect(forProperty("collect"), inits.get("collect")) : null;
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
    }

}

