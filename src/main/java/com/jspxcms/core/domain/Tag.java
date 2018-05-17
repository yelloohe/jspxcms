package com.jspxcms.core.domain;

import com.google.common.base.Objects;
import com.jspxcms.core.support.Siteable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Tag
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_tag")
public class Tag implements Siteable, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public void applyDefaultValue() {
        if (getCreationDate() == null) {
            setCreationDate(new Timestamp(System.currentTimeMillis()));
        }
        if (getRefers() == null) {
            setRefers(0);
        }
    }

    @Transient
    public int refer() {
        if (refers == null) {
            refers = 0;
        }
        return ++refers;
    }

    @Transient
    public int derefer() {
        if (refers == null) {
            refers = 0;
        }
        return --refers;
    }

    public Tag() {
    }

    public Tag(Site site, String name) {
        this.site = site;
        this.name = name;
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
        if (!(o instanceof Tag)) {
            return false;
        }
        Tag that = (Tag) o;
        return Objects.equal(id, that.id);
    }

    private Integer id;
    private Set<InfoTag> infoTags = new HashSet<InfoTag>(0);

    private Site site;

    private String name;
    private Date creationDate;
    private Integer refers;

    @Id
    @Column(name = "f_tag_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_tag", pkColumnValue = "cms_tag", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_tag")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
    public Set<InfoTag> getInfoTags() {
        return infoTags;
    }

    public void setInfoTags(Set<InfoTag> infoTags) {
        this.infoTags = infoTags;
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

    @Column(name = "f_name", nullable = false, length = 150)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "f_creation_date", nullable = false, length = 19)
    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "f_refers", nullable = false)
    public Integer getRefers() {
        return refers;
    }

    public void setRefers(Integer refers) {
        this.refers = refers;
    }

}
