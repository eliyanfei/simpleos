package net.simpleframework.core.id;

import net.simpleframework.util.ConvertUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LongID extends AbstractID<Long> {

	private static final long serialVersionUID = 4193421687986152568L;

	public LongID(final Object id) {
		this.id = ConvertUtils.toLong(id, 0l);
	}
}
