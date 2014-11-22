package net.simpleos.mvc.myfavorite;

import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.SimpleosUtil;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class MyFavoriteUtils {
	public static IMyFavoriteAppModule appModule = null;

	public static long getFavorites(Object refId, final ComponentParameter compParameter) {
		IAccount account = SimpleosUtil.getLoginAccount(compParameter);
		if (account == null)
			return 0;
		return getFavorites(refId, account.getId());
	}

	public static long getFavorites(Object refId, final Object userId) {
		return appModule.count("select * from simple_my_favorite where refId=" + refId + " and userId=" + userId, new Object[] {});
	}

	/**
	 * 删除关联ID
	 * @param refId
	 * @param userId
	 */
	public static void cancelFavorite(Object refId, final Object userId) {
		appModule.doUpdate("delete from simple_my_favorite where refId=" + refId + " and userId=" + userId);
	}

	/**
	 * 删除主ID
	 * @param id
	 * @param userId
	 */
	public static void cancelFavoriteById(Object id, final Object userId) {
		appModule.doUpdate("delete from simple_my_favorite where id=" + id + " and userId=" + userId);
	}
}
