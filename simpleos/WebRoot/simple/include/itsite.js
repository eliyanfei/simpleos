var PagerUtil = {};
PagerUtil.rowId = function(o) {
	return (o = PagerUtil.row(o)) ? o.getAttribute("rowId") : null;
};
PagerUtil.attr = function(o, attr) {
	return (o = PagerUtil.row(o)) ? o.getAttribute(attr) : null;
};
PagerUtil.row = function(o) {
	return o.hasClassName("titem") ? o : $target(o).up(".titem");
};
PagerUtil.each = function(o) {
	var ele = $target(o).up(".titem");
	while (ele != null) {
		if (ele.hasClassName("titem")) {
			return ele;
		} else {
			ele = ele.parentNode;
		}
	}
	return o;
};
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
function getCookie(name, value) {
	var strCookie = document.cookie;
	var arrCookie = strCookie.split(";");
	for ( var i = 0; i < arrCookie.length; i++) {
		var arr = arrCookie[i].split("=");
		if (arr[0].trim() == name)
			return arr[1];
	}
	return value;
};
function openUrl(url) {
	window.open(url, '_blank');
}
function openUrl1(url, callback) {
	window.open(url, '_self');
	callback();
}
function addTextButton(id, action, td, isDate, value) {
	var ele = $Comp.textButton(id, function(ev) {
		if (isDate)
			$Actions[action].show();
		else
			$Actions[action]();
	});
	$Actions.readonly(ele.textObject);
	$(td).update(ele);
	if (arguments.length == 5) {
		$(id).value = value;
	}
}
function addDivButton(id, action, td) {
	var ele = $Comp.textButton(id, function(ev) {
		$Actions[action]();
	});
	$Actions.readonly(ele.textObject);
	$(td).update(ele);
	var tdp = $(id).parentNode;
	tdp.removeChild($(id));
	tdp.innerHTML = "<div id='" + id + "'></div>";
	if (arguments.length == 5) {
		$(id).value = value;
	}
}
var $IT = {
	exec2 : null,
	_dhxLayout : null
};
$IT.insertText = function(obj, myValue) {
	if (document.selection) {
		obj.focus();
		var sel = document.selection.createRange();
		sel.text = myValue;
		sel.select();
	} else if (obj.selectionStart || obj.selectionStart == '0') {
		var startPos = obj.selectionStart;
		var endPos = obj.selectionEnd;
		obj.value = obj.value.substring(0, startPos) + myValue
				+ obj.value.substring(endPos, obj.value.length);
	} else {
		obj.value += myValue;
	}
};

$IT.setMeta = function(content) {
	var metas = document.getElementsByTagName("meta");
	for ( var i = 0; i < metas.length; i++) {
		if (metas[i].name == 'description') {
			if (content == null || content == '') {
				document.removeChild(metas[i]);
			} else
				metas[i].content = content;
			break;
		}
	}
};
$IT.startupExec = function(func) {
	$IT.stopExec();
	$IT.exec2 = new PeriodicalExecuter(function(executer) {
		func();
	}, 5);
};
$IT.stopExec = function() {
	if ($IT.exec2 != null) {
		$IT.exec2.stop();
		$IT.exec2 = null;
	}
};
$IT.alert = function(info) {
	$Actions['alertWin']('info=' + info);
};
$IT.confirm = function(info) {
	$Actions['confirmWin']('info=' + info);
};
$IT.hidden = function(ss, id) {
	if ($$(ss).length == 0)
		$(id).style.display = 'none';
};
$IT.toogleClass = function(s1, t, cla) {
	$$(s1).each(function(c) {
		c.className = '';
	});
	if (t)
		t.className = cla;
};
$IT.show = function(id) {
	$(id).style.display = '';
};
$IT.C = function(act) {
	if ($Actions[act])
		$Actions[act].close();
};
$IT.R = function(act) {
	if ($Actions[act])
		$Actions[act].refresh();
}
$IT.A = function(act, p) {
	if (arguments.length == 2) {
		if ($Actions[act])
			$Actions[act](p);
	} else {
		if ($Actions[act])
			$Actions[act]();
	}
}
$IT.bind = function(seletor, flag) {
	$$(seletor).each(function(c) {
		var act = "";
		if (flag) {
			act = c.id;
		} else {
			act = c.className;
			act = act.split(" ")[0];
		}
		act = act.substring(2, act.length);
		var param = c.getAttribute("param");
		var p = "";
		if (param != null && param != '' && param != 'undefined') {
			p = param;
		}
		c.observe("click", function() {
			$Actions[act + "Act"](p);
		});
	});
};
$IT.radio = function(seletor) {
	var type = '';
	$$(seletor).each(function(c) {
		if (c.checked) {
			type = c.id;
		}
	});
	return type;
};

$IT.radioValue = function(s) {
	var type = '';
	$$(s).each(function(c) {
		if (c.checked) {
			type = c.value;
		}
	});
	return type;
};

$IT.bindEle = function(seletor, f) {
	$$(seletor).each(function(c) {
		c.observe("click", function() {
			f(c);
		});
	});
};
$IT.attention = function(act, text) {
	if (act == 'add') {
		$('__doAttention').innerHTML = '\u53D6\u6D88' + text;
		$('__attentionWindow').innerHTML = parseInt($('__attentionWindow').innerHTML) + 1;
	} else {
		$('__doAttention').innerHTML = text;
		$('__attentionWindow').innerHTML = parseInt($('__attentionWindow').innerHTML) - 1;
	}
};

$IT.active = function(t, c1, c2) {
	$$(c1).each(function(c) {
		c.removeClassName(c2);
	});
	t.addClassName(c2);
}

$IT.tab = function(c) {
	var obj = c.up().up().up();
	if (obj != null) {
		$$("#" + obj.id + ' .action_refresh').each(function(c) {
			c.click();
		});
	}
};

$IT.pageletOne = function(c) {
	var obj = c.up(".pagelet");
	if (obj != null) {
		$$("#" + obj.id + ' .action_refresh').each(function(c) {
			c.click();
		});
	}
};

$IT.togglePagelet = function(c, params) {
	var obj = c.up(".pagelet");
	if (obj != null) {
		$$("#" + obj.id + ' .tabs .tab').each(function(t) {
			t.removeClassName("active");
		});
		c.addClassName("active");
		$$("#" + obj.id + ' .action_refresh').each(function(c) {
			c.setAttribute("params", params);
			c.click();
		});
	}
};

$IT.togglePageletV = function(c, act, params) {
	var obj = c.up(".tabs");
	if (obj != null) {
		$$("#" + obj.id + ' .tab').each(function(t) {
			t.removeClassName("active");
		});
		c.addClassName("active");
		$IT.A(act, params);
	}
};

$IT.pagelet = function(c, params) {
	var obj = c.up().up().up();
	if (obj != null) {
		var ar = $$("#" + obj.id + ' .action_refresh');
		ar.setAttribute("params", params);
		ar.each(function(c) {
			c.click();
		});
	}
};

$IT.createSplitbar = function(bar, left, callback) {
	if (!(bar = $(bar)) || !(left = $(left))) {
		return;
	}
	var p, w, ow = left.getHeight();
	bar.observe("dblclick", function(evt) {
		left.setStyle("height: " + ow + "px");
	});
	bar.observe("mousedown", function(evt) {
		$(document.body).fixOnSelectStart();
		p = evt.pointer();
		w = left.getHeight();
	});
	document.observe("mouseup", function(evt) {
		if (p) {
			$(document.body).fixOnSelectStart(true);
			p = null;
		}
	});
	document.observe("mousemove", function(evt) {
		if (p) {
			var nw = w - p.y + evt.pointer().y;
			if (nw < 50 || nw > (document.viewport.getHeight() - 50))
				return;
			left.setStyle("height: " + nw + "px");
			$eval(callback);
			bar.fire("size:splitbar");
			Event.stop(evt);
		}
	});
};

$IT.stree = function(id) {
	var o = $(id);
	if (o) {
		var branch = o.up(".tafelTreecontent");
		var branch_root = o.up(".tafelTree_root");
		branch_root.select(".tafelTreecontent").each(function(b) {
			b.className = b.className.replace('sstree', '');
		});
		branch.className = branch.className + ' sstree';
	}
}

$IT.filter = function() {
	$$('.tablepager .tfilter').each(function(tfilter) {
		var title = '过滤条件';
		tfilter.select("input").each(function(txt) {
			var v = txt.value;
			txt.setAttribute("title", "Enter键查询");
			if (v != undefined && v != null && v != '') {
				txt.value = v;
			} else {
				$Comp.addBackgroundTitle(txt, title);
			}
			$Comp.addReturnEvent(txt, function(ev) {
				var v = $F(txt);
				if (v != "" && v != title) {
					eval(txt.getAttribute("fun"));
				}
			});
		});
	});
}

$IT.searchButton = function(id, inputText, dq, dv, sf) {
	var show = true;
	if (!inputText)
		inputText = $MessageConst["Text.Search"];
	$$(id + ' .txt').each(function(c) {
		if (inputText) {
			$Comp.addBackgroundTitle(c, inputText);
		}
	});
	$$(id + ' #search_value,' + id + ' .ar').each(function(c) {
		c.observe("click", function(ev) {
			$('search_list').toggle();
			show = false;
		});
	});

	$(document).observe('click', function() {
		if (show) {
			if ($('search_list'))
				if ($('search_list').style.display != 'none')
					$('search_list').toggle();
		}
		show = true;
	});

	$$(id + ' #search_list a').each(function(c) {
		c.observe("click", function(ev) {
			$('search_value').innerHTML = c.innerHTML;
			$('search_id').value = c.id;
			$('search_list').toggle();
			show = false;
		});
	});
	if (dv != 'null' & dv != '') {
		$('search_id').value = dq + "_";
		$('search_value').innerHTML = dv;
	}
	$('searchBtn').observe(
					"click",
					function(ev) {
						if (sf) {
							sf(inputText);
						} else {
							if ($('txtValue').value == ''
									|| $('txtValue').value == '\u8F93\u5165\u641C\u7D22\u5185\u5BB9') {
								alert(inputText);
							} else {
								var a = $($('search_id').value);
								$Actions.loc(a.getAttribute('thref')
										.addParameter(
												'c=' + $('txtValue').value));
							}
						}
					});
	$Comp.addReturnEvent(
					$('txtValue'),
					function(ev) {
						if (sf) {
							sf(inputText);
						} else {
							if ($('txtValue').value == ''
									|| $('txtValue').value == '\u8F93\u5165\u641C\u7D22\u5185\u5BB9') {
								alert(inputText);
							} else {
								var a = $($('search_id').value);
								$Actions.loc(a.getAttribute('thref')
										.addParameter(
												'c=' + $('txtValue').value));
							}
						}
					});
};
