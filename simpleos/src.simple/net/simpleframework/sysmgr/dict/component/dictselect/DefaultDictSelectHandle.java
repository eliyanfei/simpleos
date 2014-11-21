package net.simpleframework.sysmgr.dict.component.dictselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.AbstractDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultDictSelectHandle extends AbstractDictionaryHandle implements IDictSelectHandle {

	@Override
	public Collection<SysDict> getDictItems(final ComponentParameter compParameter, final SysDict parent) {
		final ArrayList<SysDict> al = new ArrayList<SysDict>();
		final String dictName = (String) compParameter.getBeanProperty("dictName");
		if (StringUtils.hasText(dictName)) {
			final String rKey = "sysDict_" + StringUtils.hash(compParameter.componentBean);
			SysDict sysDict = (SysDict) compParameter.getRequestAttribute(rKey);
			final ITableEntityManager temgr = DictUtils.getTableEntityManager(SysDict.class);
			if (sysDict == null) {
				sysDict = temgr.queryForObject(new ExpressionValue(Table.nullExpr(temgr.getTable(), "documentid") + " and name=?",
						new Object[] { dictName }), SysDict.class);
				compParameter.setRequestAttribute(rKey, sysDict);
			}
			if (sysDict == null)
				return al;
			final StringBuilder sb = new StringBuilder();
			final ArrayList<Object> params = new ArrayList<Object>();
			sb.append("documentid=? and ");
			params.add(sysDict.getId());
			if (parent == null) {
				sb.append(Table.nullExpr(temgr.getTable(), "parentid"));
			} else {
				sb.append("parentid=?");
				params.add(parent.getId());
			}
			sb.append(" order by oorder asc");
			final IQueryEntitySet<SysDict> qs = temgr.query(new ExpressionValue(sb.toString(), params.toArray()), SysDict.class);
			SysDict tSysDict;
			while ((tSysDict = qs.next()) != null) {
				al.add(tSysDict);
			}
		}
		return al;
	}

	@Override
	public Map<String, Object> getDictItemAttributes(final ComponentParameter compParameter, final SysDict dictItem) {
		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("extend1", dictItem.getExtend1());
		attributes.put("extend2", dictItem.getExtend2());
		attributes.put("extend3", dictItem.getExtend3());
		attributes.put("extend4", dictItem.getExtend4());
		return attributes;
	}
}
