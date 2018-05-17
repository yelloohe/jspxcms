package com.jspxcms.core.repository.plus;

import java.util.Date;

public interface VoteMarkDaoPlus {
	public int countMark(String ftype, Integer fid, Integer userId, String ip,
			String cookie, Date after);
}
