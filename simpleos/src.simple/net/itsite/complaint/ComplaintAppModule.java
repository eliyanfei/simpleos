package net.itsite.complaint;

import java.util.Map;

import net.itsite.i.IItSiteApplicationModule;
import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:22:31 
 * @Description: 初始化参数类
 *
 */
public class ComplaintAppModule extends AItSiteAppclicationModule {
	private String deployName = "complaint";
	public static IItSiteApplicationModule applicationModule;

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
	public String getDeployPath() {
		return deployName;
	}

	@Override
	protected Map<Integer, String> getOrderData() {
		Map<Integer, String> orderData = super.getOrderData();
		orderData.remove(4);
		return orderData;
	}

}
