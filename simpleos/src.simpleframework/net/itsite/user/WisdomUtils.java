package net.itsite.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.util.ConvertUtils;

public class WisdomUtils {
	public final static List<String> jokList = new ArrayList<String>();

	/**
	 * 初始化笑话
	 */
	public static void initJok() {
		jokList.clear();
		final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, WisdomBean.class);
		final StringBuffer sql = new StringBuffer();
		int type = ConvertUtils.toInt(DictUtils.getSysDictByName("wisdom.wisdom_v"), 0);
		sql.append("type=?");
		final List<Object> lv = new ArrayList<Object>();
		lv.add(type);
		IQueryEntitySet<WisdomBean> qs = tMgr.query(new ExpressionValue(sql.toString(), lv.toArray()), WisdomBean.class);
		if (qs != null) {
			WisdomBean wisdomBean = null;
			while ((wisdomBean = qs.next()) != null) {
				jokList.add(wisdomBean.getContent());
			}
		}
	}

	public static String getJok() {
		if (jokList.isEmpty()) {
			return "";
		}
		final StringBuffer buf = new StringBuffer();
		buf.append("<span style='font-family:楷体,仿宋,隶书;font-weight: bold;font-size: 15pt;'>");
		if (jokList.size() != 1) {
			buf.append("言:");
		}
		buf.append("</span>");
		buf.append("<span style='font-size: 12pt;color:ccc;'>");
		buf.append(jokList.get(new Random().nextInt(jokList.size())));
		buf.append("</span>");
		return buf.toString();
	}
}
