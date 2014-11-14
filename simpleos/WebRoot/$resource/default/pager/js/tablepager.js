/**
 * @author 闄堜緝(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
function __table_pager_addMethods(pa) {
	(function(pa) {
		var tablepager = pa.pager.down(".tablepager");
		var thead = tablepager.down(".thead");
		tablepager.observe("scroll", function() {
			thead.style.top = tablepager.scrollTop + "px";
		});

		var items = tablepager.select(".titem");
		items.invoke("observe", "click", function(evn) {
			items.each(function(item) {
				if (item.hasClassName("titem_selected")) {
					item.removeClassName("titem_selected");
				}
			});
			this.addClassName("titem_selected");
		});
		tablepager.select(".cb input[type=checkbox]").invoke(
				"observe",
				"click",
				function(evn) {
					var b = this.checked;
					var item = this.up(".titem");
					if (item) {
						item[b ? "addClassName" : "removeClassName"]
								("titem_highlight");
					} else {
						items.each(function(i) {
							i[b ? "addClassName" : "removeClassName"]
									("titem_highlight");
						});
					}
				});
	})(pa);

	if (pa.sort) {
		return;
	}
	pa.sort = function(col) {
		var sort = $F(hiddenInput("sort"));
		var col2 = $F(hiddenInput("sort_col"));

		if (col != col2)
			sort = "";
		if (sort == "up")
			sort = "down";
		else if (sort == "down")
			sort = "";
		else
			sort = "up";
		pa("sort=" + sort + "&sort_col=" + col);
	};

	pa.checkAll = function(cb) {
		pa.pager.select("input[type='checkbox']").each(function(c) {
			if (cb != c) {
				c.checked = cb.checked;
				if (c.clickFunc) {
					try {
						c.clickFunc();
					} catch (e) {
					}
				}
			}
		});
	};

	pa.move = function(o, up, moveAction) {
		var row = pa.row(o);
		var row2 = up ? row.previous() : row.next();
		if (row2) {
			var rowId = pa.rowId(row);
			var rowId2 = pa.rowId(row2);
			if (!rowId2)
				return;
			if (Object.isString(moveAction)) {
				moveAction = $Actions[moveAction];
			}
			moveAction.selector = pa.selector;
			moveAction("up=" + up + "&rowId=" + rowId + "&rowId2=" + rowId2);
		}
	};

	pa.move2 = function(o, up, moveAction) {
		var row = pa.row(o);
		var row2 = up ? hiddenInput("firstRow") : hiddenInput("lastRow");

		var rowId = pa.rowId(row);
		var rowId2 = $F(row2);

		if (rowId != rowId2) {
			if (Object.isString(moveAction)) {
				moveAction = $Actions[moveAction];
			}
			moveAction.selector = pa.selector;
			moveAction("up=" + up + "&rowId=" + rowId + "&rowId2=" + rowId2);
		}
	};

	pa.row = function(o) {
		return o.hasClassName("titem") ? o : $target(o).up(".titem");
	};

	pa.rowId = function(o) {
		return (o = pa.row(o)) ? o.getAttribute("rowId") : null;
	};

	pa.rowData = function(o, i) {
		return pa.row(o).select("td")[i].innerHTML.stripTags().strip();
	};

	pa.__checkall = function(actionFunc) {
		var ids = "";
		pa.pager.select("input[type='checkbox']").each(function(c) {
			if (c.value && c.value != "on" && c.checked) {
				ids += ";" + c.value;
			}
		});
		if (ids.length > 0) {
			if (actionFunc)
				actionFunc(ids.substring(1));
		} else {
			alert($MessageConst["Error.delete2"]);
		}
	};

	pa.bindMenu = function(menuAction) {
		$Actions.callSafely(menuAction, null, function(act) {
			act.bindEvent(pa.pager.select(".m"));
			return true;
		});
		$Actions.callSafely(menuAction + "2", null, function(act) {
			act.bindEvent(pa.pager.select(".m2"));
			return true;
		});
	};

	pa.checkCacheArr = function(o) {
		if (!o._checkCacheArr) {
			o._checkCacheArr = new Array();
		}
		return o._checkCacheArr;
	};

	pa.checkCache = function(o) {
		var find = function(d) {
			return pa.checkCacheArr(o).find(function(e) {
				if (pa.rowId(e) == pa.rowId(d)) {
					return e;
				}
			});
		};
		pa.pager.select(".titem").each(function(d) {
			var cb = d.down("input[type=checkbox]");
			if (!cb)
				return;
			if (find(d)) {
				cb.checked = true;
			}
			cb.clickFunc = function() {
				var e = find(d);
				if (e) {
					pa.checkCacheArr(o).remove(e);
				}
				if (cb.checked) {
					pa.checkCacheArr(o).push(d);
				}
			};
			cb.observe("click", cb.clickFunc);
		});
	};

	var hiddenInput = function(id) {
		var form = pa.pager.down("form");
		var o = form.down("#" + id);
		if (!o) {
			o = new Element("input", {
				"type" : "hidden",
				"name" : id,
				"id" : id
			});
			form.insert(o);
		}
		return o;
	};

	pa.setHeight = function(height) {
		if (height) {
			pa.pager.down(".tablepager").setStyle("height: " + height);
			hiddenInput("tbl_height").setValue(height);
		}
	};

	pa.setWidth = function(width) {
		if (width) {
			pa.pager.down(".tablepager").setStyle("width: " + width);
			hiddenInput("tbl_width").setValue(width);
		}
	};

	pa.exportFile = function(params) {
		var ep = $Actions["tablePagerExportWin"];
		ep.selector = pa.selector;
		ep(params);
	};

}

var TableRowDraggable = {
	init : function(containerId, hoverclass) {
		$Comp.initDragDrop(containerId, hoverclass, function(a) {
			return $MessageConst["Table.Draggable.0"]
					+ TableRowDraggable.checked(a).length
					+ $MessageConst["Table.Draggable.1"];
		});
	},

	checked : function(a) {
		var arr = [];
		a.up(".tablepager").select(".titem input").each(function(c) {
			if (c.value && c.value != "on" && c.checked) {
				arr.push(c.value);
			}
		});
		if (arr.length == 0) {
			arr.push(a.up(".titem").down("input").value);
		}
		return arr;
	}
};
