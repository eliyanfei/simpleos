package net.simpleos.commons.cache;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:40:28 
 * @Description: 缓存数据存储数据库结构类型
 *
 */
public enum ECacheType {
	/**
	 * 表示一行数据是一个String
	 */
	string,
	/**
	 * 表示一行数据是一个Map&lt;String,String&gt;
	 */
	map_string,
	/**
	 * 表示一行数据是一个Map&lt;String,Map&lt;String,String&gt;&gt;
	 */
	map_map_string,
}