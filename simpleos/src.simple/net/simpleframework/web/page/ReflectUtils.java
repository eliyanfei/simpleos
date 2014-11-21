package net.simpleframework.web.page;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.util.BeanUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ReflectUtils {
	static Logger logger = ALoggerAware.getLogger(ReflectUtils.class);

	public static String jobManager, jobAnonymous;

	public static Method methodIsMember;

	public static Method methodAccessForward;

	public static Method methodGetLogin;
	static {
		try {
			// AccountSession
			final Class<?> classAccountSession = Class
					.forName("net.simpleframework.organization.account.AccountSession");
			methodAccessForward = classAccountSession.getMethod("accessForward",
					PageRequestResponse.class, String.class, String.class);
			methodGetLogin = classAccountSession.getMethod("getLogin", HttpSession.class);

			// OrgUtils
			final Class<?> classOrgUtils = BeanUtils
					.forName("net.simpleframework.organization.OrgUtils");
			methodIsMember = classOrgUtils.getMethod("isMember", String.class, HttpSession.class,
					Object[].class);

			// IJob
			final Class<?> classIJob = BeanUtils.forName("net.simpleframework.organization.IJob");
			jobManager = (String) classIJob.getField("sj_manager").get(null);
			jobAnonymous = (String) classIJob.getField("sj_anonymous").get(null);
		} catch (final Exception e) {
			logger.warn(e);
		}
	}
}
