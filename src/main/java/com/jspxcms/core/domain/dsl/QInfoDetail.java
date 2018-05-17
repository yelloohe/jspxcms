package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoDetail;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoDetail is a Querydsl query type for InfoDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoDetail extends EntityPathBase<InfoDetail> {

    private static final long serialVersionUID = -625396803L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoDetail infoDetail = new QInfoDetail("infoDetail");

    public final BooleanPath allowComment = createBoolean("allowComment");

    public final StringPath author = createString("author");

    public final StringPath color = createString("color");

    public final StringPath doc = createString("doc");

    public final NumberPath<Long> docLength = createNumber("docLength", Long.class);

    public final StringPath docName = createString("docName");

    public final StringPath docPdf = createString("docPdf");

    public final StringPath docSwf = createString("docSwf");

    public final BooleanPath em = createBoolean("em");

    public final StringPath file = createString("file");

    public final NumberPath<Long> fileLength = createNumber("fileLength", Long.class);

    public final StringPath fileName = createString("fileName");

    public final StringPath fullTitle = createString("fullTitle");

    public final StringPath html = createString("html");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QInfo info;

    public final StringPath infoPath = createString("infoPath");

    public final StringPath infoTemplate = createString("infoTemplate");

    public final StringPath largeImage = createString("largeImage");

    public final StringPath link = createString("link");

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath mobileHtml = createString("mobileHtml");

    public final BooleanPath newWindow = createBoolean("newWindow");

    public final StringPath smallImage = createString("smallImage");

    public final StringPath source = createString("source");

    public final StringPath sourceUrl = createString("sourceUrl");

    public final StringPath stepName = createString("stepName");

    public final BooleanPath strong = createBoolean("strong");

    public final StringPath subtitle = createString("subtitle");

    public final StringPath title = createString("title");

    public final StringPath video = createString("video");

    public final NumberPath<Long> videoLength = createNumber("videoLength", Long.class);

    public final StringPath videoName = createString("videoName");

    public final StringPath videoTime = createString("videoTime");

    public final BooleanPath weixinMass = createBoolean("weixinMass");

    public QInfoDetail(String variable) {
        this(InfoDetail.class, forVariable(variable), INITS);
    }

    public QInfoDetail(Path<? extends InfoDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoDetail(PathMetadata metadata, PathInits inits) {
        this(InfoDetail.class, metadata, inits);
    }

    public QInfoDetail(Class<? extends InfoDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
    }

}

