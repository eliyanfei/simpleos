package net.itsite.ad;

public enum EAd {
	ad_0() {
		@Override
		public String toString() {
			return "首页顶部";
		}

		@Override
		public String range() {
			return "500*70";
		}

		@Override
		public String price() {
			return "700";
		}
	},
	list_0() {
		@Override
		public String toString() {
			return "全站列表右上";
		}

		@Override
		public String price() {
			return "1000";
		}
	},
	list_1() {
		@Override
		public String toString() {
			return "全站列表右下";
		}

		@Override
		public String price() {
			return "700";
		}
	},
	view_0() {
		@Override
		public String toString() {
			return "全站内容右上";
		}

		@Override
		public String price() {
			return "1000";
		}
	},
	view_1() {
		@Override
		public String toString() {
			return "全站内容右下";
		}

		@Override
		public String price() {
			return "700";
		}
	};
	/**
	 * 图片大小
	 * @return
	 */
	public String range() {
		return "250*300";
	}

	/**
	 * 价位
	 * @return
	 */
	public String price() {
		return "";
	}
}
