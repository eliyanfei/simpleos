package net.simpleframework.organization.web;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IOrgConst {
	static final int NO_DEPARTMENT_ID = -1;

	static final int ONLINE_ID = -3;

	static final int STATE_DELETE_ID = -2;

	static final int STATE_NORMAL_ID = -4;

	static final int STATE_REGISTER_ID = -5;

	static final int STATE_LOCKED_ID = -6;

	static class Helper {
		static String __user_list(final Object idValue) {
			final StringBuilder sb = new StringBuilder();
			sb.append("$Actions['__user_list']('");
			sb.append(OrgUtils.dm().getDepartmentIdParameterName());
			sb.append("=").append(StringUtils.blank(idValue)).append("');");
			return sb.toString();
		}
	}

	static abstract class StatTableEntityAdapter extends TableEntityAdapter {
		abstract void resetStat();

		@Override
		public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
			resetStat();
		}

		@Override
		public void afterUpdate(final ITableEntityManager manager, final Object[] object) {
			resetStat();
		}

		@Override
		public void afterDelete(final ITableEntityManager manager,
				final IDataObjectValue dataObjectValue) {
			resetStat();
		}
	}
}
