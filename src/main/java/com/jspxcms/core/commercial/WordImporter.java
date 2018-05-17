package com.jspxcms.core.commercial;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.htmlparser.util.ParserException;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.office.MsOfficeConverter;
import com.jspxcms.common.office.OpenOfficeConverter;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.service.AttachmentService;

/**
 * Word文档导入功能类
 * 
 * @author liufang
 *
 */
public class WordImporter {
	/**
	 * 导入doc文件，转换为HTML
	 * 
	 * @param file
	 *            上传的文件
	 * @param userId
	 *            用户ID
	 * @param siteId
	 *            站点ID
	 * @param ip
	 *            操作者的ID地址
	 * @param prefix
	 *            图片url地址前缀
	 * @param path
	 *            图片保存路径
	 * @param point
	 *            发布点
	 * @param pathResolver
	 *            路径Resolver
	 * @param attachmentService
	 *            附件Service
	 * @return 转换后的HTML
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParserException
	 * @throws URISyntaxException
	 */
	public static String importDoc(MultipartFile file, Integer userId, Integer siteId, String ip, String prefix,
			String path, PublishPoint point, PathResolver pathResolver, AttachmentService attachmentService)
			throws IllegalStateException, IOException, ParserException, URISyntaxException {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		File temp = FilesEx.getTempFile(ext);
		file.transferTo(temp);
		File to = new File(Files.createTempDir(), FilenameUtils.getBaseName(file.getName()) + ".html");
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String result = null;
		try {
			if (Constants.isDoc2HtmlByMsOffice()) {
				MsOfficeConverter.wordSaveAsFilteredHTML(temp.getAbsolutePath(), to.getAbsolutePath());
			} else {
				OpenOfficeConverter.convert(temp, to, Constants.OPENOFFICE_PORT);
			}
			result = WordHTMLResolver.resolver(to.getAbsolutePath(), prefix, path, fileHandler, ip, userId, siteId,
					attachmentService);
		} finally {
			FileUtils.deleteQuietly(temp);
			FileUtils.deleteQuietly(to);
		}
		return result;
	}
}
