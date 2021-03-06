package com.ve.itemcenter.common.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品评论DTO
 * 
 * @author chen.huang
 * @date 2015年1月21日
 */
public class ItemCommentDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4051055219641294698L;

	private Long id; // 主键

	private Long userId; // 用户ID

	private String userName;// 用户昵称

	private Long orderId;// 订单ID

	private Long itemId; // 商品ID

	private Long sellerId;// 卖家ID

	private String title;// 评论标题

	private String content;// 评论内容

	private Integer score; // 用户打分

	private String imgs;// 晒的图片地址，用逗号分隔

	private Integer praiseNum;// 点赞数

	private Date replyTime;// 回复时间

	private Long replyUserId; // 回复者用户ID

	private String replyContent;// 回复内容

	private Integer auditStatus; // 审核状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs == null ? null : imgs.trim();
	}

	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public Long getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(Long replyUserId) {
		this.replyUserId = replyUserId;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	@Override
	public String toString() {
		return "ItemCommentDTO [id=" + id + ", userName=" + userName + "]";
	}
}