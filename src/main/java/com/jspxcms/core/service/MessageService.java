package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Message;
import com.jspxcms.core.domain.MessageText;

/**
 * 消息Service
 * 
 * @author liufang
 *
 */
public interface MessageService {
	public Page<Message> findAll(Map<String, String[]> params, Pageable pageable);

	public RowSide<Message> findSide(Map<String, String[]> params, Message bean, Integer position, Sort sort);

	public Page<Object[]> findByUserId(Integer userId, boolean unread, Pageable pageable);

	public List<Object[]> findByUserId(Integer userId, boolean unread, Limitable limitable);

	public Page<Message> findByContactId(Integer userId, Integer contactId, Pageable pageable);

	public List<Message> findByContactId(Integer userId, Integer contactId, Limitable limitable);

	public Message get(Integer id);

	public Message send(Integer senderId, Integer receiverId, MessageText messageText);

	public Message save(Message bean);

	public Message update(Message bean);

	/**
	 * 设置为已读
	 * 
	 * @param userId
	 *            接收用户
	 * @param senderId
	 *            发送用户
	 * @return 被设置为已读的消息数量
	 */
	public int setRead(Integer userId, Integer senderId);

	/**
	 * 根据联系人删除消息。如果对方还未删除，则标记为己方删除；如果对方已经删除，则彻底删除数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @param contactId
	 *            联系人ID
	 * @return 被删除的消息数量（包括标记为删除和彻底删除的数据）
	 */
	public int deleteByContactId(Integer userId, Integer contactId);

	/**
	 * 删除消息。如果对方还未删除，则标记为己方删除；如果对方已经删除，则彻底删除数据。
	 * 
	 * @param ids
	 *            要删除的信息ID数组
	 * @param userId
	 *            删除方ID
	 * @return 被删除的消息数量（包括标记为删除和彻底删除的数据）
	 */
	public int deleteById(Integer[] ids, Integer userId);

	public Message delete(Integer id);

	public List<Message> delete(Integer[] ids);
}
