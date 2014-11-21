package net.simpleframework.applets.tag;

import net.simpleframework.content.EContentType;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.bean.IViewsBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.EFunctionModule;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TagBean extends AbstractIdDataObjectBean implements IViewsBeanAware {
	private EFunctionModule vtype; // bbs=0 blog=1 news=2

	private ID catalogId;

	private String tagText;

	private int frequency;

	private long views;

	private EContentType ttype;

	public EFunctionModule getVtype() {
		return vtype;
	}

	public void setVtype(final EFunctionModule vtype) {
		this.vtype = vtype;
	}

	public ID getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(final ID catalogId) {
		this.catalogId = catalogId;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(final String tagText) {
		this.tagText = tagText;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(final int frequency) {
		this.frequency = frequency;
	}

	@Override
	public long getViews() {
		return views;
	}

	@Override
	public void setViews(final long views) {
		this.views = views;
	}

	public EContentType getTtype() {
		return ttype == null ? EContentType.normal : ttype;
	}

	public void setTtype(final EContentType ttype) {
		this.ttype = ttype;
	}

	private static final long serialVersionUID = 4643467385781286595L;
}
