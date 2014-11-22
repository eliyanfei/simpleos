package net.simpleos;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.AbstractPropEditorHandle;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:58:51 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosDBPropEditorHandle extends AbstractPropEditorHandle {
	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		return super.getFormParameters(compParameter);
	}

	@Override
	public Collection<PropField> getFormFields(ComponentParameter compParameter) {
		final String path = IWebApplication.Instance.getApplication().getServletContext().getRealPath("/base.properties");
		final File dsFile = new File(path);
		Properties pro = new Properties();
		if (dsFile.exists()) {
			try {
				pro.load(new FileInputStream(dsFile));
			} catch (Exception e) {
			}
		}
		final PropEditorBean formEditor = (PropEditorBean) compParameter.componentBean;
		final Collection<PropField> colls = new ArrayList<PropField>(formEditor.getFormFields());
		for (final PropField pf : colls) {
			if ("URL".equals(pf.getLabel())) {
				getFieldComponent(pf.getFieldComponents(), 0).setDefaultValue(StringUtils.text(pro.getProperty("url"), ""));
			} else if ("用户名".equals(pf.getLabel())) {
				getFieldComponent(pf.getFieldComponents(), 0).setDefaultValue(StringUtils.text(pro.getProperty("username"), ""));
			} else if ("密码".equals(pf.getLabel())) {
				getFieldComponent(pf.getFieldComponents(), 0).setDefaultValue(StringUtils.text(pro.getProperty("password"), ""));
			}
		}
		return colls;
	}

	private FieldComponent getFieldComponent(Collection<FieldComponent> fieldComponents, final int p) {
		return ((List<FieldComponent>) fieldComponents).get(p);
	}
}
