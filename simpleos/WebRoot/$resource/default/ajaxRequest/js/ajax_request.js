/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
$Comp.AjaxRequest = Class.create({
	initialize : function(container, responseText, id, isJSON) {
		if (!responseText || isJSON)
			return;
		
		var heads = $$('head');
		if (!heads || heads.length == 0)
			return;
		this.head = heads[0];

		container = $(container);
		if (container) {
			container.update(responseText.stripStylesheets().stripScripts());
		}

		this.textScript = '';
		var jsFiles = [];
		var scripts = responseText
				.match(new RegExp(Prototype.ScriptFragment, 'img'))
				|| [];
		for ( var i = 0; i < scripts.length; i++) {
			var s = scripts[i];
			var script;
			try {
				script = document.createElement(s);
				script.text = s.extractScripts();
			} catch (e) {
				script = s.makeElement();
			}

			if (script.getAttribute("src") || script.getAttribute("id")) {
				jsFiles.push(script);
			} else if (script.text) {
				this.textScript += script.text;
			}
		}

		if (!this.textScript.blank()) {
			this.textScript = this.textScript.replace(/[\r\n\t]/g, "");
		}

		this.addScript(jsFiles, id);
		this.addStylesheet(responseText.match(StylesheetFragment));
	},

	createScriptElement : function() {
		var script = document.createElement("script");
		script.type = "text/javascript";
		this.head.appendChild(script);
		return script;
	},

	addScript : function(scripts, id) {
		if (scripts.length == 0) {
			if (!this.textScript.blank()) {
				var update = this.getUpdateByID(id);
				if (update) {
					update.remove();
				}

				update = this.createScriptElement();
				if (id)
					update.id = id;
				update.text = this.textScript;
			}
		} else {
			var script = scripts.removeAt(0);
			var src = script.getAttribute("src");
			if (src) {
				var update = $$("SCRIPT").find(function(_script) {
					var _src = _script.getAttribute("src");
					if (_src && this.getUrl(_src) == this.getUrl(src)) {
						return true;
					}
				}.bind(this));
				if (!update) {
					update = this.createScriptElement();
					update.done = "doing";
					if (update.readyState) {
						update.onreadystatechange = function() {
							var s = update.readyState;
							if (s == "loaded" /* || s == "complete" */) {
								update.onreadystatechange = null;
								update.done = "done";
								this.addScript(scripts, id);
							}
						}.bind(this);
					} else {
						update.onload = function() {
							update.done = "done";
							this.addScript(scripts, id);
						}.bind(this);
					}
					update.src = src;
				} else {
					if (!update.done || update.done == "done") {
						this.addScript(scripts, id);
					} else {
						var i = 0;
						new PeriodicalExecuter(function(executer) {
							if (update.done == "done" || i++ > 50) {
								this.addScript(scripts, id);
								executer.stop();
								executer = null;
							}
						}.bind(this), 0.1);
					}
				}
			} else {
				var jsCode = script.text;
				if (!jsCode.blank()) {
					var update = this.getUpdateByID(script.id);
					if (update) {
						update.remove();
					}
					update = this.createScriptElement();
					update.id = script.id;
					update.text = jsCode;
				}
				this.addScript(scripts, id);
			}
		}
	},

	getUpdateByID : function(id) {
		if (id) {
			return $$("SCRIPT").find(function(_script) {
				var _id = _script.getAttribute("id");
				if (_id && _id == id) {
					return true;
				}
			});
		}
	},

	getUrl : function(s) {
		if (!Object.isString(s))
			return;
		var p = s.indexOf("?");
		return ((p < 0) ? s : s.substring(0, p)).toUpperCase();
	},

	addStylesheet : function(stylesheets) {
		if (!stylesheets)
			return;
		for ( var i = 0; i < stylesheets.length; i++) {
			var s = stylesheets[i];
			var stylesheet;
			try {
				stylesheet = document.createElement(s);
			} catch (e) {
				stylesheet = s.makeElement();
			}
			var href = stylesheet.getAttribute("href");
			if (!href) {
				continue;
			}
			var update = $$("LINK").find(function(l) {
				var _href = l.getAttribute("href");
				if (_href && this.getUrl(_href) == this.getUrl(href)) {
					return true;
				}
			}.bind(this));
			if (!update) {
				this.head.appendChild(stylesheet);
			}
		}
	}
});

var $Loading = {
	id : 'ajax_request_loading',
	
	show : function(modal) {
		var ele = $(this.id);
		var f = function() {
			ele.style.top = (document.viewport.getScrollOffsets().top + 4) + "px";
		};
		if (!ele) {
			ele = new Element('div', {
				style : 'display: none;'
			}).appendText($MessageConst['$Loading.0']);
			ele.id = this.id;
			document.body.appendChild(ele);
			Event.observe(window, 'scroll', function() {
				if (ele.visible()) {
					f();
				}
			});
			ele.modalOverlay = new Element('div', {
				className : "loading_overlay",
				style : "display: none;"
			});
			document.body.appendChild(ele.modalOverlay);
		}
		if (ele.executer) {
			ele.executer.stop();
			ele.executer = null;
		}
		if (!ele.visible()) {
			f();
			ele.showing = true;
			if (modal) {
				ele.modalOverlay.setStyle("height: " + 
						document.viewport.getScrollDimensions().height + "px");
				ele.modalOverlay.show();
			}
			ele.$show({
				afterFinish : function() {			
					ele.showing = false;
				}
			});
		}
	},

	hide : function() {
		var ele = $(this.id);
		if (!ele)
			return;
		if (!ele.executer) {
			ele.executer = new PeriodicalExecuter(function() {
				if (ele.showing) {
					return;
				}
				ele.executer.stop();
				ele.executer = null;
				ele.hide();
				if (ele.modalOverlay.visible()) {
					ele.modalOverlay.hide();
				}
			}, 0.1);
		}
	}
};

function __ajax_actions_init(actionFunc, name) {
	var triggerAct = function(disabled) {
		if (!actionFunc.trigger) {
			return;
		}
		actionFunc.trigger.disabled = disabled;
	};

	actionFunc.doInit = function(containerId, confirmMessage, parallel,
			showLoading, loadingModal, disabledTriggerAction) {
		var ev = document.getEvent();
		if (ev) {
			actionFunc.trigger = Event.element(ev);
		}
		var c = $(actionFunc.container);
		if (!c && containerId)
			c = $(containerId);
		if (c && actionFunc.cache) {
			c.update(actionFunc.cache.stripStylesheets());
			return true;
		}
		if (confirmMessage && !confirm(confirmMessage)) {
			return true;
		}
		if (!parallel && actionFunc.loading) {
			return true;
		}
		if (showLoading) {
			$Loading.show(loadingModal);
		}
		if (!parallel) {
			actionFunc.loading = true;
		}
		actionFunc.disabledTriggerAction = disabledTriggerAction;
		if (disabledTriggerAction) {
			triggerAct(true);
		}
	};

	actionFunc.doComplete = function(req, containerId, updateContainerCache,
			alert) {
		try {
			var rJSON = req.responseText.evalJSON();
			var rt = rJSON.rt;
			var c = $(actionFunc.container);
			if (!c && containerId)
				c = $(containerId);
			if (c && updateContainerCache) {
				actionFunc.cache = rt;
			}

			new $Comp.AjaxRequest(c, rt, rJSON.id, rJSON.isJSON);

			var cb = function(req, responseText, json) {
				var err = json["exception"];
				if (err) {
					$Actions.showError(err, alert);
					return;
				}
				if (!rJSON.cb)
					return;
				if (actionFunc.doCallback) {
					actionFunc.doCallback(req, responseText, json);
				}
			};
			var json = rt.isJSON() ? rt.evalJSON() : {};
			cb.defer(req, rt, json);
		} finally {
			actionFunc.doLoaded(req);
		}
	};

	actionFunc.doLoaded = function(req) {
		$Loading.hide();
		actionFunc.loading = false;
		if (actionFunc.disabledTriggerAction) {
			triggerAct();
		}
	};
}
