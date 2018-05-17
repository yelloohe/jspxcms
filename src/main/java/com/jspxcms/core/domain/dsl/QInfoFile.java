package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.InfoFile;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QInfoFile is a Querydsl query type for InfoFile
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QInfoFile extends BeanPath<InfoFile> {

    private static final long serialVersionUID = 1277623272L;

    public static final QInfoFile infoFile = new QInfoFile("infoFile");

    public final NumberPath<Integer> downloads = createNumber("downloads", Integer.class);

    public final StringPath file = createString("file");

    public final NumberPath<Long> length = createNumber("length", Long.class);

    public final StringPath name = createString("name");

    public QInfoFile(String variable) {
        super(InfoFile.class, forVariable(variable));
    }

    public QInfoFile(Path<? extends InfoFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInfoFile(PathMetadata metadata) {
        super(InfoFile.class, metadata);
    }

}

