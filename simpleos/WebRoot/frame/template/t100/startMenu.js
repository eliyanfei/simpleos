/*var menus = [ {
	id : 'ddd',
	text : 'ddd',
	click : function() {
		alert('d');
	},
	clildren : [ {
		id : 'ddd',
		text : 'ddd',
		click : function() {
			alert('d');
		}
	} ]
}, {
	id : 'ddddwe',
	text : 'deerere',
	click : function() {
		alert('d');
	},
	clildren : [ {
		id : 'ddd',
		text : 'deerere3',
		click : function() {
			alert('d');
		}
	}, {
		id : 'ddd',
		text : 'deerere3',
		click : function() {
			alert('d');
		}
	}, {
		id : 'ddd',
		text : 'deerere3',
		click : function() {
			alert('d');
		},
		clildren : [ {
			id : 'ddd',
			text : 'deerere3',
			click : function() {
				alert('d');
			}
		}, {
			id : 'ddd',
			text : 'deerere3',
			icon:'/desktop/themes/app/16authorize.png',
			click : function() {
				alert('d');
			}
		}, {
			id : 'ddd',
			text : 'deerere3',
			click : function() {
				alert('d');
			}
		} ]
	} ]
} ];
 */
$startMenu = Class
		.create({
			initialize : function(contentId, menus) {
				this.contentId = contentId;
				this.menus = menus;
			},
			createMenu : function() {
				this.reverCreatMenu(null, this.menus);
			},
			reverCreatMenu : function(content, menu) {
				var d;
				if (!content) {
					d = new Element('div', {
						'class' : 'startmenu',
						'style' : 'display:none'
					});
				} else {
					d = new Element('div', {
						'class' : 'startmenu startmenu1',
						'style' : 'display:none'
					});
				}

				if (!content) {
					this.parMenu = d;
					this.bindElement();
					d.setStyle('position:absolute;');
					d.addClassName('startmenuMain');
					document
							.observe(
									"click",
									function(e) {
										if (!e.findElement('.startmenuMain')
												&& $$('.startmenuMain')[0]
														.getStyle('display') != 'none') {
											this.hideAll(null, true);
										}
									}.bind(this));
					$(document.body).insert(d);
				}
				var content1 = new Element('div', {
					'class' : 'content'
				});
				d.insert(content1);
				if (content) {
					d.setStyle('position:absolute;');
					content.insert(d);
				}
				if (menu && menu.length > 0) {
					for ( var m = 0; m < menu.length; m++) {
						var d = new Element('div');
						content1.insert(d);
						if (menu[m].icon) {
							/*
							 * d.addClassName('iconsplit'); var dd = new
							 * Element('div'); d.insert(dd); d=dd;
							 */
							d.addClassName('icon');
							d.setStyle('background-image:url(' + menu[m].icon
									+ ')');
						}
						d.setStyle('padding:5px;');
						var a = new Element('a', {
							'class' : 'submenu',
							'style' : 'display: block;height:20px;'
						});
						a.update(menu[m].text);
						d.insert(a);
						if (menu[m].click) {
							menu[m].a = a;
							menu[m].obj = this;
							a.observe('click', function(evt) {
								if (evt.findElement() != this.a) {
									return;
								}
								this.obj.hideAll(this.a, true);
								this.click();
							}.bind(menu[m]));
						}
						a.obj = this;
						a.observe('mouseover', function() {
							this.obj.show(this);
						}.bind(a));
						if (menu[m].clildren) {
							d.addClassName('more');
							this.reverCreatMenu(a, menu[m].clildren);
						}

					}
				}
			},
			bindElement : function() {
				$$(this.contentId)[0].observe('click', function(evt) {
					this.parMenu.setStyle('left:'
							+ $$(this.contentId)[0].viewportOffset()[0]
							+ 'px;top:'
							+ (Math.abs(this.parMenu.getHeight()
									- $$(this.contentId)[0].viewportOffset()[1]
									+ 5)) + 'px');
					this.parMenu.$toggle();
				}.bind(this));
			},
			hideAll : function(evtEle, hideAll) {
				if (hideAll) {
					this.hideAll(this.parMenu, false);
					if (this.parMenu.active) {
						this.parMenu.active.$toggle();
						this.parMenu.active = null;
					}
					this.parMenu.$toggle();
					return;
				}
				/* 隐藏这个节点下的子节点 */
				if (evtEle) {
					var menuPanel = evtEle.down('.startmenu');
					if (menuPanel) {
						if (menuPanel.active) {
							menuPanel.active.$toggle();
							menuPanel.active = null;
						}
						this.hideAll(menuPanel);
					}
				}
			},
			show : function(evtEle) {
				if (evtEle.up('.startmenu').active) {
					if (evtEle.up('.startmenu').active == evtEle.down('div')
							&& evtEle.up('.startmenu').active
									.getStyle('display') != 'none') {
						return;
					}
					evtEle.up('.startmenu').active.setStyle('display:none');
				}
				var showDiv = evtEle.down('div');
				evtEle.up('.startmenu').active = showDiv;
				if (showDiv && showDiv.getStyle('display') == 'none') {
					evtEle.down('div').$toggle();
				}
			}
		});