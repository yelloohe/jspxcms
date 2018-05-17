package com.jspxcms.core.repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Message;
import com.jspxcms.core.repository.plus.MessageDaoPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

public interface MessageDao extends Repository<Message, Integer>, MessageDaoPlus {
	public Page<Message> findAll(Specification<Message> spec, Pageable pageable);

	public List<Message> findAll(Specification<Message> spec, Limitable limitable);

	public Message findOne(Integer id);

	public Message save(Message bean);

	public void delete(Message bean);

	// --------------------
	/**
	 * 设置为已读
	 * 
	 * @param receiverId
	 *            接收人ID
	 * @param senderId
	 *            发送人ID
	 * @return
	 */
	@Modifying
	@Query("update Message bean set bean.unread=false where bean.receiver.id=?1 and bean.sender.id=?2 and bean.unread=true")
	public int setRead(Integer receiverId, Integer senderId);

	/**
	 * 删除已被对方标记为删除的消息
	 * 
	 * @param userId
	 *            用户ID
	 * @param contactId
	 *            联系人ID
	 * @return 被删除的数量
	 */
	@Modifying
	@Query("delete from Message bean where (bean.sender.id=?1 and bean.receiver.id=?2 and bean.deletionFlag=2) or (bean.receiver.id=?1 and bean.sender.id=?2 and bean.deletionFlag=1)")
	public int deleteByContactId(Integer userId, Integer contactId);

	/**
	 * 标记为发送人删除
	 * 
	 * @param senderId
	 *            发送人ID
	 * @param receiverId
	 *            接收人ID
	 * @return 被设置删除的数量
	 */
	@Modifying
	@Query("update Message bean set bean.deletionFlag=1 where bean.sender.id=?1 and bean.receiver.id=?2 and bean.deletionFlag=0")
	public int setDeleteFlagBySender(Integer senderId, Integer receiverId);

	/**
	 * 设置为接收人删除
	 * 
	 * @param senderId
	 *            发送人ID
	 * @param receiverId
	 *            接收人ID
	 * @return 被设置删除的数量
	 */
	@Modifying
	@Query("update Message bean set bean.deletionFlag=2 where bean.sender.id=?1 and bean.receiver.id=?2 and bean.deletionFlag=0")
	public int setDeleteFlagByReceiver(Integer senderId, Integer receiverId);

	/**
	 * 删除用户消息。包括收件人和发件人。
	 * 
	 * @param userIds
	 * @return
	 */
	@Modifying
	@Query("delete from Message bean where bean.receiver.id in (?1) or bean.sender.id in (?1)")
	public int deleteByUserId(Collection<Integer> userIds);

	/**
	 * 删除用户消息。包括收件人和发件人。
	 * 
	 * @param userIds
	 * @return
	 */
	@Modifying
//	@Query(value = "delete mt from cms_message_text mt join cms_message m on mt.message_id_=m.message_id_ where m.receiver_id_ in (?1) or m.sender_id_ in (?1)", nativeQuery = true)
	@Query(value = "delete from cms_message_text mt where exists (select 1 from cms_message m where mt.message_id_=m.message_id_ and m.receiver_id_ in (?1) or m.sender_id_ in (?1))", nativeQuery = true)
	public int deleteMessageTextByUserId(Collection<Integer> userIds);

}
