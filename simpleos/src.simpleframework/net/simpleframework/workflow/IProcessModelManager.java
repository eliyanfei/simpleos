package net.simpleframework.workflow;

import java.io.Reader;
import java.util.Map;

import net.simpleframework.ado.db.IBeanManagerAware;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IProcessModelManager extends IBeanManagerAware<ProcessModelBean>,
		IListenerAware<ProcessModelBean>, IScriptAware<ProcessModelBean> {

	ProcessModelBean add(IUser user, Reader reader);

	IDataObjectQuery<ProcessModelBean> models(EProcessModelStatus status);

	Map<ID, InitiateItem> items(IUser user);
}
