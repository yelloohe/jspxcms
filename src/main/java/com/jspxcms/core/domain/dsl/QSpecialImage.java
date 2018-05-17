package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.SpecialImage;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpecialImage is a Querydsl query type for SpecialImage
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSpecialImage extends BeanPath<SpecialImage> {

    private static final long serialVersionUID = -1049511488L;

    public static final QSpecialImage specialImage = new QSpecialImage("specialImage");

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final StringPath text = createString("text");

    public QSpecialImage(String variable) {
        super(SpecialImage.class, forVariable(variable));
    }

    public QSpecialImage(Path<? extends SpecialImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpecialImage(PathMetadata metadata) {
        super(SpecialImage.class, metadata);
    }

}

