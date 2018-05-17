package com.jspxcms.core.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 文档推送实体类
 */
@Entity
@Table(name = "cms_info_push")
public class InfoPush {
    /**
     * 是否允许撤销推送。如果父站点向子站点推送，随时可以撤销。如果不是父站点向子站推送且文档不是推送状态（如已经审核），则不允许撤销。
     *
     * @return 是否允许撤销推送
     */
    public boolean isAllowCancel() {
        if (!isParentPush()) {
            return info.getStatus() == Info.PUSH;
        }
        return true;
    }

    /**
     * 是否父站点向子站点推送
     *
     * @return
     */
    public boolean isParentPush() {
        if (fromSite == null || toSite == null) {
            return false;
        }
        // 源站点treeNumber目标站点treeNumber的前缀
        return toSite.getTreeNumber().startsWith(fromSite.getTreeNumber());
    }

    public void applyDefaultValue() {
        if (created == null) {
            created = new Timestamp(System.currentTimeMillis());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Site getFromSite() {
        return fromSite;
    }

    public void setFromSite(Site fromSite) {
        this.fromSite = fromSite;
    }

    public Site getToSite() {
        return toSite;
    }

    public void setToSite(Site toSite) {
        this.toSite = toSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoPush that = (InfoPush) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @Id
    @Column(name = "infopush_id_", nullable = false)
    @TableGenerator(name = "cms_info_push", pkColumnValue = "cms_info_push", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "cms_info_push")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id_", nullable = false)
    private Info info;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_site_id_", nullable = false)
    private Site fromSite;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_site_id_", nullable = false)
    private Site toSite;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_", nullable = false)
    private Date created;
}
