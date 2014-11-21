package net.simpleframework.web.page.component.ui.validatecode;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.ImageUtils;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ValidateCodeUtils {
	static Logger logger = ALoggerAware.getLogger(ValidateCodeUtils.class);

	public static final String BEAN_ID = "validateCode_@bid";

	public static void doRender(final HttpServletRequest request, final HttpServletResponse response) {
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			final ComponentParameter nComponentParameter = ComponentParameter.get(request, response,
					BEAN_ID);
			final IValidateCodeHandle vHandle = (IValidateCodeHandle) nComponentParameter
					.getComponentHandle();
			if (vHandle == null) {
				return;
			}
			HTTPUtils.setNoCache(response);
			response.setContentType("image/png");
			response.reset();
			vHandle.doValidateCode(nComponentParameter, ImageUtils.genCode(outputStream));
		} catch (final Exception ex) {
			logger.warn(ex);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (final IOException e) {
			}
		}
	}
}
