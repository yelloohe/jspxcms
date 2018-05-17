package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.User;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 719855465L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.util.Date> birthDate = createDateTime("birthDate", java.util.Date.class);

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final SetPath<com.jspxcms.core.domain.UserDetail, QUserDetail> details = this.<com.jspxcms.core.domain.UserDetail, QUserDetail>createSet("details", com.jspxcms.core.domain.UserDetail.class, QUserDetail.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final QGlobal global;

    public final QMemberGroup group;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mobile = createString("mobile");

    public final QOrg org;

    public final StringPath password = createString("password");

    public final StringPath qqOpenid = createString("qqOpenid");

    public final NumberPath<Integer> rank = createNumber("rank", Integer.class);

    public final StringPath realName = createString("realName");

    public final StringPath salt = createString("salt");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final ListPath<com.jspxcms.core.domain.UserMemberGroup, QUserMemberGroup> userGroups = this.<com.jspxcms.core.domain.UserMemberGroup, QUserMemberGroup>createList("userGroups", com.jspxcms.core.domain.UserMemberGroup.class, QUserMemberGroup.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public final ListPath<com.jspxcms.core.domain.UserOrg, QUserOrg> userOrgs = this.<com.jspxcms.core.domain.UserOrg, QUserOrg>createList("userOrgs", com.jspxcms.core.domain.UserOrg.class, QUserOrg.class, PathInits.DIRECT2);

    public final ListPath<com.jspxcms.core.domain.UserRole, QUserRole> userRoles = this.<com.jspxcms.core.domain.UserRole, QUserRole>createList("userRoles", com.jspxcms.core.domain.UserRole.class, QUserRole.class, PathInits.DIRECT2);

    public final StringPath validationKey = createString("validationKey");

    public final StringPath validationType = createString("validationType");

    public final StringPath weiboUid = createString("weiboUid");

    public final StringPath weixinOpenid = createString("weixinOpenid");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.global = inits.isInitialized("global") ? new QGlobal(forProperty("global"), inits.get("global")) : null;
        this.group = inits.isInitialized("group") ? new QMemberGroup(forProperty("group")) : null;
        this.org = inits.isInitialized("org") ? new QOrg(forProperty("org"), inits.get("org")) : null;
    }

}

