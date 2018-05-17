package com.jspxcms.core.domain;

import com.google.common.base.Objects;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.util.Reflections;
import com.jspxcms.common.util.Strings;
import com.jspxcms.common.web.Anchor;
import com.jspxcms.common.web.ImageAnchor;
import com.jspxcms.common.web.ImageAnchorBean;
import com.jspxcms.common.web.PageUrlResolver;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.InfoOrg.InfoOrgComparator;
import com.jspxcms.core.support.Commentable;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.Siteable;
import com.jspxcms.core.support.TitleText;
import com.jspxcms.ext.domain.InfoFavorite;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.Type;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jspxcms.core.constant.Constants.DYNAMIC_SUFFIX;
import static com.jspxcms.core.constant.Constants.SITE_PREFIX;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Info
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_info")
public class Info implements java.io.Serializable, Anchor, Siteable, Commentable, PageUrlResolver {
    private static final long serialVersionUID = 1L;
    /**
     * 附件类型
     */
    public static final String ATTACH_TYPE = "info";
    /**
     * 评论类型
     */
    public static final String COMMENT_TYPE = "Info";
    /**
     * 评分标记
     */
    public static final String SCORE_MARK = "InfoScore";
    /**
     * Digg标记
     */
    public static final String DIGG_MARK = "InfoDigg";
    /**
     * 模型类型
     */
    public static final String MODEL_TYPE = "info";
    /**
     * 工作流类型
     */
    public static final int WORKFLOW_TYPE = 1;
    /**
     * 审核中
     */
    public static final String AUDITING = "1";
    /**
     * 正常
     */
    public static final String NORMAL = "A";
    /**
     * 草稿
     */
    public static final String DRAFT = "B";
    /**
     * 投稿
     */
    public static final String CONTRIBUTION = "C";
    /**
     * 退稿
     */
    public static final String REJECTION = "D";
    /**
     * 采集
     */
    public static final String COLLECTED = "E";
    /**
     * 待发布
     */
    public static final String TOBE_PUBLISH = "F";
    /**
     * 已过期
     */
    public static final String EXPIRED = "G";
    /**
     * 推送
     */
    public static final String PUSH = "H";
    /**
     * 删除
     */
    public static final String DELETED = "X";
    /**
     * 归档
     */
    public static final String ARCHIVE = "Z";

    /**
     * 替换标识:栏目ID
     */
    public static final String PATH_NODE_ID = "{node_id}";
    /**
     * 替换标识:栏目编码
     */
    public static final String PATH_NODE_NUMBER = "{node_number}";
    /**
     * 替换标识:内容ID
     */
    public static final String PATH_INFO_ID = "{info_id}";
    /**
     * 替换标识:年
     */
    public static final String PATH_YEAR = "{year}";
    /**
     * 替换标识:月
     */
    public static final String PATH_MONTH = "{month}";
    /**
     * 替换标识:日
     */
    public static final String PATH_DAY = "{day}";

    /**
     * 信息正文KEY
     */
    public static final String INFO_TEXT = "text";
    /**
     * 分页标签
     */
    public static final String PAGEBREAK_OPEN = "[PageBreak]";
    public static final String PAGEBREAK_CLOSE = "[/PageBreak]";

    public static final Pattern PAGEBREAK_PATTERN = Pattern
            .compile("([\\s\\S]*?)(<div>\\s*?|<p>\\s*?)?\\[PageBreak\\]([\\s\\S]*?)\\[/PageBreak\\](\\s*?</div>|\\s*?</p>)?");

    /**
     * 获取没有分页信息的正文
     *
     * @return
     */
    @Transient
    public static String getTextWithoutPageBreak(String text) {
        if (text == null) {
            return text;
        }
        // 末尾加上分页，便于处理
        text += PAGEBREAK_OPEN + PAGEBREAK_CLOSE;
        StringBuilder sb = new StringBuilder();
        Matcher m = PAGEBREAK_PATTERN.matcher(text);
        while (m.find()) {
            sb.append(m.group(1));
        }
        return sb.toString();
    }

    @Transient
    public static String getDescription(Map<String, String> map, String title) {
        String html = map != null ? map.get(INFO_TEXT) : null;
        String text = getTextWithoutPageBreak(html);
        String desciption = Strings.getTextFromHtml(text, 450);
        return StringUtils.isNotBlank(desciption) ? desciption : title;
    }

    /**
     * 获取正文中第一张图片
     *
     * @param map
     * @return
     */
    @Transient
    public static List<String> getTextImages(Map<String, String> map) {
        List<String> textImages = new ArrayList<String>();
        String html = map != null ? map.get(INFO_TEXT) : null;
        if (StringUtils.isBlank(html)) {
            return textImages;
        }
        try {
            Parser parser = new Parser(new Lexer(html));
            NodeFilter filter = new TagNameFilter("img");
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
            SimpleNodeIterator it = nodes.elements();
            while (it.hasMoreNodes()) {
                ImageTag tag = (ImageTag) it.nextNode();
                String textImage = tag.getAttribute("src");
                if (StringUtils.isNotBlank(textImage)) {
                    textImages.add(textImage);
                }
            }
        } catch (ParserException e) {
            // 忽略
        }
        return textImages;
    }

    /**
     * 获得分页的InfoText(标题、正文)列表
     *
     * @return
     */
    @Transient
    public List<TitleText> getTextList() {
        List<TitleText> list = new ArrayList<TitleText>();
        String text = getText();
        String title = getFullTitleOrTitle();
        if (text != null) {
            text += PAGEBREAK_OPEN + PAGEBREAK_CLOSE;
            Matcher m = PAGEBREAK_PATTERN.matcher(text);
            while (m.find()) {
                list.add(new TitleText(title, m.group(1)));
                String t = m.group(3);
                if (StringUtils.isBlank(t)) {
                    t = title;
                }
            }
        } else {
            list.add(new TitleText(title, ""));
        }
        return list;
    }

    /**
     * 获取没有分页信息的正文
     *
     * @return
     */
    @Transient
    public String getTextWithoutPageBreak() {
        String text = getText();
        return getTextWithoutPageBreak(text);
    }

    /**
     * 获取没有html的正文
     *
     * @return
     */
    @Transient
    public String getPlainText() {
        String text = getTextWithoutPageBreak();
        return Strings.getTextFromHtml(text);
    }

    @Transient
    public String getDescription() {
        String desciption = getMetaDescription();
        if (StringUtils.isBlank(desciption)) {
            String text = getTextWithoutPageBreak();
            desciption = Strings.getTextFromHtml(text, 450);
            if (StringUtils.isBlank(desciption)) {
                desciption = getTitle();
            }
            desciption = StringUtils.trim(desciption);
            return desciption;
        } else {
            return desciption;
        }
    }

    @Transient
    public void adjustStatus() {
        String status = getStatus();
        if (NORMAL.equals(status) || TOBE_PUBLISH.equals(status) || EXPIRED.equals(status)) {
            if (isBeforeOnline()) {
                setStatus(TOBE_PUBLISH);
            } else if (isAfterOnline()) {
                setStatus(EXPIRED);
            } else {
                setStatus(NORMAL);
            }
        }
    }

    @Transient
    public void updateHtmlStatus() {
        if (getGenerate()) {
            if (isNormal()) {
                if (StringUtils.isNotBlank(getHtml())) {
                    setHtmlStatus(Node.HTML_TOBE_UPDATE);
                } else {
                    setHtmlStatus(Node.HTML_TOBE_GENERATE);
                }
            } else {
                setHtmlStatus(Node.HTML_TOBE_PUBLISH);
            }
        } else {
            if (StringUtils.isNotBlank(getHtml())) {
                setHtmlStatus(Node.HTML_TOBE_DELETE);
            } else {
                setHtmlStatus(Node.HTML_DISABLED);
            }
        }
    }

    @Transient
    public boolean isOnline() {
        long now = System.currentTimeMillis();
        long begin = getPublishDate().getTime();
        long end = Long.MAX_VALUE;
        if (getOffDate() != null) {
            end = getOffDate().getTime();
        }
        return begin <= now && now <= end;
    }

    @Transient
    public boolean isBeforeOnline() {
        long now = System.currentTimeMillis();
        long begin = getPublishDate().getTime();
        return now < begin;
    }

    @Transient
    public boolean isAfterOnline() {
        if (getOffDate() != null) {
            long now = System.currentTimeMillis();
            long end = getOffDate().getTime();
            return end < now;
        } else {
            return false;
        }
    }

    @Transient
    public boolean isNormal() {
        return getStatus() != null ? getStatus().equals(NORMAL) : false;
    }

    @Transient
    public boolean isDraft() {
        return getStatus() != null ? getStatus().equals(DRAFT) : false;
    }

    @Transient
    public boolean isAuditing() {
        return getStatus() != null ? getStatus().equals(AUDITING) : false;
    }

    @Transient
    public boolean isContribute() {
        return getStatus() != null ? getStatus().equals(CONTRIBUTION) : false;
    }

    @Transient
    public boolean isCollected() {
        return getStatus() != null ? getStatus().equals(COLLECTED) : false;
    }

    @Transient
    public boolean isRejection() {
        return getStatus() != null ? getStatus().equals(REJECTION) : false;
    }

    @Transient
    public boolean isArchive() {
        return getStatus() != null ? getStatus().equals(ARCHIVE) : false;
    }

    @Transient
    public boolean isDeleted() {
        return getStatus() != null ? getStatus().equals(DELETED) : false;
    }

    @Transient
    public boolean isDataPerm(User user) {
        Integer siteId = getSite().getId();
        Node node = getNode();
        if (user.getAllInfoPerm(siteId) || Reflections.contains(user.getInfoPerms(siteId), node.getId(), "id")) {
            Integer permType = user.getInfoPermType(siteId);
            if (permType == Role.INFO_PERM_SELF) {
                Integer creatorId = getCreator().getId();
                Integer userId = user.getId();
                if (creatorId.equals(userId)) {
                    return true;
                }
            } else if (permType == Role.INFO_PERM_ORG) {
                Integer orgId = getOrg().getId();
                if (user.getOrg().getId().equals(orgId)) {
                    return true;
                }
            } else if (permType == Role.INFO_PERM_ALL) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断已终审、审核中的信息是审核权限
     *
     * @param user
     * @return
     */
    @Transient
    public boolean isAuditPerm(User user) {
        if (!isNormal() && !isAuditing()) {
            // 状态不属于已终审、审核中的，都有权限
            return true;
        }
        if (user.getInfoFinalPerm(getSite().getId())) {
            // 拥有终审权
            return true;
        }
        Workflow workflow = getNode().getWorkflow();
        WorkflowProcess process = getProcess();
        if (process != null && !process.getEnd()) {
            // 如果流程存在且未结束，以流程的工作流为准。
            workflow = process.getWorkflow();
        }
        if (workflow == null) {
            // 工作流不存在，有权限。
            return true;
        }
        List<WorkflowStep> steps = workflow.getSteps();
        int size = steps.size();
        if (size == 0) {
            // 流程步骤为空，有权限。
            return true;
        }
        Collection<Role> userRoles = user.getRoles();
        Collection<Role> stepRoles;
        WorkflowStep step;
        if (isNormal()) {
            step = steps.get(size - 1);
            stepRoles = step.getRoles();
            return Reflections.containsAny(stepRoles, userRoles, "id");
        } else if (isAuditing()) {
            Integer currStepId = null;
            if (process != null && !process.getEnd() && process.getStep() != null) {
                currStepId = process.getStep().getId();
            }
            for (int i = size - 1; i >= 0; i--) {
                step = steps.get(i);
                stepRoles = step.getRoles();
                if (Reflections.containsAny(stepRoles, userRoles, "id")) {
                    return true;
                }
                if (step.getId().equals(currStepId)) {
                    return false;
                }
            }
            return false;
        } else {
            // never
            throw new IllegalStateException("not normal or auditing!");
        }
    }

    /**
     * 判断已终审、审核中的信息是审核权限。从线程变量中获取user
     *
     * @return
     */
    @Transient
    public boolean isAuditPerm() {
        User user = Context.getCurrentUser();
        if (user != null) {
            return isAuditPerm(user);
        } else {
            return false;
        }
    }

    @Transient
    public boolean isViewPerm(Collection<MemberGroup> groups, Collection<Org> orgs) {
        if (getNode().isViewPerm(groups, orgs)) {
            return true;
        }
        if (Reflections.containsAny(getViewGroups(), groups, "id")) {
            return true;
        }
        if (CollectionUtils.isNotEmpty(orgs) && Reflections.containsAny(getViewOrgs(), orgs, "id")) {
            return true;
        }
        return false;
    }

    @Transient
    public boolean isFavorite(User user) {
        if (user == null) {
            return false;
        }
        for (InfoFavorite bean : getInfoFavorites()) {
            if (bean.getUser().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得标题列表
     *
     * @return
     */
    @Transient
    public List<String> getTitleList() {
        List<TitleText> textList = getTextList();
        List<String> titleList = new ArrayList<String>(textList.size());
        for (TitleText it : textList) {
            titleList.add(it.getTitle());
        }
        return titleList;
    }

    @Transient
    public String getUrl() {
        return getUrl(1);
    }

    @Transient
    public String getUrl(Integer page) {
        return getGenerate() ? getUrlStatic(page) : getUrlDynamic(page);
    }


    @Transient
    public String getUrlMobile() {
        boolean isFull = getSite().getIdentifyDomain();
        return getUrlMobile(isFull);
    }

    @Transient
    public String getUrlMobile(boolean isFull) {
        return getGenerate() ? getUrlStatic(1, isFull, false, true) : getUrlDynamic(1, isFull, true);
    }

    @Transient
    public String getUrlFull() {
        return getUrlFull(1);
    }

    @Transient
    public String getUrlFull(Integer page) {
        return getGenerate() ? getUrlStaticFull(page) : getUrlDynamicFull(page);
    }

    @Transient
    public String getUrlDynamic() {
        return getUrlDynamic(1);
    }

    @Transient
    public String getUrlDynamic(Integer page) {
        boolean isFull = getSite().getIdentifyDomain();
        return getUrlDynamic(page, isFull, Context.isMobile());
    }

    @Transient
    public String getUrlDynamicFull() {
        return getUrlDynamicFull(1);
    }

    @Transient
    public String getUrlDynamicFull(Integer page) {
        return getUrlDynamic(page, true, Context.isMobile());
    }

    @Transient
    public String getUrlDynamic(Integer page, boolean isFull, boolean isMobile) {
        if (isLinked()) {
            return getLinkUrl();
        }
        Site site = getSite();
        StringBuilder sb = new StringBuilder();
        if (isFull) {
            sb.append(site.getProtocol()).append(":");
            if (isMobile) {
                sb.append("//").append(site.getMobileDomain());
            } else {
                sb.append("//").append(site.getDomain());
            }
            if (site.getPort() != null) {
                sb.append(":").append(site.getPort());
            }
        }
        String ctx = site.getContextPath();
        if (StringUtils.isNotBlank(ctx)) {
            sb.append(ctx);
        }
        boolean sitePrefix = !site.getIdentifyDomain() && !site.isDef();
        if (sitePrefix) {
            sb.append(SITE_PREFIX).append(site.getNumber());
        }
        sb.append("/").append(Constants.INFO_PATH);
        sb.append("/").append(getId());
        if (page != null && page > 1) {
            sb.append("_").append(page);
        }
        sb.append(DYNAMIC_SUFFIX);
        return sb.toString();
    }

    @Transient
    public String getUrlStatic() {
        return getUrlStatic(1);
    }

    @Transient
    public String getUrlStatic(Integer page) {
        Site site = getSite();
        boolean isFull = site.getIdentifyDomain();
        return getUrlStatic(page, isFull, false, Context.isMobile());
    }

    @Transient
    public String getUrlStaticFull() {
        return getUrlStaticFull(1);
    }

    @Transient
    public String getUrlStaticFull(Integer page) {
        return getUrlStatic(page, true, false, Context.isMobile());
    }

    @Transient
    public String getUrlStatic(Integer page, boolean isFull, boolean forRealPath, boolean isMobile) {
        if (isLinked()) {
            return getLinkUrl();
        }
        Node node = getNode();
        String path = node.getInfoPathOrDef();
        Calendar now = Calendar.getInstance();
        now.setTime(getPublishDate());
        String year = String.valueOf(now.get(Calendar.YEAR));
        int m = now.get(Calendar.MONTH) + 1;
        int d = now.get(Calendar.DAY_OF_MONTH);
        String month = StringUtils.leftPad(String.valueOf(m), 2, '0');
        String day = StringUtils.leftPad(String.valueOf(d), 2, '0');
        path = StringUtils.replace(path, PATH_NODE_ID, node.getId().toString());
        String nodeNumber = node.getNumber();
        if (StringUtils.isBlank(nodeNumber)) {
            nodeNumber = node.getId().toString();
        }
        path = StringUtils.replace(path, PATH_NODE_NUMBER, nodeNumber);
        path = StringUtils.replace(path, PATH_YEAR, year);
        path = StringUtils.replace(path, PATH_MONTH, month);
        path = StringUtils.replace(path, PATH_DAY, day);
        if (StringUtils.isNotBlank(getInfoPath())) {
            path = StringUtils.replace(path, PATH_INFO_ID, getInfoPath());
        } else {
            path = StringUtils.replace(path, PATH_INFO_ID, getId().toString());
        }
        if (page != null && page > 1) {
            path += "_" + page;
        }
        String extension = node.getInfoExtensionOrDef();
        if (StringUtils.isNotBlank(extension)) {
            path += extension;
        }

        StringBuilder sb = new StringBuilder();
        Site site = getSite();
        if (isFull && !forRealPath) {
            String domain = isMobile ? site.getMobileDomain() : site.getDomain();
            sb.append(site.getProtocol()).append("://").append(domain);
            if (site.getPort() != null) {
                sb.append(":").append(site.getPort());
            }
        }
        if (!forRealPath) {
            PublishPoint point = isMobile ? getSite().getMobilePublishPoint() : getSite()
                    .getHtmlPublishPoint();
            String urlPrefix = point.getUrlPrefix();
            if (StringUtils.isNotBlank(urlPrefix)) {
                sb.append(urlPrefix);
            }
        }
        sb.append(path);
        return sb.toString();
    }

    @Transient
    public String getPageUrl(Integer page) {
        return getUrl(page);
    }

    @Transient
    public void addComments(int comments) {
        Set<InfoBuffer> buffers = getBuffers();
        if (buffers == null) {
            buffers = new HashSet<InfoBuffer>(1);
            setBuffers(buffers);
        }
        InfoBuffer buffer;
        if (buffers.isEmpty()) {
            buffer = new InfoBuffer();
            buffer.applyDefaultValue();
            buffer.setInfo(this);
            buffers.add(buffer);
        } else {
            buffer = buffers.iterator().next();
        }
        // 根据设置处理缓冲。
        Integer origComments = getComments();
        Integer bufferComments = buffer.getComments() + comments;
        if (bufferComments >= getSite().getGlobal().getOther().getBufferInfoComments()) {
            buffer.setComments(0);
            setComments(origComments + bufferComments);
        } else {
            buffer.setComments(bufferComments);
        }
    }

    /**
     * 获得评论状态
     */
    @Transient
    public int getCommentStatus(User user, Collection<MemberGroup> groups) {
        SiteComment conf = getSite().getConf(SiteComment.class);
        int mode = conf.getMode();
        if (mode == SiteComment.MODE_OFF || !isNormal() || isLinked()) {
            return SiteComment.STATUS_OFF;
        }
        Boolean allow = getAllowComment();
        if (allow != null && !allow) {
            return SiteComment.STATUS_OFF;
        }
        if (user == null && mode == SiteComment.MODE_USER) {
            return SiteComment.STATUS_LOGIN;
        }
        if (allow != null && allow) {
            return SiteComment.STATUS_ALLOWED;
        }
        Set<MemberGroup> commentGroups = getNode().getCommentGroups();
        if (Reflections.containsAny(commentGroups, groups, "id")) {
            return SiteComment.STATUS_ALLOWED;
        } else {
            if (user != null) {
                return SiteComment.STATUS_DENIED;
            } else {
                return SiteComment.STATUS_LOGIN;
            }
        }
    }

    @Transient
    public boolean allowComment(Collection<MemberGroup> groups) {
        // 非已审核状态或者是转向链接，不允许评论。
        if (!isNormal() || isLinked()) {
            return false;
        }
        Boolean allow = getAllowComment();
        // 信息单独设置是否可以评论
        if (allow != null) {
            return allow;
        }
        Set<MemberGroup> commentGroups = getNode().getCommentGroups();
        return Reflections.containsAny(commentGroups, groups, "id");
    }

    @Transient
    public Boolean getGenerate() {
        if (isLinked()) {
            // 转向链接不需要生成
            return false;
        }
        Node node = getNode();
        return node != null ? node.getGenerateInfoOrDef() : null;
    }

    @Transient
    public String getTemplate() {
        String infoTemplate = getInfoTemplate();
        if (infoTemplate != null) {
            infoTemplate = getSite().getTemplate(infoTemplate);
        } else {
            Node node = getNode();
            if (node != null) {
                infoTemplate = node.getInfoTemplateOrDef();
            }
        }
        return infoTemplate;
    }

    @Transient
    public String getTagKeywords() {
        List<InfoTag> infoTags = getInfoTags();
        if (infoTags != null) {
            StringBuilder keywordsBuff = new StringBuilder();
            for (InfoTag infoTag : infoTags) {
                keywordsBuff.append(infoTag.getTag().getName()).append(',');
            }
            if (keywordsBuff.length() > 0) {
                keywordsBuff.setLength(keywordsBuff.length() - 1);
            }
            return keywordsBuff.toString();
        } else {
            return null;
        }
    }

    @Transient
    public String getKeywords() {
        String keywords = getTagKeywords();
        if (StringUtils.isBlank(keywords)) {
            return getTitle();
        } else {
            return keywords;
        }
    }

    @Transient
    public List<Node> getNodes() {
        List<InfoNode> infoNodes = getInfoNodes();
        int len = CollectionUtils.size(infoNodes);
        List<Node> nodes = new ArrayList<Node>(len);
        if (len > 0) {
            for (InfoNode infoNode : infoNodes) {
                nodes.add(infoNode.getNode());
            }
        }
        return nodes;
    }

    @Transient
    public List<Tag> getTags() {
        List<InfoTag> infoTags = getInfoTags();
        if (infoTags == null) {
            return null;
        }
        List<Tag> tags = new ArrayList<Tag>(infoTags.size());
        for (InfoTag infoTag : infoTags) {
            tags.add(infoTag.getTag());
        }
        return tags;
    }

    @Transient
    public List<Special> getSpecials() {
        List<InfoSpecial> infoSpecials = getInfoSpecials();
        if (infoSpecials == null) {
            return null;
        }
        List<Special> specials = new ArrayList<Special>(infoSpecials.size());
        for (InfoSpecial infoSpecial : infoSpecials) {
            specials.add(infoSpecial.getSpecial());
        }
        return specials;
    }

    @Transient
    public Set<MemberGroup> getViewGroups() {
        Set<InfoMemberGroup> infoGroups = getInfoGroups();
        if (infoGroups == null) {
            return null;
        }
        Set<MemberGroup> groups = new HashSet<MemberGroup>();
        for (InfoMemberGroup ig : infoGroups) {
            if (ig.getViewPerm()) {
                groups.add(ig.getGroup());
            }
        }
        return groups;
    }

    @Transient
    public List<Org> getViewOrgs() {
        Set<InfoOrg> ios = getInfoOrgs();
        if (ios == null) {
            return null;
        }
        List<Org> orgs = new ArrayList<Org>(ios.size());
        for (InfoOrg io : ios) {
            if (io.getViewPerm()) {
                orgs.add(io.getOrg());
            }
        }
        return orgs;
    }

    @Transient
    public Boolean hasAttr(String attrNumber) {
        List<InfoAttribute> infoAttrs = getInfoAttrs();
        if (infoAttrs == null) {
            return null;
        }
        for (InfoAttribute ia : infoAttrs) {
            if (ia.getAttribute().getNumber().equals(attrNumber)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public Attribute getAttr(String attrNumber) {
        List<InfoAttribute> infoAttrs = getInfoAttrs();
        if (infoAttrs == null) {
            return null;
        }
        for (InfoAttribute ia : infoAttrs) {
            if (ia.getAttribute().getNumber().equals(attrNumber)) {
                return ia.getAttribute();
            }
        }
        return null;
    }

    @Transient
    public List<Attribute> getAttrs() {
        List<InfoAttribute> infoAttrs = getInfoAttrs();
        if (infoAttrs == null) {
            return null;
        }
        List<Attribute> attrs = new ArrayList<Attribute>(infoAttrs.size());
        for (InfoAttribute infoAttr : infoAttrs) {
            attrs.add(infoAttr.getAttribute());
        }
        return attrs;
    }

    @Transient
    public InfoAttribute getInfoAttr(Attribute attr) {
        Collection<InfoAttribute> infoAttrs = getInfoAttrs();
        if (!CollectionUtils.isEmpty(infoAttrs)) {
            for (InfoAttribute infoAttr : infoAttrs) {
                if (infoAttr.getAttribute().equals(attr)) {
                    return infoAttr;
                }
            }
        }
        return null;
    }

    @Transient
    public InfoAttribute getInfoAttr(Integer attrId) {
        Collection<InfoAttribute> infoAttrs = getInfoAttrs();
        if (!CollectionUtils.isEmpty(infoAttrs)) {
            for (InfoAttribute infoAttr : infoAttrs) {
                if (infoAttr.getAttribute().getId().equals(attrId)) {
                    return infoAttr;
                }
            }
        }
        return null;
    }

    @Transient
    public InfoAttribute getInfoAttr(String attrName) {
        Collection<InfoAttribute> infoAttrs = getInfoAttrs();
        if (!CollectionUtils.isEmpty(infoAttrs)) {
            for (InfoAttribute infoAttr : infoAttrs) {
                if (infoAttr.getAttribute().getName().equals(attrName)) {
                    return infoAttr;
                }
            }
        }
        return null;
    }

    @Transient
    public String getAttrImage(String attr) {
        Collection<InfoAttribute> infoAttrs = getInfoAttrs();
        for (InfoAttribute infoAttr : infoAttrs) {
            if (infoAttr.getAttribute().getNumber().equals(attr)) {
                return infoAttr.getImage();
            }
        }
        return null;
    }

    @Transient
    public String getAttrImageUrl(String attr) {
        String url = getAttrImage(attr);
        return StringUtils.isBlank(url) ? getSite().getNoPictureUrl() : url;
    }

    @Transient
    public String getAttrImage(Integer attrId) {
        Collection<InfoAttribute> infoAttrs = getInfoAttrs();
        Attribute attr;
        for (InfoAttribute infoAttr : infoAttrs) {
            attr = infoAttr.getAttribute();
            if ((attrId == null && attr.getWithImage()) || attr.getId().equals(attrId)) {
                return infoAttr.getImage();
            }
        }
        return null;
    }

    @Transient
    public String getAttrImageUrl(Integer attrId) {
        String url = getAttrImage(attrId);
        return StringUtils.isBlank(url) ? getSite().getNoPictureUrl() : url;
    }

    @Transient
    public String getAttrImage() {
        String attrImage;
        String attrName = getAttrName();
        if (StringUtils.isNotBlank(attrName)) {
            attrImage = getAttrImage(attrName);
        } else {
            Integer attrId = getAttrId();
            attrImage = getAttrImage(attrId);
        }
        return attrImage;
    }

    @Transient
    public String getAttrImageUrl() {
        String url = getAttrImage();
        return StringUtils.isBlank(url) ? getSite().getNoPictureUrl() : url;
    }

    @Transient
    public ImageAnchor getAttrImageBean() {
        InfoAttribute infoAttr;
        String attrName = getAttrName();
        if (StringUtils.isNotBlank(attrName)) {
            infoAttr = getInfoAttr(attrName);
        } else {
            Integer attrId = getAttrId();
            infoAttr = getInfoAttr(attrId);
        }
        ImageAnchorBean bean = new ImageAnchorBean();
        bean.setTitle(getTitle());
        String url = getUrl();
        bean.setUrl(url);
        String noPictrue = getSite().getNoPictureUrl();
        String src = getAttrImageUrl();
        src = StringUtils.isBlank(src) ? noPictrue : src;
        bean.setSrc(src);
        if (infoAttr != null) {
            Attribute attr = infoAttr.getAttribute();
            bean.setWidth(attr.getImageWidth());
            bean.setHeight(attr.getImageHeight());
        }
        return bean;
    }

    @Transient
    public String getText() {
        Map<String, String> clobs = getClobs();
        return clobs != null ? clobs.get(INFO_TEXT) : null;
    }

    @Transient
    public void setText(String text) {
        Map<String, String> clobs = getClobs();
        if (clobs == null) {
            clobs = new HashMap<String, String>();
            setClobs(clobs);
        }
        clobs.put(INFO_TEXT, text);
    }

    @Transient
    public Model getModel() {
        return getNode() != null ? getNode().getInfoModel() : null;
    }

    @Transient
    public Workflow getWorkflow() {
        return getNode() != null ? getNode().getWorkflow() : null;
    }

    /**
     * 标题
     */
    @Transient
    public String getTitle() {
        return getDetail() != null ? getDetail().getTitle() : null;
    }

    @Transient
    public String getSubtitle() {
        return getDetail() != null ? getDetail().getSubtitle() : null;
    }

    @Transient
    public String getFullTitle() {
        return getDetail() != null ? getDetail().getFullTitle() : null;
    }

    @Transient
    public String getFullTitleOrTitle() {
        String fullTitle = getFullTitle();
        return StringUtils.isNotBlank(fullTitle) ? fullTitle : getTitle();
    }

    @Transient
    public String getLink() {
        return getDetail() != null ? getDetail().getLink() : null;
    }

    /**
     * 获得加上上下文路径的外部链接地址。
     *
     * @return
     */
    @Transient
    public String getLinkUrl() {
        String link = getLink();
        if (StringUtils.isBlank(link)) {
            return link;
        }
        if (link.startsWith("/") && !link.startsWith("//")) {
            StringBuilder sb = new StringBuilder();
            Site site = getSite();
            if (site.getIdentifyDomain()) {
                sb.append(site.getProtocol()).append("://").append(site.getDomain());
                if (site.getPort() != null) {
                    sb.append(":").append(site.getPort());
                }
            }
            String ctx = site.getContextPath();
            if (StringUtils.isNotBlank(ctx)) {
                sb.append(ctx);
            }
            sb.append(link);
            link = sb.toString();
        }
        return link;
    }

    /**
     * 是否是转向链接
     *
     * @return
     */
    @Transient
    public boolean isLinked() {
        return StringUtils.isNotBlank(getLink());
    }

    /**
     * 是否是最近一天的文档
     *
     * @return
     */
    @Transient
    public boolean isNew() {
        return isNew(1);
    }

    @Transient
    public Set<String> getAttachUrls() {
        Set<String> urls = new HashSet<String>();
        // 取InfoDetail字段
        urls.add(getSmallImage());
        urls.add(getLargeImage());
        urls.add(getVideo());
        urls.add(getFile());
        urls.add(getDoc());
        urls.add(getDocPdf());
        urls.add(getDocSwf());
        // 属性图
        for (InfoAttribute infoAttr : getInfoAttrs()) {
            urls.add(infoAttr.getImage());
        }
        // 图片集
        for (InfoImage infoImage : getImages()) {
            urls.add(infoImage.getImage());
            urls.add(infoImage.getImageMin());
        }
        // 文件集
        for (InfoFile infoFile : getFiles()) {
            urls.add(infoFile.getFile());
        }
        // 自定义字段
        Set<String> clobEditorNames = new HashSet<String>();
        clobEditorNames.add(INFO_TEXT);
        Map<String, String> customs = getCustoms();
        Map<String, String> clobs = getClobs();
        getModel().getAttachUrls(urls, clobEditorNames, clobs, customs);
        return urls;
    }

    /**
     * 是否是最近的文档
     *
     * @param day
     * @return
     */
    @Transient
    public boolean isNew(int day) {
        Date date = getPublishDate();
        return System.currentTimeMillis() - date.getTime() < 3600000 * 24 * day;
    }

    @Transient
    public String getHtml() {
        return getDetail() != null ? getDetail().getHtml() : null;
    }

    @Transient
    public String getMobileHtml() {
        return getDetail() != null ? getDetail().getMobileHtml() : null;
    }

    @Transient
    public Boolean getNewWindow() {
        return getDetail() != null ? getDetail().getNewWindow() : null;
    }

    @Transient
    public String getColor() {
        return getDetail() != null ? getDetail().getColor() : null;
    }

    @Transient
    public Boolean getStrong() {
        return getDetail() != null ? getDetail().getStrong() : null;
    }

    @Transient
    public Boolean getEm() {
        return getDetail() != null ? getDetail().getEm() : null;
    }

    @Transient
    public String getMetaDescription() {
        return getDetail() != null ? getDetail().getMetaDescription() : null;
    }

    @Transient
    public String getInfoPath() {
        return getDetail() != null ? getDetail().getInfoPath() : null;
    }

    @Transient
    public String getInfoTemplate() {
        return getDetail() != null ? getDetail().getInfoTemplate() : null;
    }

    /**
     * 来源
     *
     * @return
     */
    @Transient
    public String getSource() {
        return getDetail() != null ? getDetail().getSource() : null;
    }

    /**
     * 来源url
     *
     * @return
     */
    @Transient
    public String getSourceUrl() {
        return getDetail() != null ? getDetail().getSourceUrl() : null;
    }

    /**
     * 作者
     *
     * @return
     */
    @Transient
    public String getAuthor() {
        return getDetail() != null ? getDetail().getAuthor() : null;
    }

    @Transient
    public String getSmallImage() {
        return getDetail() != null ? getDetail().getSmallImage() : null;
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
        Model model = getModel();
        if (model == null) {
            return bean;
        }
        ModelField mf = model.getField("smallImage");
        if (mf == null) {
            return bean;
        }
        Map<String, String> map = mf.getCustoms();
        String width = map.get(ModelField.IMAGE_WIDTH);
        if (StringUtils.isNotBlank(width)) {
            bean.setWidth(NumberUtils.createInteger(width));
        }
        String height = map.get(ModelField.IMAGE_HEIGHT);
        if (StringUtils.isNotBlank(height)) {
            bean.setHeight(NumberUtils.createInteger(height));
        }
        return bean;
    }

    @Transient
    public String getLargeImage() {
        return getDetail() != null ? getDetail().getLargeImage() : null;
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
        Model model = getModel();
        if (model == null) {
            return bean;
        }
        ModelField mf = model.getField("largeImage");
        if (mf == null) {
            return bean;
        }
        Map<String, String> map = mf.getCustoms();
        String width = map.get(ModelField.IMAGE_WIDTH);
        if (StringUtils.isNotBlank(width)) {
            bean.setWidth(NumberUtils.createInteger(width));
        }
        String height = map.get(ModelField.IMAGE_HEIGHT);
        if (StringUtils.isNotBlank(height)) {
            bean.setHeight(NumberUtils.createInteger(height));
        }
        return bean;
    }

    @Transient
    public String getVideo() {
        return getDetail() != null ? getDetail().getVideo() : null;
    }

    @Transient
    public String getVideoName() {
        return getDetail() != null ? getDetail().getVideoName() : null;
    }

    @Transient
    public Long getVideoLength() {
        return getDetail() != null ? getDetail().getVideoLength() : null;
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
    public String getVideoTime() {
        return getDetail() != null ? getDetail().getVideoTime() : null;
    }

    @Transient
    public String getFile() {
        return getDetail() != null ? getDetail().getFile() : null;
    }

    @Transient
    public String getFileName() {
        return getDetail() != null ? getDetail().getFileName() : null;
    }

    @Transient
    public Long getFileLength() {
        return getDetail() != null ? getDetail().getFileLength() : null;
    }

    @Transient
    public String getFileSize() {
        Long length = getFileLength();
        return FilesEx.getSize(length);
    }

    @Transient
    public String getFileExtension() {
        return FilenameUtils.getExtension(getFile());
    }

    @Transient
    public String getDoc() {
        return getDetail() != null ? getDetail().getDoc() : null;
    }

    @Transient
    public String getDocName() {
        return getDetail() != null ? getDetail().getDocName() : null;
    }

    @Transient
    public Long getDocLength() {
        return getDetail() != null ? getDetail().getDocLength() : null;
    }

    @Transient
    public String getDocPdf() {
        return getDetail() != null ? getDetail().getDocPdf() : null;
    }

    @Transient
    public String getDocSwf() {
        return getDetail() != null ? getDetail().getDocSwf() : null;
    }

    @Transient
    public Boolean getAllowComment() {
        return getDetail() != null ? getDetail().getAllowComment() : null;
    }

    @Transient
    public Boolean getWeixinMass() {
        return getDetail() != null ? getDetail().getWeixinMass() : null;
    }

    @Transient
    public String getStepName() {
        return getDetail() != null ? getDetail().getStepName() : null;
    }

    @Transient
    public List<Node> getNodesExcludeMain() {
        List<Node> nodes = getNodes();
        List<Node> list = new ArrayList<Node>();
        for (int i = 1, len = nodes.size(); i < len; i++) {
            list.add(nodes.get(i));
        }
        return list;
    }

    @Transient
    public Integer[] getNodeIdsExcludeMain() {
        List<Node> nodes = getNodes();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1, len = nodes.size(); i < len; i++) {
            list.add(nodes.get(i).getId());
        }
        return list.toArray(new Integer[list.size()]);
    }

    @Transient
    public InfoProcess getProcess() {
        Set<InfoProcess> processes = getProcesses();
        if (processes != null && !processes.isEmpty()) {
            return processes.iterator().next();
        } else {
            return null;
        }
    }

    @Transient
    public Integer getBufferViews() {
        InfoBuffer buffer = getBuffer();
        if (buffer != null) {
            return getViews() + buffer.getViews();
        } else {
            return getViews();
        }
    }

    @Transient
    public Integer getBufferDownloads() {
        InfoBuffer buffer = getBuffer();
        if (buffer != null) {
            return getDownloads() + buffer.getDownloads();
        } else {
            return getDownloads();
        }
    }

    @Transient
    public Integer getBufferComments() {
        InfoBuffer buffer = getBuffer();
        if (buffer != null) {
            return getComments() + buffer.getComments();
        } else {
            return getComments();
        }
    }

    @Transient
    public Integer getBufferInvolveds() {
        InfoBuffer buffer = getBuffer();
        if (buffer != null) {
            return buffer.getInvolveds();
        } else {
            return 0;
        }
    }

    @Transient
    public Integer getBufferDiggs() {
        InfoBuffer buffer = getBuffer();
        if (buffer != null) {
            return getDiggs() + buffer.getDiggs();
        } else {
            return getDiggs();
        }
    }

    @Transient
    public Integer getBufferBurys() {
        InfoBuffer buffer = getBuffer();
        if (buffer != null) {
            return buffer.getBurys();
        } else {
            return 0;
        }
    }

    @XmlTransient
    @Transient
    public InfoBuffer getBuffer() {
        Set<InfoBuffer> set = getBuffers();
        if (!CollectionUtils.isEmpty(set)) {
            return set.iterator().next();
        } else {
            return null;
        }
    }

    @Transient
    public void setBuffer(InfoBuffer buffer) {
        Set<InfoBuffer> set = getBuffers();
        if (set == null) {
            set = new HashSet<InfoBuffer>(1);
            setBuffers(set);
        } else {
            set.clear();
        }
        set.add(buffer);
    }

    /**
     * 页数线程变量
     */
    private static ThreadLocal<Integer> attrIdHolder = new ThreadLocal<Integer>();
    private static ThreadLocal<String> attrNameHolder = new ThreadLocal<String>();

    @Transient
    public static void setAttrId(Integer attrId) {
        attrIdHolder.set(attrId);
    }

    @Transient
    public static Integer getAttrId() {
        return attrIdHolder.get();
    }

    @Transient
    public static void resetAttrId() {
        attrIdHolder.remove();
    }

    @Transient
    public static void setAttrName(String attrName) {
        attrNameHolder.set(attrName);
    }

    @Transient
    public static String getAttrName() {
        return attrNameHolder.get();
    }

    @Transient
    public static void resetAttrName() {
        attrNameHolder.remove();
    }

    /**
     * 获取自定义字段选项的value值
     *
     * @param name 自定义字段名称
     * @return option的value
     */
    @Transient
    public String getCustomsValue(String name) {
        String key = getCustoms().get(name);
        return getModel().getCustomOptionValue(name, key);
    }

    /**
     * 获取查询字段选项p0的value值
     *
     * @return option的value
     */
    @Transient
    public String getP0Value() {
        if (getP0() == null) {
            return null;
        }
        String key = String.valueOf(getP0());
        return getModel().getQueryableOptionValue("p0", key);
    }

    /**
     * 获取查询字段选项p1的value值
     *
     * @return option的value
     */
    @Transient
    public String getP1Value() {
        if (getP1() == null) {
            return null;
        }
        String key = String.valueOf(getP1());
        return getModel().getQueryableOptionValue("p1", key);
    }

    /**
     * 获取查询字段选项p2的value值
     *
     * @return option的value
     */
    @Transient
    public String getP2Value() {
        if (getP2() == null) {
            return null;
        }
        String key = String.valueOf(getP2());
        return getModel().getQueryableOptionValue("p2", key);
    }

    /**
     * 获取查询字段选项p3的value值
     *
     * @return option的value
     */
    @Transient
    public String getP3Value() {
        if (getP3() == null) {
            return null;
        }
        String key = String.valueOf(getP3());
        return getModel().getQueryableOptionValue("p3", key);
    }

    /**
     * 获取查询字段选项p4的value值
     *
     * @return option的value
     */
    @Transient
    public String getP4Value() {
        if (getP4() == null) {
            return null;
        }
        String key = String.valueOf(getP4());
        return getModel().getQueryableOptionValue("p4", key);
    }

    /**
     * 获取查询字段选项p5的value值
     *
     * @return option的value
     */
    @Transient
    public String getP5Value() {
        if (getP5() == null) {
            return null;
        }
        String key = String.valueOf(getP5());
        return getModel().getQueryableOptionValue("p5", key);
    }

    /**
     * 获取查询字段选项p6的value值
     *
     * @return option的value
     */
    @Transient
    public String getP6Value() {
        if (getP6() == null) {
            return null;
        }
        String key = String.valueOf(getP6());
        return getModel().getQueryableOptionValue("p6", key);
    }

    @Transient
    public void applyDefaultValue() {
        if (getOrg() == null) {
            setOrg(getCreator().getOrg());
        }
        if (getViews() == null) {
            setViews(0);
        }
        if (getDownloads() == null) {
            setDownloads(0);
        }
        if (getComments() == null) {
            setComments(0);
        }
        if (getDiggs() == null) {
            setDiggs(0);
        }
        if (getScore() == null) {
            setScore(0);
        }
        if (getFavorites() == null) {
            setFavorites(0);
        }
        if (getWithImage() == null) {
            setWithImage(false);
        }
        if (getPriority() == null) {
            setPriority(0);
        }
        if (getPublishDate() == null) {
            setPublishDate(new Timestamp(System.currentTimeMillis()));
        }
        if (getStatus() == null) {
            setStatus(NORMAL);
        }
        if (getHtmlStatus() == null) {
            setHtmlStatus(Node.HTML_DISABLED);
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
        if (!(o instanceof Info)) {
            return false;
        }
        Info that = (Info) o;
        return Objects.equal(id, that.id);
    }

    private Integer id;
    private List<InfoNode> infoNodes = new ArrayList<InfoNode>(0);
    private List<InfoTag> infoTags = new ArrayList<InfoTag>(0);
    private List<InfoSpecial> infoSpecials = new ArrayList<InfoSpecial>(0);
    private List<InfoAttribute> infoAttrs = new ArrayList<InfoAttribute>(0);
    private List<InfoImage> images = new ArrayList<InfoImage>(0);
    private List<InfoFile> files = new ArrayList<InfoFile>(0);
    private Map<String, String> customs = new HashMap<String, String>(0);
    private Map<String, String> clobs = new HashMap<String, String>(0);
    private Set<InfoBuffer> buffers = new HashSet<InfoBuffer>(0);
    private Set<InfoMemberGroup> infoGroups = new HashSet<InfoMemberGroup>(0);
    private SortedSet<InfoOrg> infoOrgs = new TreeSet<InfoOrg>(new InfoOrgComparator());
    private Set<InfoProcess> processes = new HashSet<InfoProcess>(0);
    private List<InfoFavorite> infoFavorites = new ArrayList<InfoFavorite>(0);

    private Node node;
    private Org org;
    private User creator;
    private Site site;
    private Site fromSite;
    private InfoDetail detail;

    private Date publishDate;
    private Date offDate;
    private Integer priority;
    private Boolean withImage;
    private Integer views;
    private Integer downloads;
    private Integer comments;
    private Integer favorites;
    private Integer diggs;
    private Integer score;
    private String status;
    private String htmlStatus;
    private Integer p0;
    private Integer p1;
    private Integer p2;
    private Integer p3;
    private Integer p4;
    private Integer p5;
    private Integer p6;

    private String highlightTitle;
    private String highlightText;

    public Info() {
    }

    public Info(Node node, User creator, Site site) {
        this.node = node;
        this.creator = creator;
        this.site = site;
    }

    @XmlTransient
    @Id
    @Column(name = "f_info_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_info", pkColumnValue = "cms_info", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_info")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    @OrderColumn(name = "f_node_index")
    public List<InfoNode> getInfoNodes() {
        return infoNodes;
    }

    public void setInfoNodes(List<InfoNode> infoNodes) {
        this.infoNodes = infoNodes;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    @OrderColumn(name = "f_tag_index")
    public List<InfoTag> getInfoTags() {
        return infoTags;
    }

    public void setInfoTags(List<InfoTag> infoTags) {
        this.infoTags = infoTags;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    @OrderColumn(name = "f_special_index")
    public List<InfoSpecial> getInfoSpecials() {
        return infoSpecials;
    }

    public void setInfoSpecials(List<InfoSpecial> infoSpecials) {
        this.infoSpecials = infoSpecials;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    @OrderBy("attribute asc")
    public List<InfoAttribute> getInfoAttrs() {
        return infoAttrs;
    }

    public void setInfoAttrs(List<InfoAttribute> infoAttrs) {
        this.infoAttrs = infoAttrs;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    @OrderBy("created asc")
    public List<InfoFavorite> getInfoFavorites() {
        return infoFavorites;
    }

    public void setInfoFavorites(List<InfoFavorite> infoFavorites) {
        this.infoFavorites = infoFavorites;
    }

    @ElementCollection
    @CollectionTable(name = "cms_info_image", joinColumns = @JoinColumn(name = "f_info_id"))
    @OrderColumn(name = "f_index")
    public List<InfoImage> getImages() {
        return images;
    }

    public void setImages(List<InfoImage> images) {
        this.images = images;
    }

    @ElementCollection
    @CollectionTable(name = "cms_info_file", joinColumns = @JoinColumn(name = "f_info_id"))
    @OrderColumn(name = "f_index")
    public List<InfoFile> getFiles() {
        return files;
    }

    public void setFiles(List<InfoFile> files) {
        this.files = files;
    }

    @ElementCollection
    @CollectionTable(name = "cms_info_custom", joinColumns = @JoinColumn(name = "f_info_id"))
    @MapKeyColumn(name = "f_key", length = 50)
    @Column(name = "f_value", length = 2000)
    public Map<String, String> getCustoms() {
        return this.customs;
    }

    public void setCustoms(Map<String, String> customs) {
        this.customs = customs;
    }

    @ElementCollection
    @CollectionTable(name = "cms_info_clob", joinColumns = @JoinColumn(name = "f_info_id"))
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
    // 为了addComments的处理，级联加上PERSIST。
    @OneToMany(fetch = FetchType.LAZY, cascade = {PERSIST, REMOVE}, mappedBy = "info")
    public Set<InfoBuffer> getBuffers() {
        return buffers;
    }

    public void setBuffers(Set<InfoBuffer> buffers) {
        this.buffers = buffers;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    public Set<InfoMemberGroup> getInfoGroups() {
        return infoGroups;
    }

    public void setInfoGroups(Set<InfoMemberGroup> infoGroups) {
        this.infoGroups = infoGroups;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "info")
    @SortComparator(InfoOrgComparator.class)
    public SortedSet<InfoOrg> getInfoOrgs() {
        return infoOrgs;
    }

    public void setInfoOrgs(SortedSet<InfoOrg> infoOrgs) {
        this.infoOrgs = infoOrgs;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, cascade = {REMOVE}, mappedBy = "info")
    public Set<InfoProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(Set<InfoProcess> processes) {
        this.processes = processes;
    }

    @OneToOne(cascade = {REMOVE}, mappedBy = "info", fetch = FetchType.LAZY)
    public InfoDetail getDetail() {
        return detail;
    }

    public void setDetail(InfoDetail detail) {
        this.detail = detail;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_node_id", nullable = false)
    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_org_id", nullable = false)
    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_creator_id", nullable = false)
    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_from_site_id", nullable = false)
    public Site getFromSite() {
        return fromSite;
    }

    public void setFromSite(Site fromSite) {
        this.fromSite = fromSite;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "f_publish_date", nullable = false, length = 19)
    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "f_off_date", length = 19)
    public Date getOffDate() {
        return offDate;
    }

    public void setOffDate(Date offDate) {
        this.offDate = offDate;
    }

    @Column(name = "f_priority", nullable = false)
    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "f_is_with_image", nullable = false, length = 1)
    public Boolean getWithImage() {
        return this.withImage;
    }

    public void setWithImage(Boolean withImage) {
        this.withImage = withImage;
    }

    @Column(name = "f_views", nullable = false)
    public Integer getViews() {
        return this.views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Column(name = "f_downloads", nullable = false)
    public Integer getDownloads() {
        return this.downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Column(name = "f_comments", nullable = false)
    public Integer getComments() {
        return this.comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @Column(name = "f_favorites", nullable = false)
    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    @Column(name = "f_diggs", nullable = false)
    public Integer getDiggs() {
        return diggs;
    }

    public void setDiggs(Integer diggs) {
        this.diggs = diggs;
    }

    @Column(name = "f_score", nullable = false)
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Column(name = "f_status", nullable = false, length = 1)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "f_html_status", nullable = false, length = 1)
    public String getHtmlStatus() {
        return htmlStatus;
    }

    public void setHtmlStatus(String htmlStatus) {
        this.htmlStatus = htmlStatus;
    }

    @Column(name = "f_p0")
    public Integer getP0() {
        return this.p0;
    }

    public void setP0(Integer p0) {
        this.p0 = p0;
    }

    @Column(name = "f_p1")
    public Integer getP1() {
        return this.p1;
    }

    public void setP1(Integer p1) {
        this.p1 = p1;
    }

    @Column(name = "f_p2")
    public Integer getP2() {
        return this.p2;
    }

    public void setP2(Integer p2) {
        this.p2 = p2;
    }

    @Column(name = "f_p3")
    public Integer getP3() {
        return this.p3;
    }

    public void setP3(Integer p3) {
        this.p3 = p3;
    }

    @Column(name = "f_p4")
    public Integer getP4() {
        return this.p4;
    }

    public void setP4(Integer p4) {
        this.p4 = p4;
    }

    @Column(name = "f_p5")
    public Integer getP5() {
        return this.p5;
    }

    public void setP5(Integer p5) {
        this.p5 = p5;
    }

    @Column(name = "f_p6")
    public Integer getP6() {
        return this.p6;
    }

    public void setP6(Integer p6) {
        this.p6 = p6;
    }

    @XmlTransient
    @Transient
    public String getHighlightTitle() {
        if (StringUtils.isBlank(highlightTitle)) {
            return getFullTitleOrTitle();
        } else {
            return highlightTitle;
        }
    }

    public void setHighlightTitle(String highlightTitle) {
        this.highlightTitle = highlightTitle;
    }

    @XmlTransient
    @Transient
    public String getHighlightText() {
        if (StringUtils.isBlank(highlightText)) {
            return getDescription();
        } else {
            return highlightText;
        }
    }

    public void setHighlightText(String highlightText) {
        this.highlightText = highlightText;
    }
}
