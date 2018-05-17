package com.jspxcms.common.freemarker;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

import com.jspxcms.common.util.Strings;

import freemarker.cache.TemplateLoader;

/**
 * FreeMarker模板加载类
 * 
 * @author liufang
 * 
 */
public class AdapterTemplateLoader implements TemplateLoader {
	private TemplateLoader templateLoader;

	public AdapterTemplateLoader(TemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
		templateLoader.closeTemplateSource(templateSource);
	}

	public Object findTemplateSource(String name) throws IOException {
		return templateLoader.findTemplateSource(name);
	}

	public long getLastModified(Object templateSource) {
		return templateLoader.getLastModified(templateSource);
	}

	public Reader getReader(final Object templateSource, final String encoding)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		Reader superReader = templateLoader.getReader(templateSource, encoding);
		try {
			char[] buff = new char[8192];
			int readed;
			while ((readed = superReader.read(buff)) != -1) {
				sb.append(buff, 0, readed);
			}
		} finally {
			superReader.close();
		}
		Strings.replace(sb, "/_files", "${_files}");
		Strings.replace(sb, "_files", "${_files}");
		int len = sb.length();
		char[] readerChar = new char[len];
		sb.getChars(0, len, readerChar, 0);
		CharArrayReader charArrayReader = new CharArrayReader(readerChar);
		return charArrayReader;
	}
}
