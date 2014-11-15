package net.itsite.utils;

import java.net.URL;

/**
 * @author QianFei.Xu;E-Mail:qianfei.xu@rosense.cn
 * @time Apr 26, 2009 6:50:18 PM
 */
public interface ISubTypeFilter {
	public boolean accept(Class<?> baseType, URL pathUrl, String typePath);
}
