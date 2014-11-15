package net.simpleframework.content.component.remark;

import net.simpleframework.content.IContentHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IRemarkHandle extends IContentHandle {

	long getCount(ComponentParameter compParameter);

	IDataObjectQuery<RemarkItem> getRemarkItems(ComponentParameter compParameter);

	IDataObjectQuery<RemarkItem> getRemarkItems(ComponentParameter compParameter, Object parentId);

	RemarkItem doSupportOpposition(ComponentParameter compParameter, Object itemId, boolean support);
}
