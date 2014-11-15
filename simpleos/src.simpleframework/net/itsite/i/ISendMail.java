package net.itsite.i;

import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.organization.IUser;

public interface ISendMail extends ISentCallback {
	IUser getUser();
}
