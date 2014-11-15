package net.simpleframework.content;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ado.IJobPropertyAware;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractContentBean extends AbstractContainerBean implements
		IJobPropertyAware {
	private String jobAdd, jobEdit, jobDelete, jobExchange;

	private Object documentId;

	public AbstractContentBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public Object getDocumentId() {
		return documentId;
	}

	public void setDocumentId(final Object documentId) {
		this.documentId = documentId;
	}

	@Override
	public String getJobAdd() {
		return jobAdd;
	}

	@Override
	public void setJobAdd(final String jobAdd) {
		this.jobAdd = jobAdd;
	}

	@Override
	public String getJobEdit() {
		return jobEdit;
	}

	@Override
	public void setJobEdit(final String jobEdit) {
		this.jobEdit = jobEdit;
	}

	@Override
	public String getJobDelete() {
		return jobDelete;
	}

	@Override
	public void setJobDelete(final String jobDelete) {
		this.jobDelete = jobDelete;
	}

	@Override
	public String getJobExchange() {
		return jobExchange;
	}

	@Override
	public void setJobExchange(final String jobExchange) {
		this.jobExchange = jobExchange;
	}
}
