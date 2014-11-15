package net.simpleframework.sysmgr.dict;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DictUtils {
	public static ISysDictApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static SysDict getSysDictById(final Object id) {
		if (id == null) {
			return null;
		}
		return getTableEntityManager(SysDict.class).queryForObjectById(id, SysDict.class);
	}

	public static SysDict getSysDictByName(final String name) {
		final String[] nameArr = StringUtils.split(name, ".");
		if (nameArr == null || nameArr.length == 0) {
			return null;
		}
		final ITableEntityManager temgr = getTableEntityManager(SysDict.class);
		final SysDict sysDict = temgr.queryForObject(new ExpressionValue(Table.nullExpr(temgr.getTable(), "documentid") + " and name=?",
				new Object[] { nameArr[0] }), SysDict.class);
		if (sysDict == null) {
			return null;
		}
		if (nameArr.length == 1) {
			return sysDict;
		} else {
			return temgr.queryForObject(new ExpressionValue("documentid=? and name=?", new Object[] { sysDict.getId(), nameArr[1] }), SysDict.class);
		}
	}

	public static String buildSysDict(final Object id) {
		final StringBuffer buf = new StringBuffer();
		final SysDict sys = DictUtils.getSysDictById(id);
		if (sys != null) {
			buildSysDict(sys, buf);
			buf.append(sys.getText());
		}
		return buf.toString();
	}

	private static void buildSysDict(final SysDict sys, final StringBuffer buf) {
		final SysDict psys = (SysDict) sys.parent();
		if (psys != null) {
			buildSysDict(psys, buf);
			buf.append(psys.getText()).append("-");
		}
	}

	public static void createData(final InputStream dictTypeOS) {
		final ITableEntityManager temgr = DictUtils.getTableEntityManager(SysDict.class);

		new AbstractXmlDocument(dictTypeOS) {

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

}
