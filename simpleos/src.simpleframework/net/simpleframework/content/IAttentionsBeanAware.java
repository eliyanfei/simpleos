package net.simpleframework.content;

import net.simpleframework.core.bean.IIdBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IAttentionsBeanAware extends IIdBeanAware {
	int getAttentions();

	void setAttentions(final int attentions);
}
