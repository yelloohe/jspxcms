package com.jspxcms.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jspxcms.core.domain.MailText;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMailText is a Querydsl query type for MailText
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMailText extends EntityPathBase<MailText> {

    private static final long serialVersionUID = 1089814562L;

    public static final QMailText mailText = new QMailText("mailText");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath subject = createString("subject");

    public final StringPath text = createString("text");

    public QMailText(String variable) {
        super(MailText.class, forVariable(variable));
    }

    public QMailText(Path<? extends MailText> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMailText(PathMetadata metadata) {
        super(MailText.class, metadata);
    }

}

