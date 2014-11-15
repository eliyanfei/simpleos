package net.simpleframework.web.page;

import java.io.IOException;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.prj.core.$VType;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.Version;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.web.page.component.ComponentRegistryFactory;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestRegistry;
import net.simpleframework.web.page.component.base.jspinclude.JspIncludeRegistry;
import net.simpleframework.web.page.component.base.submit.SubmitRegistry;
import net.simpleframework.web.page.component.base.validation.ValidationRegistry;
import net.simpleframework.web.page.component.ui.calendar.CalendarRegistry;
import net.simpleframework.web.page.component.ui.chart.ChartRegistry;
import net.simpleframework.web.page.component.ui.chosen.ChosenRegistry;
import net.simpleframework.web.page.component.ui.colorpalette.ColorPaletteRegistry;
import net.simpleframework.web.page.component.ui.dhx.DhxLayoutRegistry;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.web.page.component.ui.grid.GridRegistry;
import net.simpleframework.web.page.component.ui.htmleditor.HtmlEditorRegistry;
import net.simpleframework.web.page.component.ui.imageslide.ImageSlideRegistry;
import net.simpleframework.web.page.component.ui.listbox.ListboxRegistry;
import net.simpleframework.web.page.component.ui.menu.MenuRegistry;
import net.simpleframework.web.page.component.ui.pager.GroupTablePagerRegistry;
import net.simpleframework.web.page.component.ui.pager.PagerRegistry;
import net.simpleframework.web.page.component.ui.pager.TablePagerRegistry;
import net.simpleframework.web.page.component.ui.picshow.PicShowRegistry;
import net.simpleframework.web.page.component.ui.portal.PortalRegistry;
import net.simpleframework.web.page.component.ui.progressbar.ProgressBarRegistry;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorRegistry;
import net.simpleframework.web.page.component.ui.pwdstrength.PwdStrengthRegistry;
import net.simpleframework.web.page.component.ui.slider.SliderRegistry;
import net.simpleframework.web.page.component.ui.swfupload.SwfUploadRegistry;
import net.simpleframework.web.page.component.ui.syntaxhighlighter.SyntaxHighlighterRegistry;
import net.simpleframework.web.page.component.ui.tabs.TabsRegistry;
import net.simpleframework.web.page.component.ui.tooltip.TooltipRegistry;
import net.simpleframework.web.page.component.ui.tree.FolderTreeRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeRegistry;
import net.simpleframework.web.page.component.ui.tree.db.DbTreeRegistry;
import net.simpleframework.web.page.component.ui.validatecode.ValidateCodeRegistry;
import net.simpleframework.web.page.component.ui.videoplayer.VideoPlayerRegistry;
import net.simpleframework.web.page.component.ui.window.WindowRegistry;
import net.simpleframework.web.page.impl.DefaultPageResourceProvider;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageContext extends ALoggerAware implements IPageContext {
	private ServletContext servletContext;

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	private IApplication application;

	@Override
	public IApplication getApplication() {
		return application;
	}

	@Override
	public void setApplication(final IApplication application) {
		this.application = application;
	}

	@Override
	public void doInit(final ServletContext servletContext) throws IOException {
		this.servletContext = servletContext;

		JavascriptUtils.unzipJsAndCss(
				new ZipInputStream(PageFilter.class.getClassLoader().getResourceAsStream(
						BeanUtils.getResourceClasspath(PageFilter.class, "resource.zip"))), servletContext.getRealPath(PageUtils.getResourcePath()),
				PageUtils.pageConfig.isResourceCompress());

		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
		for (final IComponentRegistry componentRegistry : new IComponentRegistry[] { new AjaxRequestRegistry(servletContext),
				new SubmitRegistry(servletContext), new JspIncludeRegistry(servletContext), new ValidationRegistry(servletContext),
				new WindowRegistry(servletContext), new GridRegistry(servletContext), new TreeRegistry(servletContext),
				new FolderTreeRegistry(servletContext), new DbTreeRegistry(servletContext), new CalendarRegistry(servletContext),
				new ListboxRegistry(servletContext), new MenuRegistry(servletContext), new PagerRegistry(servletContext),
				new TablePagerRegistry(servletContext), new GroupTablePagerRegistry(servletContext), new DictionaryRegistry(servletContext),
				new DhxLayoutRegistry(servletContext), new TooltipRegistry(servletContext), new TabsRegistry(servletContext),
				new ValidateCodeRegistry(servletContext), new PortalRegistry(servletContext), new ProgressBarRegistry(servletContext),
				new SwfUploadRegistry(servletContext), new ChartRegistry(servletContext), new HtmlEditorRegistry(servletContext),
				new SliderRegistry(servletContext), new ColorPaletteRegistry(servletContext), new ImageSlideRegistry(servletContext),
				new VideoPlayerRegistry(servletContext), new PropEditorRegistry(servletContext), new PwdStrengthRegistry(servletContext),
				new ChosenRegistry(servletContext), new SyntaxHighlighterRegistry(servletContext), new PicShowRegistry(servletContext) }) {
			try {
				factory.regist(componentRegistry);
			} catch (Exception e) {
			}
		}

		final PageEventAdapter adapter = PageEventAdapter.getInstance(servletContext);
		adapter.addListener(SessionCache.sessionListener);
	}

	@Override
	public IPageResourceProvider createPageResourceProvider() {
		return new DefaultPageResourceProvider(getServletContext());
	}

	@Override
	public IMultipartPageRequest createMultipartPageRequest(final HttpServletRequest request) {
		return new MultipartPageRequest(request);
	}

	@Override
	public PageHtmlBuilder createPageHtmlBuilder() {
		return new PageHtmlBuilder();
	}

	protected PageConfig createPageConfig() {
		return new PageConfig(this);
	}

	private final PageConfig pageConfig = createPageConfig();

	@Override
	public PageConfig getPageConfig() {
		return pageConfig;
	}

	private Version version;

	@Override
	public Version getVersion() {
		if (version == null) {
			version = $VType.getNowVType().ver;
		}
		return version;
	}
}
