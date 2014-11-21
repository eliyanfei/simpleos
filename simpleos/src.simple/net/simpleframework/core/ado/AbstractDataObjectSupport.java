package net.simpleframework.core.ado;

import java.util.Collection;
import java.util.LinkedHashSet;

import net.simpleframework.core.AAttributeAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDataObjectSupport extends AAttributeAware implements
		IDataObjectListenerAware {
	private Collection<IDataObjectListener> listeners;

	@Override
	public Collection<IDataObjectListener> getListeners() {
		if (listeners == null) {
			listeners = new LinkedHashSet<IDataObjectListener>();
		}
		return listeners;
	}

	@Override
	public void addListener(final IDataObjectListener listener) {
		getListeners().add(listener);
	}

	@Override
	public boolean removeListener(final IDataObjectListener listener) {
		return getListeners().remove(listener);
	}
}
