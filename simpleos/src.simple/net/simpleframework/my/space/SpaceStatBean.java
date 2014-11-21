package net.simpleframework.my.space;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.bean.IViewsBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SpaceStatBean extends AbstractIdDataObjectBean implements IViewsBeanAware {
	private long views;

	private int attentions;

	private int fans;

	@Override
	public long getViews() {
		return views;
	}

	@Override
	public void setViews(final long views) {
		this.views = views;
	}

	public int getAttentions() {
		return attentions;
	}

	public void setAttentions(final int attentions) {
		this.attentions = attentions;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(final int fans) {
		this.fans = fans;
	}

	private static final long serialVersionUID = -949811670986506553L;
}
