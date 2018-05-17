package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Special;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpecial is a Querydsl query type for Special
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSpecial extends EntityPathBase<Special> {

    private static final long serialVersionUID = -1418788773L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpecial special = new QSpecial("special");

    public final QSpecialCategory category;

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final QUser creator;

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final ListPath<com.jspxcms.core.domain.SpecialFile, QSpecialFile> files = this.<com.jspxcms.core.domain.SpecialFile, QSpecialFile>createList("files", com.jspxcms.core.domain.SpecialFile.class, QSpecialFile.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.jspxcms.core.domain.SpecialImage, QSpecialImage> images = this.<com.jspxcms.core.domain.SpecialImage, QSpecialImage>createList("images", com.jspxcms.core.domain.SpecialImage.class, QSpecialImage.class, PathInits.DIRECT2);

    public final StringPath largeImage = createString("largeImage");

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath metaKeywords = createString("metaKeywords");

    public final QModel model;

    public final BooleanPath recommend = createBoolean("recommend");

    public final NumberPath<Integer> refers = createNumber("refers", Integer.class);

    public final QSite site;

    public final StringPath smallImage = createString("smallImage");

    public final StringPath specialTemplate = createString("specialTemplate");

    public final StringPath title = createString("title");

    public final StringPath video = createString("video");

    public final NumberPath<Long> videoLength = createNumber("videoLength", Long.class);

    public final StringPath videoName = createString("videoName");

    public final StringPath videoTime = createString("videoTime");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final BooleanPath withImage = createBoolean("withImage");

    public QSpecial(String variable) {
        this(Special.class, forVariable(variable), INITS);
    }

    public QSpecial(Path<? extends Special> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpecial(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpecial(PathMetadata metadata, PathInits inits) {
        this(Special.class, metadata, inits);
    }

    public QSpecial(Class<? extends Special> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QSpecialCategory(forProperty("category"), inits.get("category")) : null;
        this.creator = inits.isInitialized("creator") ? new QUser(forProperty("creator"), inits.get("creator")) : null;
        this.model = inits.isInitialized("model") ? new QModel(forProperty("model"), inits.get("model")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

