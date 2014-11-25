package org.tuckey.web.filters.urlrewrite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.simpleframework.util.IoUtils;
import net.simpleos.PrjVersion;
import net.simpleos.module.ISimpleosModule;
import net.simpleos.module.SimpleosModuleUtils;
import net.simpleos.utils.IOUtils;
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
	public void init(FilterConfig arg0) throws ServletException {
		initSimpleFiles(arg0.getServletContext().getRealPath(""));
		super.init(arg0);
	}

	private void initSimpleFiles(String path) {
		PrintWriter pw = null;
		try {
			final File versionFile = new File(path + File.separator + "simpleos.v");
			if (versionFile.exists()) {
				String version = null;
				try {
					version = IOUtils.readListFromFile(versionFile).get(0);
					if (!PrjVersion.latest.toString().equals(version)) {
						pw = new PrintWriter(versionFile);
					}
				} catch (Exception e) {
					pw = new PrintWriter(versionFile);
				}
			} else {
				pw = new PrintWriter(versionFile);
			}
			if (pw != null) {
				pw.append(PrjVersion.latest.toString());
				pw.flush();
			}
			System.out.println("开始覆盖修改后的simpleos.....");
			final Set<String> set = new HashSet<String>();
			set.add("desktop_index_c.xml");
			set.add("logo.png");
			set.add("favicon.png");
			IoUtils.unzip(getClass().getResourceAsStream("simpleos.zip"), path, pw != null, set);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeIO(pw);
		}
	}

	@Override
	protected void checkConf(Conf conf) {
		try {
			ReflectUtils.createSharedReflections("classes", "bin", "simpleos");
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
