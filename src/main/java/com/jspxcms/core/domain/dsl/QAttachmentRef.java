package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.AttachmentRef;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttachmentRef is a Querydsl query type for AttachmentRef
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAttachmentRef extends EntityPathBase<AttachmentRef> {

    private static final long serialVersionUID = 1501245074L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttachmentRef attachmentRef = new QAttachmentRef("attachmentRef");

    public final QAttachment attachment;

    public final NumberPath<Integer> fid = createNumber("fid", Integer.class);

    public final StringPath ftype = createString("ftype");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QSite site;

    public QAttachmentRef(String variable) {
        this(AttachmentRef.class, forVariable(variable), INITS);
    }

    public QAttachmentRef(Path<? extends AttachmentRef> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttachmentRef(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttachmentRef(PathMetadata metadata, PathInits inits) {
        this(AttachmentRef.class, metadata, inits);
    }

    public QAttachmentRef(Class<? extends AttachmentRef> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attachment = inits.isInitialized("attachment") ? new QAttachment(forProperty("attachment"), inits.get("attachment")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

