package net.prj.manager.datatemp;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2014-2-13下午04:49:04
 */
public enum EProductStatus {
	normal() {
		@Override
		public String toString() {
			return "上架";
		}
	},
	offline() {
		@Override
		public String toString() {
			return "下架";
		}
	}
}
