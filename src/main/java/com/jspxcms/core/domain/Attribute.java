package com.jspxcms.core.domain;

import com.google.common.base.Objects;
import com.jspxcms.core.support.Siteable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Attribute
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_attribute")
public class Attribute implements Siteable, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Transient
    public void applyDefaultValue() {
        if (getSeq() == null) {
            setSeq(Integer.MAX_VALUE);
        }
        if (getScale() == null) {
            setScale(false);
        }
        if (getExact() == null) {
            setExact(false);
        }
        if (getWatermark() == null) {
            setWatermark(false);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribute)) {
            return false;
        }
        Attribute that = (Attribute) o;
        return Objects.equal(id, that.id);
    }

    private Integer id;

    private Site site;

    private String number;
    private String name;
    private Integer seq;
    private Boolean withImage;
    private Boolean scale;
    private Boolean exact;
    private Boolean watermark;
    private Integer imageWidth;
    private Integer imageHeight;

    @XmlTransient
    @Id
    @Column(name = "f_attribute_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_attribute", pkColumnValue = "cms_attribute", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_attribute")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_site_id", nullable = false)
    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Column(name = "f_number", nullable = false, length = 20)
    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "f_name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "f_seq", nullable = false)
    public Integer getSeq() {
        return this.seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Column(name = "f_is_with_image", nullable = false, length = 1)
    public Boolean getWithImage() {
        return this.withImage;
    }

    public void setWithImage(Boolean withImage) {
        this.withImage = withImage;
    }

    @Column(name = "f_is_scale", nullable = false, length = 1)
    public Boolean getScale() {
        return scale;
    }

    public void setScale(Boolean scale) {
        this.scale = scale;
    }

    @Column(name = "f_is_exact", nullable = false, length = 1)
    public Boolean getExact() {
        return exact;
    }

    public void setExact(Boolean exact) {
        this.exact = exact;
    }

    @Column(name = "f_is_watermark", nullable = false, length = 1)
    public Boolean getWatermark() {
        return watermark;
    }

    public void setWatermark(Boolean watermark) {
        this.watermark = watermark;
    }

    @Column(name = "f_image_width")
    public Integer getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    @Column(name = "f_image_height")
    public Integer getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

}
