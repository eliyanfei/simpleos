package net.simpleframework.web.page.component.ui.picshow;

import net.simpleframework.web.page.component.ComponentParameter;

public interface IPicShowHandle {
	/**
	 * �������û��޸�
	 * @param compParameter
	 * @return
	 * @throws Exception
	 */
	PicShowBean getPicShowBean(final PicShowBean picShow, final ComponentParameter compParameter) throws Exception;
}
