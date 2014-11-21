package net.simpleframework.core.id;

import net.simpleframework.util.ConvertUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class StringID extends AbstractID<String> {
	private static final long serialVersionUID = 8283766253505696610L;

	public StringID(final Object id) {
		this.id = ConvertUtils.toString(id);
	}
}
