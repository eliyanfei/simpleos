package net.prj.manager.datatemp;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.utils.ReflectUtils;
import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.EComponentType;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleos.module.IModuleBean;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:55
 */
public class PrjDataTempAppModule extends AItSiteAppclicationModule implements IPrjDataTempAppModule {

	public static Table catalog = new Table("prj_menu_catalog", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(PrjProductCatalogBean.class, catalog);
	}

	static final String deployName = "prjmenu";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(PrjDataTempUtils.class, deployName);
		PrjDataTempUtils.appModule = this;
		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "app.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(IModuleBean.class);//
				for (final String subType : subTypes) {
					final IModuleBean impl = ReflectUtils.initClass(subType, IModuleBean.class);
					if (null == impl)
						continue;
					if (impl.isMenu()) {
						if (count("name=?", new Object[] { impl.getName() }, PrjProductCatalogBean.class) == 0) {
							PrjProductCatalogBean menuBean = new PrjProductCatalogBean();
							menuBean.setName(impl.getName());
							menuBean.setText(impl.getTitle());
							menuBean.setUrl(impl.getUrl());
							menuBean.setBuildIn(true);
							menuBean.setOorder(impl.getOorder());
							doUpdate(menuBean);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			PrjDataTempUtils.loadMenu();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return PrjProductCatalogBean.class;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

	@Override
	public Collection<PropField> doDictItemPropEditor(final HttpServletRequest request, final HttpServletResponse response,
			final CatalogBean catalogBean, final PropEditorBean formEditor) {
		final ArrayList<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
		final PropField propField = new PropField();
		propField.setLabel(LocaleI18n.getMessage("App.Menu.0"));
		final FieldComponent fieldComponent = new FieldComponent();
		fieldComponent.setName("catalog_mark");
		fieldComponent.setType(EComponentType.select);
		final StringBuilder sb = new StringBuilder();
		sb.append(SysDict.MARK_SELECTED).append("=").append(LocaleI18n.getMessage("App.Menu.1")).append(";");
		sb.append(SysDict.MARK_UNSELECTED).append("=").append(LocaleI18n.getMessage("App.Menu.2"));
		fieldComponent.setDefaultValue(sb.toString());
		propField.getFieldComponents().add(fieldComponent);
		al.add(3, propField);
		final PropField urlpropField = new PropField();
		urlpropField.setLabel(LocaleI18n.getMessage("App.Menu.3"));
		final FieldComponent urlComponent = new FieldComponent();
		urlComponent.setName("catalog_url");
		urlComponent.setType(EComponentType.text);
		urlpropField.getFieldComponents().add(urlComponent);
		al.add(3, urlpropField);
		return al;
	}
}
