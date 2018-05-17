package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Model;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QModel is a Querydsl query type for Model
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QModel extends EntityPathBase<Model> {

    private static final long serialVersionUID = 833174347L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QModel model = new QModel("model");

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final ListPath<com.jspxcms.core.domain.ModelField, QModelField> fields = this.<com.jspxcms.core.domain.ModelField, QModelField>createList("fields", com.jspxcms.core.domain.ModelField.class, QModelField.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public final StringPath type = createString("type");

    public QModel(String variable) {
        this(Model.class, forVariable(variable), INITS);
    }

    public QModel(Path<? extends Model> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QModel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QModel(PathMetadata metadata, PathInits inits) {
        this(Model.class, metadata, inits);
    }

    public QModel(Class<? extends Model> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

