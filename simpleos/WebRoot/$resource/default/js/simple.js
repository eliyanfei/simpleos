/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
var $ready = function(callback) {
	if (document.loaded)
		callback.delay(0.1, document);
	else
		document.observe('dom:loaded', callback);
};

var $$Form = function(form) {
	var oForm = $(form);
	if (oForm) {
		return Form.Methods.serialize(oForm);
	} else {
		var str = "", oForms = $$(form);
		for ( var i = 0; i < oForms.length; i++) {
			if (i > 0)
				str += "&";
			str += Form.Methods.serialize(oForms[i]);
		}
		return str;
	}
};

(function() {
	Object.extend(Browser, Prototype.Browser);

	Object.extend(Browser, {
		effects : IS_EFFECTS && !Object.isUndefined(window.Effect),

		WebKit419 : Browser.WebKit && !document.evaluate
	});

	if (Browser.IE) {
		Browser.IEVersion = parseFloat(navigator.appVersion.split(';')[1].strip()
				.split(' ')[1]);
		if (Browser.IEVersion <= 6.0) {
			$ready(function() {
				try {
					document.execCommand("BackgroundImageCache", false, true);
				} catch (e) {
				}
			});
		}
	}
	
	var $bak = $;
	window.$ = function(element) {
		if (typeof element == "string" && element.length > 0
				&& (element[0] == '.' || element[0] == '#')) {
			var elements = $$(element);
			return elements.length > 0 ? elements[0] : undefined;
		} else {
			var elements;
			return $bak(element)
					|| ((elements = $$(element)).length > 0 ? elements[0] : undefined);
		}
	}; 
})();

Object.extend($Actions, {
	callSafely : function(act, parameters, callback) {
		if (!act)
			return;
		var action = typeof act == "string" ? $Actions[act] : act;
		var exec = function() {
			if (callback && callback(action))
				return;
			action(parameters);
		};
		if (action) {
			exec.defer();
		} else {
			var i = 0;
			new PeriodicalExecuter(function(executer) {
				if (action || i++ > 10) {
					executer.stop();
					if (action) 
						exec();
				}
			}, 0.5);
		}
	},
	
	valueBinding : function(json, callback) {
		var f = function(key) {
			var element = $(key);
			if (!element) {
				var selector = $$(key);
				if (selector && selector.length > 0) {
					element = selector[0];
				}
			}
			var v = json[key];
			if (element && v) {
				this.setValue(element, v);
			}
		}.bind(this);
		(function() {
			Object.keys(json).each(f);
			$eval(callback);
		}).delay(0.1);
	},

	setValue : function(element, v, insert) {
		element = $(element);
		if (!element)
			return;
		if (element.htmlEditor) {
			if (insert) {
				if (Object.isElement(v)) {
					element.htmlEditor.insertElement(CKEDITOR.dom.element.get(v));
				} else {
					element.htmlEditor.insertHtml(v);
				}
			} else {
				element.htmlEditor.setData(v);
			}
		} else {
			if (element.tagName.toUpperCase() == "TEXTAREA" && insert) {
				if (document.selection) { // ie
					element.focus();
					sel = document.selection.createRange();
					sel.text = v;
				} else if (element.selectionStart || element.selectionStart == '0') {
					var start = element.selectionStart;
					var end = element.selectionEnd;
					var v2 = element.value;
					element.setValue(v2.substring(0, start) + v
							+ v2.substring(end, v2.length));
				} else {
					element.setValue(element.value + v);
				}
			} else {
				element.setValue(v);
			}
		}
	},

	addHidden : function(form, params) {
		if (!form || !params) {
			return;
		}
		$H(params.toQueryParams()).each(
				function(pair) {
					var hidden = form.down("#" + pair.key);
					if (Object.isElement(hidden)
							&& hidden.tagName.toUpperCase() == "INPUT"
							&& hidden.type == "hidden") {
						hidden.value = pair.value;
					} else {
						form.insert(new Element("INPUT", {
							type : "hidden",
							id : pair.key,
							name : pair.key,
							value : pair.value
						}));
					}
				});
	},

	visibleToggle : function(element) {
		if (Object.isString(element)) {
			$$(element).each(function(eobj) {
				eobj.toggle();
			});
		} else if (Object.isElement(element)) {
			element.toggle();
		}
	},

	readonly : function(element) {
		if (Object.isString(element)) {
			$$(element).each(function(eobj) {
				$Actions.readonly(eobj);
			});
		} else if (Object.isElement(element)) {
			if (/input|textarea/i.test(element.tagName)) {
				element.readOnly = true;
			} else {
				Form.getElements(element).each(function(eobj) {
					$Actions.readonly(eobj);
				});
			}
		}
	},

	disable : function(element) {
		this._enable_disable(element);
	},

	enable : function(element) {
		this._enable_disable(element, true);
	},

	_enable_disable : function(element, enable) {
		var method = enable ? "enable" : "disable";
		if (Object.isString(element)) {
			$$(element).each(function(eobj) {
				$Actions[method](eobj);
			});
		} else if (Object.isElement(element)) {
			if (/input|select|textarea|button/i.test(element.tagName)) {
				element[method]();
			} else {
				Form.Methods[method](element);
			}
		}
	},

	showError : function(err, alertType) {
		if (!window.UI || !window.UI.Window || alertType) {
			alert(err.title);
		} else {
			if (!this.errWindows) {
				this.errWindows = {};
			}
			var _WIDTH = 600;
			var _HEIGHT = 150;
			var w;
			if (this.errWindows[err.hash]) {
				w = this.errWindows[err.hash];
			} else {
				w = this.errWindows[err.hash] = new UI.Window({
					minimize : false,
					maximize : false,
					width : _WIDTH,
					height : _HEIGHT
				});
				w.observe("hidden", function() {
					delete this.errWindows[err.hash];
				}.bind(this));
			}

			var detail = new Element("div");
			var c = new Element("div", {
				className : "sy_error_dialog"
			}).insert(new Element("div", {
				className : "simple_toolbar et wrap_text"
			}).update(err.title.convertHtmlLines())).insert(detail);

			var right = new Element("div", {
				style : "float: right;"
			});

			c.insert(new Element("div", {
				className : "eb"
			}));

			w.setHeader($MessageConst["Error.Title"]).setContent(c);
			w.center().observe("shown", function() {
				w.adapt().center().activate();
			}).show(true);
		}
	},

	observeSubmit : function(selector, sFunc) {
		$elements(selector).invoke("observe", "keydown", function(ev) {
			var code = (ev.which) ? ev.which : ev.keyCode;
			if (code == Event.KEY_RETURN) {
				sFunc(Event.element(ev));
			}
		});
	}
});

window.$elements = function(object) {
	if (Object.isString(object)) {
		return $$(object);
	} else if (Object.isArray(object)) {
		return object;
	} else if (Object.isElement(object)) {
		if (object = $(object)) {
			var r = [];
			r.push(object);
			return r;
		}
	}
	return [];
};

Object.extend(Form.Element.Serializers, {
	textarea : function(element, value) {
		if (Object.isUndefined(value)) {
			if (element.htmlEditor) {
				element.htmlEditor.updateElement();
			}
			return element.value;
		} else {
			element.value = value;
		}
	}
});

/** String */
Object.extend(String.prototype, {
	addParameter : function(parameters) {
		if (!parameters || parameters == '') {
			return this.toString();
		}

		var p = this.indexOf('?');
		var request;
		var query;
		if (p > -1) {
			request = this.substring(0, p);
			query = this.substring(p + 1);
		} else {
			var isQueryString = this.indexOf('=') > 0;
			if (isQueryString) {
				request = '';
				query = this.toString();
			} else {
				request = this.toString();
				query = '';
			}
		}

		var s = $H(query.toQueryParams());
		var d = $H(parameters.toQueryParams());
		if (request.length > 0)
			request += '?';
		return request + Object.toQueryString(s.merge(d));
	},

	addSelectorParameter : function(selector) {
		var str = this.toString();
		if (selector) {
			if (Object.isElement(selector)) {
				str = str.addParameter($$Form(selector));
			} else {
				var elements = $$(selector);
				if (elements) {
					for ( var i = 0; i < elements.length; i++)
						str = str.addParameter($$Form(elements[i]));
				}
			}
		}
		return str;
	},

	makeElement : function() {
		var wrapper = new Element('div');
		wrapper.innerHTML = this.toString();
		return wrapper.down();
	},

	camelcase : function() {
		var string = this.dasherize().camelize();
		return string.charAt(0).toUpperCase() + string.slice(1);
	}
});

var $Comp = {
	setBrowserTitle : function(str, append) {
		document.title = append ? document.title + " - " + str : str;
	},

	createLink : function(text, options, click) {
		options = Object.extend({
			href : (Browser.IEVersion && Browser.IEVersion <= 6.0) ? "###"
					: "javascript:void(0);"
		}, options || {});
		if (Browser.IEVersion && Browser.IEVersion <= 7.0) {
			options = Object.extend(options, {
				hideFocus : true
			});
		}
		var a = new Element("a", options);
		if (text)
			a.update(text);
		if (click)
			a.observe("click", click);
		return a;
	},

	addStyleSheet : function(css) {
		var style = new Element('style', {
			type : 'text/css',
			media : 'screen'
		});
		$(document.getElementsByTagName('head')[0]).insert(style);
		if (style.styleSheet)
			style.styleSheet.cssText = css;
		else
			style.appendText(css);
		return style;
	},

	getPopup : function(popup) {
		var e = document.getEvent();
		var trigger;
		if (!e || !(trigger = Event.element(e)))
			return [ 0, 0 ];

		if (window.$target)
			trigger = $target(trigger);

		var tl = trigger.cumulativeOffset();
		var valueT = tl.top, valueL = tl.left;

		valueT = valueT + trigger.getDimensions().height;

		tl = trigger.cumulativeScrollOffset();
		valueT -= tl.top, valueL -= tl.left;

		var bodyDelta = 35;
		var vpDim = document.viewport.getDimensions();
		var bodyWidth = vpDim.width - bodyDelta;
		var bodyHeight = vpDim.height - bodyDelta;

		var popupWidth, popupHeight;
		if (Object.isElement(popup)) {
			popup = $(popup);
			popupWidth = popup.getWidth();
			popupHeight = popup.getHeight();
		} else {
			popupWidth = popup.width;
			popupHeight = popup.height;
		}

		if (valueL + popupWidth > bodyWidth) {
			var d = bodyWidth - popupWidth;
			valueL = d < 0 ? 2 : d;
		}
		if (valueT + popupHeight > bodyHeight) {
			var d = bodyHeight - popupHeight;
			valueT = d < 0 ? 2 : d;
		}

		var vpOff = document.viewport.getScrollOffsets();
		valueT += vpOff.top;
		valueL += vpOff.left;
		return [ valueL, valueT ];
	},

	imageToggle : function(image, target, options) {
		if (!(image = $(image)))
			return;
		if (!(target = $(target))) {
			target = image.up().next();
		}
		var src = image.src;
		if (!src)
			return;

		image.setStyle("cursor:pointer;border:0px");
		options = Object.extend({
			cookie : true,
			open : true,
			onShow : Prototype.emptyFunction,
			onHide : Prototype.emptyFunction
		}, options);

		var p = src.lastIndexOf("/") + 1;
		var path = src.substring(0, p);
		var file = src.substring(p);
		var doImage = function() {
			if (target.visible()) {
				if (!file.startsWith("p_")) {
					file = "p_" + file;
				}
			} else {
				if (file.startsWith("p_")) {
					file = file.substring(2);
				}
			}
			image.src = path + file;
			if (options.cookie) {
				document.setCookie("toggle_" + image.identify(), target.visible());
			}
		};
		if (options.cookie) {
			var s = document.getCookie("toggle_" + image.identify());
			if (s == "true") {
				target.show();
			} else if (s == "false") {
				target.hide();
			} else {
				if (options.open) {
					target.show();
				} else {
					target.hide();
				}
			}
		}
		image.observe("click", function() {
			if (target.visible()) {
				target.$hide2({
					afterFinish : function() {
						if (options.onHide)
							options.onHide();
						doImage();
					}
				});
			} else {
				target.$show2({
					afterFinish : function() {
						if (options.onShow)
							options.onShow();
						doImage();
					}
				});
			}
		});
		doImage();
	},

	textButton : function(textId, bFunc) {
		var element = new Element("div", {
			className : "text clear_float",
			style : "padding:0;margin:0;width:100%;"
		});
		element.textObject = new Element("input", {
			type : "text",
			style : "width:100%;border:0px;"
		});
		if (textId) {
			element.textObject.id = element.textObject.name = textId;
		}
		element.buttonObject = new Element("div", {
			className : "textButton",
			style : "float: right; width: 20px; margin-top: 1px;"
		});
		if (bFunc) {
			element.buttonObject.observe("click", bFunc);
		}
		return element.insert(new Element("div", {
			style : "float: left; margin: 0 -20px 0 0; width: 100%;"
		}).insert(new Element("div", {
			style : "margin: 0px 20px 0 0;"
		}).insert(element.textObject))).insert(element.buttonObject);
	},

	textFileButton : function() {
		var tb = this.textButton();
		var f = new Element("input", {
			type : "file",
			style : "position:absolute;top:0;right:0;",
			hidefocus : "hidefocus",
			size : "1"
		}).setOpacity(0);
		f.observe("change", function(e) {
			tb.textObject.value = f.value;
		});
		tb.textObject.observe("change", function(e) {
			if (tb.textObject.value == '') {
				var up = f.up();
				new Element("form").insert(f).reset();
				up.insert(f);
			}
		});
		var ele = new Element("div", {
			style : "position:relative;"
		}).insert(tb).insert(f);
		ele.file = f;
		ele.textButton = tb;
		return ele;
	},

	addBackgroundTitle : function(txt, TITLE) {
		txt = $(txt);
		if (!txt)
			return;
		var c = txt.getStyle("color");
		var f = function() {
			txt.setValue(TITLE);
			txt.setStyle("color: #aaa;");
		};
		f();
		txt.observe("focus", function(ev) {
			if ($F(txt) == TITLE) {
				txt.setValue("");
				txt.setStyle("color:" + c);
			}
		});
		txt.observe("blur", function(ev) {
			if ($F(txt) == "") {
				f();
			}
		});
	},

	addReturnEvent : function(txt, func) {
		txt.observe("keypress", function(ev) {
			if (((ev.which) ? ev.which : ev.keyCode) != Event.KEY_RETURN) {
				return;
			}
			if (func)
				func(ev);
		});
	},

	searchButton : function(sFunc, aFunc, inputText, inputWidth, buttonText) {
		if (!inputText)
			inputText = $MessageConst["Text.Search"];
		if (!buttonText)
			buttonText = $MessageConst["Button.Search"];
		var sp = new Element("SPAN", {
			className : "sech_pane"
		});
		var t1 = new Element("input", {
			className : "txt",
			type : "text"
		});
		inputWidth = parseInt(inputWidth);
		if (inputWidth > 0) {
			t1.setStyle("width:" + inputWidth + "px");
		}
		if (inputText) {
			this.addBackgroundTitle(t1, inputText);
		}
		if (sFunc) {
			this.addReturnEvent(t1, function(ev) {
				var v = $F(t1);
				if (v == "" || v == inputText) {
					return;
				}
				sFunc(sp, ev);
			});
		}
		var b1 = new Element("SPAN", {
			className : "br"
		}).update(buttonText);
		if (sFunc) {
			b1.observe("click", function(ev) {
				var v = $F(t1);
				if (v == "" || v == inputText) {
					return;
				}
				sFunc(sp, ev);
			});
		}
		var b2 = new Element("SPAN", {
			className : "ar"
		});
		if (aFunc) {
			b2.observe("click", function(ev) {
				aFunc(sp, ev);
			});
		}
		return sp.insert(t1).insert(new Element("SPAN", {
			className : "btn"
		}).insert(b1).insert(b2));
	},

	createTable : function(options) {
		options = Object.extend({
			width : "100%",
			cellpadding : "0",
			cellspacing : "0"
		}, options || {});
		return new Element("table", options).insert(new Element("tbody"));
	},

	createSplitbar : function(bar, left, callback) {
		if (!(bar = $(bar)) || !(left = $(left))) {
			return;
		}
		var p, w, ow = left.getWidth();
		bar.observe("dblclick", function(evt) {
			left.setStyle("width: " + ow + "px");
		});
		bar.observe("mousedown", function(evt) {
			$(document.body).fixOnSelectStart();
			p = evt.pointer();
			w = left.getWidth();
		});
		document.observe("mouseup", function(evt) {
			if (p) {
				$(document.body).fixOnSelectStart(true);
				p = null;
			}
		});
		document.observe("mousemove", function(evt) {
			if (p) {
				var nw = w - p.x + evt.pointer().x;
				if (nw < 50 || nw > (document.viewport.getWidth() - 50))
					return;
				left.setStyle("width: " + nw + "px");
				$eval(callback);
				bar.fire("size:splitbar");
				Event.stop(evt);
			}
		});
	},

	initDragDrop : function(containerId, hoverclass, textFunc) {
		containerId = $(containerId);
		if (!containerId)
			return;
		containerId.select(".drag_image").each(function(a) {
			new Draggable(a, {
				revert : true,
				scroll : window,
				onStart : function() {
					a.addClassName("drag_tooltip");
					if (textFunc)
						a.update(textFunc(a));
					// backup drops
					a.drops = Droppables.drops.clone();
					Droppables.drops = Droppables.drops.reject(function(d) {
						return d.hoverclass != hoverclass;
					});
				},
				onEnd : function() {
					a.removeClassName("drag_tooltip");
					a.update("");
					Droppables.drops = a.drops;
				}
			});
		});
	},

	fixMaxWidth : function(selector, maxWidth) {
		$elements(selector).each(function(c) {
			c.select("img, table").each(function(ele) {
				if (ele.getWidth() > maxWidth) {
					ele.wrap(new Element("DIV", {
						style : "width: " + maxWidth + "px; overflow-x:auto;"
					}));
				}
			});
		});
	}
};

(function() {
	var initPointer, currentDraggable, dragging;

	document.observe('mousedown', onMousedown);

	function onMousedown(event) {
		var draggable = event.findElement('[draggable="true"]');
		if (!draggable) {
			return;
		}
		var element = event.element();
		if (element && element.tagName == "DIV") {
			event.stop();
			currentDraggable = draggable;
			initPointer = event.pointer();

			if (document.body.setCapture)
				document.body.setCapture();
			document.observe("mousemove", onMousemove).observe("mouseup", onMouseup);
		}
	}

	function onMousemove(event) {
		event.stop();

		if (dragging)
			fire('drag:updated', event);
		else {
			dragging = true;
			fire('drag:started', event);
		}
	}

	function onMouseup(event) {
		if (document.body.releaseCapture)
			document.body.releaseCapture();
		document.stopObserving('mousemove', onMousemove).stopObserving('mouseup',
				onMouseup);

		if (dragging) {
			dragging = false;
			fire('drag:ended', event);
		}
	}

	function fire(eventName, mouseEvent) {
		var pointer = mouseEvent.pointer();

		currentDraggable.fire(eventName, {
			dx : pointer.x - initPointer.x,
			dy : pointer.y - initPointer.y,
			mouseEvent : mouseEvent
		});
	}

	document.observe("keydown", function(ev) {
		var code = (ev.which) ? ev.which : ev.keyCode;
		if (ev.ctrlKey && code == Event.KEY_RETURN) {
			var arr = $$("input[key=ctrlReturn]");
			if (arr && arr.length > 0) {
				arr[0].click();
			}
		}
	});
})();

/** Element */
Element.addMethods({
	getScrollDimensions : function(element) {
		return {
			width : element.scrollWidth,
			height : element.scrollHeight
		};
	},

	setDimensions : function(element) {
		var h, w;
		var opt = arguments[1];
		if (typeof opt == "object") {
			h = opt.height;
			w = opt.width;
		} else {
			h = opt;
			w = arguments[2];
		}
		var f = function(p) {
			return Object.isNumber(p) ? p + "px" : (Object.isString(p) ? (p
					.strip().endsWith("%") ? p : parseInt(p) + "px") : "auto");
		};
		element.setStyle({
			height : f(h),
			width : f(w)
		});
	},

	getScrollOffsets : function(element) {
		return Element._returnOffset(element.scrollLeft, element.scrollTop);
	},

	setScrollOffsets : function(element, offset) {
		if (arguments.length == 3)
			offset = {
				left : offset,
				top : arguments[2]
			};
		element.scrollLeft = offset.left;
		element.scrollTop = offset.top;
		return element;
	},

	appendText : function(element, text) {
		text = String.interpret(text);
		element.appendChild(document.createTextNode(text));
		return element;
	},

	createDrag : function(element) {
		element.enableDrag();
		var t = 0, l = 0;
		element.observe("drag:started", function(p) {
			t = parseInt(element.style.top);
			l = parseInt(element.style.left);
		}).observe("drag:updated", function(p) {
			element.style.top = t + p.memo.dy + "px";
			element.style.left = l + p.memo.dx + "px";
		});
	},

	enableDrag : function(element) {
		element.writeAttribute('draggable', 'true');
		return element;
	},

	disableDrag : function(element) {
		element.writeAttribute('draggable', null);
		return element;
	},

	isDraggable : function(element) {
		return element.readAttribute('draggable') == 'true';
	},

	fixOnSelectStart : function(element, remove) {
		if (remove) {
			if (Browser.Gecko) {
				element.style.removeProperty("-moz-user-select");
			} else {
				element.removeAttribute("onselectstart");
			}
		} else {
			if (Browser.Gecko) {
				element.setStyle("-moz-user-select: none;");
			} else {
				element.setAttribute("onselectstart", "return false;");
			}
		}
	},

	setPngBackground : function(element, src) {
		var f = Object.extend({
			align : "top left",
			repeat : "no-repeat",
			sizingMethod : "scale",
			backgroundColor : ""
		}, arguments[2] || {});
		element.setStyle((Browser.IEVersion && Browser.IEVersion <= 6.0) ? {
			filter : "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
					+ src + "'', sizingMethod='" + f.sizingMethod + "')"
		} : {
			background : f.backgroundColor + " url(" + src + ") " + f.align + " "
					+ f.repeat
		});
		return element;
	},

	observeImageLoad : function(img, callback) {
		if (img.tagName !== 'IMG')
			return;
		img.onreadystatechange = img.onload = function() {
			var state = img.readyState;
			if (callback
					&& ((state && (state == "complete" || state == "loaded")) || img.complete == true)) {
				callback();
			}
		};
	},

	clearImageLoad : function(img) {
		img.onreadystatechange = img.onload = null;
	},

	$visible : function(element) {
		while (element.tagName != 'BODY') {
			if (!$(element).visible())
				return false;
			element = element.parentNode;
		}
		return true;
	},

	$remove : function(element) {
		element.$hide2({
			duration : 0.3,
			afterFinish : function() {
				element.remove();
			}
		});
	},

	$insert : function(element, insert) {
		if (Object.isString(insert)) {
		}
	},

	$style : function(element, style, options) {
		options = options || {};
		if (options.effects || Browser.effects) {
			new Effect.Morph(element, Object.extend({
				"duration" : 0.4,
				"style" : style
			}, options));
		} else {
			element.setStyle(style);
			$eval(options.afterFinish);
		}
	},

	$show : function(element, options) {
		if (element.visible()) {
			return;
		}
		options = options || {};
		if (options.effects || Browser.effects) {
			element.appear(Object.extend({
				duration : 0.2
			}, options));
		} else {
			element.show();
			$eval(options.afterFinish);
		}
	},

	$hide : function(element, options) {
		if (!element.visible()) {
			return;
		}
		options = options || {};
		if (options.effects || Browser.effects) {
			element.fade(Object.extend({
				duration : 0.1
			}, options));
		} else {
			element.hide();
			$eval(options.afterFinish);
		}
	},

	$toggle : function(element, options) {
		element[element.visible() ? "$hide" : "$show"](options);
	},

	$show2 : function(element, options) {
		if (element.visible()) {
			return;
		}
		options = options || {};
		if (options.effects || Browser.effects) {
			element.slideDown(Object.extend({
				duration : 0.2
			}, options));
		} else {
			element.show();
			$eval(options.afterFinish);
		}
	},

	$hide2 : function(element, options) {
		if (!element.visible()) {
			return;
		}
		options = options || {};
		if (options.effects || Browser.effects) {
			element.blindUp(Object.extend({
				duration : 0.2
			}, options));
		} else {
			element.hide();
			$eval(options.afterFinish);
		}
	},

	$shake : function(element, options) {
		options = options || {};
		if (options.effects || Browser.effects) {
			var win = $win(element);
			if (win) {
				win.content.setStyle("overflow: hidden;");
			}
			element.shake(Object.extend({
				duration : 0.5
			}, options || {}));
			(function() {
				if (win) {
					win.content.setStyle("overflow: auto;");
				}
			}).delay(0.6);
		}
	},

	$highlight : function(element, color, options) {
		if (element.set_style) {
			return;
		}
		element.set_style = true;
		options = options || {};
		if (options.effects || Browser.effects) {
			var aFinish = options.afterFinish;
			options.afterFinish = function() {
				$eval(aFinish);
				element.set_style = false;
			};
			var ocolor = element.getStyle('background-color').parseColor(
					"#FFFFFF");
			element.highlight(Object.extend({
				duration : 1,
				startcolor : ocolor,
				endcolor : color
			}, options));
		} else {
			var bg = element.getStyle('background');
			element.setStyle("background: " + color);
			(function() {
				element.setStyle("background: " + (bg ? bg : "none"));
				$eval(options.afterFinish);
				element.set_style = false;
			}).delay(1);
		}
	},

	$opacity : function(element, options) {
		options = options || {};
		if (options.effects || Browser.effects) {
			new Effect.Opacity(element, Object.extend({
				duration : 0.1,
				transition : Effect.Transitions.linear
			}, options || {}));
		}
	}
});

Object.extend(document.viewport, {
	setScrollOffsets : function(offset) {
		Element.setScrollOffsets(Prototype.Browser.WebKit ? document.body
				: document.documentElement, offset);
	},

	getScrollDimensions : function() {
		return Element.getScrollDimensions(Prototype.Browser.WebKit ? document.body
				: document.documentElement);
	}
});

(function() {
	var eventMatchers = {
		'HTMLEvents' : /^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,
		'MouseEvents' : /^(?:click|mouse(?:down|up|over|move|out))$/
	};
	var defaultOptions = {
		pointerX : 0,
		pointerY : 0,
		button : 0,
		ctrlKey : false,
		altKey : false,
		shiftKey : false,
		metaKey : false,
		bubbles : true,
		cancelable : true
	};

	Event.simulate = function(element, eventName) {
		var options = Object.extend(defaultOptions, arguments[2] || {});
		var oEvent, eventType = null;

		element = $(element);

		for (var name in eventMatchers) {
			if (eventMatchers[name].test(eventName)) {
				eventType = name;
				break;
			}
		}

		if (!eventType)
			throw new SyntaxError(
					'Only HTMLEvents and MouseEvents interfaces are supported');

		if (document.createEvent) {
			oEvent = document.createEvent(eventType);
			if (eventType == 'HTMLEvents') {
				oEvent.initEvent(eventName, options.bubbles, options.cancelable);
			} else {
				oEvent.initMouseEvent(eventName, options.bubbles, options.cancelable,
						document.defaultView, options.button, options.pointerX,
						options.pointerY, options.pointerX, options.pointerY,
						options.ctrlKey, options.altKey, options.shiftKey, options.metaKey,
						options.button, element);
			}
			element.dispatchEvent(oEvent);
		} else {
			options.clientX = options.pointerX;
			options.clientY = options.pointerY;
			oEvent = Object.extend(document.createEventObject(), options);
			element.fireEvent('on' + eventName, oEvent);
		}
		return element;
	};

	Element.addMethods({
		simulate : Event.simulate
	});
})();
