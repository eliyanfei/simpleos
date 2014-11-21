package net.simpleos.module.ad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:03:14 
 * @Description: 广告的公共数据处理
 *
 */
public class AdUtils {
	public static Map<String, AdBean> adMap = new HashMap<String, AdBean>();

	/**
	 * 停止广告
	 * @param ad
	 */
	public static void stopAd(final String ad) {
		final AdBean adBean = adMap.get(ad);
		if (adBean != null) {
			if (adBean.getStatus() != 2) {
				adBean.setStatus(2);
				AdAppModule.applicationModule.doUpdate(adBean);
			}
		}
	}

	/**
	* 启动广告
	* @param ad
	*/
	public static void startAd(final String ad) {
		final AdBean adBean = adMap.get(ad);
		if (adBean != null) {
			if (adBean.getStatus() != 1) {
				adBean.setStatus(1);
				AdAppModule.applicationModule.doUpdate(adBean);
			}
		}
	}

	public static String getAd(final EAd ad) {
		final StringBuffer buf = new StringBuffer();
		final AdBean adBean = adMap.get(ad.name());
		if (adBean != null) {
			if (adBean.startup()) {
				buf.append("<div");
				if (ad == EAd.right) {
					buf.append(" class='block_layout1'");
				}
				buf.append(">");
				if (adBean.getAdType() == 0) {
					buf.append("<a href='").append(adBean.getUrl()).append("' target='_blank'>");
					buf.append("<img src='").append(adBean.getSrc()).append("'>").append("</a>");
				} else {
					buf.append(adBean.getContent());
				}
				buf.append("</div>");
			}
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		String line = "@eliyanfei ,@afei;@rrr 你好";
		String newLine = line, ss;
		final Set<Character> set = new HashSet<Character>();
		set.add('@');
		set.add(',');
		set.add(';');
		set.add(' ');
		int t = 0;
		for (int i = 0; i < line.length(); i++) {
			if (set.contains(line.charAt(i))) {
				if (t != i) {
					ss = line.substring(t, i);
					newLine = newLine.replaceAll(ss, "啊啊");
				}
			}
			if (line.charAt(i) == '@') {
				t = i;
			}
		}
	}
}
