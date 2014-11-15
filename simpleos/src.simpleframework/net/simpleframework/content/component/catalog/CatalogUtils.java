package net.simpleframework.content.component.catalog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class CatalogUtils {

	public static final String BEAN_ID = "catalog_@bid";

	public static final String CATALOG_ID = AbstractDbTablePagerHandle.CATALOG_ID;

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static final Table catalog_table = new Table("simple_catalog");

	public static final Table catalog_owner_table = new Table("simple_catalog_owner", new String[] {
			"catalogid", "ownertype", "ownerid" });

	public static String getCatalogJob(final IDataObjectQuery<? extends CatalogOwner> doq) {
		final StringBuilder sb = new StringBuilder();
		if (doq != null) {
			CatalogOwner catalogOwner;
			while ((catalogOwner = doq.next()) != null) {
				final ID ownerId = catalogOwner.getOwnerId();
				if (catalogOwner.getOwnerType() == EMemberType.user) {
					final IUser user = OrgUtils.um().queryForObjectById(ownerId);
					if (user != null) {
						sb.append(";").append("#").append(user.getName());
					}
				} else {
					final IJob job = OrgUtils.jm().queryForObjectById(ownerId);
					if (job != null) {
						sb.append(";").append(job.getName());
					}
				}
			}
		}
		return sb.length() > 0 ? sb.substring(1) : null;
	}
}
