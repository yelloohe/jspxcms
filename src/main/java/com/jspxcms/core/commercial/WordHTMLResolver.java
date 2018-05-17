package com.jspxcms.core.commercial;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.upload.Uploader;
import com.jspxcms.core.service.AttachmentService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 处理由word文档（doc、docx）转换成的HTML
 * 
 * @author liufang
 *
 */
public class WordHTMLResolver {
	/**
	 * 提取body及style的内容，以及处理图片
	 * 
	 * @param filename
	 *            HTML文件的完整文件名，可通过文件名直接访问文件
	 * @param prefix
	 *            图片url地址前缀
	 * @param path
	 *            保存图片的相对路径
	 * @param fileHandler
	 *            文件处理器
	 * @param ip
	 *            IP地址
	 * @param userId
	 *            用户ID
	 * @param siteId
	 *            站点ID
	 * @param attachmentService
	 *            附件Service
	 * @return 处理后的html
	 * @throws ParserException
	 * @throws URISyntaxException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String resolver(String filename, String prefix, String path, FileHandler fileHandler, String ip,
			Integer userId, Integer siteId, AttachmentService attachmentService) throws ParserException,
			URISyntaxException, IllegalStateException, IOException {
		Parser parser = new Parser(filename);
		File file = new File(filename);
		String extension = FilenameUtils.getExtension(filename);

		// 获取内容
		HasParentFilter body = new HasParentFilter(new TagNameFilter("body"));
		// 不加style的内容，会导致页面样式被覆盖
		// OrFilter filter = new OrFilter(new TagNameFilter("style"), body);
		NodeList nodeList = parser.extractAllNodesThatMatch(body);
		// 处理图片
		TagNameFilter imgFilter = new TagNameFilter("img");
		NodeList imgList = nodeList.extractAllNodesThatMatch(imgFilter, true);
		for (int i = 0, len = imgList.size(); i < len; i++) {
			ImageTag imgTag = (ImageTag) imgList.elementAt(i);
			String src = imgTag.getAttribute("src");
			URI uri = new URI(StringEscapeUtils.unescapeHtml4(src));
			// 图片数据不用处理，如：data:image/png;base64,...
			if (uri.getScheme() == null) {
				File imgFile = new File(file.getParentFile(), src);
				// 保存图片
				String pathname = path + Uploader.getQuickPathname(Uploader.IMAGE, extension);
				String url = prefix + pathname;
				imgTag.setAttribute("src", url);
				fileHandler.storeFile(imgFile, pathname);
				attachmentService.save(pathname, imgFile.length(), ip, userId, siteId);
			}
		}
		return nodeList.toHtml();
	}
}
