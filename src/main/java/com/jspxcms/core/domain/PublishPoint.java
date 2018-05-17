package com.jspxcms.core.domain;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.file.FtpTemplate;
import com.jspxcms.common.web.PathResolver;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.net.URI;

@Entity
@Table(name = "cms_publish_point")
public class PublishPoint implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 文件系统
     */
    public static int METHOD_FILE = 1;
    /**
     * FTP
     */
    public static int METHOD_FTP = 2;
    /**
     * HTML发布点
     */
    public static int TYPE_HTML = 1;
    /**
     * 附件发布点
     */
    public static int TYPE_UPLOAD = 2;

    // @Transient
    // public boolean isPointFile(String filename) {
    // return StringUtils.startsWith(filename, getUrlPrefix());
    // }

    /**
     * 获取显示路径，如为Web上下文发布，则加上上下文路径。如/uploads, /ctx/uploads
     *
     * @return
     */
    @Transient
    public String getUrlPrefix() {
        StringBuilder sb = new StringBuilder();
        if (getMethod() == METHOD_FILE
                && !StringUtils.startsWith(getStorePath(), "file:")) {
            String ctx = getGlobal().getContextPath();
            if (StringUtils.isNotBlank(ctx)) {
                sb.append(ctx);
            }
        }
        String displayPath = getDisplayPath();
        if (StringUtils.isNotBlank(displayPath)) {
            sb.append(displayPath);
        }
        return sb.toString();
    }

    @Transient
    public boolean isFtpMethod() {
        return getMethod() == METHOD_FTP;
    }

    @Transient
    public FtpTemplate getFtpTemplate() {
        return new FtpTemplate(getFtpHostname(), getFtpPort(),
                getFtpUsername(), getFtpPassword());
    }

    @Transient
    public FileHandler getFileHandler(PathResolver pathResolver) {
        String prefix = getStorePath();
        FileHandler fileHandler;
        if (isFtpMethod()) {
            fileHandler = FileHandler.getFileHandler(getFtpTemplate(), prefix);
        } else {
            fileHandler = FileHandler.getLocalFileHandler(pathResolver, prefix);
        }
        return fileHandler;
    }

    @Transient
    public String getDisplayDomain() {
        URI uri = URI.create(getDisplayPath());
        return uri.getHost();
    }

    @Transient
    public void applyDefaultValue() {
        if (getSeq() == null) {
            setSeq(Integer.MAX_VALUE);
        }
        if (getMethod() == null) {
            setMethod(METHOD_FILE);
        }
        if (getType() == null) {
            setType(TYPE_HTML);
        }
    }

    private Integer id;
    private Global global;

    private String name;
    private String description;
    private String storePath;
    private String displayPath;
    private String ftpHostname;
    private Integer ftpPort;
    private String ftpUsername;
    private String ftpPassword;
    private Integer seq;
    private Integer method;
    private Integer type;

    @XmlTransient
    @Id
    @Column(name = "f_publishpoint_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_publish_point", pkColumnValue = "cms_publish_point", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_publish_point")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_global_id", nullable = false)
    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    @Column(name = "f_name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "f_description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "f_store_path", length = 100)
    public String getStorePath() {
        return this.storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    @Column(name = "f_display_path", length = 255)
    public String getDisplayPath() {
        return this.displayPath;
    }

    public void setDisplayPath(String displayPath) {
        this.displayPath = displayPath;
    }

    @Column(name = "f_ftp_hostname", length = 255)
    public String getFtpHostname() {
        return this.ftpHostname;
    }

    public void setFtpHostname(String ftpHostname) {
        this.ftpHostname = ftpHostname;
    }

    @Column(name = "f_ftp_port")
    public Integer getFtpPort() {
        return this.ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    @Column(name = "f_ftp_username", length = 100)
    public String getFtpUsername() {
        return this.ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    @Column(name = "f_ftp_password", length = 100)
    public String getFtpPassword() {
        return this.ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    @Column(name = "f_seq", nullable = false)
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Column(name = "f_method", nullable = false)
    public Integer getMethod() {
        return this.method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    @Column(name = "f_type", nullable = false)
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
