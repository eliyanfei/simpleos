package net.simpleframework.my.friends;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Friends extends AbstractIdDataObjectBean {
	private static final long serialVersionUID = 3298967879805836223L;

	private ID userId;

	private ID friendId;

	private Date createDate;

	private ID groupId;

	private EFriendsRelation relation;

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public ID getFriendId() {
		return friendId;
	}

	public void setFriendId(final ID friendId) {
		this.friendId = friendId;
	}

	public ID getGroupId() {
		return groupId;
	}

	public void setGroupId(final ID groupId) {
		this.groupId = groupId;
	}

	public Date getCreateDate() {
		if (createDate == null) {
			createDate = new Date();
		}
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public EFriendsRelation getRelation() {
		if (relation == null) {
			relation = EFriendsRelation.friendly;
		}
		return relation;
	}

	public void setRelation(final EFriendsRelation relation) {
		this.relation = relation;
	}
}
