package net.simpleframework.example;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;
import net.simpleframework.web.page.component.ui.pager.IPagerHandle;
import net.simpleframework.web.page.component.ui.pager.PagerUtils;

public class MyPagerHandle extends AbstractPagerHandle implements IPagerHandle {
	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public int getCount(final ComponentParameter compParameter) {
		return 10000;
	}

	@Override
	public void process(final ComponentParameter compParameter, final int start) {
		int i = 0;
		final int c = PagerUtils.getPageItems(compParameter);
		final List<Object> data = new ArrayList<Object>();
		while (i++ < c) {
			data.add("Test " + (start + i));
		}
		compParameter.request.setAttribute("t", data);
	}
}
