package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Site;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSite is a Querydsl query type for Site
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSite extends EntityPathBase<Site> {

    private static final long serialVersionUID = 719786725L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSite site = new QSite("site");

    public final ListPath<Site, QSite> children = this.<Site, QSite>createList("children", Site.class, QSite.class, PathInits.DIRECT2);

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final StringPath domain = createString("domain");

    public final StringPath fullName = createString("fullName");

    public final QGlobal global;

    public final QPublishPoint htmlPublishPoint;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath identifyDomain = createBoolean("identifyDomain");

    public final StringPath mobileDomain = createString("mobileDomain");

    public final QPublishPoint mobilePublishPoint;

    public final StringPath mobileTheme = createString("mobileTheme");

    public final StringPath name = createString("name");

    public final StringPath noPicture = createString("noPicture");

    public final StringPath number = createString("number");

    public final QOrg org;

    public final QSite parent;

    public final ListPath<com.jspxcms.core.domain.Role, QRole> roles = this.<com.jspxcms.core.domain.Role, QRole>createList("roles", com.jspxcms.core.domain.Role.class, QRole.class, PathInits.DIRECT2);

    public final BooleanPath staticHome = createBoolean("staticHome");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath templateTheme = createString("templateTheme");

    public final NumberPath<Integer> treeLevel = createNumber("treeLevel", Integer.class);

    public final StringPath treeMax = createString("treeMax");

    public final StringPath treeNumber = createString("treeNumber");

    public QSite(String variable) {
        this(Site.class, forVariable(variable), INITS);
    }

    public QSite(Path<? extends Site> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSite(PathMetadata metadata, PathInits inits) {
        this(Site.class, metadata, inits);
    }

    public QSite(Class<? extends Site> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.global = inits.isInitialized("global") ? new QGlobal(forProperty("global"), inits.get("global")) : null;
        this.htmlPublishPoint = inits.isInitialized("htmlPublishPoint") ? new QPublishPoint(forProperty("htmlPublishPoint"), inits.get("htmlPublishPoint")) : null;
        this.mobilePublishPoint = inits.isInitialized("mobilePublishPoint") ? new QPublishPoint(forProperty("mobilePublishPoint"), inits.get("mobilePublishPoint")) : null;
        this.org = inits.isInitialized("org") ? new QOrg(forProperty("org"), inits.get("org")) : null;
        this.parent = inits.isInitialized("parent") ? new QSite(forProperty("parent"), inits.get("parent")) : null;
    }

}

