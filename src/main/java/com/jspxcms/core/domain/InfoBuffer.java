package com.jspxcms.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 * InfoBuffer
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_info_buffer")
public class InfoBuffer implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public void applyDefaultValue() {
        if (getViews() == null) {
            setViews(0);
        }
        if (getDownloads() == null) {
            setDownloads(0);
        }
        if (getComments() == null) {
            setComments(0);
        }
        if (getInvolveds() == null) {
            setInvolveds(0);
        }
        if (getDiggs() == null) {
            setDiggs(0);
        }
        if (getBurys() == null) {
            setBurys(0);
        }
        if (getScore() == null) {
            setScore(0);
        }
    }

    private Integer id;

    private Info info;

    private Integer views;
    private Integer downloads;
    private Integer comments;
    private Integer involveds;
    private Integer diggs;
    private Integer burys;
    private Integer score;

    @XmlTransient
    @Id
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_info_id")
    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Column(name = "f_views", nullable = false)
    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Column(name = "f_downloads", nullable = false)
    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Column(name = "f_comments", nullable = false)
    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @Column(name = "f_involveds", nullable = false)
    public Integer getInvolveds() {
        return involveds;
    }

    public void setInvolveds(Integer involveds) {
        this.involveds = involveds;
    }

    @Column(name = "f_diggs", nullable = false)
    public Integer getDiggs() {
        return diggs;
    }

    public void setDiggs(Integer diggs) {
        this.diggs = diggs;
    }

    @Column(name = "f_burys", nullable = false)
    public Integer getBurys() {
        return burys;
    }

    public void setBurys(Integer burys) {
        this.burys = burys;
    }

    @Column(name = "f_score", nullable = false)
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
