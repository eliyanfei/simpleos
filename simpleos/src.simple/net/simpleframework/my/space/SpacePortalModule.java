package net.simpleframework.my.space;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.AbstractContentLayoutModuleHandle;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2014-1-5下午02:09:01
 */
public class SpacePortalModule extends AbstractContentLayoutModuleHandle {
	public SpacePortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_space_rows=6" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(MySpaceUtils.deployPath + "/jsp/space_option.jsp");
	}

	@Override
	public void optionLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding) {
		super.optionLoaded(pageParameter, dataBinding);
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		final IQueryEntitySet<SapceLogBean> qs = MySpaceUtils.getTableEntityManager(SapceLogBean.class).query(
				new ExpressionValue("refModule in (?)  order by createdate desc", new Object[] { EFunctionModule.space_log }), SapceLogBean.class);
		final int _space_rows = ConvertUtils.toInt(getPagelet().getOptionProperty("_space_rows"), 64);
		qs.setFetchSize(_space_rows);
		qs.setCount(_space_rows);
		compParameter.setRequestAttribute("__qs", qs);
		String forward = MySpaceUtils.deployPath + "/jsp/space_layout.jsp?";
		return new UrlForward(forward, "pa");
	}
}
