package net.simpleos.mvc.myfavorite;

import java.util.Map;

import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.impl.ASimpleosAppclicationModule;

/**
 * 站点设置
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:55
 */
public class MyFavoriteAppModule extends ASimpleosAppclicationModule implements IMyFavoriteAppModule {
	Table table = new Table("simple_my_favorite", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(MyFavoriteBean.class, table);
	}

	static final String deployName = "myfavorite";

	@Override
	public Class<?> getEntityBeanClass() {
		return MyFavoriteBean.class;
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(MyFavoriteUtils.class, deployName);
		MyFavoriteUtils.appModule = this;
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
