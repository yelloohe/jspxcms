package com.jspxcms.core.domain;

import com.jspxcms.core.support.Siteable;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;
import java.util.Map.Entry;

/**
 * 模型
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_model")
public class Model implements Siteable, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Model.class);

    @Transient
    public Set<String> getPredefinedNames() {
        Set<String> names = new HashSet<String>();
        for (ModelField field : getFields()) {
            if (field.isPredefined()) {
                names.add(field.getName());
            }
        }
        return names;
    }

    @Transient
    public List<ModelField> getEnabledFields() {
        List<ModelField> fields = getFields();
        List<ModelField> enabledFields = new ArrayList<ModelField>();
        for (ModelField field : fields) {
            if (!field.getDisabled()) {
                enabledFields.add(field);
            }
        }
        return enabledFields;
    }

    @Transient
    public List<ModelField> getNormalFields() {
        List<ModelField> fields = getFields();
        List<ModelField> normalFields = new ArrayList<ModelField>();
        for (ModelField field : fields) {
            if (!field.getDisabled() && !field.isEditor()) {
                normalFields.add(field);
            }
        }
        return normalFields;
    }

    /**
     * 获取可查询字段
     *
     * @return
     */
    @Transient
    public List<ModelField> getQueryableFields() {
        List<ModelField> fields = getFields();
        List<ModelField> queryableFields = new ArrayList<ModelField>();
        for (ModelField field : fields) {
            if (!field.getDisabled() && field.isQueryable()) {
                queryableFields.add(field);
            }
        }
        return queryableFields;
    }

    @Transient
    public List<ModelField> getEditorFields() {
        List<ModelField> fields = getFields();
        List<ModelField> enabledFields = new ArrayList<ModelField>();
        for (ModelField field : fields) {
            if (!field.getDisabled() && field.isEditor()) {
                enabledFields.add(field);
            }
        }
        return enabledFields;
    }

    @Transient
    public ModelField getField(String name) {
        for (ModelField field : getFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    @Transient
    public void addField(ModelField field) {
        List<ModelField> fields = getFields();
        if (fields == null) {
            fields = new ArrayList<ModelField>();
            setFields(fields);
        }
        fields.add(field);
    }

    /**
     * 获取带选项(options)的自定义字段的值(value)。
     *
     * @param name 字段名称
     * @param key  选项键值(key)。如果改参数为null，则返回null。
     * @return 不存在则返回null
     */
    @Transient
    public String getCustomOptionValue(String name, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        for (ModelField field : getFields()) {
            if (field.getInnerType() == ModelField.FIELD_CUSTOM && field.getName().equals(name)) {
                return field.getOptions().get(key);
            }
        }
        return null;
    }

    /**
     * 获取带选项(options)的可查询字段的值(value)。
     *
     * @param name 字段名称
     * @param key  选项键值(key)。如果改参数为null，则返回null。
     * @return 不存在则返回null
     */
    @Transient
    public String getQueryableOptionValue(String name, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        for (ModelField field : getFields()) {
            if (field.getInnerType() == ModelField.FIELD_QUERYABLE && field.getName().equals(name)) {
                return field.getOptions().get(key);
            }
        }
        return null;
    }

    public void getAttachUrls(Set<String> urls, Set<String> clobEditorNames, Map<String, String> clobs,
                              Map<String, String> customs) {

        // 自定义字段
        for (ModelField field : getFields()) {
            if (field.getDisabled() || field.isCustom()) {
                continue;
            }
            if (field.isEditor() && field.isClob()) {
                clobEditorNames.add(field.getName());
                continue;
            }
            int type = field.getType();
            if (type == ModelField.IMAGE || type == ModelField.VIDEO || type == ModelField.FILE) {
                urls.add(customs.get(field.getName()));
            }
        }
        // 取clobs图片和链接
        NodeFilter imgf = new NodeClassFilter(ImageTag.class);
        NodeFilter linkf = new NodeClassFilter(LinkTag.class);
        NodeFilter filter = new OrFilter(imgf, linkf);
        if (clobs != null) {
            for (Entry<String, String> clob : clobs.entrySet()) {
                String name = clob.getKey();
                String html = clob.getValue();
                if (!clobEditorNames.contains(name) || StringUtils.isBlank(html)) {
                    continue;
                }
                try {
                    Parser parser = new Parser(new Lexer(html));
                    NodeList nodes = parser.extractAllNodesThatMatch(filter);
                    SimpleNodeIterator it = nodes.elements();
                    while (it.hasMoreNodes()) {
                        org.htmlparser.Node n = it.nextNode();
                        if (n instanceof ImageTag) {
                            urls.add(((ImageTag) n).getImageURL());
                        }
                        if (n instanceof LinkTag) {
                            urls.add(((LinkTag) n).extractLink());
                        }
                    }
                } catch (ParserException e) {
                    logger.error(null, e);
                }
            }
        }
    }

    public void applyDefaultValue() {
        if (getSeq() == null) {
            setSeq(Integer.MAX_VALUE);
        }
    }

    private Integer id;
    private List<ModelField> fields = new ArrayList<ModelField>(0);
    private Map<String, String> customs = new HashMap<String, String>(0);

    private Site site;

    private String type;
    private String name;
    private String number;
    private Integer seq;

    public Model() {
    }

    @XmlTransient
    @Id
    @Column(name = "f_model_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_model", pkColumnValue = "cms_model", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_model")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, mappedBy = "model")
    @OrderBy(value = "seq asc, id asc")
    public List<ModelField> getFields() {
        return this.fields;
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }

    @ElementCollection
    @CollectionTable(name = "cms_model_custom", joinColumns = @JoinColumn(name = "f_model_id"))
    @MapKeyColumn(name = "f_key", length = 50)
    @Column(name = "f_value", length = 2000)
    public Map<String, String> getCustoms() {
        return this.customs;
    }

    public void setCustoms(Map<String, String> customs) {
        this.customs = customs;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_site_id", nullable = false)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Column(name = "f_type", nullable = false, length = 100)
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "f_name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "f_number", length = 100)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "f_seq", nullable = false)
    public Integer getSeq() {
        return this.seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

}
