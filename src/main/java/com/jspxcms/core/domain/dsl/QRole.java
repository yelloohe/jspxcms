package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.Role;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRole is a Querydsl query type for Role
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRole extends EntityPathBase<Role> {

    private static final long serialVersionUID = 719762452L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRole role = new QRole("role");

    public final BooleanPath allInfoPerm = createBoolean("allInfoPerm");

    public final BooleanPath allNodePerm = createBoolean("allNodePerm");

    public final BooleanPath allPerm = createBoolean("allPerm");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath infoFinalPerm = createBoolean("infoFinalPerm");

    public final NumberPath<Integer> infoPermType = createNumber("infoPermType", Integer.class);

    public final StringPath name = createString("name");

    public final SetPath<com.jspxcms.core.domain.NodeRole, QNodeRole> nodeRoles = this.<com.jspxcms.core.domain.NodeRole, QNodeRole>createSet("nodeRoles", com.jspxcms.core.domain.NodeRole.class, QNodeRole.class, PathInits.DIRECT2);

    public final StringPath perms = createString("perms");

    public final NumberPath<Integer> rank = createNumber("rank", Integer.class);

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final QSite site;

    public final SetPath<com.jspxcms.core.domain.UserRole, QUserRole> userRoles = this.<com.jspxcms.core.domain.UserRole, QUserRole>createSet("userRoles", com.jspxcms.core.domain.UserRole.class, QUserRole.class, PathInits.DIRECT2);

    public QRole(String variable) {
        this(Role.class, forVariable(variable), INITS);
    }

    public QRole(Path<? extends Role> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRole(PathMetadata metadata, PathInits inits) {
        this(Role.class, metadata, inits);
    }

    public QRole(Class<? extends Role> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

