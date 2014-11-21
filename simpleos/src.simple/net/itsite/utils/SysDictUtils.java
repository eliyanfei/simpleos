package net.itsite.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.sysmgr.dict.SysDict;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public final class SysDictUtils {

	public static String getSysDictTextByName(final String dictType, final String dictName) {
		if (StringsUtils.isBlank(dictName)) {
			return null;
		}
		final SysDict dict = DictUtils.getSysDictByName(dictType + "." + dictName);
		return dict == null ? "" : dict.getText();
	}

	public static List<SysDict> getRootSysDictList() {
		final List<SysDict> al = new ArrayList<SysDict>();
		final ITableEntityManager sysDictMgr = DictUtils.getTableEntityManager(SysDict.class);
		final IQueryEntitySet<SysDict> qs = sysDictMgr.query(new ExpressionValue("documentid = ?", new Object[] { 0 }), SysDict.class);
		SysDict tSysDict;
		while ((tSysDict = qs.next()) != null) {
			al.add(tSysDict);
		}
		return al;
	}

	public static Collection<SysDict> getSysDictChilds(final String dictType) {
		final ArrayList<SysDict> al = new ArrayList<SysDict>();
		final SysDict sysDict = DictUtils.getSysDictByName(dictType);
		final ITableEntityManager temgr = DictUtils.getTableEntityManager(SysDict.class);
		final IQueryEntitySet<SysDict> qs = temgr.query(new ExpressionValue("documentid=?", new Object[] { sysDict.getId() }), SysDict.class);
		SysDict tSysDict;
		while ((tSysDict = qs.next()) != null) {
			al.add(tSysDict);
		}
		return al;
	}

	public static List<SysDict> getEngineeringCostType() {
		final List<SysDict> al = new ArrayList<SysDict>();
		final SysDict sysDict = DictUtils.getSysDictByName(SysDictUtils.indicator_type);
		final ITableEntityManager temgr = DictUtils.getTableEntityManager(SysDict.class);
		final IQueryEntitySet<SysDict> qs = temgr.query(new ExpressionValue("documentid = ? and parentid <> 0", new Object[] { sysDict.getId() }),
				SysDict.class);
		SysDict tSysDict;
		while ((tSysDict = qs.next()) != null) {
			al.add(tSysDict);
		}
		return al;
	}

	public static final String district = "district";
	public static final String subject_level = "subject_level";
	public static final String exam_level = "exam_level";
	public static final String pay_type = "pay_type";
	public static final String indicator_type = "indicator_type";
	public static final String user_level = "user_level";
	public static final String project_group = "project_group";
	public static final String task_type = "task_type";

	public static String getSubjectTextByName(final String dictName) {
		return getSysDictTextByName(subject_level, StringsUtils.trimNull(dictName, "SU0"));
	}

	public static String getTiKuTextByName(final String dictName) {
		return getSysDictTextByName(exam_level, dictName);
	}

	public static String getPay_typeTextByName(final String dictName) {
		return getSysDictTextByName(pay_type, dictName);
	}

	public static String getIndicator_typeTextByName(final String dictName) {
		return getSysDictTextByName(indicator_type, dictName);
	}

	public static String getUser_levelTextByName(final String dictName) {
		return getSysDictTextByName(user_level, dictName);
	}

	public static String getProjectGroupTextByName(final String dictName) {
		return getSysDictTextByName(project_group, dictName);
	}

	public static String getTaskTypeTextByName(final String dictName) {
		return getSysDictTextByName(task_type, dictName);
	}

	public static String getOperatorsTextByName(final String dictName) {
		return getSysDictTextByName("project_operators", dictName);
	}

	public static String getNetworkTextByName(final String dictName) {
		return getSysDictTextByName("project_network", dictName);
	}
}
