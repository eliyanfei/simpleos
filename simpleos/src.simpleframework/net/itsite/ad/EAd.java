package net.itsite.ad;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:03:18 
 * @Description: 目前支持的广告类型
 *
 */
public enum EAd {
	right() {
		@Override
		public String toString() {
			return "内容右侧";
		}
	},
	content() {
		@Override
		public String toString() {
			return "内容下侧";
		}
	};

}
