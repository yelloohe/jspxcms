package com.jspxcms.core.commercial;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.media.MediaUploadResult;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.model.Group;
import com.foxinmy.weixin4j.tuple.MpArticle;
import com.foxinmy.weixin4j.tuple.MpNews;
import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Context;

/**
 * 微信群发
 * 
 * @author liufang
 *
 */
public class Weixin {

	private static Logger logger = LoggerFactory.getLogger(Weixin.class);

	public static String massWeixinForm(Integer[] ids, Integer queryNodeId, Integer queryNodeType,
			Integer queryInfoPermType, String queryStatus, HttpServletRequest request,
			org.springframework.ui.Model modelMap, WeixinProxy weixinProxy, InfoQueryService query)
			throws WeixinException {
		List<Info> list = new ArrayList<Info>();
		if (weixinProxy != null) {
			List<Group> groups = weixinProxy.getGroups();
			modelMap.addAttribute("weixinGroups", groups);
		}
		for (Integer id : ids) {
			list.add(query.get(id));
		}
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("queryNodeId", queryNodeId);
		modelMap.addAttribute("queryNodeType", queryNodeType);
		modelMap.addAttribute("queryInfoPermType", queryInfoPermType);
		modelMap.addAttribute("queryStatus", queryStatus);
		return "core/info/info_mass_weixin";
	}

	public static void massWeixin(String mode, Integer groupId, String towxname, String[] title, String[] author,
			String[] contentSourceUrl, String[] digest, Boolean[] showConverPic, String[] thumb,
			HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap,
			WeixinProxy weixinProxy, OperationLogService logService, PathResolver pathResolver) throws IOException {
		Site site = Context.getCurrentSite();
		Integer siteId = site.getId();
		try {
			String[] content = Servlets.getParamPrefix(request, "content_");
			List<MpArticle> articles = getMpArticles(title, author, contentSourceUrl, digest, showConverPic, thumb,
					content, site, weixinProxy, pathResolver);
			String mediaId = weixinProxy.uploadMassArticle(articles);
			MpNews mpnews = new MpNews(mediaId);
			if ("preview".equals(mode)) {
				weixinProxy.previewMassNews(null, towxname, mpnews);
			} else if ("gourp".equals(mode)) {
				weixinProxy.massByGroupId(mpnews, false, groupId);
			} else {
				weixinProxy.massByGroupId(mpnews, true, 0);
			}
			Integer userId = Context.getCurrentUserId();
			String ip = Servlets.getRemoteAddr(request);
			logService.operation("opr.info.antiArchive", mode + " " + mediaId, null, null, ip, userId, siteId);
			Servlets.writeHtml(response, "ok");
		} catch (WeixinException e) {
			Servlets.writeHtml(response, e.getMessage());
		}
	}

	private static List<MpArticle> getMpArticles(String[] title, String[] author, String[] contentSourceUrl,
			String[] digest, Boolean[] showConverPic, String[] thumb, String[] content, final Site site,
			final WeixinProxy weixinProxy, PathResolver pathResolver) throws IOException, WeixinException {
		PublishPoint publishPoint = site.getUploadsPublishPoint();
		final FileHandler fileHandler = publishPoint.getFileHandler(pathResolver);
		final String urlPrefix = publishPoint.getUrlPrefix();
		final URL siteUrl = new URL(site.getUrlFull());
		List<MpArticle> articles = new ArrayList<MpArticle>();
		for (int i = 0, len = title.length; i < len; i++) {
			if (StringUtils.isNotBlank(thumb[i])) {
				MediaUploadResult result;
				InputStream is;
				if (StringUtils.startsWith(thumb[i], urlPrefix)) {
					thumb[i] = thumb[i].substring(urlPrefix.length());
					is = fileHandler.getInputStream(thumb[i]);
				} else {
					is = new URL(siteUrl, thumb[i]).openConnection().getInputStream();
				}
				result = weixinProxy.uploadMedia(false, is, null);
				IOUtils.closeQuietly(is);
				thumb[i] = result.getMediaId();
			}
			try {
				Parser parser = new Parser(new Lexer(content[i]));
				NodeIterator it = parser.elements();
				StringBuilder sb = new StringBuilder();
				while (it.hasMoreNodes()) {
					org.htmlparser.Node node = it.nextNode();
					node.accept(new NodeVisitor() {
						public void visitTag(Tag tag) {
							if (tag instanceof ImageTag) {
								String src = tag.getAttribute("src");
								String resultUrl;
								InputStream is = null;
								try {
									try {
										if (StringUtils.startsWith(src, urlPrefix)) {
											src = src.substring(urlPrefix.length());
											is = fileHandler.getInputStream(src);
										} else {
											is = new URL(siteUrl, src).openConnection().getInputStream();
										}
										resultUrl = weixinProxy.uploadImage(is, null);
										tag.setAttribute("src", resultUrl);
									} catch (MalformedURLException e) {
										logger.error(null, e);
									} catch (IOException e) {
										logger.error(null, e);
									} catch (WeixinException e) {
										logger.error(null, e);
									}
								} finally {
									IOUtils.closeQuietly(is);
								}
							}
						}
					});
					String html = node.toHtml();
					sb.append(html);
				}
				content[i] = sb.toString();
			} catch (ParserException e) {
				// 忽略
			}
			MpArticle article = new MpArticle(thumb[i], title[i], content[i]);
			article.setAuthor(author[i]);
			article.setSourceUrl(contentSourceUrl[i]);
			article.setDigest(digest[i]);
			article.setShowCoverPic(showConverPic[i]);
			articles.add(article);
		}
		return articles;
	}
}
