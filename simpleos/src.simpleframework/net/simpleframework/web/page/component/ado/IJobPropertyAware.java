package net.simpleframework.web.page.component.ado;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IJobPropertyAware {

	String getJobAdd();

	void setJobAdd(String jobAdd);

	String getJobEdit();

	void setJobEdit(final String jobEdit);

	String getJobDelete();

	void setJobDelete(final String jobDelete);

	String getJobExchange();

	void setJobExchange(final String jobExchange);
}
