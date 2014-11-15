package net.itsite.ad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdUtils {
	public static IAdApplicationModule applicationModule;
	public static String deployPath;
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
				applicationModule.doUpdate(adBean);
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
				applicationModule.doUpdate(adBean);
			}
		}
	}

	public static String getAd(final EAd ad) {
		final StringBuffer buf = new StringBuffer();
		final AdBean adBean = adMap.get(ad.name());
		if (adBean != null) {
			if (adBean.startup()) {
				buf.append("<div");
				if (ad != EAd.ad_0) {
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
