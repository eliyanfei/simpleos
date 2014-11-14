var barnum = 0;
var d_full = false;
var prenav;
var contextPath;
var sw = document.documentElement.clientWidth;
var sh = document.documentElement.clientHeight;
var winArr = new Array();
var baiduMap = new Array();

function winHidden() {
	for ( var i = 0, n = 0; i < winArr.length; i++) {
		if ($(winArr[i])) {
			$(winArr[i]).style.display = 'none';
		}
	}
};
Array.prototype.del = function(name) {
	for ( var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != name) {
			this[n++] = this[i];
		}
	}
	this.length -= 1
};
var isNULL = function(v) {
	return v == null || typeof (v) == 'undefined';
};
var isUnDef = function(v) {
	return typeof (v) == 'undefined';
};
var isNotZero = function(v) {
	if (isNULL(v)) {
		return false;
	}
	if (v == 0) {
		return false;
	}
	return true;
};
var isNotBlank = function(v) {
	if (isNULL(v)) {
		return false;
	}
	if (v.toLowerCase() == 'null' || v == '') {
		return false;
	}
	return true;
};
var $desktop = {
	hideFaskBar : function(name) {
		if ($(name)) {
			$(name).style.display = 'none';
		}
	},
	putMapIdx : function(bm) {
		baiduMap[bm.idx] = bm;
	},
	getMapByIdx : function(idx) {
		return baiduMap[idx];
	},
	createLeftMove : function(id, width) {
		document.observe("mousemove", function(evt) {
			if (evt.pointer().x <= 0) {
				var left = $(id).style.left.replace('px', '');
				$(id).style.left = '0px';
			}
			if (evt.pointer().x > 73) {
				var left = $(id).style.left.replace('px', '');
				$(id).style.left = '-70px';
			}
		});
	},
	setCookies : function(name, value) {
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString();
	},
	getCookies : function(name, def) {
		var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
		if (arr = document.cookie.match(reg))
			return unescape(arr[2]);
		else
			return def ? def : "";
	},
	delCookies : function(name) {
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = this.getCookies(name);
		if (cval != null)
			document.cookie = name + "=" + cval + ";expires="
					+ exp.toGMTString();
	},

	init : function(sh, sb) {
		this.initHeader(sh);
		this.initBody(sb);
		if ($('showZm_btn')) {
			$('showZm_btn').observe('click', function(ev) {
				if (!d_full) {
					$desktop.fullscreen();
					d_full = true;
				} else {
					$desktop.exitFullscreen();
					d_full = false;
				}
			});
		}

		window.onresize = function() {
			$desktop.positionTab();
		}
	},
	formatDesktopApp : function(tab) {
		var left = 0;
		var top = 0;
		$$('#desktopInnerPanel #' + tab + '__ .desktop_icon').each(function(c) {
			c.style.left = '0px';
			c.style.top = '0px';
			$desktop.move(c, {
				left : left,
				top : top
			});
			top += 87;
			if (top > (sh - 150)) {
				left += 100;
				top = 0;
			}
		});
	},
	initHeader : function(ss) {
		$$(ss).each(function(c) {
			if (prenav == null)
				prenav = c;
			c.idx = barnum;
			c.className = 'ui-navbar';
			c.observe('click', function(ev) {
				prenav.className = "ui-navbar";
				c.className += ' currTab';
				$desktop.hideWin(c);
				prenav = c;
				$('desktopmenu').setAttribute("tabId", c.id);
				$$('#desktopInnerPanel .deskIcon ').each(function(c1) {
					c1.style.display = 'none';
				});
				$(c.id + '__').style.display = '';
				$desktop.setCookies("tabId", c.id);
				$desktop.formatDesktopApp(c.id);
			}.bind(this));
			barnum = barnum + 1;
		});
		prenav.className = "ui-navbar currTab";
	},
	initBody : function(ss) {
		$$(ss).each(function(c) {
			c.style.width = (sw - 85) + 'px';
		});
	},
	hideWin : function(c) {
		var obj = $($('desktopmenu').getAttribute("tabId"));
		if (obj != null) {
			var arrs = obj.getAttribute("appId").split(',');
			for ( var i = 0; i < arrs.length; i++) {
				if ($(arrs[i]))
					$(arrs[i]).style.display = 'none';
			}
		}
		if (c) {
			obj = c;
			var arrs = obj.getAttribute("appId").split(',');
			for ( var i = 0; i < arrs.length; i++) {
				if ($(arrs[i]))
					$(arrs[i]).style.display = '';
			}
		}
	},
	positionTab : function(t) {
		sw = document.documentElement.clientWidth;
		sh = document.documentElement.clientHeight;
		var tab = t || $desktop.getCookies("tabId") || "ui-tab1";
		if (tab) {
			if ($(tab))
				$(tab).click();
		}
		$('desktopPanel').style.minHeight = (document.documentElement.clientHeight - 100) + 'px';
	},
	fullscreen : function() {
		var docElm = document.documentElement;
		if (docElm.requestFullscreen) {
			docElm.requestFullscreen();
		} else if (docElm.mozRequestFullScreen) {
			docElm.mozRequestFullScreen();
		} else if (docElm.webkitRequestFullScreen) {
			docElm.webkitRequestFullScreen();
		}
	},
	exitFullscreen : function() {
		if (document.exitFullscreen) {
			document.exitFullscreen();
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		} else if (document.webkitCancelFullScreen) {
			document.webkitCancelFullScreen();
		}
	},
	playAudio : function(name, run) {
		var gaotielogin = true;
		if (name == 'login.wav') {
			gaotielogin = this.getCookies("login__") == null
					|| this.getCookies("login__") == 'true';
			this.setCookies("login__", 'false');
		}
		if (this.getCookies("playAudio", "true") == 'true' && gaotielogin) {
			$('audioId').innerHTML = "<audio id='audio_sound' autoplay='true' src='"
					+ contextPath
					+ "/frame/template/t100/themes/audio/"
					+ name
					+ "'/>";
		}
		if (run && this.getCookies("playAudio", "true") == 'true') {
			$('audioId').innerHTML = "<audio id='audio_sound' autoplay='true' src='"
					+ contextPath
					+ "/frame/template/t100/themes/audio/"
					+ name
					+ "'/>";
		}
	},
	setSound : function(t) {
		if ($desktop.getCookies('playAudio') == 'true') {
			if ($('audio_sound'))
				$('audio_sound').pause();
			t.src = contextPath + '/frame/template/t100/themes/icon/sound_no_32.png';
			$desktop.setCookies("playAudio", 'false');
		} else {
			t.src = contextPath + '/frame/template/t100/themes/icon/sound_yes_32.png';
			$desktop.setCookies("playAudio", 'true');
		}
	},
	initDesktopConfig : function() {
		if ($desktop.getCookies('announce_') == null) {
			$desktop.setCookies('announce_', 'true');
		}
		if ($desktop.getCookies('announce_') == 'true') {
			this.announce($('handle_open'), false);
			if ($('handle_close')) {
				$('handle_close').toggle();
			}
			if ($('handle_open')) {
				$('handle_open').toggle();
			}
		} else {
			this.announce($('handle_close'), true);
		}

		$('desktopPanel').style.minHeight = (sh - 100) + 'px';
		if ($desktop.getCookies('playAudio') == null) {
			$desktop.setCookies('playAudio', 'true');
		}
		if ($desktop.getCookies('playAudio') == 'false') {
			$('desktop_sound_id').src = contextPath + '/frame/template/t100/themes/icon/sound_no_32.png';
		}
		if ($desktop.getCookies('autotheme_') == 'true' && skins) {
			new PeriodicalExecuter(function(executer) {
				var ti = $desktop.getCookies('autotheme_timer', 0) + 5;
				if (ti >= 120) {
					var n = Math.floor(Math.random() * skins.length);
					$desktop.setCookies('desktop_theme', contextPath
							+ '/frame/template/t100/themes/theme/' + skins[n]);
					document.body.style.backgroundImage = "url(" + contextPath
							+ "/frame/template/t100/themes/theme/" + skins[n]
							+ ")";
					ti = 0
				}
				$desktop.setCookies('autotheme_timer', ti);
			}, 5);
		}
	},
	addEvent : function(func) {
		document.observe("keypress", function(ev) {
			var code = ((ev.which) ? ev.which : ev.keyCode);
			if (code == 33 && ev.shiftKey) {
				$Actions['addUIWin']('tabId=' + $('desktopmenu').getAttribute(
						'tabId'));
				return;
			}
		});
	},
	announce : function(obj, close) {
		if (close) {
			$desktop.move($('panel'), {
				left : sw - 16,
				top : -obj.offsetTop
			});
			$desktop.setCookies("announce_", 'false');
		} else {
			$desktop.move($('panel'), {
				left : sw - 300,
				top : 0
			});
			$desktop.setCookies("announce_", 'true');
		}
		if ($('handle_close')) {
			$('handle_close').toggle();
		}
		if ($('handle_open')) {
			$('handle_open').toggle();
		}
	},
	announceSave : function(obj, close) {
		if (close) {
			$desktop.move(obj, {
				left : sw,
				top : -200
			});
		} else {
			$desktop.move(obj, {
				left : sw - 300,
				top : 0
			});
		}
	},
	move : function(element, position, speed, callback) {
		if (typeof (element) == 'string')
			element = document.getElementById(element);
		if (!element.effect) {
			element.effect = {};
			element.effect.move = 0;
		}
		clearInterval(element.effect.move);
		var speed = speed || 10;
		var start = (function(elem) {
			var posi = {
				left : elem.offsetLeft,
				top : elem.offsetTop
			};
			while (elem = elem.offsetParent) {
				posi.left += elem.offsetLeft;
				posi.top += elem.offsetTop;
			}
			;
			return posi;
		})(element);
		element.style.position = 'absolute';
		var style = element.style;
		var styleArr = [];
		if (typeof (position.left) == 'number')
			styleArr.push('left');
		if (typeof (position.top) == 'number')
			styleArr.push('top');
		element.effect.move = setInterval(
				function() {
					for ( var i = 0; i < styleArr.length; i++) {
						start[styleArr[i]] += (position[styleArr[i]] - start[styleArr[i]])
								* speed / 100;
						style[styleArr[i]] = start[styleArr[i]] + 'px';
					}
					for ( var i = 0; i < styleArr.length; i++) {
						if (Math.round(start[styleArr[i]]) == position[styleArr[i]]) {
							if (i != styleArr.length - 1)
								continue;
						} else {
							break;
						}
						for ( var i = 0; i < styleArr.length; i++)
							style[styleArr[i]] = position[styleArr[i]] + 'px';
						clearInterval(element.effect.move);
						if (callback)
							callback.call(element);
					}
				}, 20);
	},
	fade : function(element, transparency, speed, callback) {
		if (typeof (element) == 'string')
			element = document.getElementById(element);
		if (!element.effect) {
			element.effect = {};
			element.effect.fade = 0;
		}
		clearInterval(element.effect.fade);
		var speed = speed || 1;
		var start = (function(elem) {
			var alpha;
			if (navigator.userAgent.toLowerCase().indexOf('msie') != -1) {
				alpha = elem.currentStyle.filter.indexOf("opacity=") >= 0 ? (parseFloat(elem.currentStyle.filter
						.match(/opacity=([^)]*)/)[1])) + ''
						: '100';
			} else {
				alpha = 100 * elem.ownerDocument.defaultView.getComputedStyle(
						elem, null)['opacity'];
			}
			return alpha;
		})(element);
		if (window.console && window.console.log)
			console.log('start: ' + start + " end: " + transparency);
		element.effect.fade = setInterval(function() {
			start = start < transparency ? Math
					.min(start + speed, transparency) : Math.max(start - speed,
					transparency);
			element.style.opacity = start / 100;
			element.style.filter = 'alpha(opacity=' + start + ')';
			if (Math.round(start) == transparency) {
				element.style.opacity = transparency / 100;
				element.style.filter = 'alpha(opacity=' + transparency + ')';
				clearInterval(element.effect.fade);
				if (callback)
					callback.call(element);
			}
		}, 20);
	}
};
window.windowShow = function(obj) {
	if ($('now_ui_name') && $('now_ui_name').value) {
		winArr.push(obj.id);
		var appIds = $("task_lb").getAttribute('appIds');
		if (appIds.indexOf(obj.id) == -1) {
			$('task_lb').innerHTML += "<div class='task_lb_sub defaultTab' crow='"
					+ $('now_ui_name').value
					+ "' id='"
					+ obj.id
					+ "_' onclick='winHidden();$("
					+ obj.id
					+ ").toggle();'><div class='task_lb_sub_icon'><img src='"
					+ $('ui_' + $('now_ui_name').value).src
					+ "'></div><div class='task_lb_sub_desc' style=''>"
					+ obj.title + "</div></div>";
			$("task_lb").setAttribute('appIds', appIds + "," + obj.id);
		}
		$Actions['desktopClose']();
		$('now_ui_name').value = null;
		var uap = $('desktopmenu').getAttribute("tabId");
		$(uap).setAttribute("appId",
				$(uap).getAttribute("appId") + "," + obj.id);
	}
};
window.windowHide = function(obj) {
	if ($(obj.id + '_')) {
		winArr.del(obj.id);
		var crow = $(obj.id + '_').getAttribute("crow");
		if ($(crow + '__')) {
			$(crow + '__').style.display = '';
		}
		$('task_lb').removeChild($(obj.id + '_'));
		$("task_lb").setAttribute('appIds',
				$("task_lb").getAttribute("appIds").replace("," + obj.id, ""));
		var uap = $('desktopmenu').getAttribute("tabId");
		$(uap).setAttribute('appId',
				$(uap).getAttribute("appId").replace("," + obj.id, ""));
	}
};
