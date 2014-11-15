package net.itsite.complaint;

/**
 * 投诉
 * @author 李岩飞
 *
 */
public enum EComplaint {
	eroticism() {
		@Override
		public String toString() {
			return "色情";
		}
	},
	ad() {
		@Override
		public String toString() {
			return "广告";
		}
	},
	copyright() {
		@Override
		public String toString() {
			return "侵犯版权";
		}
	},
	duplicate() {
		@Override
		public String toString() {
			return "重复内容";
		}

		public String toTip() {
			return "请指出和那些内容重复，列出重复的URL地址";
		}
	},
	notshow() {
		@Override
		public String toString() {
			return "无法正常显示";
		}
	},
	other() {
		@Override
		public String toString() {
			return "其他";
		}
	};

	public String toTip() {
		return "请详述举报原因";
	}
}
