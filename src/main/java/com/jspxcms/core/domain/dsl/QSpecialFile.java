package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.SpecialFile;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpecialFile is a Querydsl query type for SpecialFile
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSpecialFile extends BeanPath<SpecialFile> {

    private static final long serialVersionUID = 1628619895L;

    public static final QSpecialFile specialFile = new QSpecialFile("specialFile");

    public final NumberPath<Integer> downloads = createNumber("downloads", Integer.class);

    public final StringPath file = createString("file");

    public final NumberPath<Long> length = createNumber("length", Long.class);

    public final StringPath name = createString("name");

    public QSpecialFile(String variable) {
        super(SpecialFile.class, forVariable(variable));
    }

    public QSpecialFile(Path<? extends SpecialFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpecialFile(PathMetadata metadata) {
        super(SpecialFile.class, metadata);
    }

}

