/**
 * @author 陈侃(cknet@126.com, 13910090885)
 */
$Comp.ImageSlide = Class.create({
	initialize : function(container, datas, options) {
		this.options = {
			titleHeight : 35,
			titleOpacity : 0.6,
			frequency : 4,
			autoStart : true,
			start : 0,
			showNextAction : true,
			showPreAction : true
		};
		Object.extend(this.options, options || {});

		container = $(container);
		container.setDimensions(this.options);

		this.current = null;
		this.element = new Element("div", {
			className : "imageslide"
		});
		container.update(this.element);
		this.createMenu();

		var bar = new Element("div", {
			className : "s_bar",
			style : "height:" + (this.options.titleHeight + 25) + "px"
		});
		bar.setOpacity(this.options.titleOpacity);
		var title = new Element("div", {
			className : "s_title",
			style : "height:" + this.options.titleHeight + "px"
		}).update("Title");
		var nav = new Element("div", {
			className : "s_nav"
		});
		nav.observe("mouseenter", function(e) {
			if (this.executer)
				this.executer.stop();
		}.bind(this)).observe("mouseleave", function(e) {
			if (this.executer)
				this.executer.registerCallback();
		}.bind(this));
		this.element.insert(bar).insert(title).insert(nav);

		this.createPreLink(nav);
		this.slides = [];
		var i = 0;
		datas.each(function(data) {
			var slide = new Element("div", {
				style : "display: none;width:100%;height:100%"
			});
			slide.hide();
			slide.data = data;
			this.element.insert(slide);
			this.slides.push(slide);

			var index = i++;
			nav.insert($Comp.createLink((index + 1), {
				className : "number"
			}, function(e) {
				this.showImage(index);
			}.bind(this)));
		}.bind(this));
		this.createNextLink(nav);

		this.showImage(this.options.start);

		if (this.options.autoStart) {
			this.createExecuter();
		}
	},

	createPreLink : function(nav) {
		if (!this.options.showPreAction) {
			return;
		}
		nav.insert($Comp.createLink("&laquo;", {
			className : "action"
		}, function(e) {
			var j = this.index();
			if (--j < 0)
				j = 0;
			this.showImage(j);
		}.bind(this)));
	},

	createNextLink : function(nav) {
		if (!this.options.showNextAction) {
			return;
		}
		nav.insert($Comp.createLink("&raquo;", {
			className : "action"
		}, function(e) {
			var j = this.index();
			if (++j >= this.slides.length)
				j = this.slides.length - 1;
			this.showImage(j);
		}.bind(this)));
	},

	showImage : function(i) {
		if (this.exec_showImage) {
			return;
		}
		if (this.slides[i].visible()) {
			return;
		}
		this.exec_showImage = true;
		var nav = this.element.down(".s_nav");
		if (!nav)
			return;
		var aObjs = nav.select(".number");
		var f;
		if (!this.current) {
			this.current = this.slides[this.options.start];
			f = true;
		} else {
			var j = this.index();
			if (i == j) {
				return;
			}

			this.current.hide();
			if (j > -1)
				aObjs[j].removeClassName("active");
		}

		aObjs[i].addClassName("active");
		this.current = this.slides[i];

		var data = this.current.data;
		var img = this.current.down("img");
		if (!img) {
			var img = new Element("img", {
				src : data.imageUrl,
				style : "width:100%;height:100%"
			});
			if (data.link) {
				this.current.update($Comp.createLink(img, {
					href : data.link,
					target : '_blank',
					style : "width:100%;height:100%;border:0;margin:0;padding:0"
				}));
			} else {
				this.current.update(img);
			}
		}
		
		var title = this.element.down(".s_title");
		if (title) {
			title.update(data.title);
		}
		
		if (f) {
			this.current.show();
			this.exec_showImage = false;
		} else {
			this.current.$show({
				effects : this.options.effects,
				duration : 0.8,
				afterFinish : function() {
					this.exec_showImage = false;
				}.bind(this)
			});
		}	
	},

	createMenu : function() {
		var items = [ {
			name : $MessageConst["ImageSlide.0"],
			onSelect : function(item, e) {
				var src = $target(item).src;
				if (src)
					window.open(src);
			}
		} ];
		if (this.options.autoStart) {
			items.push({
				separator : true
			});
			items.push({
				name : $MessageConst["ImageSlide.1"],
				onSelect : function(item, e) {
					this.createExecuter();
				}.bind(this)
			});
			items.push({
				name : $MessageConst["ImageSlide.2"],
				onSelect : function(item, e) {
					this.stopExecuter();
				}.bind(this)
			});
		}
		new $Comp.Menu(null, {
			selector : "#" + this.element.identify(),
			menuEvent : "contextmenu",
			minWidth : "100px",
			menuItems : items,
			onBeforeShow : function(menu, e) {
				if (this.options.autoStart) {
					var items = menu.getItems();
					items[1].setItemDisabled(this.executer != null);
					items[2].setItemDisabled(this.executer == null);
				}
			}.bind(this)
		});
	},

	index : function() {
		return this.slides.indexOf(this.current);
	},

	createExecuter : function() {
		this.executer = new PeriodicalExecuter(function() {
			var img = this.current.down("img");
			if ((img.readyState && img.readyState != 'complete') || !img.complete) {
				return;
			}
			var j = this.index();
			j++;
			if (j == this.slides.length) {
				j = 0;
			}
			this.showImage(j);
		}.bind(this), this.options.frequency);
	},

	stopExecuter : function() {
		if (this.executer) {
			this.executer.stop();
			this.executer = null;
		}
	}
});
