package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoComment;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfoComment is a Querydsl query type for InfoComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfoComment extends EntityPathBase<InfoComment> {

    private static final long serialVersionUID = 1480212595L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfoComment infoComment = new QInfoComment("infoComment");

    public final QComment _super;

    //inherited
    public final StringPath area;

    //inherited
    public final DateTimePath<java.util.Date> auditDate;

    // inherited
    public final QUser auditor;

    //inherited
    public final StringPath country;

    //inherited
    public final DateTimePath<java.util.Date> creationDate;

    // inherited
    public final QUser creator;

    //inherited
    public final NumberPath<Integer> fid;

    //inherited
    public final StringPath ftype;

    //inherited
    public final NumberPath<Integer> id;

    public final QInfo info;

    //inherited
    public final StringPath ip;

    // inherited
    public final QComment parent;

    //inherited
    public final NumberPath<Integer> score;

    // inherited
    public final QSite site;

    //inherited
    public final NumberPath<Integer> status;

    //inherited
    public final StringPath text;

    public QInfoComment(String variable) {
        this(InfoComment.class, forVariable(variable), INITS);
    }

    public QInfoComment(Path<? extends InfoComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfoComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfoComment(PathMetadata metadata, PathInits inits) {
        this(InfoComment.class, metadata, inits);
    }

    public QInfoComment(Class<? extends InfoComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QComment(type, metadata, inits);
        this.area = _super.area;
        this.auditDate = _super.auditDate;
        this.auditor = _super.auditor;
        this.country = _super.country;
        this.creationDate = _super.creationDate;
        this.creator = _super.creator;
        this.fid = _super.fid;
        this.ftype = _super.ftype;
        this.id = _super.id;
        this.info = inits.isInitialized("info") ? new QInfo(forProperty("info"), inits.get("info")) : null;
        this.ip = _super.ip;
        this.parent = _super.parent;
        this.score = _super.score;
        this.site = _super.site;
        this.status = _super.status;
        this.text = _super.text;
    }

}

