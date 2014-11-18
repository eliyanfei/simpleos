var BBS_UTILS = {
	search_init : function() {
		$$(".sech_pane_params .v").each(function(b) {
			var tb;
			if (b.hasClassName("b1")) {
				tb = $Comp.textButton("_s_startDate", function() {
					$Actions['calStartDate'].show();
				});
			} else if (b.hasClassName("b2")) {
				tb = $Comp.textButton("_s_endDate", function() {
					$Actions['calEndDate'].show();
				});
			}
			if (tb) {
				tb.textObject.setAttribute("readonly", "readonly");
				b.update(tb);
			}
		});

		$Actions.observeSubmit(".sech_pane_params input[type=text]", function(obj) {
			this.search_submit(obj);
		}.bind(this));
	},

	search_submit : function(obj) {
		var sp = obj.up('.sech_pane_params');
		var sDate = $F(sp.down("#_s_startDate"));
		var eDate = $F(sp.down("#_s_endDate"));
		if (sDate != "" && eDate != "") {
			if (Date.parseString(sDate, "yyyy-MM-dd").isAfter(
					Date.parseString(eDate, "yyyy-MM-dd"))) {
				alert("#(bbs_search_pane.5)");
				return;
			}
		}
		var catalog = "_s_catalog=";
		var i = 0;
		$$("#_s_catalog div[id]").each(function(d) {
			if (i++ > 0)
				catalog += ";";
			catalog += d.id;
		});
		var params = $$Form(sp).addParameter(catalog + "&c=");
		if ($Actions['bbsManagerToolsWindow']) {
			$Actions.loc(this.topicUrl.addParameter(params));
		} else {
			$Actions['bbsTopicPager'](params);
		}
	},

	insert_forum : function(selects) {
		if (!selects.any(function(node) {
			return node.selected;
		})) {
			return;
		}
		var c = $("_s_catalog");
		selects.each(function(node) {
			if (!node.selected || c.down("#" + node.id)) {
				return;
			}
			c.insert(new Element("div", {
				id : node.id
			}).insert(new Element("a", {
				className : "delete_image",
				style : "float: right",
				onclick : "this.up().$remove();"
			})).insert(new Element("span").update(node.text)));
		});
		return true;
	},

	create_searchbar : function() {
		return $Comp.searchButton(function(sp) {
			$Actions.loc(this.topicUrl.addParameter("c=" + $F(sp.down(".txt"))));
		}.bind(this), function(sp) {
			/*sp.up(".tbar").next().$toggle();*/
		}, this.searchbar_msg, 210);
	}
};