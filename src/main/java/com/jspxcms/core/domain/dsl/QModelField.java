package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.ModelField;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QModelField is a Querydsl query type for ModelField
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QModelField extends EntityPathBase<ModelField> {

    private static final long serialVersionUID = -1578447761L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QModelField modelField = new QModelField("modelField");

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final BooleanPath dblColumn = createBoolean("dblColumn");

    public final StringPath defValue = createString("defValue");

    public final BooleanPath disabled = createBoolean("disabled");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> innerType = createNumber("innerType", Integer.class);

    public final StringPath label = createString("label");

    public final QModel model;

    public final StringPath name = createString("name");

    public final StringPath prompt = createString("prompt");

    public final BooleanPath required = createBoolean("required");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public QModelField(String variable) {
        this(ModelField.class, forVariable(variable), INITS);
    }

    public QModelField(Path<? extends ModelField> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QModelField(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QModelField(PathMetadata metadata, PathInits inits) {
        this(ModelField.class, metadata, inits);
    }

    public QModelField(Class<? extends ModelField> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.model = inits.isInitialized("model") ? new QModel(forProperty("model"), inits.get("model")) : null;
    }

}

