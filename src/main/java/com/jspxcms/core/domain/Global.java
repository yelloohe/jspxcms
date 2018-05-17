package com.jspxcms.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import com.jspxcms.core.commercial.Enterprise;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.constant.Versions;

/**
 * 全局对象
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_global")
public class Global implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 模型类型
	 */
	public static final String MODEL_TYPE = "global";
	/**
	 * 系统预定义字段前缀
	 */
	public static final String SYS_PREFIX = "sys_";

	@Transient
	public static void removeAttr(Map<String, String> map, String prefix) {
		Set<String> keysToRemove = new HashSet<String>();
		for (String key : map.keySet()) {
			if (key.startsWith(prefix)) {
				keysToRemove.add(key);
			}
		}
		for (String key : keysToRemove) {
			map.remove(key);
		}
	}

	@Transient
	public static void removeAttrExcludeSys(Map<String, String> map) {
		Set<String> keysToRemove = new HashSet<String>();
		for (String key : map.keySet()) {
			if (!key.startsWith(SYS_PREFIX)) {
				keysToRemove.add(key);
			}
		}
		for (String key : keysToRemove) {
			map.remove(key);
		}
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	@Transient
	public String getVersion() {
		return Versions.getVersion();
	}

	/**
	 * 是否商业版
	 * 
	 * @return
	 */
	@Transient
	public boolean isEnterpriseEdition() {
		return Enterprise.isEp();
	}

	@Transient
	public boolean isDocEnabled() {
		if (Constants.OPENOFFICE_PORT <= 0 || StringUtils.isBlank(Constants.SWFTOOLS_PDF2SWF)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取模版的显示路径，如为Web上下文发布，则加上上下文路径。如/template, /ctx/template
	 * 
	 * @return
	 */
	@Transient
	public String getTemplateDisplayPathByCtx() {
		String ctx = getContextPath();
		StringBuilder sb = new StringBuilder();
		// 存储路径是file:开头代表模版在独立应用里部署，不需加上下文路径。
		if (StringUtils.isNotBlank(ctx) && !StringUtils.startsWith(Constants.TEMPLATE_STORE_PATH, "file:")) {
			sb.append(ctx);
		}
		if (StringUtils.isNotBlank(Constants.TEMPLATE_DISPLAY_PATH)) {
			sb.append(Constants.TEMPLATE_DISPLAY_PATH);
		}
		return sb.toString();
	}

	@Transient
	public String getUploadsDomain() {
		return getUploadsPublishPoint().getDisplayDomain();
	}

	@Transient
	public Set<String> getValidDomains() {
		Set<String> domains = new HashSet<String>();
		List<Site> sites = getSites();
		for (Site site : sites) {
			domains.add(site.getDomain());
		}
		return domains;
	}

	/**
	 * 获取邮件配置对象
	 * 
	 * @return
	 */
	@Transient
	public GlobalMail getMail() {
		return new GlobalMail(getCustoms());
	}

	/**
	 * 获取注册配置对象
	 * 
	 * @return
	 */
	@Transient
	public GlobalRegister getRegister() {
		return new GlobalRegister(getCustoms());
	}

	/**
	 * 获取上传配置对象
	 * 
	 * @return
	 */
	@Transient
	public GlobalUpload getUpload() {
		return new GlobalUpload(getCustoms());
	}

	/**
	 * 获取其他配置对象
	 * 
	 * @return
	 */
	@Transient
	public GlobalOther getOther() {
		return new GlobalOther(getCustoms());
	}

	/**
	 * 根据配置对象的类名获取配置对象
	 * 
	 * @param className
	 *            类完整名
	 * @return 类名对应的配置对象
	 */
	@Transient
	public Object getConf(String className) {
		try {
			return Class.forName(className).getConstructor(Map.class).newInstance(getCustoms());
		} catch (Exception e) {
			throw new IllegalArgumentException("Class '" + className + "' is not Conf Class", e);
		}
	}

	private Site site;

	/**
	 * 获取默认站点。从所有站点中查找ID为1的站点。ID为1的站点为默认站点。
	 * 
	 * @return
	 */
	@Transient
	public Site getSite() {
		if (site == null) {
			for (Site s : getSites()) {
				if (s.getId() == 1) {
					site = s;
					break;
				}
			}
		}
		return site;
	}

	private Integer id;
	private PublishPoint uploadsPublishPoint;
	private List<Site> sites = new ArrayList<Site>(0);
	private Map<String, String> customs = new HashMap<String, String>(0);
	private Map<String, String> clobs = new HashMap<String, String>(0);

	private String protocol;
	private Integer port;
	private String contextPath;
	private Integer captchaErrors;

	private String dataVersion;

	@Id
	@Column(name = "f_global_id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_uploads_publishpoint_id")
	public PublishPoint getUploadsPublishPoint() {
		return uploadsPublishPoint;
	}

	public void setUploadsPublishPoint(PublishPoint uploadsPublishPoint) {
		this.uploadsPublishPoint = uploadsPublishPoint;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "global")
	@OrderBy("treeNumber asc")
	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	@ElementCollection
	@CollectionTable(name = "cms_global_custom", joinColumns = @JoinColumn(name = "f_global_id"))
	@MapKeyColumn(name = "f_key", length = 50)
	@Column(name = "f_value", length = 2000)
	public Map<String, String> getCustoms() {
		return this.customs;
	}

	public void setCustoms(Map<String, String> customs) {
		this.customs = customs;
	}

	@ElementCollection
	@CollectionTable(name = "cms_global_clob", joinColumns = @JoinColumn(name = "f_global_id"))
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

	@Length(max = 50)
	@Column(name = "f_protocol", length = 50)
	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Column(name = "f_port")
	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Length(max = 255)
	@Column(name = "f_context_path", length = 255)
	public String getContextPath() {
		return this.contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	@Column(name = "f_captcha_errors", nullable = false)
	public Integer getCaptchaErrors() {
		return captchaErrors;
	}

	public void setCaptchaErrors(Integer captchaErrors) {
		this.captchaErrors = captchaErrors;
	}

	@Length(max = 50)
	@Column(name = "f_version", nullable = false, length = 50)
	public String getDataVersion() {
		return this.dataVersion;
	}

	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}
}
