package com.jspxcms.core.domain;

import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.Type;
import org.springframework.web.util.WebUtils;

import javax.persistence.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigInteger;
import java.util.*;

import static com.jspxcms.core.constant.Constants.*;

/**
 * Site
 *
 * @author liufang
 */
@XmlRootElement
@Entity
@Table(name = "cms_site")
public class Site implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 模型类型
     */
    public static final String MODEL_TYPE = "site";
    /**
     * 正常状态
     */
    public static final int NORMAL = 0;
    /**
     * 禁用状态
     */
    public static final int DISABELD = 1;
    /**
     * 树编码长度
     */
    public static final int TREE_NUMBER_LENGTH = 4;

    public static String getIdentityCookie(HttpServletRequest request, HttpServletResponse response) {
        String value;
        Cookie cookie = WebUtils.getCookie(request, IDENTITY_COOKIE_NAME);
        if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
            value = cookie.getValue();
        } else {
            value = UUID.randomUUID().toString();
            value = StringUtils.remove(value, '-');
            cookie = new Cookie(IDENTITY_COOKIE_NAME, value);
            String ctx = request.getContextPath();
            if (StringUtils.isBlank(ctx)) {
                ctx = "/";
            }
            cookie.setPath(ctx);
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }
        return value;
    }

    @Transient
    public static String long2hex(long num) {
        BigInteger big = BigInteger.valueOf(num);
        String hex = big.toString(Character.MAX_RADIX);
        return StringUtils.leftPad(hex, TREE_NUMBER_LENGTH, '0');
    }

    @Transient
    public static long hex2long(String hex) {
        BigInteger big = new BigInteger(hex, Character.MAX_RADIX);
        return big.longValue();
    }

    @Transient
    public Boolean isNormal() {
        Integer status = getStatus();
        if (status == null) {
            return null;
        }
        return status == NORMAL;
    }

    @Transient
    public boolean isDef() {
        return getGlobal().getSite().getId().equals(getId());
    }

    @Transient
    public long getTreeMaxLong() {
        BigInteger big = new BigInteger(getTreeMax(), Character.MAX_RADIX);
        return big.longValue();
    }

    @Transient
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        Site bean = this;
        sb.append(bean.getName());
        bean = bean.getParent();
        while (bean != null) {
            sb.insert(0, " - ");
            sb.insert(0, bean.getName());
            bean = bean.getParent();
        }
        return sb.toString();
    }

    @Transient
    public void addChild(Site bean) {
        List<Site> set = getChildren();
        if (set == null) {
            set = new ArrayList<Site>();
            setChildren(set);
        }
        set.add(bean);
    }

    @Transient
    public String getUrl() {
        return getUrl(Context.isMobile());
    }

    @Transient
    public String getUrlMobile() {
        return getUrl(true);
    }

    @Transient
    public String getUrl(boolean isMobile) {
        boolean isFull = getIdentifyDomain();
        return getUrl(isFull, isMobile);
    }

    @Transient
    public String getUrlFull() {
        return getUrl(true, Context.isMobile());
    }

    @Transient
    public String getUrl(boolean isFull, boolean isMobile) {
        StringBuilder sb = new StringBuilder();
        if (isFull) {
            String domain = isMobile ? getMobileDomain() : getDomain();
            sb.append(getProtocol()).append("://").append(domain);
            if (getPort() != null) {
                sb.append(":").append(getPort());
            }
        }
        if (getStaticHome()) {
            String urlPrefix = isMobile ? getMobilePublishPoint().getUrlPrefix() : getHtmlPublishPoint().getUrlPrefix();
            if (StringUtils.isNotBlank(urlPrefix)) {
                sb.append(urlPrefix).append("/");
            }
        } else {
            if (StringUtils.isNotBlank(getContextPath())) {
                sb.append(getContextPath());
            }
            if (!getIdentifyDomain() && !isDef()) {
                sb.append(SITE_PREFIX).append(getNumber()).append(DYNAMIC_SUFFIX);
            }
        }
        if (sb.length() == 0) {
            // 如果为根目录，要加上"/"
            sb.append("/");
        }
        return sb.toString();
    }

    /**
     * 动态地址前缀
     *
     * @return
     */
    @Transient
    public String getDy() {
        StringBuilder sb = new StringBuilder();
        boolean isFull = getIdentifyDomain();
        if (isFull) {
            String domain = Context.isMobile() ? getMobileDomain() : getDomain();
            sb.append(getProtocol()).append("://").append(domain);
            if (getPort() != null) {
                sb.append(":").append(getPort());
            }
        }
        String ctx = getContextPath();
        if (StringUtils.isNotBlank(ctx)) {
            sb.append(ctx);
        }
        if (!getIdentifyDomain() && !isDef()) {
            sb.append(SITE_PREFIX).append(getNumber());
        }
        return sb.toString();
    }

    /**
     * 获得当前模板主题的资源文件路径
     * <p>
     * 例如：/jspxcms/template/1/bluewise/_files
     *
     * @return
     */
    @Transient
    public String getFilesUrl() {
        return getFilesUrl(null);
    }

    /**
     * 获得当前模板主题的资源文件路径
     * <p>
     * 例如：/jspxcms/template/1/bluewise/_files
     *
     * @param path
     * @return
     */
    @Transient
    public String getFilesUrl(String path) {
        return getFiles(path, true);
    }

    @Transient
    public String getFilesPath() {
        return getFilesPath(null);
    }

    @Transient
    public String getFilesPath(String path) {
        return getFiles(path, false);
    }

    @Transient
    public String getFiles(String path, boolean forDisplay) {
        StringBuilder sb = new StringBuilder();
        if (forDisplay) {
            String displayPath = getGlobal().getTemplateDisplayPathByCtx();
            if (StringUtils.isNotBlank(displayPath)) {
                sb.append(displayPath);
            }
        }
        sb.append("/").append(getId());
        String theme = Context.isMobile() ? getMobileTheme() : getTemplateTheme();
        sb.append("/").append(theme);
        sb.append("/").append(ForeContext.FILES);
        if (StringUtils.isNotBlank(path)) {
            sb.append(path);
        }
        return sb.toString();
    }

    /**
     * 获得模板路径
     * <p>
     * 例如：/1/bluewise/index.html
     *
     * @param tpl
     * @return
     */
    @Transient
    public String getTemplate(String tpl) {
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(getId());
        if (Context.isMobile()) {
            sb.append("/").append(getMobileTheme());
        } else {
            sb.append("/").append(getTemplateTheme());
        }
        if (tpl != null) {
            if (!tpl.startsWith("/")) {
                sb.append("/");
            }
            sb.append(tpl);
        }
        return sb.toString();
    }

    /**
     * 获得模板的基础路径
     * <p>
     * 例如：/1/...
     *
     * @param path
     * @return
     */
    @Transient
    public String getSiteBase(String path) {
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(getId());
        if (StringUtils.isNotBlank(path)) {
            if (!path.startsWith("/")) {
                sb.append("/");
            }
            sb.append(path);
        }
        return sb.toString();
    }

    @Transient
    public PublishPoint getUploadsPublishPoint() {
        return getGlobal() != null ? getGlobal().getUploadsPublishPoint() : null;
    }

    @Transient
    public String getProtocol() {
        return getGlobal() != null ? getGlobal().getProtocol() : null;
    }

    @Transient
    public Integer getPort() {
        return getGlobal() != null ? getGlobal().getPort() : null;
    }

    @Transient
    public String getContextPath() {
        return getGlobal() != null ? getGlobal().getContextPath() : null;
    }

    @Transient
    public String getVersion() {
        return getGlobal() != null ? getGlobal().getVersion() : null;
    }

    @Transient
    public String getNoPictureUrl() {
        return getFilesUrl(getNoPicture());
    }

    @Transient
    public String getFullNameOrName() {
        String fullName = getFullName();
        return StringUtils.isBlank(fullName) ? getName() : fullName;
    }

    @Transient
    public SiteWatermark getWatermark() {
        return new SiteWatermark(this);
    }

    @Transient
    public SiteWeixin getWeixin() {
        return new SiteWeixin(this);
    }

    @Transient
    public SiteComment getCommment() {
        return new SiteComment(this);
    }

    @Transient
    public Object getConf(String className) {
        try {
            return Class.forName(className).getConstructor(Site.class).newInstance(this);
        } catch (Exception e) {
            throw new IllegalArgumentException("Class '" + className + "' is not Conf Class", e);
        }
    }

    @Transient
    public <T> T getConf(Class<T> clazz) {
        try {
            return clazz.getConstructor(Site.class).newInstance(this);
        } catch (Exception e) {
            throw new IllegalArgumentException("Class '" + clazz.getName() + "' is not Conf Class", e);
        }
    }

    @Transient
    public void applyDefaultValue() {
        if (getTemplateTheme() == null) {
            setTemplateTheme("default");
        }
        if (getNoPicture() == null) {
            setNoPicture("/img/no_picture.jpg");
        }
        if (getStaticHome() == null) {
            setStaticHome(false);
        }
        if (getStatus() == null) {
            setStatus(NORMAL);
        }
    }

    private Integer id;
    private List<Site> children = new ArrayList<Site>(0);
    private List<Role> roles = new ArrayList<Role>(0);
    private Map<String, String> customs = new HashMap<String, String>(0);
    private Map<String, String> clobs = new HashMap<String, String>(0);

    private Global global;
    private PublishPoint htmlPublishPoint;
    private PublishPoint mobilePublishPoint;
    private Org org;
    private Site parent;

    private String name;
    private String number;
    private String fullName;
    private String domain;
    private String templateTheme;
    private String noPicture;
    private Boolean identifyDomain;
    private Boolean staticHome;

    private String mobileDomain;
    private String mobileTheme;

    private String treeNumber;
    private Integer treeLevel;
    private String treeMax;
    private Integer status;

    @XmlTransient
    @Id
    @Column(name = "f_site_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_site", pkColumnValue = "cms_site", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_site")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy(value = "treeNumber asc, id asc")
    public List<Site> getChildren() {
        return children;
    }

    public void setChildren(List<Site> children) {
        this.children = children;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @OrderBy(value = "seq asc, id asc")
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @ElementCollection
    @CollectionTable(name = "cms_site_custom", joinColumns = @JoinColumn(name = "f_site_id"))
    @MapKeyColumn(name = "f_key", length = 50)
    @Column(name = "f_value", length = 2000)
    public Map<String, String> getCustoms() {
        return this.customs;
    }

    public void setCustoms(Map<String, String> customs) {
        this.customs = customs;
    }

    @ElementCollection
    @CollectionTable(name = "cms_site_clob", joinColumns = @JoinColumn(name = "f_site_id"))
    @MapKeyColumn(name = "f_key", length = 50)
    @MapKeyType(value = @Type(type = "string"))
    @Lob
    @Column(name = "f_value", nullable = false)
    public Map<String, String> getClobs() {
        return this.clobs;
    }

    public void setClobs(Map<String, String> clobs) {
        this.clobs = clobs;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_parent_id")
    public Site getParent() {
        return parent;
    }

    public void setParent(Site parent) {
        this.parent = parent;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_global_id", nullable = false)
    public Global getGlobal() {
        return this.global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_html_publishpoint_id", nullable = false)
    public PublishPoint getHtmlPublishPoint() {
        return htmlPublishPoint;
    }

    public void setHtmlPublishPoint(PublishPoint htmlPublishPoint) {
        this.htmlPublishPoint = htmlPublishPoint;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_mobile_publishpoint_id")
    public PublishPoint getMobilePublishPoint() {
        return mobilePublishPoint;
    }

    public void setMobilePublishPoint(PublishPoint mobilePublishPoint) {
        this.mobilePublishPoint = mobilePublishPoint;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_org_id", nullable = false)
    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    @Column(name = "f_name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "f_number", nullable = false, unique = true, length = 100)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "f_full_name", length = 100)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "f_domain", nullable = false, length = 100)
    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Column(name = "f_template_theme", nullable = false, length = 100)
    public String getTemplateTheme() {
        return this.templateTheme;
    }

    public void setTemplateTheme(String templateTheme) {
        this.templateTheme = templateTheme;
    }

    @Column(name = "f_no_picture", nullable = true)
    public String getNoPicture() {
        return noPicture;
    }

    public void setNoPicture(String noPicture) {
        this.noPicture = noPicture;
    }

    @Column(name = "f_is_identify_domain", nullable = false, length = 1)
    public Boolean getIdentifyDomain() {
        return identifyDomain;
    }

    public void setIdentifyDomain(Boolean identifyDomain) {
        this.identifyDomain = identifyDomain;
    }

    @Column(name = "f_is_static_home", nullable = false, length = 1)
    public Boolean getStaticHome() {
        return staticHome;
    }

    public void setStaticHome(Boolean staticHome) {
        this.staticHome = staticHome;
    }

    @Column(name = "f_mobile_domain", length = 100)
    public String getMobileDomain() {
        return mobileDomain;
    }

    public void setMobileDomain(String mobileDomain) {
        this.mobileDomain = mobileDomain;
    }

    @Column(name = "f_mobile_theme", length = 100)
    public String getMobileTheme() {
        return mobileTheme;
    }

    public void setMobileTheme(String mobileTheme) {
        this.mobileTheme = mobileTheme;
    }

    @Column(name = "f_tree_number", nullable = false, length = 100)
    public String getTreeNumber() {
        return this.treeNumber;
    }

    public void setTreeNumber(String treeNumber) {
        this.treeNumber = treeNumber;
    }

    @Column(name = "f_tree_level", nullable = false)
    public Integer getTreeLevel() {
        return this.treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    @Column(name = "f_tree_max", nullable = false, length = 20)
    public String getTreeMax() {
        return this.treeMax;
    }

    public void setTreeMax(String treeMax) {
        this.treeMax = treeMax;
    }

    @Column(name = "f_status", nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
