package net.simpleframework.applets.attention;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.EFunctionModule;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttentionBean extends AbstractIdDataObjectBean {
	private EFunctionModule vtype; // bbs=0 blog=1 news=2

	private ID accountId;

	private ID attentionId;

	private Date createDate;

	public EFunctionModule getVtype() {
		return vtype;
	}

	public void setVtype(final EFunctionModule vtype) {
		this.vtype = vtype;
	}

	public ID getAccountId() {
		return accountId;
	}

	public void setAccountId(final ID accountId) {
		this.accountId = accountId;
	}

	public ID getAttentionId() {
		return attentionId;
	}

	public void setAttentionId(final ID attentionId) {
		this.attentionId = attentionId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	private static final long serialVersionUID = -4251173116301623873L;
}
