package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.NodeDetail;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNodeDetail is a Querydsl query type for NodeDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNodeDetail extends EntityPathBase<NodeDetail> {

    private static final long serialVersionUID = 1403792913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNodeDetail nodeDetail = new QNodeDetail("nodeDetail");

    public final BooleanPath defPage = createBoolean("defPage");

    public final BooleanPath generateInfo = createBoolean("generateInfo");

    public final BooleanPath generateNode = createBoolean("generateNode");

    public final StringPath html = createString("html");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath infoExtension = createString("infoExtension");

    public final StringPath infoPath = createString("infoPath");

    public final StringPath infoTemplate = createString("infoTemplate");

    public final StringPath largeImage = createString("largeImage");

    public final StringPath link = createString("link");

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath metaKeywords = createString("metaKeywords");

    public final StringPath mobileHtml = createString("mobileHtml");

    public final BooleanPath newWindow = createBoolean("newWindow");

    public final QNode node;

    public final StringPath nodeExtension = createString("nodeExtension");

    public final StringPath nodePath = createString("nodePath");

    public final StringPath nodeTemplate = createString("nodeTemplate");

    public final StringPath smallImage = createString("smallImage");

    public final NumberPath<Integer> staticMethod = createNumber("staticMethod", Integer.class);

    public final NumberPath<Integer> staticPage = createNumber("staticPage", Integer.class);

    public QNodeDetail(String variable) {
        this(NodeDetail.class, forVariable(variable), INITS);
    }

    public QNodeDetail(Path<? extends NodeDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNodeDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNodeDetail(PathMetadata metadata, PathInits inits) {
        this(NodeDetail.class, metadata, inits);
    }

    public QNodeDetail(Class<? extends NodeDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.node = inits.isInitialized("node") ? new QNode(forProperty("node"), inits.get("node")) : null;
    }

}

