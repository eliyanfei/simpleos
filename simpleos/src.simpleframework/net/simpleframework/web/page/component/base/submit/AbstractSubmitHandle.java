package net.simpleframework.web.page.component.base.submit;

import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.MultipartPageRequest;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractSubmitHandle extends AbstractComponentHandle implements ISubmitHandle {

	@Override
	public AbstractUrlForward submit(final ComponentParameter compParameter) {
		return null;
	}

	protected IMultipartFile getMultipartFile(final PageRequestResponse requestResponse,
			final String filename) {
		return requestResponse.request instanceof MultipartPageRequest ? ((MultipartPageRequest) requestResponse.request)
				.getFile(filename) : null;
	}
}
