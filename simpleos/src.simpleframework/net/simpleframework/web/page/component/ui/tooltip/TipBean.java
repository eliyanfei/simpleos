package net.simpleframework.web.page.component.ui.tooltip;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.EJavascriptEvent;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TipBean extends AbstractElementBean {

	public TipBean(final Element element, final TooltipBean tooltipBean) {
		super(element);
	}

	public TipBean(final TooltipBean tooltipBean) {
		this(null, tooltipBean);
	}

	private String selector;

	private String title;

	private String content;

	private int width = 200;

	private String ajaxRequest;

	private int radius = 6;

	private boolean fixed;

	private double delay = 0.14;

	private EJavascriptEvent showOn;

	private double hideAfter;

	private boolean hideOthers;

	private HideOn hideOn;

	private Hook hook;

	private ETipPosition stem;

	private int offsetX, offsetY;

	private String target;

	private String jsTipCreate;

	public String getSelector() {
		return selector;
	}

	public void setSelector(final String selector) {
		this.selector = selector;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getContent() {
		return StringUtils.blank(content);
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public String getAjaxRequest() {
		return ajaxRequest;
	}

	public void setAjaxRequest(final String ajaxRequest) {
		this.ajaxRequest = ajaxRequest;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(final int radius) {
		this.radius = radius;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(final boolean fixed) {
		this.fixed = fixed;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(final double delay) {
		this.delay = delay;
	}

	public EJavascriptEvent getShowOn() {
		return showOn;
	}

	public void setShowOn(final EJavascriptEvent showOn) {
		this.showOn = showOn;
	}

	public ETipPosition getStem() {
		return stem;
	}

	public void setStem(final ETipPosition stem) {
		this.stem = stem;
	}

	public double getHideAfter() {
		return hideAfter;
	}

	public void setHideAfter(final double hideAfter) {
		this.hideAfter = hideAfter;
	}

	public boolean isHideOthers() {
		return hideOthers;
	}

	public void setHideOthers(final boolean hideOthers) {
		this.hideOthers = hideOthers;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(final int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(final int offsetY) {
		this.offsetY = offsetY;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public HideOn getHideOn() {
		return hideOn;
	}

	public void setHideOn(final HideOn hideOn) {
		this.hideOn = hideOn;
	}

	public Hook getHook() {
		return hook;
	}

	public void setHook(final Hook hook) {
		this.hook = hook;
	}

	public String getJsTipCreate() {
		return jsTipCreate;
	}

	public void setJsTipCreate(final String jsTipCreate) {
		this.jsTipCreate = jsTipCreate;
	}

	public static class HideOn extends AbstractElementBean {

		public HideOn(final Element element) {
			super(element);
		}

		public HideOn() {
			this(null);
		}

		private ETipElement tipElement;

		private EJavascriptEvent event;

		public ETipElement getTipElement() {
			return tipElement == null ? ETipElement.target : tipElement;
		}

		public void setTipElement(final ETipElement tipElement) {
			this.tipElement = tipElement;
		}

		public EJavascriptEvent getEvent() {
			return event == null ? EJavascriptEvent.mouseout : event;
		}

		public void setEvent(final EJavascriptEvent event) {
			this.event = event;
		}
	}

	public static class Hook extends AbstractElementBean {

		public Hook(final Element element) {
			super(element);
		}

		public Hook() {
			this(null);
		}

		private ETipPosition target;

		private ETipPosition tip;

		private boolean mouse;

		public ETipPosition getTarget() {
			return target == null ? ETipPosition.topLeft : target;
		}

		public void setTarget(final ETipPosition target) {
			this.target = target;
		}

		public ETipPosition getTip() {
			return tip == null ? ETipPosition.bottomLeft : tip;
		}

		public void setTip(final ETipPosition tip) {
			this.tip = tip;
		}

		public boolean isMouse() {
			return mouse;
		}

		public void setMouse(final boolean mouse) {
			this.mouse = mouse;
		}
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsTipCreate", "content" };
	}
}
