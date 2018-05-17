package com.jspxcms.core.repository.plus;

import com.jspxcms.core.domain.MailOutbox;

public interface MailInboxDaoPlus {
	public int allReceive(MailOutbox outbox);

	public int groupReceiveGroup(MailOutbox outbox, Integer[] groupIds);
}
