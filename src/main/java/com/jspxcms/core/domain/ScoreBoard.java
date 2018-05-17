package com.jspxcms.core.domain;

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

/**
 * ScoreBoard
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_score_board")
public class ScoreBoard implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public void applyDefaultValue() {
		if (getVotes() == null) {
			setVotes(0);
		}
	}

	private Integer id;
	
	private ScoreItem item;

	private String ftype;
	private Integer fid;
	private Integer votes;
	
	@Id
	@Column(name = "f_scoreboard_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_scoreboard", pkColumnValue = "cms_scoreboard", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_scoreboard")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_scoreitem_id", nullable = false)
	public ScoreItem getItem() {
		return this.item;
	}

	public void setItem(ScoreItem item) {
		this.item = item;
	}

	@Column(name = "f_ftype", nullable = false, length = 4)
	public String getFtype() {
		return this.ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	@Column(name = "f_fid", nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	@Column(name = "f_votes", nullable = false)
	public Integer getVotes() {
		return this.votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

}
