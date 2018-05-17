package com.jspxcms.ext.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.ext.domain.Collect;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCollect is a Querydsl query type for Collect
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCollect extends EntityPathBase<Collect> {

    private static final long serialVersionUID = -771734288L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCollect collect = new QCollect("collect");

    public final BooleanPath allowDuplicate = createBoolean("allowDuplicate");

    public final StringPath blockAreaPattern = createString("blockAreaPattern");

    public final BooleanPath blockAreaReg = createBoolean("blockAreaReg");

    public final StringPath blockPattern = createString("blockPattern");

    public final BooleanPath blockReg = createBoolean("blockReg");

    public final StringPath charset = createString("charset");

    public final BooleanPath desc = createBoolean("desc");

    public final BooleanPath downloadImage = createBoolean("downloadImage");

    public final ListPath<com.jspxcms.ext.domain.CollectField, QCollectField> fields = this.<com.jspxcms.ext.domain.CollectField, QCollectField>createList("fields", com.jspxcms.ext.domain.CollectField.class, QCollectField.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> intervalMax = createNumber("intervalMax", Integer.class);

    public final NumberPath<Integer> intervalMin = createNumber("intervalMin", Integer.class);

    public final StringPath itemAreaPattern = createString("itemAreaPattern");

    public final BooleanPath itemAreaReg = createBoolean("itemAreaReg");

    public final StringPath itemPattern = createString("itemPattern");

    public final BooleanPath itemReg = createBoolean("itemReg");

    public final StringPath listNextPattern = createString("listNextPattern");

    public final BooleanPath listNextReg = createBoolean("listNextReg");

    public final StringPath listPattern = createString("listPattern");

    public final StringPath name = createString("name");

    public final com.jspxcms.core.domain.dsl.QNode node;

    public final NumberPath<Integer> pageBegin = createNumber("pageBegin", Integer.class);

    public final NumberPath<Integer> pageEnd = createNumber("pageEnd", Integer.class);

    public final com.jspxcms.core.domain.dsl.QSite site;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final BooleanPath submit = createBoolean("submit");

    public final com.jspxcms.core.domain.dsl.QUser user;

    public final StringPath userAgent = createString("userAgent");

    public QCollect(String variable) {
        this(Collect.class, forVariable(variable), INITS);
    }

    public QCollect(Path<? extends Collect> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCollect(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCollect(PathMetadata metadata, PathInits inits) {
        this(Collect.class, metadata, inits);
    }

    public QCollect(Class<? extends Collect> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.node = inits.isInitialized("node") ? new com.jspxcms.core.domain.dsl.QNode(forProperty("node"), inits.get("node")) : null;
        this.site = inits.isInitialized("site") ? new com.jspxcms.core.domain.dsl.QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jspxcms.core.domain.dsl.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

