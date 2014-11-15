package net.simpleframework.sysmgr.dict.component.dictselect;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictSelectBean extends DictionaryBean {
	private String dictName;

	private EDictSelectType dictType;

	public DictSelectBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultDictSelectHandle.class;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(final String dictName) {
		this.dictName = dictName;
	}

	public EDictSelectType getDictType() {
		return dictType == null ? EDictSelectType.tree : dictType;
	}

	public void setDictType(final EDictSelectType dictType) {
		this.dictType = dictType;
	}
}
