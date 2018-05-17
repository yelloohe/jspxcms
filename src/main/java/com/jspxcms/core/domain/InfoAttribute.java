package com.jspxcms.core.domain;

import com.google.common.base.Objects;
import com.jspxcms.core.domain.InfoAttribute.InfoAttributeId;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * InfoAttribute
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_info_attribute")
@IdClass(InfoAttributeId.class)
public class InfoAttribute implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public InfoAttribute() {
    }

    public InfoAttribute(Info info, Attribute attribute) {
        this.info = info;
        this.attribute = attribute;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_info_id", nullable = false)
    private Info info;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_attribute_id", nullable = false)
    private Attribute attribute;

    @Column(name = "f_image")
    private String image;

    @XmlTransient
    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoAttribute)) {
            return false;
        }
        InfoAttribute that = (InfoAttribute) o;
        return Objects.equal(info, that.info) && Objects.equal(attribute, that.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(info, attribute);
    }

    public static class InfoAttributeId implements Serializable {
        private static final long serialVersionUID = 1L;

        Integer info;
        Integer attribute;

        public InfoAttributeId() {
        }

        public InfoAttributeId(Integer info, Integer attribute) {
            this.info = info;
            this.attribute = attribute;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof InfoAttributeId)) {
                return false;
            }
            InfoAttributeId that = (InfoAttributeId) o;
            return Objects.equal(info, that.info) && Objects.equal(attribute, that.attribute);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(info, attribute);
        }
    }
}
