package net.simpleframework.core.id;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NullID implements ID {
	private static final long serialVersionUID = 3207817821426296535L;

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public boolean equals2(final Object obj) {
		return obj == null;
	}
}
