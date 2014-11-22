package org.tuckey.web.filters.urlrewrite;

import java.beans.Beans;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.simpleos.module.ISimpleosModule;
import net.simpleos.module.SimpleosModuleUtils;
import net.simpleos.utils.ReflectUtils;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午9:32:47 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *	 
*/
public class SimpleosUrlRewriteFilter extends UrlRewriteFilter {
	@Override
	protected void checkConf(Conf conf) {
		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "simpleos.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(ISimpleosModule.class);//
				final List<ISimpleosModule> list = new ArrayList<ISimpleosModule>();
				for (final String subType : subTypes) {
					final ISimpleosModule module = ReflectUtils.initClass(subType, ISimpleosModule.class);
					if (null == module)
						continue;
					list.add(module);
					InputStream is = module.getClass().getResourceAsStream("urlrewrite.xml");
					if (is != null) {
						conf.loadDom(is);
						conf.initialise();
					}
				}
				Collections.sort(list);
				for (final ISimpleosModule module : list) {
					SimpleosModuleUtils.moduleMap.put(module.getModuleName(), module);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		super.checkConf(conf);
	}

	@Override
	protected UrlRewriter getUrlRewriter(ServletRequest request, ServletResponse response, FilterChain chain) {
		UrlRewriter rewriter = super.getUrlRewriter(request, response, chain);
		return rewriter;
	}
}
