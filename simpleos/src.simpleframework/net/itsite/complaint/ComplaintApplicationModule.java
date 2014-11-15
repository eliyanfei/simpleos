package net.itsite.complaint;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;

public class ComplaintApplicationModule extends AItSiteAppclicationModule implements IComplaintApplicationModule {
	private String deployName = "complaint";

	@Override
	public void init(IInitializer initializer) {
		try {
			super.init(initializer);
			doInit(ComplaintUtils.class, deployName);
		} catch (Exception e) {
		}
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return ComplaintBean.class;
	}

	static final Table it_complaint = new Table("it_complaint");

	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		super.putTables(tables);
		tables.put(ComplaintBean.class, it_complaint);
	}

	@Override
	public AbstractComponentBean getComponentBean(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public String getViewUrl(Object id) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/datalib").append("/v/").append(id);
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public String getViewUrl1(PageRequestResponse requestResponse, Object... params) {
		final StringBuilder sb = new StringBuilder();
		final int ll = ConvertUtils.toInt(requestResponse.getRequestParameter("ll"), 0);
		sb.append("/datalib/").append(params[0]).append("-").append(params[1]).append("-").append(ll);
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public String tabs2(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public String getApplicationUrl(PageRequestResponse requestResponse) {
		return "/datalib.html";
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

	@Override
	protected Map<Integer, String> getOrderData() {
		Map<Integer, String> orderData = super.getOrderData();
		orderData.remove(4);
		return orderData;
	}

	@Override
	public ITableEntityManager getDataObjectManager() {
		return super.getDataObjectManager(ComplaintBean.class);
	}

}
