package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Info;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfo is a Querydsl query type for Info
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInfo extends EntityPathBase<Info> {

    private static final long serialVersionUID = 719493196L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfo info = new QInfo("info");

    public final SetPath<com.jspxcms.core.domain.InfoBuffer, QInfoBuffer> buffers = this.<com.jspxcms.core.domain.InfoBuffer, QInfoBuffer>createSet("buffers", com.jspxcms.core.domain.InfoBuffer.class, QInfoBuffer.class, PathInits.DIRECT2);

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final NumberPath<Integer> comments = createNumber("comments", Integer.class);

    public final QUser creator;

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final QInfoDetail detail;

    public final NumberPath<Integer> diggs = createNumber("diggs", Integer.class);

    public final NumberPath<Integer> downloads = createNumber("downloads", Integer.class);

    public final NumberPath<Integer> favorites = createNumber("favorites", Integer.class);

    public final ListPath<com.jspxcms.core.domain.InfoFile, QInfoFile> files = this.<com.jspxcms.core.domain.InfoFile, QInfoFile>createList("files", com.jspxcms.core.domain.InfoFile.class, QInfoFile.class, PathInits.DIRECT2);

    public final QSite fromSite;

    public final StringPath htmlStatus = createString("htmlStatus");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.jspxcms.core.domain.InfoImage, QInfoImage> images = this.<com.jspxcms.core.domain.InfoImage, QInfoImage>createList("images", com.jspxcms.core.domain.InfoImage.class, QInfoImage.class, PathInits.DIRECT2);

    public final ListPath<com.jspxcms.core.domain.InfoAttribute, QInfoAttribute> infoAttrs = this.<com.jspxcms.core.domain.InfoAttribute, QInfoAttribute>createList("infoAttrs", com.jspxcms.core.domain.InfoAttribute.class, QInfoAttribute.class, PathInits.DIRECT2);

    public final ListPath<com.jspxcms.ext.domain.InfoFavorite, com.jspxcms.ext.domain.dsl.QInfoFavorite> infoFavorites = this.<com.jspxcms.ext.domain.InfoFavorite, com.jspxcms.ext.domain.dsl.QInfoFavorite>createList("infoFavorites", com.jspxcms.ext.domain.InfoFavorite.class, com.jspxcms.ext.domain.dsl.QInfoFavorite.class, PathInits.DIRECT2);

    public final SetPath<com.jspxcms.core.domain.InfoMemberGroup, QInfoMemberGroup> infoGroups = this.<com.jspxcms.core.domain.InfoMemberGroup, QInfoMemberGroup>createSet("infoGroups", com.jspxcms.core.domain.InfoMemberGroup.class, QInfoMemberGroup.class, PathInits.DIRECT2);

    public final ListPath<com.jspxcms.core.domain.InfoNode, QInfoNode> infoNodes = this.<com.jspxcms.core.domain.InfoNode, QInfoNode>createList("infoNodes", com.jspxcms.core.domain.InfoNode.class, QInfoNode.class, PathInits.DIRECT2);

    public final SetPath<com.jspxcms.core.domain.InfoOrg, QInfoOrg> infoOrgs = this.<com.jspxcms.core.domain.InfoOrg, QInfoOrg>createSet("infoOrgs", com.jspxcms.core.domain.InfoOrg.class, QInfoOrg.class, PathInits.DIRECT2);

    public final ListPath<com.jspxcms.core.domain.InfoSpecial, QInfoSpecial> infoSpecials = this.<com.jspxcms.core.domain.InfoSpecial, QInfoSpecial>createList("infoSpecials", com.jspxcms.core.domain.InfoSpecial.class, QInfoSpecial.class, PathInits.DIRECT2);

    public final ListPath<com.jspxcms.core.domain.InfoTag, QInfoTag> infoTags = this.<com.jspxcms.core.domain.InfoTag, QInfoTag>createList("infoTags", com.jspxcms.core.domain.InfoTag.class, QInfoTag.class, PathInits.DIRECT2);

    public final QNode node;

    public final DateTimePath<java.util.Date> offDate = createDateTime("offDate", java.util.Date.class);

    public final QOrg org;

    public final NumberPath<Integer> p0 = createNumber("p0", Integer.class);

    public final NumberPath<Integer> p1 = createNumber("p1", Integer.class);

    public final NumberPath<Integer> p2 = createNumber("p2", Integer.class);

    public final NumberPath<Integer> p3 = createNumber("p3", Integer.class);

    public final NumberPath<Integer> p4 = createNumber("p4", Integer.class);

    public final NumberPath<Integer> p5 = createNumber("p5", Integer.class);

    public final NumberPath<Integer> p6 = createNumber("p6", Integer.class);

    public final NumberPath<Integer> priority = createNumber("priority", Integer.class);

    public final SetPath<com.jspxcms.core.domain.InfoProcess, QInfoProcess> processes = this.<com.jspxcms.core.domain.InfoProcess, QInfoProcess>createSet("processes", com.jspxcms.core.domain.InfoProcess.class, QInfoProcess.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> publishDate = createDateTime("publishDate", java.util.Date.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final QSite site;

    public final StringPath status = createString("status");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final BooleanPath withImage = createBoolean("withImage");

    public QInfo(String variable) {
        this(Info.class, forVariable(variable), INITS);
    }

    public QInfo(Path<? extends Info> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfo(PathMetadata metadata, PathInits inits) {
        this(Info.class, metadata, inits);
    }

    public QInfo(Class<? extends Info> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new QUser(forProperty("creator"), inits.get("creator")) : null;
        this.detail = inits.isInitialized("detail") ? new QInfoDetail(forProperty("detail"), inits.get("detail")) : null;
        this.fromSite = inits.isInitialized("fromSite") ? new QSite(forProperty("fromSite"), inits.get("fromSite")) : null;
        this.node = inits.isInitialized("node") ? new QNode(forProperty("node"), inits.get("node")) : null;
        this.org = inits.isInitialized("org") ? new QOrg(forProperty("org"), inits.get("org")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

