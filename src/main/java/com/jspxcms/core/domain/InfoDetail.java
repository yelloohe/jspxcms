package com.jspxcms.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 * InfoDetail
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_info_detail")
public class InfoDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public void applyDefaultValue() {
        if (getStrong() == null) {
            setStrong(false);
        }
        if (getEm() == null) {
            setEm(false);
        }
        if (getWeixinMass() == null) {
            setWeixinMass(false);
        }
    }

    private Integer id;

    private Info info;

    private String title;
    private String html;
    private String mobileHtml;
    private String subtitle;
    private String fullTitle;
    private String link;
    private Boolean newWindow;
    private String color;
    private Boolean strong;
    private Boolean em;
    private String metaDescription;
    private String infoPath;
    private String infoTemplate;
    private String source;
    private String sourceUrl;
    private String author;
    private String smallImage;
    private String largeImage;
    private String video;
    private String videoName;
    private Long videoLength;
    private String videoTime;
    private String file;
    private String fileName;
    private Long fileLength;
    private String doc;
    private String docName;
    private Long docLength;
    private String docPdf;
    private String docSwf;
    private Boolean allowComment;
    private Boolean weixinMass;
    private String stepName;

    public InfoDetail() {
    }

    public InfoDetail(Info info, String title) {
        this.info = info;
        this.title = title;
    }

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
    @OneToOne
    @JoinColumn(name = "f_info_id")
    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Column(name = "f_title", nullable = false, length = 150)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "f_html")
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Column(name = "f_mobile_html")
    public String getMobileHtml() {
        return mobileHtml;
    }

    public void setMobileHtml(String mobileHtml) {
        this.mobileHtml = mobileHtml;
    }

    @Column(name = "f_subtitle", length = 150)
    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Column(name = "f_full_title", length = 150)
    public String getFullTitle() {
        return this.fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    @Column(name = "f_link")
    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "f_is_new_window", length = 1)
    public Boolean getNewWindow() {
        return this.newWindow;
    }

    public void setNewWindow(Boolean newWindow) {
        this.newWindow = newWindow;
    }

    @Column(name = "f_color", length = 50)
    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "f_is_strong", nullable = false, length = 1)
    public Boolean getStrong() {
        return this.strong;
    }

    public void setStrong(Boolean strong) {
        this.strong = strong;
    }

    @Column(name = "f_is_em", nullable = false, length = 1)
    public Boolean getEm() {
        return this.em;
    }

    public void setEm(Boolean em) {
        this.em = em;
    }

    @Column(name = "f_meta_description", length = 450)
    public String getMetaDescription() {
        return this.metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @Column(name = "f_info_path")
    public String getInfoPath() {
        return infoPath;
    }

    public void setInfoPath(String infoPath) {
        this.infoPath = infoPath;
    }

    @Column(name = "f_info_template")
    public String getInfoTemplate() {
        return this.infoTemplate;
    }

    public void setInfoTemplate(String infoTemplate) {
        this.infoTemplate = infoTemplate;
    }

    @Column(name = "f_source", length = 50)
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "f_source_url")
    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Column(name = "f_author", length = 50)
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "f_small_image")
    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    @Column(name = "f_large_image")
    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    @Column(name = "f_video")
    public String getVideo() {
        return this.video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Column(name = "f_video_name")
    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Column(name = "f_video_length")
    public Long getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(Long videoLength) {
        this.videoLength = videoLength;
    }

    @Column(name = "f_video_time", length = 100)
    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    @Column(name = "f_file")
    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Column(name = "f_file_name")
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "f_file_length")
    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    @Column(name = "f_doc")
    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    @Column(name = "f_doc_name")
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Column(name = "f_doc_length")
    public Long getDocLength() {
        return docLength;
    }

    public void setDocLength(Long docLength) {
        this.docLength = docLength;
    }

    @Column(name = "f_doc_pdf")
    public String getDocPdf() {
        return docPdf;
    }

    public void setDocPdf(String docPdf) {
        this.docPdf = docPdf;
    }

    @Column(name = "f_doc_swf")
    public String getDocSwf() {
        return docSwf;
    }

    public void setDocSwf(String docSwf) {
        this.docSwf = docSwf;
    }

    @Column(name = "f_is_allow_comment", length = 1)
    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    @Column(name = "f_is_weixin_mass",nullable = false, length = 1)
    public Boolean getWeixinMass() {
        return weixinMass;
    }

    public void setWeixinMass(Boolean weixinMass) {
        this.weixinMass = weixinMass;
    }

    @Column(name = "f_step_name", length = 100)
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

}
