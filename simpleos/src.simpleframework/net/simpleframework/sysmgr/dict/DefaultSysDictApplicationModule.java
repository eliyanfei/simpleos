package net.simpleframework.sysmgr.dict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.page.component.ui.propeditor.EComponentType;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultSysDictApplicationModule extends AbstractWebApplicationModule implements
		ISysDictApplicationModule {
	public static final String deployName = "sysmgr/dict";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(SysDict.class, new Table("simple_sys_dict"));
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(DictUtils.class, deployName);

		final ITableEntityManager temgr = DictUtils.getTableEntityManager(SysDict.class);

		if (temgr.getCount(null) == 0) {
			final String classpath = BeanUtils.getResourceClasspath(
					DefaultSysDictApplicationModule.class, "dict_type.xml");
			createData(classpath, temgr);
		}
	}

	protected void createData(final String classpath, final ITableEntityManager temgr) {
		new AbstractXmlDocument(getClass().getClassLoader().getResourceAsStream(classpath)) {

			@Override
			protected void init() throws Exception {
				__init(getRoot(), null);
			}

			private void __init(final Element element, final SysDict parent) {
				if (element == null) {
					return;
				}
				final Iterator<?> it = element.elementIterator("type");
				while (it.hasNext()) {
					final Element element2 = (Element) it.next();
					final SysDict sysDict = createSysDict(element2, parent);
					if (parent != null) {
						sysDict.setParentId(parent.getId());
					}
					temgr.insert(sysDict);
					__init(element2, sysDict);
				}
				if (parent != null) {
					final Iterator<?> it2 = element.elementIterator("item");
					while (it2.hasNext()) {
						final Element element2 = (Element) it2.next();
						final SysDict sysDict = createSysDict(element2, parent);
						if (parent.getDocumentId() == null) {
							sysDict.setDocumentId(parent.getId());
						} else {
							sysDict.setParentId(parent.getId());
							sysDict.setDocumentId(parent.getDocumentId());
						}
						temgr.insert(sysDict);
						__init(element2, sysDict);
					}
				}
			}

			private SysDict createSysDict(final Element element2, final SysDict parent) {
				final SysDict sysDict = new SysDict();
				sysDict.setCreateDate(new Date());
				sysDict.setName(element2.attributeValue("name"));
				sysDict.setText(element2.attributeValue("text"));
				return sysDict;
			}
		};
	}

	@Override
	public Collection<PropField> doDictItemPropEditor(final HttpServletRequest request,
			final HttpServletResponse response, final CatalogBean catalogBean,
			final PropEditorBean formEditor) {
		final ArrayList<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
		final PropField propField = new PropField();
		propField.setLabel(LocaleI18n.getMessage("DictItemHandle.0"));
		final FieldComponent fieldComponent = new FieldComponent();
		fieldComponent.setName("catalog_mark");
		fieldComponent.setType(EComponentType.select);
		final StringBuilder sb = new StringBuilder();
		sb.append(SysDict.MARK_SELECTED).append("=")
				.append(LocaleI18n.getMessage("DictItemHandle.1")).append(";");
		sb.append(SysDict.MARK_UNSELECTED).append("=")
				.append(LocaleI18n.getMessage("DictItemHandle.2"));
		fieldComponent.setDefaultValue(sb.toString());
		propField.getFieldComponents().add(fieldComponent);
		al.add(3, propField);
		return al;
	}
}
