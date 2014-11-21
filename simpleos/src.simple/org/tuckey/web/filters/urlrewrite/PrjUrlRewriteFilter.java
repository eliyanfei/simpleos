package org.tuckey.web.filters.urlrewrite;

import java.beans.Beans;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.itsite.utils.ReflectUtils;
import net.simpleos.module.IModuleBean;

import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午9:32:47 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *	 
*/
public class PrjUrlRewriteFilter extends UrlRewriteFilter {
	@Override
	protected void checkConf(Conf conf) {
		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "app.", "simple.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(IModuleBean.class);//
				for (final String subType : subTypes) {
					final IModuleBean impl = ReflectUtils.initClass(subType, IModuleBean.class);
					if (null == impl)
						continue;
					InputStream is = impl.getClass().getResourceAsStream(impl.getName() + "_urlrewrite.xml");
					if (is != null) {
						conf.loadDom(is);
						conf.initialise();
					}
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
