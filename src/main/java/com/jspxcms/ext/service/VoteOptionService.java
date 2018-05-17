package com.jspxcms.ext.service;

import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.domain.VoteOption;

public interface VoteOptionService {
	public VoteOption get(Integer id);

	public VoteOption[] save(String[] title, Integer[] count, Vote vote);

	public VoteOption[] update(Integer[] id, String[] title, Integer[] count,
			Vote vote);

	public VoteOption delete(VoteOption entity);
}
