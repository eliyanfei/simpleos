package net.itsite.ad;

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
