package com.jspxcms.core.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.Type;

import com.google.common.base.Objects;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.web.Anchor;
import com.jspxcms.common.web.ImageAnchor;
import com.jspxcms.common.web.ImageAnchorBean;
import com.jspxcms.core.support.Siteable;

/**
 * Special
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_special")
public class Special implements java.io.Serializable, Anchor, Siteable {
	private static final long serialVersionUID = 1L;

	public static final String DEF_TEMPLATE = "sys_special.html";

	public static final String MODEL_TEMPLATE = "template";
	/**
	 * 模型类型
	 */
	public static final String MODEL_TYPE = "special";
	/**
	 * 附件类型
	 */
	public static final String ATTACH_TYPE = "node";

	public static String getTemplate(Map<String, String> customs) {
		return customs != null ? customs.get(MODEL_TEMPLATE) : null;
	}

	@Transient
	public String getTemplate() {
		String template = getSpecialTemplate();
		if (template == null) {
			template = getTemplate(getModel().getCustoms());
		}
		if (template == null) {
			template = DEF_TEMPLATE;
		}
		return getSite().getTemplate(template);
	}

	@Transient
	public String getUrl() {
		// TODO
		return null;
	}

	@Transient
	public Boolean getNewWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public String getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public Boolean getStrong() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public Boolean getEm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public String getKeywords() {
		String keywords = getMetaKeywords();
		if (StringUtils.isBlank(keywords)) {
			return getTitle();
		} else {
			return keywords;
		}
	}

	@Transient
	public String getDescription() {
		String description = getMetaDescription();
		if (StringUtils.isBlank(description)) {
			return getTitle();
		} else {
			return description;
		}
	}

	@Transient
	public String getSmallImageUrl() {
		String url = getSmallImage();
		return StringUtils.isBlank(url) ? getSite().getNoPictureUrl() : url;
	}

	@Transient
	public ImageAnchor getSmallImageBean() {
		ImageAnchorBean bean = new ImageAnchorBean();
		bean.setTitle(getTitle());
		bean.setUrl(getUrl());
		bean.setSrc(getSmallImageUrl());
		return bean;
	}

	@Transient
	public String getLargeImageUrl() {
		String url = getLargeImage();
		return StringUtils.isBlank(url) ? getSite().getNoPictureUrl() : url;
	}

	@Transient
	public ImageAnchor getLargeImageBean() {
		ImageAnchorBean bean = new ImageAnchorBean();
		bean.setTitle(getTitle());
		bean.setUrl(getUrl());
		bean.setSrc(getLargeImageUrl());
		return bean;
	}

	/**
	 * 视频大小，自动转换为KB、MB或GB
	 * 
	 * @return
	 */
	@Transient
	public String getVideoSize() {
		Long length = getVideoLength();
		return FilesEx.getSize(length);
	}

	@Transient
	public Set<String> getAttachUrls() {
		Set<String> urls = new HashSet<String>();
		urls.add(getSmallImage());
		urls.add(getLargeImage());
		urls.add(getVideo());
		Set<String> clobEditorNames = new HashSet<String>();
		Map<String, String> clobs = getClobs();
		Map<String, String> customs = getCustoms();
		getModel().getAttachUrls(urls, clobEditorNames, clobs, customs);
		return urls;
	}

	@Transient
	public void applyDefaultValue() {
		setWithImage(StringUtils.isNotBlank(getSmallImage()));
		if (getCreationDate() == null) {
			setCreationDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getRefers() == null) {
			setRefers(0);
		}
		if (getViews() == null) {
			setViews(0);
		}
		if (getRecommend() == null) {
			setRecommend(false);
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
		if (!(o instanceof Special)) {
			return false;
		}
		Special that = (Special) o;
		return Objects.equal(id, that.id);
	}

	private Integer id;
	private List<SpecialImage> images = new ArrayList<SpecialImage>(0);
	private List<SpecialFile> files = new ArrayList<SpecialFile>(0);
	private Map<String, String> customs = new HashMap<String, String>(0);
	private Map<String, String> clobs = new HashMap<String, String>(0);

	private SpecialCategory category;
	private Site site;
	private User creator;
	private Model model;

	private Date creationDate;
	private String title;
	private String metaKeywords;
	private String metaDescription;
	private String specialTemplate;
	private String smallImage;
	private String largeImage;
	private String video;
	private String videoName;
	private Long videoLength;
	private String videoTime;
	private Integer refers;
	private Integer views;
	private Boolean withImage;
	private Boolean recommend;

	public Special() {
	}

	public Special(String title, SpecialCategory category, Site site) {
		this.title = title;
		this.category = category;
		this.site = site;
	}

	@Id
	@Column(name = "f_special_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_special", pkColumnValue = "cms_special", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_special")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ElementCollection
	@CollectionTable(name = "cms_special_image", joinColumns = @JoinColumn(name = "f_special_id"))
	@OrderColumn(name = "f_index")
	public List<SpecialImage> getImages() {
		return images;
	}

	public void setImages(List<SpecialImage> images) {
		this.images = images;
	}

	@ElementCollection
	@CollectionTable(name = "cms_special_file", joinColumns = @JoinColumn(name = "f_special_id"))
	@OrderColumn(name = "f_index")
	public List<SpecialFile> getFiles() {
		return files;
	}

	public void setFiles(List<SpecialFile> files) {
		this.files = files;
	}

	@ElementCollection
	@CollectionTable(name = "cms_special_custom", joinColumns = @JoinColumn(name = "f_special_id"))
	@MapKeyColumn(name = "f_key", length = 50)
	@Column(name = "f_value", length = 2000)
	public Map<String, String> getCustoms() {
		return this.customs;
	}

	public void setCustoms(Map<String, String> customs) {
		this.customs = customs;
	}

	@ElementCollection
	@CollectionTable(name = "cms_special_clob", joinColumns = @JoinColumn(name = "f_special_id"))
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_speccate_id", nullable = false)
	public SpecialCategory getCategory() {
		return this.category;
	}

	public void setCategory(SpecialCategory category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_creator_id", nullable = false)
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_model_id")
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_creation_date", nullable = false, length = 19)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "f_title", nullable = false, length = 150)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "f_meta_keywords", length = 150)
	public String getMetaKeywords() {
		return this.metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	@Column(name = "f_meta_description", length = 450)
	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Column(name = "f_special_template")
	public String getSpecialTemplate() {
		return specialTemplate;
	}

	public void setSpecialTemplate(String specialTemplate) {
		this.specialTemplate = specialTemplate;
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

	@Column(name = "f_refers", nullable = false)
	public Integer getRefers() {
		return refers;
	}

	public void setRefers(Integer refers) {
		this.refers = refers;
	}

	@Column(name = "f_views", nullable = false)
	public Integer getViews() {
		return this.views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	@Column(name = "f_is_with_image", nullable = false, length = 1)
	public Boolean getWithImage() {
		return this.withImage;
	}

	public void setWithImage(Boolean withImage) {
		this.withImage = withImage;
	}

	@Column(name = "f_is_recommend", nullable = false, length = 1)
	public Boolean getRecommend() {
		return this.recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}
}
