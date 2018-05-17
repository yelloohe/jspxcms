package com.jspxcms.core.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.core.support.Configurable;

public class GlobalOther implements Configurable {
	public static final String PREFIX = "sys_other_";
	public static final String BUFFER_NODE_VIEWS = PREFIX + "bufferNodeViews";
	public static final String BUFFER_INFO_VIEWS = PREFIX + "bufferInfoViews";
	public static final String BUFFER_INFO_DOWNLOADS = PREFIX
			+ "bufferInfoDownloads";
	public static final String BUFFER_INFO_DIGGS = PREFIX + "bufferInfoDiggs";
	public static final String BUFFER_INFO_SCORE = PREFIX + "bufferInfoScore";
	public static final String BUFFER_INFO_COMMENTS = PREFIX
			+ "bufferInfoComments";

	private Map<String, String> customs;

	public GlobalOther() {
	}

	public GlobalOther(Map<String, String> customs) {
		this.customs = customs;
	}

	/**
	 * 获取栏目浏览次数缓存大小。默认为10。
	 * 
	 * @return
	 */
	public Integer getBufferNodeViews() {
		String bufferNodeViews = getCustoms().get(BUFFER_NODE_VIEWS);
		if (StringUtils.isNotBlank(bufferNodeViews)) {
			return Integer.parseInt(bufferNodeViews);
		} else {
			// 默认为10
			return 10;
		}
	}

	/**
	 * 设置栏目浏览次数缓存大小
	 * 
	 * @param bufferNodeViews
	 */
	public void setBufferNodeViews(Integer bufferNodeViews) {
		if (bufferNodeViews != null) {
			getCustoms().put(BUFFER_NODE_VIEWS, bufferNodeViews.toString());
		} else {
			getCustoms().remove(BUFFER_NODE_VIEWS);
		}
	}

	/**
	 * 获取信息浏览次数缓存大小。默认为10。
	 * 
	 * @return
	 */
	public Integer getBufferInfoViews() {
		String bufferInfoViews = getCustoms().get(BUFFER_INFO_VIEWS);
		if (StringUtils.isNotBlank(bufferInfoViews)) {
			return Integer.parseInt(bufferInfoViews);
		} else {
			// 默认为10
			return 10;
		}
	}

	/**
	 * 设置信息浏览次数缓存大小
	 * 
	 * @param bufferInfoViews
	 */
	public void setBufferInfoViews(Integer bufferInfoViews) {
		if (bufferInfoViews != null) {
			getCustoms().put(BUFFER_INFO_VIEWS, bufferInfoViews.toString());
		} else {
			getCustoms().remove(BUFFER_INFO_VIEWS);
		}
	}

	/**
	 * 获取信息下载次数缓存大小。默认为0。
	 * 
	 * @return
	 */
	public Integer getBufferInfoDownloads() {
		String bufferInfoDownloads = getCustoms().get(BUFFER_INFO_DOWNLOADS);
		if (StringUtils.isNotBlank(bufferInfoDownloads)) {
			return Integer.parseInt(bufferInfoDownloads);
		} else {
			// 默认为0
			return 0;
		}
	}

	/**
	 * 设置信息下载次数缓存大小
	 * 
	 * @param bufferInfoDownloads
	 */
	public void setBufferInfoDownloads(Integer bufferInfoDownloads) {
		if (bufferInfoDownloads != null) {
			getCustoms().put(BUFFER_INFO_DOWNLOADS,
					bufferInfoDownloads.toString());
		} else {
			getCustoms().remove(BUFFER_INFO_DOWNLOADS);
		}
	}

	/**
	 * 获取信息点赞次数缓存大小。默认为0。
	 * 
	 * @return
	 */
	public Integer getBufferInfoDiggs() {
		String bufferInfoDiggs = getCustoms().get(BUFFER_INFO_DIGGS);
		if (StringUtils.isNotBlank(bufferInfoDiggs)) {
			return Integer.parseInt(bufferInfoDiggs);
		} else {
			// 默认为0
			return 0;
		}
	}

	/**
	 * 设置信息点赞次数缓存大小
	 * 
	 * @param bufferInfoDiggs
	 */
	public void settBufferInfoDiggs(Integer bufferInfoDiggs) {
		if (bufferInfoDiggs != null) {
			getCustoms().put(BUFFER_INFO_DIGGS, bufferInfoDiggs.toString());
		} else {
			getCustoms().remove(BUFFER_INFO_DIGGS);
		}
	}

	/**
	 * 获取信息评分次数缓存大小。默认为0。
	 * 
	 * @return
	 */
	public Integer getBufferInfoScore() {
		String bufferInfoScore = getCustoms().get(BUFFER_INFO_SCORE);
		if (StringUtils.isNotBlank(bufferInfoScore)) {
			return Integer.parseInt(bufferInfoScore);
		} else {
			// 默认为0
			return 0;
		}
	}

	/**
	 * 设置信息评分次数缓存大小
	 * 
	 * @param bufferInfoScore
	 */
	public void setBufferInfoScore(Integer bufferInfoScore) {
		if (bufferInfoScore != null) {
			getCustoms().put(BUFFER_INFO_SCORE, bufferInfoScore.toString());
		} else {
			getCustoms().remove(BUFFER_INFO_SCORE);
		}
	}

	/**
	 * 获取信息评论次数缓存大小。默认为0。
	 * 
	 * @return
	 */
	public Integer getBufferInfoComments() {
		String bufferInfoComments = getCustoms().get(BUFFER_INFO_COMMENTS);
		if (StringUtils.isNotBlank(bufferInfoComments)) {
			return Integer.parseInt(bufferInfoComments);
		} else {
			// 默认为0
			return 0;
		}
	}

	/**
	 * 设置信息评论次数缓存大小
	 * 
	 * @param bufferInfoComments
	 */
	public void setBufferInfoComments(Integer bufferInfoComments) {
		if (bufferInfoComments != null) {
			getCustoms().put(BUFFER_INFO_COMMENTS,
					bufferInfoComments.toString());
		} else {
			getCustoms().remove(BUFFER_INFO_COMMENTS);
		}
	}

	public Map<String, String> getCustoms() {
		if (customs == null) {
			customs = new HashMap<String, String>();
		}
		return customs;
	}

	public void setCustoms(Map<String, String> customs) {
		this.customs = customs;
	}

	public String getPrefix() {
		return PREFIX;
	}
}
