package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Org;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrg is a Querydsl query type for Org
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrg extends EntityPathBase<Org> {

    private static final long serialVersionUID = 715952006L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrg org = new QOrg("org");

    public final StringPath address = createString("address");

    public final ListPath<Org, QOrg> children = this.<Org, QOrg>createList("children", Org.class, QOrg.class, PathInits.DIRECT2);

    public final StringPath contacts = createString("contacts");

    public final StringPath description = createString("description");

    public final StringPath fax = createString("fax");

    public final StringPath fullName = createString("fullName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final QOrg parent;

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> treeLevel = createNumber("treeLevel", Integer.class);

    public final StringPath treeMax = createString("treeMax");

    public final StringPath treeNumber = createString("treeNumber");

    public final SetPath<com.jspxcms.core.domain.UserOrg, QUserOrg> userOrgs = this.<com.jspxcms.core.domain.UserOrg, QUserOrg>createSet("userOrgs", com.jspxcms.core.domain.UserOrg.class, QUserOrg.class, PathInits.DIRECT2);

    public final SetPath<com.jspxcms.core.domain.User, QUser> users = this.<com.jspxcms.core.domain.User, QUser>createSet("users", com.jspxcms.core.domain.User.class, QUser.class, PathInits.DIRECT2);

    public QOrg(String variable) {
        this(Org.class, forVariable(variable), INITS);
    }

    public QOrg(Path<? extends Org> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrg(PathMetadata metadata, PathInits inits) {
        this(Org.class, metadata, inits);
    }

    public QOrg(Class<? extends Org> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QOrg(forProperty("parent"), inits.get("parent")) : null;
    }

}

