package com.jspxcms.ext.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name = "cms_vote_option")
public class VoteOption implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取选项的票数比例。范围从0-1。
	 * 
	 * @return 浮点型
	 */
	@Transient
	public float getProportion() {
		Integer total = getVote().getTotal();
		if (total != null && total > 0) {
			return (float) getCount() / total;
		} else {
			return 0;
		}
	}

	@Transient
	public void applyDefaultValue() {
		if (getCount() == null) {
			setCount(0);
		}
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
	}

	private Integer id;
	private Vote vote;

	private String title;
	private Integer count;
	private Integer seq;

	@Id
	@Column(name = "f_voteoption_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_vote_option", pkColumnValue = "cms_vote_option", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_vote_option")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_vote_id", nullable = false)
	public Vote getVote() {
		return this.vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	@Column(name = "f_title", nullable = false, length = 150)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "f_count", nullable = false)
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

}
