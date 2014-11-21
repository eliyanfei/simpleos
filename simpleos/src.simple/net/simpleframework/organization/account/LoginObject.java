package net.simpleframework.organization.account;

import java.io.Serializable;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.OrganizationException;
import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginObject implements Serializable {
	private static final long serialVersionUID = -6333906898017172896L;

	public static enum EAccountType {
		normal, email, mobile
	}

	private final String name;

	private final EAccountType accountType;

	private boolean dialogUpdate = false;

	public LoginObject(final String name, final EAccountType accountType) {
		this.name = name;
		this.accountType = accountType;
	}

	public String getName() {
		return name;
	}

	public boolean isDialogUpdate() {
		return dialogUpdate;
	}

	public void setDialogUpdate(boolean dialogUpdate) {
		this.dialogUpdate = dialogUpdate;
	}

	public EAccountType getAccountType() {
		return accountType == null ? EAccountType.normal : accountType;
	}

	public IAccount getAccount() {
		final EAccountType at = getAccountType();
		if (at == EAccountType.normal) {
			return OrgUtils.am().getAccountByName(getName());
		} else if (at == EAccountType.email) {
			final IUser user = OrgUtils.um().getUserByEmail(getName());
			if (user != null) {
				final IAccount account = user.account();
				if (account != null && !account.isMailbinding()) {
					throw OrganizationException.wrapException(LocaleI18n.getMessage("LoginObject.0"));
				}
				return account;
			}
		} else if (at == EAccountType.mobile) {
		}
		return null;
	}
}
