package net.simpleos.backend.menu;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import net.simpleos.impl.ASimpleosAppclicationModule;
import net.simpleos.module.ISimpleosModule;
import net.simpleos.module.SimpleosModuleUtils;
import net.simpleos.utils.ReflectUtils;
import net.simpleos.utils.StringsUtils;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:55
 */
public class MenuNavAppModule extends ASimpleosAppclicationModule implements IMenuNavAppModule {

	public static Table catalog = new Table("simpleos_menu_catalog", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(MenuNavBean.class, catalog);
	}

	static final String deployName = "simpleosmenu";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(MenuNavUtils.class, deployName);
		MenuNavUtils.appModule = this;
		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "simpleos.");
			try {
				for (final ISimpleosModule module : SimpleosModuleUtils.moduleMap.values()) {
					if (StringsUtils.isNotBlank1(module.getFrontHtml())) {
						if (count("name=?", new Object[] { module.getModuleName() }, MenuNavBean.class) == 0) {
							MenuNavBean menuBean = new MenuNavBean();
							menuBean.setName(module.getModuleName());
							menuBean.setText(module.getFrontTitle());
							menuBean.setUrl(module.getFrontHtml());
							menuBean.setBuildIn(true);
							menuBean.setOorder(module.getOorder());
							doUpdate(menuBean);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			MenuNavUtils.loadMenu();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return MenuNavBean.class;
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
