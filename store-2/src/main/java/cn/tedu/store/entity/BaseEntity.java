package cn.tedu.store.entity;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3096732782900839505L;
	private String createdUser;
	private Date createTime;
	private String modifiedUser;
	private Date modifiedTime;
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	@Override
	public String toString() {
		return "BaseEntity [createdUser=" + createdUser + ", createTime=" + createTime + ", modifiedUser="
				+ modifiedUser + ", modifiedTime=" + modifiedTime + "]";
	}
	
}
