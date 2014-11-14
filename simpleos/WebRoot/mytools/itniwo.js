var $itniwo = {

	/*************下拉 **************/
	createPullDown : function(data, id, def, tip, callback) {
		var show = false;
		var pdObj = $it(id);
		var result;
		if (data != undefined) {
			if (typeof (data) == 'string') {
				var map = $ittool.map();
				var myobj = eval(data);
				for ( var i = 0; i < myobj.length; i++) {
					map.put(myobj[i].id, myobj[i].name);
				}
				data = map;
			}
			var span_id = $ittool.getNnum("span_");
			var a_id = $ittool.getNnum("a_");
			var ht = "<a class='pulldown_btn_gray' id='"
					+ a_id
					+ "' href='javascript:void(0);' ><span id='"
					+ span_id
					+ "' class='pulldown_btn_select_txt'></span><span class='pulldown_ico_select'></span></a>";
			pdObj.innerHTML = ht;
			var spanObj = $it(span_id);
			result = spanObj;
			var ulObj = $ittool.createTagNoId("ul", "pulldown_ul");
			var ul_id = $ittool.getNnum("ul_");
			ulObj.attr("id", ul_id);
			ulObj.toggle();
			var keys1 = data.keys();
			var firstKey;
			for ( var i = 0; i < keys1.length; i++) {
				li_id = keys1[i];
				if (isNULL(firstKey)) {
					firstKey = li_id;
				}
				li_value = data.get(li_id);
				var li = document.createElement("li");
				var liObj = $it(li);
				$ittool.addEvent(liObj, "click", function(c) {
					spanObj.attr("idd", c.attr("id"));
					spanObj.innerHTML = c.innerHTML;
					if (!isNULL(callback)) {
						callback(result);
					}
				});
				li.attr("id", li_id);
				li.innerHTML = li_value;
				ulObj.insert(li);
			}
			def = def || firstKey;

			spanObj.insertAfter(ulObj);
			spanObj.attr("idd", def);
			spanObj.innerHTML = tip ? def : map.get(def);
			var aObj = $it(a_id);
			$ittool.addEvent(aObj, "click", function(c) {
				ulObj.toggle();
				if (document.onclick) {
					document.onclick();
					document.onclick = null;
				}
				document.onclick = function() {
					if (show) {
						ulObj.setHidden();
						document.onclick = null;
					}
					show = true;
				};
				show = false;
			});
		}
		result.getId = function() {
			return result.attr("idd");
		};
		result.getName = function() {
			return result.innerHTML;
		};
		return result;
	},
	/***max***/
	createMax : function(id, text, tid) {
		var span_id1 = $ittool.getNnum("span_");
		var span_id2 = $ittool.getNnum("span_");
		var span_id3 = $ittool.getNnum("span_");
		var maxObj = $it(id);
		var swidth, sheight;
		var stop, sleft;
		var maxComp = function() {
			this.oldWidth = maxObj.wid();
			this.oldHeight = maxObj.hei();
			this.oldzIndex = maxObj.css("z-index");
		};

		var maxBean = new maxComp();
		maxComp.prototype.initData = function() {
			maxObj.css("position", 'relative');
			if (isNULL(tid)) {
				swidth = (document.documentElement.clientWidth - 2) + "px";
				sheight = (document.documentElement.clientHeight - 2) + "px";
				stop = 0 + 'px';
				sleft = 0 + 'px';
			} else {
				var tPos = $ittool.getElementPos($it(tid));
				var tObj = $it(tid);
				swidth = tObj.offsetWidth + 'px';
				sheight = tObj.offsetHeight + 'px';
				stop = tPos.y + 'px';
				sleft = tPos.x + 'px';
			}

			var spanObj1 = $ittool.createTagNoId("span", "max_btn_gray");
			spanObj1.setHidden();
			spanObj1.attr("id", span_id1);
			var ht = "<span id='" + span_id2 + "' class='max_tip'>" + text
					+ "</span><span id='" + span_id3
					+ "' class='max_ico1' max='true'></span>";
			spanObj1.innerHTML = ht;
			maxObj.insert(spanObj1);

			$ittool.addEvent(maxObj, "mouseover", function(c) {
				spanObj1.setShow();
			});
			$ittool.addEvent(maxObj, "mouseout", function(c) {
				spanObj1.setHidden();
			});
			var spanObj3 = $it(span_id3);
			$ittool.addEvent(spanObj3, "click", function(c) {
				if (spanObj3.attr("max") == 'true') {
					spanObj3.attr("max", "false");
					maxObj.wid('100%');
					maxObj.wid(swidth);
					maxObj.hei(sheight);
					maxObj.css("z-index", 999);
					maxObj.css("position", 'absolute');
					maxObj.css("top", stop);
					maxObj.css("left", sleft);
					spanObj3.addClass('max_ico2', true);
					document.onselectstart = function() {
						return false;
					};
				} else {
					spanObj3.attr("max", "true");
					maxObj.wid(maxBean.oldWidth);
					maxObj.hei(maxBean.oldHeight);
					document.onselectstart = null;
					maxObj.css("z-index", maxBean.oldzIndex);
					maxObj.css("top", null);
					maxObj.css("left", null);
					maxObj.css("position", 'relative');
					spanObj3.addClass('max_ico1', true);
				}
			});
		};

		maxBean.initData();
		return maxBean;

	},
	/***********insert value into textarea in cursor******************/
	insertText : function(obj, myValue) {
		var obj = $it(obj);
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
	},
	/*********get checked radio value or attribute***********/
	getRadio : function(name, att) {
		var radios = document.getElementsByName(name);
		for ( var i = 0; i < radios.length; i++) {
			var radio = $it(radios[i]);
			if (radio.checked) {
				if (isNULL(att)) {
					return radio.value;
				} else {
					return radio.attr(att);
				}
			}
		}
		alert('\u5FC5\u987B\u9009\u62E9\u4E00\u9879!');
	},
	addBackgroundTitle : function(obj, title, focusHidden) {
		obj = $it(obj);
		var c = obj.css("color");
		var f = function() {
			obj.css("color", "#aaa");
			obj.val(title);
		};
		if (focusHidden) {
			f();
			$ittool.addEvent(obj, "focus", function(c) {
				if (obj.value == title) {
					obj.value = '';
					if (isNULL(c))
						obj.css("color", c);
					else
						obj.css("color", null);
				}
			});
			$ittool.addEvent(obj, "blur", function(c) {
				if (obj.value == '' || obj.value == null)
					f();
			});
		} else {
			var pNode = $it(obj.parentNode);
			var span = document.createElement("span");
			pNode.css('position', 'relative');
			var spanObj = $it(span);
			$ittool.addEvent(spanObj, "click", function(c) {
				obj.focus();
			});
			pNode.insertBefore(span, obj);
			spanObj.css('display', 'block');
			spanObj.css('position', 'absolute');
			spanObj.css('cursor', 'text');
			spanObj.css('float', 'left');
			spanObj.css("color", "#aaa");
			spanObj.css("font-size", "12px");
			spanObj.css("left", "1px");
			spanObj.css("line-height", "20px");
			spanObj.innerHTML = title;
			$ittool.addEvent(obj, "keydown", function(c) {
				if (obj.value.length == 0)
					spanObj.setHidden();
			});
			$ittool.addEvent(obj, "keyup", function(c) {
				if (obj.value == null || obj.value.length == 0) {
					spanObj.css('display', 'block');
				}
			});
			$ittool.addEvent(obj, "blur", function(c) {
				if (obj.value == null || obj.value.length == 0) {
					spanObj.css('display', 'block');
				}
			});
		}
	},

	/************to top and bottom***************/
	createToTop : function(showNow, showBottom) {
		var divObj = $ittool.createTagNoId("div");
		divObj.addClass('itniwo_top_bottom');
		var topObj = $ittool.createTagNoId("a");
		topObj.addClass('i_top');
		topObj.attr("title", "\u7F6E\u9876");
		$ittool.addEvent(topObj, "click", function(c) {
			window.scrollTo(0, 0);
			if (showNow == undefined || !showNow) {
				divObj.setHidden();
			}
		});
		divObj.appendChild(topObj);
		if (showBottom == undefined || showBottom) {
			var bottomObj = $ittool.createTagNoId("a");
			bottomObj.attr("title", "\u7F6E\u5E95");
			bottomObj.addClass('i_bottom');
			$ittool.addEvent(bottomObj, "click", function(c) {
				var scrollTop = document.body.scrollTop
						|| document.documentElement.scrollTop;
				window.scrollTo(0, scrollTop + window.screen.availHeight);
			});
			divObj.appendChild(bottomObj);
		}
		document.body.appendChild(divObj);
		if (showNow == undefined || !showNow) {
			divObj.setHidden();
			window.onscroll = function() {
				var scrollTop = document.body.scrollTop
						|| document.documentElement.scrollTop;
				if (scrollTop > 10) {
					divObj.setShow();
				} else {
					divObj.setHidden();
				}
			}
		}
	},
	/************create timer*****************/
	createTimer : function(callback, sleep, stopcallback, delay, count) {
		var timer = function() {
			this.callTimer = null;
			this.sleepTimer = null;
			this.counter = 0;
		};
		var t = new timer();
		timer.prototype.stopTimer = function() {
			clearInterval(t.callTimer);
			if (!isNULL(stopcallback)) {
				stopcallback();
			}
		};
		timer.prototype.tf = function() {
			t.counter = t.counter + 1;
			callback(t);
			if (!isNULL(count) && parseFloat(count) > 0) {
				if (t.counter >= count) {
					t.stopTimer();
				}
			}
		};
		timer.prototype.sf = function() {
			if (!isNULL(t.sleepTimer)) {
				clearInterval(t.sleepTimer)
			}
			t.callTimer = setInterval(t.tf, parseFloat(sleep) * 1000);
		};
		timer.prototype.run = function() {
			if (!isNULL(delay) && parseFloat(delay) > 0) {
				t.sleepTimer = setInterval(t.sf, parseFloat(delay) * 1000);
			} else {
				t.tf();
				t.callTimer = setInterval(t.tf, parseFloat(sleep) * 1000);
			}
		};
		t.run();
		return t;
	},

	/******************create a dialog ************************/
	createDialog : function(data, d_obj) {
		var isIE = !- [ 1, ], /******** 判断IE6/7/8 不能判断IE9**/
		isIE6 = isIE && /msie 6/.test(navigator.userAgent.toLowerCase());
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				title : "Window",
				width : 360,
				height : 400,
				cache : false,
				/********是否缓存**/
				single : false,
				/********是否唯一**/
				resize : true,
				/********是否可以缩放**/
				maxmin : true,
				/********是否显示最大化，最小化**/
				overlay : false,
				/********是否显示遮罩层**/
				drag : true,
				/********是否可以拖动**/
				tip : false,
				/********是否是提示窗口**/
				timer : 0,
				/********是否自动显示，秒数**/
				callback : null,
				/********关闭是的回调函数**/
				loadcall : null,
				/********加载完后的回调函数**/
				follow : null,
				/********是否把窗口定位到组件旁边**/
				content : null,
				/********内容**/
				contentid : null,
				/********加载某个元素的内容**/
				contenturl : null,
				/********加载页面**/
				contentajaxurl : null,
				/********加载页面**/
				imgurl : null
			/********加载图片**/
			};
			for (i in defaults) {
				options[i] = !isNULL(data[i]) ? data[i] : defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};

		var mydata = dataMap();
		var dialogBox;
		if (mydata.get("cache") && d_obj) {
			dialogBox = $it(d_obj.getAttribute("didd"));
		}
		if (dialogBox) {
			dialogBox.setShow();
			return;
		} else {
			dialogBox = $it(document.createElement('div'));
		}
		var dialog_id = $ittool.getNnum();

		if ((mydata.get("single") || mydata.get("cache")) && d_obj) {
			if (!(d_obj.getAttribute("didd") == null || d_obj
					.getAttribute("didd") == 'null')) {
				return;
			}
			d_obj.setAttribute("didd", dialog_id);
		}
		dialogBox.attr('id', dialog_id);
		dialogBox.addClass('itniwo_dialog');
		if (mydata.get("tip")) {
			mydata.put("maxmin", false);
			mydata.put("drag", false);
			mydata.put("resize", false);
			mydata.put("width", 240);
			mydata.put("height", 150);
			dialogBox.css("position", 'fixed');
			dialogBox.css("bottom", '5px');
			dialogBox.css("right", '5px');
		}
		if (mydata.get("imgurl") != null) {
			mydata.put("maxmin", false);
			mydata.put("width", 812);
			mydata.put("height", 600);
		}
		var createOverlay = function() {
			var overlay = document.createElement('div'), style = overlay.style;
			style.cssText = 'margin:0;padding:0;border:none;width:100%;height:100%;background:#333;opacity:0.6;filter:alpha(opacity=60);z-index:9999;position:fixed;top:0;left:0;';

			if (isIE6) {
				document.body.style.height = '100%';
				style.position = 'absolute';
				style.setExpression('top', 'fuckIE6='
						+ document.documentElement.scrollTop + "px");
			}
			overlay.id = 'overlay';
			overlay.style.display = 'block';
			document.body.appendChild(overlay);
			return overlay;
		};
		var getScroll = function(type) {
			return document.documentElement['scroll' + type]
					|| document.body['scroll' + type];
		};
		if (mydata.get("follow") != null) {
			mydata.put("maxmin", false);
			var screen = document.body.clientWidth;
			var followObj = $it(mydata.get("follow"));
			var pos = $ittool.getElementPos(followObj);
			var d_left = pos.x;
			var d_top = pos.y + followObj.offsetHeight + 5;
			if (screen - d_left > mydata.get("width")) {
				dialogBox.css("left", d_left + 'px');
				dialogBox.css("top", d_top + 'px');
			} else {
				dialogBox.css("left", (d_left + followObj.offsetWidth - mydata
						.get("width")) + 'px');
				dialogBox.css("top", d_top + 'px');
			}
		}
		var dragMinWidth = mydata.get("width");
		var dragMinHeight = mydata.get("height");
		var resize = function(oParent, handle, isLeft, isTop, lockX, lockY) {
			handle.onmousedown = function(event) {
				var event = event || window.event;
				var disX = event.clientX - handle.offsetLeft;
				var disY = event.clientY - handle.offsetTop;
				var iParentTop = oParent.offsetTop;
				var iParentLeft = oParent.offsetLeft;
				var iParentWidth = oParent.offsetWidth;
				var iParentHeight = oParent.offsetHeight;

				document.onmousemove = function(event) {
					var event = event || window.event;

					var iL = event.clientX - disX;
					var iT = event.clientY - disY;
					var maxW = document.documentElement.clientWidth
							- oParent.offsetLeft - 2;
					var maxH = document.documentElement.clientHeight
							- oParent.offsetTop - 2;
					var iW = isLeft ? iParentWidth - iL : handle.offsetWidth
							+ iL;
					var iH = isTop ? iParentHeight - iT : handle.offsetHeight
							+ iT;

					isLeft && (oParent.style.left = iParentLeft + iL + "px");
					isTop && (oParent.style.top = iParentTop + iT + "px");

					iW < dragMinWidth && (iW = dragMinWidth);
					iW > maxW && (iW = maxW);
					lockX || (oParent.style.width = iW + "px");

					iH < dragMinHeight && (iH = dragMinHeight);
					iH > maxH && (iH = maxH);
					lockY || (oParent.style.height = iH + "px");

					if ((isLeft && iW == dragMinWidth)
							|| (isTop && iH == dragMinHeight))
						document.onmousemove = null;

					return false;
				};
				document.onmouseup = function() {
					document.onmousemove = null;
					document.onmouseup = null;
				};
				return false;
			}
		};
		var drag = function(oDrag, handle) {
			var disX = dixY = 0;
			var oRevert = $ittool.getEleByClass("itniwo_dialog_revert", oDrag)[0];
			var oClose = $ittool.getEleByClass("itniwo_dialog_close", oDrag)[0];
			handle = handle || oDrag;
			if (mydata.get("drag")) {
				handle.style.cursor = "move";
				handle.onmousedown = function(event) {
					var event = event || window.event;
					disX = event.clientX - oDrag.offsetLeft;
					disY = event.clientY - oDrag.offsetTop;

					document.onmousemove = function(event) {
						var event = event || window.event;
						var iL = event.clientX - disX;
						var iT = event.clientY - disY;
						var maxL = document.body.scrollWidth
								- oDrag.offsetWidth;
						var maxT = document.body.scrollHeight
								- oDrag.offsetHeight;

						iL <= 0 && (iL = 0);
						iT <= 0 && (iT = 0);
						iL >= maxL && (iL = maxL);
						iT >= maxT && (iT = maxT);

						oDrag.style.left = iL + "px";
						oDrag.style.top = iT + "px";

						return false
					};

					document.onmouseup = function() {
						document.onmousemove = null;
						document.onmouseup = null;
						this.releaseCapture && this.releaseCapture();
					};
					this.setCapture && this.setCapture();
					return false
				};
			}
			if (mydata.get("maxmin")) {
				if (mydata.get("overlay"))
					var overlay = createOverlay();
				var oMin = $ittool.getEleByClass("itniwo_dialog_min", oDrag)[0];
				var oMax = $ittool.getEleByClass("itniwo_dialog_max", oDrag)[0];
				//max
				oMax.onclick = function() {
					oDrag.style.top = oDrag.style.left = 0;
					oDrag.style.top = (document.body.scrollTop || document.documentElement.scrollTop) + 'px';
					oDrag.style.width = document.documentElement.clientWidth
							- 2 + "px";
					oDrag.style.height = document.documentElement.clientHeight
							- 2 + "px";
					oMax.style.display = "none";
					oRevert.style.display = "block";
				};
				var oDiv = $it(document.createElement("div"));
				oDiv.style.cssText = "position: fixed;bottom:0px;left:0px;display:none;";
				document.body.appendChild(oDiv);
				//min
				oMin.onclick = function() {
					oDiv.setShow();
					oDrag.style.display = "none";
					var oA = document.createElement("a");
					oDiv.appendChild(oA);
					oA.className = "itniwo_dialog_open";
					oA.href = "javascript:;";
					oA.title = "\u8FD8\u539F";
					oA.onclick = function() {
						oDrag.style.display = "block";
						oDiv.removeChild(this);

						if (overlay) {
							overlay.style.display = '';
						}
						this.onclick = null;
					};
					if (overlay) {
						overlay.style.display = 'none';
					}
				};
				//
				oMin.onmousedown = oMax.onmousedown = function(event) {
					this.onfocus = function() {
						this.blur()
					};
					(event || window.event).cancelBubble = true;
				};
			}
			//restore
			oRevert.onclick = function() {
				oDrag.style.width = dragMinWidth + "px";
				oDrag.style.height = dragMinHeight + "px";
				oDrag.style.left = (document.documentElement.clientWidth - oDrag.offsetWidth)
						/ 2 + "px";
				oDrag.style.top = (document.documentElement.clientHeight - oDrag.offsetHeight)
						/ 2
						+ (document.body.scrollTop || document.documentElement.scrollTop)
						+ "px";
				this.style.display = "none";
				oMax.style.display = "block";
			};
			oClose.onclick = function() {
				if (mydata.get("cache") && d_obj) {
					dialogBox.setHidden();
					return;
				}
				document.body.removeChild(dialogBox);
				if (overlay) {
					document.body.removeChild(overlay);
				}
				if (!isNULL(mydata.get("callback"))) {
					var cb = mydata.get("callback");
					cb();
				}
				if (d_obj) {
					d_obj.setAttribute("didd", null);
				}
			};
			oClose.onmousedown = function(event) {
				this.onfocus = function() {
					this.blur()
				};
				(event || window.event).cancelBubble = true
			};

			if (mydata.get("timer") != 0) {
				$itniwo.createTimer(function() {
					oClose.click();
				}, 0, null, mydata.get("timer"), 1);
			}
		};
		var xmlHttp;
		var ajaxcallback = function(contentL) {
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					contentL.innerHTML = xmlHttp.responseText;
				} else {
					contentL.innerHTML = '加载失败';
				}
			}
		};
		var createXMLHttpRequest = function() {
			if (window.ActiveXObject) {
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			} else {
				xmlHttp = new XMLHttpRequest();
			}
		};
		var ajaxpost = function(url, contentL) {
			createXMLHttpRequest();
			xmlHttp.open("GET", url, true);
			xmlHttp.onreadystatechange = function() {
				ajaxcallback(contentL)
			};
			xmlHttp.send();
		};
		var ht = "<div class='itniwo_dialog_title'><span>"
				+ mydata.get("title") + "</span><div>";
		if (mydata.get("maxmin")) {
			var ht_maxmin = "<a class='itniwo_dialog_min' href='javascript:;' title='\u6700\u5C0F\u5316'></a>"
					+ "<a class='itniwo_dialog_max' href='javascript:;' title='\u6700\u5927\u5316'></a>";
			ht = ht + ht_maxmin;
		}
		ht = ht
				+ "<a class='itniwo_dialog_revert' href='javascript:;' title='\u8FD8\u539F'></a>";
		ht = ht
				+ " <a class='itniwo_dialog_close' href='javascript:;' title='\u5173\u95ED'></a>";
		ht = ht + "</div></div>";
		if (mydata.get("resize")) {
			var ht_resize = "<div class='itniwo_dialog_resizeL'></div><div class='itniwo_dialog_resizeT'></div>"
					+ "<div class='itniwo_dialog_resizeR'></div><div class='itniwo_dialog_resizeB'></div><div class='itniwo_dialog_resizeLT'></div>"
					+ "<div class='itniwo_dialog_resizeTR'></div> <div class='itniwo_dialog_resizeBR'></div><div class='itniwo_dialog_resizeLB'></div>";
			ht = ht + ht_resize;
		}
		ht = ht + "<div class='itniwo_dialog_content'></div>";
		dialogBox.innerHTML = ht;
		document.body.appendChild(dialogBox);
		var oDrag = $it(dialogBox);
		oDrag.wid(mydata.get("width") + 'px');
		oDrag.hei(mydata.get("height") + 'px');
		var oTitle = $ittool.getEleByClass("itniwo_dialog_title", oDrag)[0];

		drag(oDrag, oTitle);

		if (mydata.get("resize")) {
			var oL = $ittool.getEleByClass("itniwo_dialog_resizeL", oDrag)[0];
			var oT = $ittool.getEleByClass("itniwo_dialog_resizeT", oDrag)[0];
			var oR = $ittool.getEleByClass("itniwo_dialog_resizeR", oDrag)[0];
			var oB = $ittool.getEleByClass("itniwo_dialog_resizeB", oDrag)[0];
			var oLT = $ittool.getEleByClass("itniwo_dialog_resizeLT", oDrag)[0];
			var oTR = $ittool.getEleByClass("itniwo_dialog_resizeTR", oDrag)[0];
			var oBR = $ittool.getEleByClass("itniwo_dialog_resizeBR", oDrag)[0];
			var oLB = $ittool.getEleByClass("itniwo_dialog_resizeLB", oDrag)[0];
			//
			resize(oDrag, oLT, true, true, false, false);
			resize(oDrag, oTR, false, true, false, false);
			resize(oDrag, oBR, false, false, false, false);
			resize(oDrag, oLB, true, false, false, false);
			//
			resize(oDrag, oL, true, false, false, true);
			resize(oDrag, oT, false, true, true, false);
			resize(oDrag, oR, false, false, false, true);
			resize(oDrag, oB, false, false, true, false);
		}
		if (!mydata.get("tip") && !mydata.get("follow")) {
			oDrag.style.left = (document.documentElement.clientWidth - oDrag.offsetWidth)
					/ 2 + "px";
			oDrag.style.top = (document.documentElement.clientHeight - oDrag.offsetHeight)
					/ 2
					+ (document.body.scrollTop || document.documentElement.scrollTop)
					+ "px";
		}
		var contentL = $ittool.getEleByClass("itniwo_dialog_content", oDrag)[0];
		if (!isNULL(mydata.get("contenturl"))) {
			var url = mydata.get("contenturl");
			if (url.indexOf('http://') == -1) {
				url = "http://" + url;
			}
			contentL.innerHTML = "<iframe style='visibility: visible;width: 100%;height: 98%;border: none;overflow: auto;' src='"
					+ url + "'/>";
		} else if (!isNULL(mydata.get("imgurl"))) {
			var url = mydata.get("imgurl");
			contentL.innerHTML = "<div align='center' style='width:100%;height:100%;'><img style='width:100%;height:98%;' src='"
					+ url + "'/></div>";
		} else if (!isNULL(mydata.get("contentajaxurl"))) {
			var url = mydata.get("contentajaxurl");
			ajaxpost(url, contentL);
		} else if (!isNULL(mydata.get("contentid"))) {
			contentL.innerHTML = $it(mydata.get("contentid")).innerHTML;
		} else {
			if (!isNULL(mydata.get("content"))) {
				contentL.innerHTML = mydata.get("content");
			}
		}
		if (!isNULL(mydata.get("loadcall"))) {
			var lc = mydata.get("loadcall");
			lc();
		}
	},
	/******************create table order ***********************/
	createSortTable : function(tId, data) {
		var tableObj = $it(tId);
		var f_desc_ = 0;
		var compareData = function(sIdx, nType) {
			return function compareTR(trLeft, trRight) {
				var left_firstChild = f_desc_ == 2 ? trLeft.cells[sIdx].parentNode
						: trLeft.cells[sIdx].firstChild;
				var right_firstChild = f_desc_ == 2 ? trRight.cells[sIdx].parentNode
						: trRight.cells[sIdx].firstChild;
				var leftValue = convertData(left_firstChild == null ? ""
						: (f_desc_ == 2 ? $it(left_firstChild).attr("sidd")
								: left_firstChild.nodeValue), nType);
				var rightValue = convertData(right_firstChild == null ? ""
						: (f_desc_ == 2 ? $it(right_firstChild).attr("sidd")
								: right_firstChild.nodeValue), nType);
				if (leftValue < rightValue) {
					return -1;
				} else if (leftValue > rightValue) {
					return 1;
				} else {
					return 0;
				}
			};
		};
		var convertData = function(_value, _dataType) {
			var value = isNULL(_value) ? "" : _value;
			var dataType = isNULL(_dataType) ? null : _dataType.toLowerCase();
			var v_result = null;
			switch (dataType) {
			case "number":
				v_result = new Number(value.trim().replace(/,/g, ''));
				return isNaN(v_result) ? null : v_result;
			case "string":
				return value.toString();
			case "nocasestring":
				return value.toString().toLowerCase();
			case "date":
				v_result = new Date(Date.parse(value.trim().trimNBSP().replace(
						/-/g, '/')));
				return isNaN(v_result) ? null : v_result;
			default:
				return value.toString();
			}
		};
		var sort = function(cIdx, nType) {
			var _tBodies_length = tableObj.tBodies.length;
			var rowArray = new Array();
			var rowIndex = 0;
			for ( var t = 0; t < _tBodies_length; t++) {
				var tableBody = tableObj.tBodies[t];
				var tableRows = tableBody.rows;
				for ( var i = 0; i < tableRows.length; i++) {
					rowArray[rowIndex++] = tableRows[i];
				}
			}
			if (tableObj.cIdx == cIdx) {
				rowArray.reverse();
			} else {
				rowArray.sort(compareData(cIdx, nType));
				if (f_desc_ == 0) {
					rowArray.reverse();
				}
			}
			var tbodyFragment = document.createDocumentFragment();
			for ( var i = 0; i < rowArray.length; i++) {
				tbodyFragment.appendChild(rowArray[i]);
			}
			tableBody.appendChild(tbodyFragment);
			tableObj.sortColumn = cIdx;
		};
		var lastSortObj;
		var mydata = eval(data);
		for ( var i = 1; i < tableObj.rows.length; i++) {
			$it(tableObj.rows[i]).attr("sidd", i);
		}
		for ( var i = 0; i < mydata.length; i++) {
			var cellObj = $it(tableObj.rows[0].cells[mydata[i].idx]);
			cellObj.style.cursor = 'pointer';
			cellObj.attr('_idx', mydata[i].idx);
			cellObj.attr('_type', mydata[i].type);
			var spanObj = $it(document.createElement("span"));
			spanObj.attr("id", "span_" + mydata[i].idx);
			cellObj.attr('attr1', "span_" + mydata[i].idx);
			cellObj.appendChild(spanObj);
			$ittool.addEvent(cellObj, "click",
					function(o) {
						if (lastSortObj) {
							$it(lastSortObj.attr('attr1')).removeClass();
						}
						if (lastSortObj != o) {
							f_desc_ = 0;
						}
						if (f_desc_ == 0) {
							$it(o.attr('attr1')).addClass(
									'itniwo_table_sort_up', true);
							sort(o.attr('_idx'), o.attr('_type'));
							f_desc_ = f_desc_ + 1;
						} else if (f_desc_ == 1) {
							$it(o.attr('attr1')).addClass(
									'itniwo_table_sort_down', true);
							sort(o.attr('_idx'), o.attr('_type'));
							f_desc_ = f_desc_ + 1;
						} else {
							$it(o.attr('attr1')).removeClass();
							sort(o.attr('_idx'), o.attr('_type'));
							f_desc_ = 0;
						}
						lastSortObj = o;
					});
		}
	},
	selectAllCheckbox : function(name, select) {
		if (typeof (select) != 'boolean') {
			select = select.checked;
		}
		var cboxs = document.getElementsByName(name);
		for ( var i = 0; i < cboxs.length; i++) {
			cboxs[i].checked = select;
		}
	},
	createTip : function(aObj, data) {
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				p : "bottom",
				width : 0,
				content : null,
				timer : 0
			};
			for (i in defaults) {
				options[i] = !isNULL(data[i]) ? data[i] : defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();
		var followObj = $it(aObj);
		$ittool.addEvent(followObj, "mouseover", function(obj) {
			if (!(obj.attr("idd") == null || obj.attr("idd") == 'null')) {
				return;
			}
			var tipDiv = $ittool.createTag("div");
			if (mydata.get("width") != 0) {
				tipDiv.wid(mydata.get("width") + 'px');
			}
			var cDiv = $ittool
					.createTagNoId("div", null, mydata.get("content"));
			tipDiv.appendChild(cDiv);
			document.body.appendChild(tipDiv);
			tipDiv.addClass("itniwo_tip");
			obj.attr("idd", tipDiv.attr("id"))
			var p = "left";
			var screenWidth = document.documentElement.clientWidth
					|| document.body.scrollWidth;
			var screenHeight = document.documentElement.clientHeight
					|| document.body.scrollHeight;
			var style = tipDiv.style;
			var pos = $ittool.getElementPos(obj);
			var d_left = pos.x;
			var d_top = pos.y + obj.offsetHeight + 5;
			if (screenWidth - d_left > cDiv.offsetWidth + 10) {
				style.left = d_left + 'px';
				if (mydata.get("p") == 'top') {
					if (d_top < (cDiv.offsetHeight + obj.offsetHeight + 20)) {
						mydata.put("p", "bottom");
						style.top = (d_top) + 'px';
					} else {
						style.top = (d_top - cDiv.offsetHeight
								- obj.offsetHeight - 20) + 'px';
					}
				} else {
					if (screenHeight - d_top > cDiv.offsetHeight) {
						style.top = d_top + 'px';
					} else {
						style.top = (d_top - cDiv.offsetHeight
								- obj.offsetHeight - 20) + 'px';
						mydata.put("p", "top");
					}
				}
			} else {
				style.left = (d_left - cDiv.offsetWidth) + 'px';
				if (mydata.get("p") == 'top') {
					if (d_top < (cDiv.offsetHeight + obj.offsetHeight + 20)) {
						mydata.put("p", "bottom");
						style.top = (d_top) + 'px';
					} else {
						style.top = (d_top - cDiv.offsetHeight
								- obj.offsetHeight - 20) + 'px';
					}
				} else {
					if (screenHeight - d_top > cDiv.offsetHeight) {
						style.top = d_top + 'px';
					} else {
						style.top = (d_top - cDiv.offsetHeight
								- obj.offsetHeight - 20) + 'px';
						mydata.put("p", "top");
					}
				}
				p = "right";
			}
			var rowSpan = $ittool.createTagNoId("span",
					"itniwo_tip_in itniwo_tip_" + mydata.get("p") + p);
			var subSpan = $ittool.createTagNoId("span", "itniwo_tip_in");
			rowSpan.appendChild(subSpan);
			tipDiv.appendChild(rowSpan);
			if (mydata.get("timer") == 0) {
				$ittool.addEvent(obj, "mouseout", function(obj1) {
					var o = $it(obj1.attr("idd"));
					if (o) {
						document.body.removeChild(o);
						obj1.attr("idd", null);
					}
				});
			} else {
				$itniwo.createTimer(function() {
					var o = $it(obj.attr("idd"));
					if (o) {
						document.body.removeChild(o);
					}
					obj.attr("idd", null);
				}, 0, null, mydata.get("timer"), 1);
			}
		});
	},
	createContextMenu : function(id, data) {
		var divMenu = $ittool.createTag("div");
		var initData = function(subMenus, liObj) {
			var subUlMenu = $it(document.createElement("ul"));
			liObj.appendChild(subUlMenu);
			for ( var i = 0; i < subMenus.length; i++) {
				var subLiMenu = $it(document.createElement("li"));
				if (subMenus[i].icon) {
					subLiMenu.css("background-image", "url(" + subMenus[i].icon
							+ ")");
				}
				if (subMenus[i].disable) {
					subLiMenu.addClass("disablevalue");
				}
				if (subMenus[i].text == '-') {
					subLiMenu.addClass("separator");
					subUlMenu.appendChild(subLiMenu);
				} else {
					if (subMenus[i].menus) {
						subLiMenu.innerHTML = subMenus[i].text
								+ "<span class='submenuicon'></span>";
						initData(subMenus[i].menus, subLiMenu);
					} else {
						subLiMenu.innerHTML = subMenus[i].text;
						if (subMenus[i].callback && !subMenus[i].disable) {
							var cb = subMenus[i].callback;
							$ittool.addEvent(subLiMenu, "click", function(o) {
								cb($it(id));
								divMenu.setHidden();
							});
						} else {
							if (!subMenus[i].disable) {
								$ittool.addEvent(subLiMenu, "click",
										function(o) {
											alert('not event');
											divMenu.setHidden();
										});
							}
						}
					}
					subUlMenu.appendChild(subLiMenu);
				}
			}
		};

		var initMenuData = function() {
			var mydata = eval(data);

			divMenu.addClass('rightMenu');
			var ulMenu = document.createElement("ul");
			divMenu.appendChild(ulMenu);
			for ( var i = 0; i < mydata.length; i++) {
				var liMenu = $it(document.createElement("li"));
				if (mydata[i].icon) {
					liMenu.style.cssText = "background-image: url("
							+ mydata[i].icon + ");";
				}
				if (mydata[i].disable) {
					liMenu.addClass("disablevalue");
				}
				liMenu.addClass("menuicon");
				if (mydata[i].text == '-') {
					liMenu.addClass("separator");
					ulMenu.appendChild(liMenu);
				} else {
					if (mydata[i].menus) {
						liMenu.innerHTML = mydata[i].text
								+ "<span class='submenuicon'></span>";
						initData(mydata[i].menus, liMenu);
					} else {
						liMenu.innerHTML = mydata[i].text;
						if (mydata[i].callback && !mydata[i].disable) {
							liMenu.cb = mydata[i].callback;
							$ittool.addEvent(liMenu, "click", function(o) {
								o.cb($it(id));
								divMenu.setHidden();
							});
						} else {
							if (!mydata[i].disable) {
								$ittool.addEvent(liMenu, "click", function(o) {
									alert('not event');
									divMenu.setHidden();
								});
							}
						}
					}
					ulMenu.appendChild(liMenu);
				}
			}
			document.body.appendChild(divMenu);
			return divMenu.id;
		};
		var menuId = initMenuData();

		var getOffset = {
			top : function(obj) {
				return obj.offsetTop
						+ (obj.offsetParent ? arguments
								.callee(obj.offsetParent) : 0)
			},
			left : function(obj) {
				return obj.offsetLeft
						+ (obj.offsetParent ? arguments
								.callee(obj.offsetParent) : 0)
			}
		};
		var menuLoad = function() {
			var oMenu = $it(document.getElementById(menuId));
			var aUl = oMenu.getElementsByTagName("ul");
			var aLi = oMenu.getElementsByTagName("li");
			var showTimer = hideTimer = null;
			var i = 0;
			var maxWidth = maxHeight = 0;
			var aDoc = [
					document.documentElement.offsetWidth
							|| document.body.offsetWidth,
					document.documentElement.offsetHeight
							|| document.body.offsetHeight ];

			oMenu.style.display = "none";

			for (i = 0; i < aLi.length; i++) {

				aLi[i].onmouseover = function() {
					var oThis = this;
					var oUl = oThis.getElementsByTagName("ul");

					if (!$it(oThis).hasClass("separator")
							&& !$it(oThis).hasClass("disablevalue")) {
						oThis.className += " active";
					}

					if (oUl[0]) {
						clearTimeout(hideTimer);
						showTimer = setTimeout(
								function() {
									for (i = 0; i < oThis.parentNode.children.length; i++) {
										oThis.parentNode.children[i]
												.getElementsByTagName("ul")[0]
												&& (oThis.parentNode.children[i]
														.getElementsByTagName("ul")[0].style.display = "none");
									}
									oUl[0].style.display = "block";
									oUl[0].style.top = oThis.offsetTop + "px";
									oUl[0].style.left = oThis.offsetWidth
											+ "px";
									setWidth(oUl[0]);

									maxWidth = aDoc[0] - oUl[0].offsetWidth;
									maxHeight = aDoc[1] - oUl[0].offsetHeight;

									maxWidth < getOffset.left(oUl[0])
											&& (oUl[0].style.left = -oUl[0].clientWidth
													+ "px");
									maxHeight < getOffset.top(oUl[0])
											&& (oUl[0].style.top = -oUl[0].clientHeight
													+ oThis.offsetTop
													+ oThis.clientHeight + "px")
								}, 300);
					}
				};

				aLi[i].onmouseout = function() {
					var oThis = this;
					var oUl = oThis.getElementsByTagName("ul");
					oThis.className = oThis.className.replace(/\s?active/, "");

					clearTimeout(showTimer);
					hideTimer = setTimeout(
							function() {
								for (i = 0; i < oThis.parentNode.children.length; i++) {
									oThis.parentNode.children[i]
											.getElementsByTagName("ul")[0]
											&& (oThis.parentNode.children[i]
													.getElementsByTagName("ul")[0].style.display = "none");
								}
							}, 300);
				};
			}

			if (!isNULL(id)) {
				$ittool.addEvent($it(id), "click", function(o) {
					document.body.setAttribute("menuFlag", 1);
					$ittool.hiddenClass('rightMenu');
					var pos = $ittool.getElementPos(o);
					oMenu.style.display = "block";
					oMenu.style.top = (pos.y + o.offsetHeight) + "px";
					oMenu.style.left = pos.x + "px";
					setWidth(aUl[0]);

					maxWidth = aDoc[0] - oMenu.offsetWidth;
					maxHeight = aDoc[1] - oMenu.offsetHeight;

					oMenu.offsetTop > maxHeight
							&& (oMenu.style.top = maxHeight + "px");
					oMenu.offsetLeft > maxWidth
							&& (oMenu.style.left = maxWidth + "px");
				});
			} else {
				document.oncontextmenu = function(event) {
					var event = event || window.event;
					oMenu.style.display = "block";
					oMenu.style.top = event.clientY + "px";
					oMenu.style.left = event.clientX + "px";
					setWidth(aUl[0]);

					maxWidth = aDoc[0] - oMenu.offsetWidth;
					maxHeight = aDoc[1] - oMenu.offsetHeight;

					oMenu.offsetTop > maxHeight
							&& (oMenu.style.top = maxHeight + "px");
					oMenu.offsetLeft > maxWidth
							&& (oMenu.style.left = maxWidth + "px");
					return false;
				};
			}

			document.onclick = function() {
				if (document.body.getAttribute("menuFlag") == 0) {
					$ittool.hiddenClass('rightMenu');
				}
				document.body.setAttribute("menuFlag", 0);
			};

			function setWidth(obj) {
				maxWidth = 0;
				for (i = 0; i < obj.children.length; i++) {
					var oLi = obj.children[i];
					var iWidth = oLi.clientWidth
							- parseInt(oLi.currentStyle ? oLi.currentStyle["paddingLeft"]
									: getComputedStyle(oLi, null)["paddingLeft"])
							* 2
					if (iWidth > maxWidth)
						maxWidth = iWidth;
				}
				for (i = 0; i < obj.children.length; i++) {
					obj.children[i].style.width = maxWidth + "px";
				}
			}
		};
		menuLoad();
	},
	createVotes : function(id, data, totalNum, showNum, pCenter) {
		var colors = [ "#669999", "#6699FF", "#FF9900", "#FF3333", "#990000",
				"#43CD80", "#000080", "#008B00", "#FFD700", "#8B658B",
				"#CD5C5C", "#FF1493", "#8B1A1A", "#A020F0", "#FF4500",
				"#8B0000" ];
		var mydata = eval(data);
		if (isNULL(totalNum)) {
			totalNum = 0;
			for ( var i = 0; i < mydata.length; i++) {
				totalNum += mydata[i].num;
			}
		}
		var divVote = $ittool.createTag("div", "itniwo_votes");
		var j = 0;
		for ( var i = 0; i < mydata.length; i++) {
			var divItem1 = $ittool.createTag("div", "itniwo_votes_item1");
			var divItem = $ittool.createTag("div", "itniwo_votes_item");
			divItem1.appendChild(divItem);
			var p = (mydata[i].num / totalNum) * 100;
			p = p.toFixed(1);
			var text_ = p + '%';
			if (!isNULL(showNum) && showNum) {
				text_ = text_ + "(" + mydata[i].num + ")";
			}
			var graphDiv = $ittool.createTag("div", "itniwo_votes_item_graph");
			if (mydata[i].tip) {
				graphDiv.title = mydata[i].tip;
			}
			if (!isNULL(pCenter) && pCenter) {
				var pDiv = $ittool.createTag("div", "itniwo_votes_item_p",
						p + '%');
				graphDiv.appendChild(pDiv);

				if (!isNULL(showNum) && showNum) {
					var percentDiv = $ittool.createTag("div",
							"itniwo_votes_item_percent", mydata[i].num);
					divItem.appendChild(percentDiv);
				}
			} else {
				var percentDiv = $ittool.createTag("div",
						"itniwo_votes_item_percent", text_);
				divItem.appendChild(percentDiv);
			}
			var colorDiv = $ittool.createTag("span", "itniwo_votes_item_color");
			colorDiv.style.cssText = "width:" + p + "%;background-color: "
					+ colors[j++] + ";";
			if (j >= colors.length) {
				j = 0;
			}
			graphDiv.appendChild(colorDiv);
			divItem.appendChild(graphDiv);
			var textDiv = $ittool.createTag("div",
					"itniwo_votes_item_itemname", mydata[i].text);
			divItem.appendChild(textDiv);
			divVote.appendChild(divItem1);
		}
		$it(id).appendChild(divVote);
	},
	createDrags : function() {
		var dragobj = {};
		var basic_ini = function() {
			window.$ = function(obj) {
				return typeof (obj) == "string" ? document.getElementById(obj)
						: obj;
			};
			window.oDel = function(obj) {
				if ($(obj) != null) {
					$(obj).parentNode.removeChild($(obj))
				}
			};
		};
		var on_ini = function() {
			String.prototype.inc = function(s) {
				return this.indexOf(s) > -1 ? true : false
			};
			var agent = navigator.userAgent;
			window.isOpr = agent.inc("Opera");
			window.isIE = agent.inc("IE") && !isOpr;
			window.isMoz = agent.inc("Mozilla") && !isOpr && !isIE;
			if (isMoz) {
				Event.prototype.__defineGetter__("x", function() {
					return this.clientX + 2
				});
				Event.prototype.__defineGetter__("y", function() {
					return this.clientY + 2
				});
			}
			basic_ini();
		};
		var initLoad = function() {
			on_ini();
			var o = document.getElementsByTagName("h1");
			for ( var i = 0; i < o.length; i++) {
				o[i].onmousedown = function(e) {
					if (dragobj.o != null)
						return false;
					e = e || event;
					dragobj.o = this.parentNode;
					dragobj.xy = getxy(dragobj.o);
					dragobj.xx = new Array((e.x - dragobj.xy[1]),
							(e.y - dragobj.xy[0]));
					dragobj.o.style.width = dragobj.xy[2] + "px";
					dragobj.o.style.height = dragobj.xy[3] + "px";
					dragobj.o.style.left = (e.x - dragobj.xx[0]) + "px";
					dragobj.o.style.top = (e.y - dragobj.xx[1]) + "px";
					dragobj.o.style.position = "absolute";
					var om = document.createElement("div");
					dragobj.otemp = om;
					om.style.width = dragobj.xy[2] + "px";
					om.style.height = dragobj.xy[3] + "px";
					dragobj.o.parentNode.insertBefore(om, dragobj.o);
					return false;
				}
			}
		};
		document.onselectstart = function() {
			return false;
		};
		window.onfocus = function() {
			document.onmouseup();
		};
		window.onblur = function() {
			document.onmouseup();
		};
		document.onmouseup = function() {
			if (dragobj.o != null) {
				dragobj.o.style.width = "auto";
				dragobj.o.style.height = "auto";
				dragobj.otemp.parentNode.insertBefore(dragobj.o, dragobj.otemp);
				dragobj.o.style.position = "";
				oDel(dragobj.otemp);
				dragobj = {};
			}
		};
		document.onmousemove = function(e) {
			e = e || event;
			if (dragobj.o != null) {
				dragobj.o.style.left = (e.x - dragobj.xx[0]) + "px";
				dragobj.o.style.top = (e.y - dragobj.xx[1]) + "px";
				createtmpl(e);
			}
		};
		var getxy = function(e) {
			var a = new Array();
			var t = e.offsetTop;
			var l = e.offsetLeft;
			var w = e.offsetWidth;
			var h = e.offsetHeight;
			while (e = e.offsetParent) {
				t += e.offsetTop;
				l += e.offsetLeft;
			}
			a[0] = t;
			a[1] = l;
			a[2] = w;
			a[3] = h
			return a;
		};
		var inner = function(o, e) {
			var a = getxy(o);
			if (e.x > a[1] && e.x < (a[1] + a[2]) && e.y > a[0]
					&& e.y < (a[0] + a[3])) {
				if (e.y < (a[0] + a[3] / 2))
					return 1;
				else
					return 2;
			} else
				return 0;
		};

		var createtmpl = function(e) {
			for ( var i = 0; i < 12; i++) {
				if ($("m" + i) == dragobj.o)
					continue;
				var b = inner($("m" + i), e);
				if (b == 0)
					continue;
				dragobj.otemp.style.width = $("m" + i).offsetWidth;
				if (b == 1) {
					$("m" + i).parentNode.insertBefore(dragobj.otemp,
							$("m" + i));
				} else {
					if ($("m" + i).nextSibling == null) {
						$("m" + i).parentNode.appendChild(dragobj.otemp);
					} else {
						$("m" + i).parentNode.insertBefore(dragobj.otemp, $("m"
								+ i).nextSibling);
					}
				}
				return;
			}
			for ( var j = 0; j < 3; j++) {
				if ($("dom" + j).innerHTML.inc("div")
						|| $("dom" + j).innerHTML.inc("DIV"))
					continue;
				var op = getxy($("dom" + j));
				if (e.x > (op[1] + 10) && e.x < (op[1] + op[2] - 10)) {
					$("dom" + j).appendChild(dragobj.otemp);
					dragobj.otemp.style.width = (op[2] - 10) + "px";
				}
			}
		};

		initLoad();
	},
	createRating : function(id, data, callback, tipArr) {
		var _ratingObj = {};
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				width : 270,
				tip : true,
				defScore : 0,
				rateScore : 0,
				peoples : 0,
				showScore : true
			};
			for (i in defaults) {
				options[i] = (!isNULL(data) && !isNULL(data[i])) ? data[i]
						: defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();

		var ratingDiv0 = $ittool.createTag("div", "itniwo_rating0");
		var ratingDiv1 = $ittool.createTag("div", "itniwo_rating1");
		ratingDiv0.appendChild(ratingDiv1);
		ratingDiv1.style.cssText = "width:" + mydata.get("width") + "px;";
		var ratingId = $ittool.getNnum();
		_ratingObj.hasRate = function() {
			if (ratingDiv1.attr("_vote") == 'true') {
				return true;
			}
			return false;
		};
		_ratingObj._showTip = function(score) {
			if (mydata.get("tip")) {
				var ratingTip = new Array("", "\u5F88\u5DEE", "\u5F88\u5DEE",
						"\u5DEE", "\u5DEE", "\u4E00\u822C", "\u4E00\u822C",
						"\u597D", "\u597D", "\u5F88\u597D", "\u5F88\u597D");
				if (!isNULL(tipArr)) {
					ratingTip = tipArr;
				}
				if (ratingDiv1.attr("_rating") != 'true') {
					$it(ratingId + "_ratingno").innerHTML = score + "\u5206 ";
					$it(ratingId + "_ratingtip").innerHTML = ratingTip[score];
				}
			}
		};
		var idObj = $it(id);
		_ratingObj._dealRating = function(score) {
			ratingDiv1.attr("_rating", 'true');
			ratingDiv1.attr("_vote", "true");
			var params = new Array();
			if (!isNULL(callback))
				callback(score, idObj);
		};

		var ratingDiv2 = $ittool.createTag("div", "itniwo_rating2");
		var ratingUl = $ittool.createTag("ul", "starrating");
		var rating_li1 = $ittool.createTag("li", "currentrating");
		rating_li1.id = ratingId + "_currentrating";
		rating_li1.style.cssText = "width: 1px;";
		ratingUl.appendChild(rating_li1);
		for ( var i = 1; i < 11; i++) {
			var rating_li = $ittool.createTag("li");
			var rating_li_a = $ittool.createTag("a", "itniwo_rating_star" + i,
					i);
			rating_li_a.id = i;
			if (!isNotZero(mydata.get("defScore"))) {
				$ittool.addEvent(rating_li_a, "click", function(o) {
					if (!_ratingObj.hasRate()) {
						_ratingObj._dealRating(o.id);
						$it(ratingId + "_currentrating").wid(o.id * 10 + 'px');
					} else {
						alert('\u4E0D\u80FD\u91CD\u590D\u8BC4\u5206');
					}
				});
				$ittool.addEvent(rating_li_a, "mouseout", function(o) {
					_ratingObj._showTip(0);
				});
				$ittool.addEvent(rating_li_a, "mouseover", function(o) {
					_ratingObj._showTip(o.id);
				});
			}

			rating_li.appendChild(rating_li_a);
			ratingUl.appendChild(rating_li);
		}
		var ratingtipSpan = $ittool.createTag("span", "ratingtip");
		ratingtipSpan.id = ratingId + "_ratingtip";
		var ratingnoSpan = $ittool.createTag("span", "ratingno");
		ratingnoSpan.id = ratingId + "_ratingno";
		ratingDiv2.appendChild(ratingUl);
		ratingDiv2.appendChild(ratingtipSpan);
		ratingDiv2.appendChild(ratingnoSpan);
		if (mydata.get("showScore")) {
			var pDiv = $ittool.createTag("p");
			var ratingshiSpan = $ittool.createTag("span", "ratingshi");
			ratingshiSpan.id = ratingId + "_ratingshi";
			var dotSpan = $ittool.createTag("span", null, ".");
			dotSpan.style.cssText = "font-weight: bold;";
			var ratinggeSpan = $ittool.createTag("span", "ratingge");
			ratinggeSpan.id = ratingId + "_ratingge";

			var noSpan = $ittool.createTag("span", "no",
					"(\u5DF2\u6709<label id=" + ratingId
							+ "'_pfpeople' class='pfpeople'>"
							+ mydata.get("peoples")
							+ "</label>\u4EBA\u8BC4\u5206)");

			pDiv.appendChild(ratingshiSpan);
			pDiv.appendChild(dotSpan);
			pDiv.appendChild(ratinggeSpan);
			pDiv.appendChild(noSpan);
			ratingDiv2.appendChild(pDiv);
		}

		ratingDiv1.appendChild(ratingDiv2);
		idObj.appendChild(ratingDiv0);
		if (mydata.get("showScore")) {
			if (!isNULL(mydata.get("rateScore"))) {
				var s = mydata.get("rateScore");
				s = (s / 10) + '';
				var ss = s.split(".");
				$it(ratingId + "_ratingshi").innerHTML = ss[0];
				$it(ratingId + "_ratingge").innerHTML = (ss.length == 2 ? ss[1]
						: 0);
			}
		}
		if (!isNULL(mydata.get("defScore"))) {
			var s = mydata.get("defScore");
			s = s > 10 ? 10 : s;
			_ratingObj._showTip(s);
			$it(ratingId + "_currentrating").wid(s * 10 + 'px');
		}
	},
	createTrees : function(showid, objName, data) {
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				type : "",
				pClick : false,
				rootNode : true,
				callback : null,
				cboxcallback : null
			/***checkbox,''*****/
			};
			for (i in defaults) {
				options[i] = (!isNULL(data) && !isNULL(data[i])) ? data[i]
						: defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();

		var Node = function(id, pid, name, url, title, target, icon, iconOpen,
				open) {
			this.id = id;
			this.pid = pid;
			this.name = name;
			this.url = url;
			this.title = title;
			this.target = target;
			this.icon = icon;
			this.iconOpen = iconOpen;
			this._io = open || false;
			this._is = false;
			this._ls = false;
			this._hc = false;
			this._ai = 0;
			this._p;
		};

		var dTree = function(objName) {
			this.dId = $ittool.getNnum() + '_';
			this.config = {
				target : null,
				folderLinks : true,
				useSelection : true,
				useCookies : true,
				useLines : true,
				useIcons : true,
				useStatusText : false,
				closeSameLevel : false,
				inOrder : false
			};

			this.icon = {
				root : 'img/base.gif',
				folder : 'img/folder.gif',
				folderOpen : 'img/folderopen.gif',
				node : 'img/page.gif',
				empty : 'img/empty.gif',
				line : 'img/line1.gif',
				join : 'img/line3.gif',
				joinBottom : 'img/line2.gif',
				plus : 'img/plus3.gif',
				plus4 : 'img/plus4.gif',
				plusBottom : 'img/plus2.gif',
				minus : 'img/minus3.gif',
				minus : 'img/minus4.gif',
				minusBottom : 'img/minus2.gif',
				nlPlus : 'img/plus0.gif',
				nlMinus : 'img/minus0.gif'
			};

			this.obj = objName;
			this.aNodes = [];
			this.aIndent = [];
			this.root = new Node(-1);
			this.selectedNode = null;
			this.selectedFound = false;
			this.completed = false;
		}
		dTree.prototype.add = function(id, pid, name, url, title, target, icon,
				iconOpen, open) {
			this.aNodes[this.aNodes.length] = new Node(id, pid, name, url,
					title, target, icon, iconOpen, open);
		};
		dTree.prototype.openAll = function() {
			this.oAll(true);
		};
		dTree.prototype.closeAll = function() {
			this.oAll(false);
		};
		dTree.prototype.toString = function() {
			var str = '<div class="dtree">\n';
			if (document.getElementById) {
				if (this.config.useCookies)
					this.selectedNode = this.getSelected();
				str += this.addNode(this.root);
			} else
				str += 'Browser not supported.';
			str += '</div>';
			if (!this.selectedFound)
				this.selectedNode = null;
			this.completed = true;
			$it(showid).innerHTML = str;
			return str;
		};
		dTree.prototype.addNode = function(pNode) {
			var str = '';
			var n = 0;
			if (this.config.inOrder)
				n = pNode._ai;
			for (n; n < this.aNodes.length; n++) {
				if (this.aNodes[n].pid == pNode.id) {
					var cn = this.aNodes[n];
					cn._p = pNode;
					cn._ai = n;
					this.setCS(cn);
					if (!cn.target && this.config.target)
						cn.target = this.config.target;
					if (cn._hc && !cn._io && this.config.useCookies)
						cn._io = this.isOpen(cn.id);
					if (!this.config.folderLinks && cn._hc)
						cn.url = null;
					if (this.config.useSelection && cn.id == this.selectedNode
							&& !this.selectedFound) {
						cn._is = true;
						this.selectedNode = n;
						this.selectedFound = true;
					}
					str += this.node(cn, n);
					if (cn._ls)
						break;
				}
			}
			return str;
		};
		dTree.prototype.node = function(node, nodeId) {
			var str = '<div class="dTreeNode">' + this.indent(node, nodeId);
			var tempId = $ittool.getNnum('tv_');
			if (this.config.useIcons
					&& (node.pid != -1 || mydata.get("rootNode"))) {
				if (!node.icon)
					node.icon = (this.root.id == node.pid) ? this.icon.root
							: ((node._hc) ? this.icon.folder : this.icon.node);
				if (!node.iconOpen)
					node.iconOpen = (node._hc) ? this.icon.folderOpen
							: this.icon.node;
				if (this.root.id == node.pid) {
					node.icon = this.icon.root;
					node.iconOpen = this.icon.root;
				}
				str += '<img id="i' + this.obj + nodeId + '" src="'
						+ ((node._io) ? node.iconOpen : node.icon)
						+ '" alt="" />';
				str += '<span id="' + tempId
						+ '" style="display:none;" cvalue="' + node.name
						+ '" cpid="' + node.pid + '" cid="' + node.id
						+ '"></span>';
				if (mydata.get("type") == 'checkbox') {
					str += '<img onmouseover="this.src=\'img/check_over\'+this.getAttribute(\'idd\')+\'.gif\'" checked="false" idd="1" onmouseout="this.src=\'img/check\'+this.getAttribute(\'idd\')+\'.gif\'" onclick="'
							+ this.obj
							+ '.cboxClick(this);'
							+ this.obj
							+ '.oCbox(this);" vid="'
							+ tempId
							+ '" id="i'
							+ this.obj
							+ nodeId
							+ '_" src="img/check1.gif" name="cb'
							+ this.dId
							+ '"/>';
				}
			}
			if (node.url && (node.pid != -1 || mydata.get("rootNode"))) {
				str += '<a id="s'
						+ this.obj
						+ nodeId
						+ '" vid="'
						+ tempId
						+ '" class="'
						+ ((this.config.useSelection) ? ((node._is ? 'nodeSel'
								: 'node')) : 'node') + '" href="' + node.url
						+ '"';
				if (node.title)
					str += ' title="' + node.title + '"';
				if (node.target)
					str += ' target="' + node.target + '"';
				if (this.config.useStatusText)
					str += ' onmouseover="window.status=\'' + node.name + '\';return true;" onmouseout="window.status=\'\';return true;" ';
				if (this.config.useSelection
						&& ((node._hc && this.config.folderLinks) || !node._hc))
					str += ' onclick="javascript: ' + this.obj + '.s(' + nodeId
							+ ',this);"';
				str += '>';

			} else if ((!this.config.folderLinks || !node.url) && node._hc
					&& node.pid != this.root.id)
				str += '<a vid="' + tempId + '" href="javascript: ' + this.obj
						+ '.o(' + nodeId + ');" class="node">';
			if (node.pid != -1 || mydata.get("rootNode"))
				str += node.name;
			if (node.url
					|| ((!this.config.folderLinks || !node.url) && node._hc))
				str += '</a>';
			str += '</div>';
			if (node._hc) {
				str += '<div id="d'
						+ this.obj
						+ nodeId
						+ '" class="clip" style="display:'
						+ ((this.root.id == node.pid || node._io) ? 'block'
								: 'none') + ';">';
				str += this.addNode(node);
				str += '</div>';
			}

			this.aIndent.pop();
			return str;

		};
		dTree.prototype.cboxClick = function(t) {
			var tiddd = t.getAttribute('iddd');
			if (tiddd == null || tiddd == 'null') {
				t.setAttribute('idd', t.getAttribute('idd') == '1' ? 2 : 1);
			} else {
				t.setAttribute('iddd', null);
			}
			t.src = 'img/check' + t.getAttribute('idd') + '.gif';
		};
		dTree.prototype.hasChild = function(t) {
			var childs = t.getAttribute('childs');
			if (childs == 'true') {
				return true;
			} else {
				var vObj = $it(t.getAttribute("vid"));
				var cid = vObj.getAttribute('cid');
				var cboxs = document.getElementsByName('cb' + this.dId)
				for ( var i = 0; i < cboxs.length; i++) {
					var vObj1 = $it(cboxs[i].getAttribute("vid"));
					if (vObj1.getAttribute("cpid") == cid) {
						t.setAttribute('childs', 'true');
						return true;
					}
				}
			}
			t.src = 'img/check' + t.getAttribute('idd') + '.gif';
		};
		dTree.prototype.oCbox = function(t) {
			var vObj = $it(t.getAttribute("vid"));
			var cid = vObj.attr('cid');
			var cboxs = document.getElementsByName('cb' + this.dId)
			for ( var i = 0; i < cboxs.length; i++) {
				var vObj1 = $it(cboxs[i].getAttribute("vid"));
				if (vObj1.getAttribute("cpid") == cid) {
					var tidd = t.getAttribute('idd');
					cboxs[i].setAttribute('iddd', tidd);
					cboxs[i].setAttribute('idd', tidd);
					cboxs[i].click();
				}
			}
		}
		dTree.prototype.cbox = function() {
			var cboxs = document.getElementsByName('cb' + this.dId)
			var s = '';
			var arr = new Array();
			var j = 0;
			for ( var i = 0; i < cboxs.length; i++) {
				var vObj = $it(cboxs[i].getAttribute("vid"));
				if (cboxs[i].getAttribute("idd") == '2') {
					arr[j] = new Array();
					arr[j][0] = vObj.getAttribute("cid");
					arr[j][1] = vObj.getAttribute("pid");
					arr[j++][2] = vObj.getAttribute("cvalue");
				}
			}
			if (mydata.get("cboxcallback")) {
				var cboxcb = mydata.get("cboxcallback");
				cboxcb(arr);
			}
		};
		dTree.prototype.indent = function(node, nodeId) {
			var str = '';
			if (this.root.id != node.pid) {
				for ( var n = 0; n < this.aIndent.length; n++)
					str += '<img src="' + ((this.aIndent[n] == 1 && this.config.useLines) ? this.icon.line
							: this.icon.empty) + '" alt="" />';
				(node._ls) ? this.aIndent.push(0) : this.aIndent.push(1);
				if (node._hc) {
					str += '<a href="javascript: ' + this.obj + '.o(' + nodeId
							+ ');"><img id="j' + this.obj + nodeId + '" src="';
					if (!this.config.useLines)
						str += (node._io) ? this.icon.nlMinus
								: this.icon.nlPlus;
					else
						str += ((node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom
								: this.icon.minus)
								: ((node._ls && this.config.useLines) ? this.icon.plusBottom
										: this.icon.plus));
					str += '" alt="" /></a>';

				} else
					str += '<img src="' + ((this.config.useLines) ? ((node._ls) ? this.icon.joinBottom
							: this.icon.join)
							: this.icon.empty) + '" alt="" />';
			}
			return str;
		};
		dTree.prototype.setCS = function(node) {
			var lastId;
			for ( var n = 0; n < this.aNodes.length; n++) {
				if (this.aNodes[n].pid == node.id)
					node._hc = true;
				if (this.aNodes[n].pid == node.pid)
					lastId = this.aNodes[n].id;
			}
			if (lastId == node.id)
				node._ls = true;
		};
		dTree.prototype.getSelected = function() {
			var sn = this.getCookie('cs' + this.obj);
			return (sn) ? sn : null;

		};
		// Highlights the selected node
		dTree.prototype.s = function(id, t) {
			if (!this.config.useSelection)
				return;
			var cn = this.aNodes[id];
			if (cn._hc && !this.config.folderLinks)
				return;
			if (this.selectedNode != id) {
				if (this.selectedNode || this.selectedNode == 0) {
					eOld = document.getElementById("s" + this.obj
							+ this.selectedNode);
					eOld.className = "node";
				}
				eNew = document.getElementById("s" + this.obj + id);
				eNew.className = "nodeSel";
				this.selectedNode = id;
				if (mydata.get("callback") && t) {
					if (!this.hasChild(t)) {
						var cb = mydata.get("callback");
						var vObj = $it(t.getAttribute("vid"));
						cb(vObj);
					}
				}
				if (this.config.useCookies)
					this.setCookie('cs' + this.obj, cn.id);
			}

		};
		// Toggle Open or close
		dTree.prototype.o = function(id) {
			var cn = this.aNodes[id];
			this.nodeStatus(!cn._io, id, cn._ls);
			cn._io = !cn._io;
			if (this.config.closeSameLevel)
				this.closeLevel(cn);
			if (this.config.useCookies)
				this.updateCookie();
		};

		// Open or close all nodes

		dTree.prototype.oAll = function(status) {
			for ( var n = 0; n < this.aNodes.length; n++) {
				if (this.aNodes[n]._hc && this.aNodes[n].pid != this.root.id) {
					this.nodeStatus(status, n, this.aNodes[n]._ls)
					this.aNodes[n]._io = status;
				}
			}
			if (this.config.useCookies)
				this.updateCookie();
		};
		// Opens the tree to a specific node
		dTree.prototype.openTo = function(nId, bSelect, bFirst) {
			if (!bFirst) {
				for ( var n = 0; n < this.aNodes.length; n++) {
					if (this.aNodes[n].id == nId) {
						nId = n;
						break;
					}
				}
			}
			var cn = this.aNodes[nId];
			if (cn.pid == this.root.id || !cn._p)
				return;
			cn._io = true;
			cn._is = bSelect;
			if (this.completed && cn._hc)
				this.nodeStatus(true, cn._ai, cn._ls);
			if (this.completed && bSelect)
				this.s(cn._ai);
			else if (bSelect)
				this._sn = cn._ai;
			this.openTo(cn._p._ai, false, true);
		};
		// Closes all nodes on the same level as certain node
		dTree.prototype.closeLevel = function(node) {
			for ( var n = 0; n < this.aNodes.length; n++) {
				if (this.aNodes[n].pid == node.pid
						&& this.aNodes[n].id != node.id && this.aNodes[n]._hc) {
					this.nodeStatus(false, n, this.aNodes[n]._ls);
					this.aNodes[n]._io = false;
					this.closeAllChildren(this.aNodes[n]);
				}
			}
		}
		// Closes all children of a node
		dTree.prototype.closeAllChildren = function(node) {
			for ( var n = 0; n < this.aNodes.length; n++) {
				if (this.aNodes[n].pid == node.id && this.aNodes[n]._hc) {
					if (this.aNodes[n]._io)
						this.nodeStatus(false, n, this.aNodes[n]._ls);
					this.aNodes[n]._io = false;
					this.closeAllChildren(this.aNodes[n]);
				}
			}
		}
		// Change the status of a node(open or closed)
		dTree.prototype.nodeStatus = function(status, id, bottom) {
			eDiv = document.getElementById('d' + this.obj + id);
			eJoin = document.getElementById('j' + this.obj + id);
			if (this.config.useIcons) {
				eIcon = document.getElementById('i' + this.obj + id);
				eIcon.src = (status) ? this.aNodes[id].iconOpen
						: this.aNodes[id].icon;
			}
			eJoin.src = (this.config.useLines) ? ((status) ? ((bottom) ? this.icon.minusBottom
					: this.icon.minus)
					: ((bottom) ? this.icon.plusBottom : this.icon.plus))
					: ((status) ? this.icon.nlMinus : this.icon.nlPlus);
			eDiv.style.display = (status) ? 'block' : 'none';
		};
		// [Cookie] Clears a cookie
		dTree.prototype.clearCookie = function() {
			var now = new Date();
			var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
			this.setCookie('co' + this.obj, 'cookieValue', yesterday);
			this.setCookie('cs' + this.obj, 'cookieValue', yesterday);
		};
		// [Cookie] Sets value in a cookie
		dTree.prototype.setCookie = function(cookieName, cookieValue, expires,
				path, domain, secure) {
			document.cookie = escape(cookieName) + '=' + escape(cookieValue)
					+ (expires ? '; expires=' + expires.toGMTString() : '')
					+ (path ? '; path=' + path : '')
					+ (domain ? '; domain=' + domain : '')
					+ (secure ? '; secure' : '');
		};
		// [Cookie] Gets a value from a cookie
		dTree.prototype.getCookie = function(cookieName) {
			var cookieValue = '';
			var posName = document.cookie.indexOf(escape(cookieName) + '=');
			if (posName != -1) {
				var posValue = posName + (escape(cookieName) + '=').length;
				var endPos = document.cookie.indexOf(';', posValue);
				if (endPos != -1)
					cookieValue = unescape(document.cookie.substring(posValue,
							endPos));
				else
					cookieValue = unescape(document.cookie.substring(posValue));
			}
			return (cookieValue);
		};

		// [Cookie] Returns ids of open nodes as a string

		dTree.prototype.updateCookie = function() {
			var str = '';
			for ( var n = 0; n < this.aNodes.length; n++) {
				if (this.aNodes[n]._io && this.aNodes[n].pid != this.root.id) {
					if (str)
						str += '.';
					str += this.aNodes[n].id;
				}
			}
			this.setCookie('co' + this.obj, str);
		};
		// [Cookie] Checks if a node id is in a cookie
		dTree.prototype.isOpen = function(id) {
			var aOpen = this.getCookie('co' + this.obj).split('.');
			for ( var n = 0; n < aOpen.length; n++)
				if (aOpen[n] == id)
					return true;
			return false;

		};
		var d = new dTree(objName);
		return d;
	},
	createCalendar : function(bindid, refObj, data) {
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				format : "yyyy-MM-dd",
				showTime : false,
				drag : true,
				callback : null
			};
			for (i in defaults) {
				options[i] = (!isNULL(data) && !isNULL(data[i])) ? data[i]
						: defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();
		var table_id = $ittool.getNnum("table_");
		var table_year_id = $ittool.getNnum("table_");
		var table_month_id = $ittool.getNnum("table_");
		var table_hour_id = $ittool.getNnum("table_");
		var table_minute_id = $ittool.getNnum("table_");
		var nowDate = new Date();
		refObj = $it(refObj);
		var pos = $ittool.getElementPos(refObj);
		var calendarObj = null;

		/***cache **/
		$ittool.hiddenClass('calendar_date_select');
		document.body.setAttribute("calFlag", 1);

		var rebuilder = function() {
			var date = new Date();
			date.setYear($it(table_year_id).value);
			date.setMonth($it(table_month_id).value - 1);
			calendar_builder($it(table_id), date);
			$it(table_id + '_tip').innerHTML = date
					.format(mydata.get("format"));
		};
		var reSelect = function(d) {
			$it(table_year_id).value = d.getFullYear();
			$it(table_month_id).value = d.getMonth() + 1;
			if (mydata.get("showTime")) {
				$it(table_hour_id).value = d.getHours();
				$it(table_minute_id).value = d.getMinutes();
			}
		};

		var calendar_top = function(calendarObj) {
			var div = $ittool.createTag("div", "cds_top");
			div.style.cssText = "clear: left;";
			calendarObj.appendChild(div);
		};
		var calendar_header = function(calendarObj) {
			var div = $ittool.createTag("div", "cds_header");
			div.style.cssText = "clear: left;";
			var a_next = $ittool.createTag("a", "next");
			a_next.href = "###";
			var a_pre = $ittool.createTag("a", "prev");
			a_pre.href = "###";
			div.appendChild(a_next);
			div.appendChild(a_pre);
			var date = new Date();
			var s_year = $ittool.createTag("select", "year");
			s_year.id = table_year_id;
			for ( var i = 1913; i < date.getFullYear() + 10; i++) {
				var op = $ittool.createTagNoId("option");
				op.value = i;
				op.innerHTML = i;
				s_year.appendChild(op);
				if (i == date.getFullYear()) {
					op.setAttribute("selected", "selected");
				}
			}
			var s_month = $ittool.createTag("select", "month");
			s_month.id = table_month_id;
			for ( var i = 0; i < 12; i++) {
				var op = $ittool.createTagNoId("option");
				op.value = i + 1;
				op.innerHTML = i + 1;
				s_month.appendChild(op);
				if (i == date.getMonth()) {
					op.setAttribute("selected", "selected");
				}
			}
			$ittool.addEvent(s_month, "change", function() {
				rebuilder();
			});
			$ittool.addEvent(s_year, "change", function() {
				rebuilder();
			});
			$ittool.addEvent(a_next, "click", function() {
				if (s_month.selectedIndex == 11) {
					s_month.options[0].selected = true;
					s_year.options[s_year.selectedIndex + 1].selected = true;
				} else {
					s_month.options[s_month.selectedIndex + 1].selected = true;
				}
				rebuilder();
			});
			$ittool.addEvent(a_pre, "click", function() {
				if (s_month.selectedIndex == 0) {
					s_month.options[11].selected = true;
					s_year.options[s_year.selectedIndex - 1].selected = true;
				} else {
					s_month.options[s_month.selectedIndex - 1].selected = true;
				}
				rebuilder();
			});
			div.appendChild(s_year);
			div.appendChild(s_month);
			calendarObj.appendChild(div);
			return div;
		};

		var calendar_builder = function(table, date_current) {
			var tbody = table.getElementsByTagName('tbody');
			if (tbody && tbody.length > 0)
				table.removeChild(tbody[0]);
			var table_body = $ittool.createTagNoId("tbody");
			var todayDate = date_current.getDate();
			var date1 = date_current.newDate();
			date1.setDate(1);
			var weekday = date1.getDay();
			date1.setMonth(date1.getMonth() + 1);
			date1.setDate(0);
			var days = date1.getDate();
			var arrMonth = new Array();
			var date_pre = date_current.newDate();
			date_pre.setMonth(date_pre.getMonth());
			date_pre.setDate(0);
			var pre_days_t = date_pre.getDate();
			for ( var i = pre_days_t - weekday + 1; i <= pre_days_t; i++) {
				arrMonth.push(i);
			}
			arrMonth.push(-2);
			for (i = 1; i <= days; i++) {
				arrMonth.push(i);
			}
			arrMonth.push(-1);
			var pre_days_t = date_pre.getDate();
			for ( var i = 1; i < 18; i++) {
				arrMonth.push(i);
			}
			var p = 0;
			var flag = true;
			var tempDate = date_current.newDate();
			tempDate.setMonth(tempDate.getMonth() - 1);
			for ( var i = 0; i < 6; i++) {
				var tbody_tr = $ittool.createTagNoId("tr", "row_" + i);
				for ( var j = 0; j < 7; j++) {
					var tbody_td = $ittool.createTagNoId("td");
					$ittool.addEvent(tbody_td, "mouseover", function(tdobj) {
						var date = Date.parseString(tdobj.attr("cv"),
								'yyyyMMdd');
						if (mydata.get("showTime")) {
							var d = new Date();
							date.setHours($it(table_hour_id).value,
									$it(table_minute_id).value, d.getSeconds(),
									d.getMilliseconds());
						}
						$it(table_id + '_tip').innerHTML = date.format(mydata
								.get("format"));
					});
					$ittool.addEvent(tbody_td, "mouseout", function(tdobj) {
						$it(table_id + '_tip').innerHTML = date_current
								.format(mydata.get("format"));
					});
					$ittool.addEvent(tbody_td, "click", function(tdobj) {
						var date = Date.parseString(tdobj.attr("cv"),
								'yyyyMMdd');
						if (mydata.get("showTime")) {
							var d = new Date();
							date.setHours($it(table_hour_id).value,
									$it(table_minute_id).value, d.getSeconds(),
									d.getMilliseconds());
						}
						$it(bindid).value = date.format(mydata.get("format"));
						var cb = mydata.get("callback");
						if (cb) {
							cb($it(bindid).value, date);
						}
						calendarObj.setHidden();
					});
					var dd = arrMonth[p++];
					if (dd == -1 || dd == -2) {
						if (dd == -2) {
							flag = false;
						} else if (dd == -1) {
							flag = true;
						}
						dd = arrMonth[p++];
						tempDate.setMonth(tempDate.getMonth() + 1);
					}
					tempDate.setDate(dd);
					tbody_td.attr("cv", tempDate.format("yyyyMMdd"));
					tempDate.setDate(1);
					if (dd == todayDate && !flag) {
						tbody_td.addClass("today selected");
					}
					if (flag) {
						tbody_td.addClass("other");
					}
					tbody_td.innerHTML = dd;
					if (j == 0 || j == 6) {
						tbody_td.addClass("weekend");
					}
					tbody_td.style.cursor = 'pointer';
					tbody_tr.appendChild(tbody_td);
				}
				table_body.appendChild(tbody_tr);
			}
			table.appendChild(table_body);
		};

		var calendar_body = function(calendarObj) {
			var div = $ittool.createTagNoId("div", "cds_body");
			div.style.cssText = "clear: left;";
			var table = $ittool.createTagNoId("table");
			table.id = table_id;
			table.setAttribute("cellpadding", '0px');
			table.setAttribute("cellspacing", '0px');
			table.width = "100%";
			var table_thead = $ittool.createTagNoId("thead");
			var thead_tr = $ittool.createTagNoId("tr");
			var week = [ "\u65E5", "\u4E00", "\u4E8C", "\u4E09", "\u56DB",
					"\u4E94", "\u516D" ];
			for ( var i = 0; i < week.length; i++) {
				var thead_th = $ittool.createTagNoId("th");
				thead_th.innerHTML = week[i];
				thead_tr.appendChild(thead_th);
			}

			table_thead.appendChild(thead_tr);
			table.appendChild(table_thead);
			calendar_builder(table, new Date());
			div.appendChild(table);
			calendarObj.appendChild(div);
		};

		var calendar_button = function(calendarObj) {
			var div = $ittool.createTag("div", "cds_buttons");
			div.style.cssText = "clear: left;";
			var date = new Date();
			if (mydata.get("showTime")) {
				var s_hour = $ittool.createTag("select", "hour");
				s_hour.id = table_hour_id;
				for ( var i = 0; i < 24; i++) {
					var op = $ittool.createTagNoId("option");
					op.value = i;
					op.innerHTML = i;
					s_hour.appendChild(op);
					if (i == date.getHours()) {
						op.setAttribute("selected", "selected");
					}
				}
				var seperator = $ittool.createTagNoId("span", "seperator", ":");
				var s_minute = $ittool.createTagNoId("select", "minute");
				s_minute.id = table_minute_id;
				for ( var i = 0; i < 60; i++) {
					var op = $ittool.createTagNoId("option");
					op.value = i + 1;
					op.innerHTML = i + 1;
					s_minute.appendChild(op);
					if (i == date.getMinutes() - 1) {
						op.setAttribute("selected", "selected");
					}
				}
				var br = $ittool.createTagNoId("br");
				div.appendChild(s_hour);
				div.appendChild(seperator);
				div.appendChild(s_minute);
				div.appendChild(br);
			}

			var a_today = $ittool.createTagNoId("a", null, "\u4ECA\u5929");
			a_today.href = "###";
			var span = $ittool.createTagNoId("span", "button_seperator", " | ");
			var a_clear = $ittool.createTagNoId("a", null, "\u6E05\u9664");
			a_clear.href = "###";

			$ittool.addEvent(a_today, "click", function() {
				calendar_builder($it(table_id), new Date());
				reSelect(new Date());
			});
			$ittool.addEvent(a_clear, "click", function() {
				$it(bindid).value = '';
				calendarObj.setHidden();
			});
			div.appendChild(a_today);
			div.appendChild(span);
			div.appendChild(a_clear);

			calendarObj.appendChild(div);
			return div;
		};
		var calendar_footer = function(calendarObj) {
			var div = $ittool.createTag("div", "cds_footer");
			div.style.cssText = "clear: left;";
			var span = $ittool.createTagNoId("span", null, "&nbsp;");
			span.id = table_id + '_tip';
			div.appendChild(span);
			calendarObj.appendChild(div);
			return div;
		};
		var calendar_bottom = function(calendarObj) {
			var div = $ittool.createTag("div", "cds_bottom");
			div.style.cssText = "clear: left;";
			calendarObj.appendChild(div);
		};
		if (isNotBlank(refObj.attr("idd"))) {
			calendarObj = document.getElementById(refObj.attr("idd"));
			table_id = calendarObj.getAttribute("tid");
			table_year_id = calendarObj.getAttribute("yid");
			table_month_id = calendarObj.getAttribute("mid");
			table_hour_id = calendarObj.getAttribute("hid");
			table_minute_id = calendarObj.getAttribute("mmid");
			calendarObj.setShow();
			document.onselectstart = function() {
				return false;
			}

		} else {
			calendarObj = $ittool.createTag("div", "calendar_date_select");
			$ittool.addEvent(calendarObj, "click", function() {
				document.body.setAttribute("calFlag", 1);
			});
			calendarObj.attr("tid", table_id);
			calendarObj.attr("yid", table_year_id);
			calendarObj.attr("mid", table_month_id);
			calendarObj.attr("hid", table_hour_id);
			calendarObj.attr("mmid", table_minute_id);
			refObj.attr("idd", calendarObj.id);

			calendar_top(calendarObj);
			var handle1 = calendar_header(calendarObj);
			calendar_body(calendarObj);
			var handle2 = calendar_button(calendarObj);
			var handle3 = calendar_footer(calendarObj);
			calendar_bottom(calendarObj);
			if (mydata.get("drag")) {
				$ittool.createDrag(handle1, calendarObj);
				$ittool.createDrag(handle2, calendarObj);
				$ittool.createDrag(handle3, calendarObj);
			}
			document.onselectstart = function() {
				return false;
			}

			document.onclick = function() {
				if (document.body.getAttribute("calFlag") == 0) {
					$ittool.hiddenClass('calendar_date_select');
					document.onselectstart = null;
				}
				document.body.setAttribute("calFlag", 0);
			};

			document.body.appendChild(calendarObj);
		}

		if (isNotBlank($it(bindid).value)) {
			var date = Date
					.parseString($it(bindid).value, mydata.get("format"));
			calendar_builder($it(table_id), date);
			reSelect(date);
		}
		calendarObj.style.top = (pos.y + refObj.offsetHeight) + 'px';
		calendarObj.style.left = pos.x + 'px';
		calendarObj.style.position = 'absolute';
	},
	createProgressBar : function(bingid, obj, data) {
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				showDetail : true,
				showStop : true,
				detailHeight : 300,
				height : 20,
				count : 0,
				callback : null,
				stopcallback : null

			};
			for (i in defaults) {
				options[i] = (!isNULL(data) && !isNULL(data[i])) ? data[i]
						: defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();
		var progressBar = function() {
			this.pid = $ittool.getNnum("pb_");
			this.msgId = null;
			this.count = mydata.get("count");
			this.step = 0;
			this.width = 0;
			this.bg = null;
			this.text = null;
			this.stop = 0;
			this.tip = true;
		};
		progressBar.prototype.createProgress = function() {
			var bg = $ittool.createTagNoId("div", "bg");

			bg.style.right = (mydata.get("showDetail") ? 10 : 0) + "px";
			bg.style.borderLeft = "0px";

			var text = $ittool.createTagNoId("div", "pb_text", "0%");
			text.style.cssText = "line-height:" + ($it(bingid).height - 1)
					+ "px";

			var progress_c = $ittool.createTagNoId("div");
			var progress = $ittool.createTagNoId("div", "progressbar");
			progress.style.height = mydata.get("height") + 'px';
			progress.appendChild(bg);
			progress.appendChild(text);

			var progressbar_detail = $ittool.createTag("div",
					"progressbar_detail");
			progressbar_detail.setHidden();
			var progressbar_menu = $ittool.createTag("div", "menu");
			var progressbar_content = $ittool.createTag("div", "content");
			this.msgId = progressbar_content.id;
			progressbar_content.style.height = mydata.get("detailHeight")
					+ "px";
			progressbar_menu
			$ittool.addEvent(progressbar_menu, "click", function() {
				$it(progressbar_detail.id).setHidden();
			});

			progressbar_detail.appendChild(progressbar_menu);
			progressbar_detail.appendChild(progressbar_content);

			if (mydata.get("showDetail")) {
				var btn = $ittool.createTagNoId("div", "btn", "&nbsp;");
				btn.detail1 = function() {

				};
				var j = "["
				if (mydata.get("showStop")) {
					j += "{text:'\u4E2D\u6B62',callback:function(){"
					j += obj
					j += ".setStop();}},"
				}
				j += "{text:'\u8BE6\u7EC6\u4FE1\u606F',callback:function(){$it('"
						+ progressbar_detail.id + "').setShow();}}]";
				$itniwo.createContextMenu(btn, j);
				progress.appendChild(btn);
			}

			progress_c.appendChild(progress);
			progress_c.appendChild(progressbar_detail);
			$it(bingid).appendChild(progress_c);
			this.width = progress.offsetWidth
					- (mydata.get("showDetail") ? 12 : 2);
			bg.style.width = this.width + 'px';
			this.bg = bg;
			this.text = text;
		};
		progressBar.prototype.addMsg = function(msg) {
			if (this.stop == 1) {
				if (this.tip) {
					var scb = mydata.get("stopcallback");
					if (scb)
						scb();
					this.tip = false;
				}
				return;
			} else if (this.stop == 2) {
				if (mydata.get("callback") && this.tip) {
					var cb = mydata.get("callback");
					if (cb)
						cb();
					this.tip = false;
				}
			}
			var msg_div = $ittool.createTagNoId("div", "msg m1", msg);
			var c_div = $it(this.msgId);
			c_div.insert(msg_div);
			$ittool.scrollToBottom(c_div);
		};
		progressBar.prototype.setCount = function(count) {
			this.count = count;
		};
		/** unnormal stop ***/
		progressBar.prototype.setStop = function() {
			this.stop = 1;
		};
		progressBar.prototype.isStop = function() {
			return this.stop != 0;
		};
		/** normal stop ***/
		progressBar.prototype.setOver = function() {
			this.stop = 2;
		};
		progressBar.prototype.setStep = function(step) {
			if (this.stop == 1) {
				if (this.tip) {
					var scb = mydata.get("stopcallback");
					if (scb)
						scb();
					this.tip = false;
				}
				return;
			} else if (this.stop == 2) {
				if (mydata.get("callback") && this.tip) {
					var cb = mydata.get("callback");
					if (cb)
						cb();
					this.tip = false;
				}
				this.step = this.count + 1;
			}
			this.step += step;
			var v = this.width - (this.step / this.count) * this.width;
			var p = (this.width - v) / this.width;
			p = $ittool.formatNumber(p * 100, 0);
			if (p >= 100) {
				p = 100;
				v = 0;
				this.stop = 2;
			}
			this.text.innerHTML = p + '%';
			this.bg.style.width = v + 'px';
		};
		var p = new progressBar();
		p.createProgress();
		return p;
	},
	createAutoComplete : function(bindid, searchFunc, selectFunc, datas) {
		var bandObj = $it(bindid);
		var autoDiv_ = $ittool.createTag("div", "itniwo_autoComplete");
		var autoComplete = function() {
			this.autoDiv = autoDiv_;
			this.autoItems = $ittool.createTagNoId("ul");
			this.autoDiv.setHidden();
			this.idx = 0;
		};
		autoComplete.prototype.search = function(datas) {
			if (datas) {
				for ( var i in datas) {
					if (datas[i]
							&& (datas[i] + '').toUpperCase().startWith(
									bandObj.value.toUpperCase())) {
						this.addItem( {}, datas[i]);
					}
				}
			}
		};
		autoComplete.prototype.addItem = function(item, text) {
			var li = $ittool.createTagNoId("li", null, text);
			for (i in item) {
				li.attr(i, item[i]);
			}
			$ittool.addEvent(li, "click", function() {
				if (selectFunc) {
					selectFunc(bandObj, li);
				}
				autoDiv_.setHidden();
				document.onclick = null;
			});
			this.autoItems.insert(li);
		};
		autoComplete.prototype.select = function(v) {
			var lis = this.autoItems.getElementsByTagName("li");
			for ( var i = 0; i < lis.length; i++) {
				var liObj = $it(lis[i]);
				liObj.removeClass("acitem");
			}
			var oo = lis[this.idx];
			if (oo)
				oo.addClass("acitem");
			this.idx += v;
			if (this.idx < 0)
				this.idx = lis.length - 1;
			if (this.idx >= lis.length)
				this.idx = 0;
		};
		var ac = new autoComplete();
		var show = false;
		$ittool.addEvent(bandObj, "keyup", function(o) {
			var v = o.value;
			if (isNotBlank(v)) {
				$ittool.removeNodes(ac.autoItems);
				ac.autoDiv.setShow();
				ac.autoItems.focus();
				if (searchFunc) {
					searchFunc(bandObj.value);
				} else if (datas) {
					ac.search(datas);
				}
			} else {
				ac.autoDiv.setHidden();
			}
		});
		$ittool.addEvent(bandObj, "click", function() {
			bandObj.insertAfter(ac.autoDiv);
			var pos = $ittool.getElementPos(bandObj);
			ac.autoDiv.css("top", (pos.y + bandObj.offsetHeight) + 'px').css(
					"left", pos.x + 'px').css("min-width",
					(bandObj.offsetWidth - 1) + 'px');
			ac.autoDiv.insert(ac.autoItems);
			show = true;
			if (isNotBlank(bandObj.value)) {
				ac.autoDiv.setShow();
				ac.search(datas);
			}
			if (document.onclick) {
				document.onclick();
				document.onclick = null;
			}
			document.onclick = function() {
				if (!show) {
					ac.autoDiv.setHidden();
					document.onclick = null;
				}
				show = false;
			};
			document.onkeyup = null;
			document.onkeyup = function(e) {
				var e = e || event;
				var currKey = e.keyCode || e.which || e.charCode;
				if (currKey == 38) {
					ac.select(-1);
				} else if (currKey == 40) {
					ac.select(1);
				} else if (currKey == 13) {
					var lis = ac.autoItems.getElementsByTagName("li");
					if (lis[ac.idx - 1])
						lis[ac.idx - 1].click();
					ac.autoDiv.setHidden();
				}
			};
		});
		return ac;
	},
	createColorPicker : function(data) {
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				bindid : '',
				symbol : true,
				text : true,
				color : null,
				callback : null

			};
			for (i in defaults) {
				options[i] = (!isNULL(data) && !isNULL(data[i])) ? data[i]
						: defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();

		var bandObj = $it(mydata.get("bindid"));
		var css_id = $ittool.getNnum();
		if (!mydata.get("text")) {
			var ht_p = "<span class='colorpspan' id='"
					+ css_id
					+ "'><span class='cp_icon' id='"
					+ css_id
					+ "_icon'><span class='cp_color'>&nbsp;</span><span class='cp_alpha' id='"
					+ css_id
					+ "_alpha'>&nbsp;</span><span class='cp_image' title='Open Color Picker'>&nbsp;</span><span class='cp_container'>&nbsp;</span></span></span>";
			$it(mydata.get("bindid")).innerHTML = ht_p;
			if (mydata.get("color")) {
				$it(css_id + '_icon').css("background-color",
						mydata.get("color"));
				$it(css_id + '_alpha').css("visibility", 'hidden');
			}
			bandObj = $it(css_id);
		}

		var correct_x = 0, correct_y = 0;
		var pos_sel, pos_slider;
		var top = '', left = '', color = '', showColor = bandObj.val();
		var brightness = 1, saturation = 1, hue = 0;
		var w = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		w[8] = 8, w[46] = 46;
		var y = new Array();
		y['0'] = 1, y['1'] = 1, y['2'] = 1, y['3'] = 1, y['4'] = 1, y['5'] = 1;
		y['6'] = 1, y['7'] = 1, y['8'] = 1, y['9'] = 1, y['A'] = 1, y['B'] = 1;
		y['C'] = 1, y['D'] = 1, y['E'] = 1, y['F'] = 1, y['a'] = 1, y['b'] = 1;
		y['c'] = 1, y['d'] = 1, y['e'] = 1, y['f'] = 1;

		var init_color = function() {
			var parsex = showColor;
			var inputhex = '';
			if (parsex == '') {
			} else {
				for ( var i = 0; i < parsex.length; i++) {
					if (parsex.charAt(i) != '#' && (parsex.charAt(i) + '') in y) {
						if (inputhex.length < 6) {
							inputhex += parsex.charAt(i) + '';
						}
					}
				}
				switch (inputhex.length) {
				case 0:
					inputhex = '000000' + inputhex;
					break;
				case 1:
					inputhex = '00000' + inputhex;
					break;
				case 2:
					inputhex = '0000' + inputhex;
					break;
				case 3:
					inputhex = '000' + inputhex;
					break;
				case 4:
					inputhex = '00' + inputhex;
					break;
				case 5:
					inputhex = '0' + inputhex;
					break;
				}
				parsex = $ittool.hex2rgb(inputhex);
				$it('cp_r').val(parsex['r']);
				$it('cp_g').val(parsex['g']);
				$it('cp_b').val(parsex['b']);
				$it('cp_hex').val(inputhex);
				parsex = $ittool.rgb2hsv(parsex['r'], parsex['g'], parsex['b']);
				hue = 120 - Math.round(parsex['h'] * 1 / 3);
				hue < 0 && (hue = 0)
				hue > 119 && (hue = 119);
				$it('cp_sample').css('background', '#' + inputhex);
				saturation = parsex['s'];
				brightness = parsex['v'];
			}
		};
		var hex_valid_and_draw = function() {
			var a = true, hexistr = '';
			hexistr = showColor;
			switch (hexistr.length) {
			case 1:
				hexistr = '00000' + hexistr;
				break;
			case 2:
				hexistr = '0000' + hexistr;
				break;
			case 3:
				hexistr = '000' + hexistr;
				break;
			case 4:
				hexistr = '00' + hexistr;
				break;
			case 5:
				hexistr = '0' + hexistr;
				break;
			}
			if (hexistr.length > 0) {
				for ( var i = 0; i < hexistr.length; i++) {
					if (!((hexistr.charAt(i) + '') in y)) {
						a = false;
					}
				}
			}
			if (a) {
				if (hexistr == '') {
					hue = 119;
					saturation = 0;
					brightness = 0;
				} else {
					var b = $ittool.hex2rgb(hexistr);
					$it('cp_r').val(b['r']);
					$it('cp_g').val(b['g']);
					$it('cp_b').val(b['b']);
					var b = $ittool.rgb2hsv(b['r'], b['g'], b['b']);
					hue = -1 * (Math.round(b['h'] * 1 / 3) - 120);
					if (hue < 0) {
						hue = 0;
					}
					if (hue > 119) {
						hue = 119;
					}
					saturation = b['s'];
					brightness = b['v']
				}
				init_positions();
				init_colors();
			}
		};
		var init_positions = function() {
			var pos_huebox = $ittool.getElementPos($it('cp_hue'));
			var pos_sbbox = $ittool.getElementPos($it('cp_grad_div'));
			left = pos_huebox.x + 'px';
			top = (pos_huebox.y + hue) + 'px';
			$it('cp_slider').css('left', left).css('top', top);
			left = (pos_sbbox.x + Math.round(saturation * 1.3) - 4) + 'px';
			top = (pos_sbbox.y + $it('cp_grad_div').offsetHeight - 4 - Math
					.round(brightness * 1.3)) + 'px';
			$it('cp_picker').css('left', left).css('top', top)
		};

		var update_inputs = function() {
			var a = $ittool.hsb2rgb_hex(-1 * (hue - 119) * 3, saturation,
					brightness, 'rgb');
			$it('cp_r').val(Math.round(a['r']) * 1);
			$it('cp_g').val(Math.round(a['g']) * 1);
			$it('cp_b').val(Math.round(a['b']) * 1);
			$it('cp_hex').val($ittool.rgb2hex(a['r'], a['g'], a['b']));
		};

		var init_colors = function() {
			$it('cp_grad_div').css(
					'background-color',
					'#' + $ittool.hsb2rgb_hex(-1 * (hue - 119) * 3, 100, 100,
							'hex'));
			$it('cp_sample').css(
					'background-color',
					'#' + $ittool.hsb2rgb_hex(-1 * (hue - 119) * 3, saturation,
							brightness, 'hex')).css('background-image', 'none');
		};

		var run_colorp_colorpicker = function() {
			init_positions();
			hex_valid_and_draw();
			init_colors();
		};

		var init_color_picker = function() {
			document.onselectstart = function() {
				return false;
			}

			var bandPos = $ittool.getElementPos(bandObj);
			var colorPicker = $ittool.createTag("div", "itniwo_colorPicker");
			colorPicker.attr("id", "colorPicker")
			colorPicker.css("top", (bandPos.y + bandObj.offsetHeight) + 'px');
			colorPicker.css("left", bandPos.x + 'px');
			colorPicker.onselectstart = function() {
				return false;
			};

			var cp_wrapper = $ittool.createTagNoId("div", "colorp_wrapper");
			cp_wrapper.attr("id", "cp_wrapper");
			var cp_grad_wrap = $ittool.createTagNoId("div", "colorp_grad_wrap");
			cp_grad_wrap.attr("id", "cp_wrapper");
			var cp_grad = $ittool.createTagNoId("div", "colorp_grad");
			cp_grad.attr("id", "cp_grad");
			var cp_grad_div = $ittool.createTagNoId("div", "colorp_grad_div");
			cp_grad_div.attr("id", 'cp_grad_div');

			cp_grad.insert(cp_grad_div);
			cp_grad_wrap.insert(cp_grad);

			cp_grad_div.onmousedown = function(event) {
				show = false;
				$it('cp_switcher').removeClass('colorp_switcher_h').addClass(
						'colorp_switcher_s');
				var pos_sbbox = $ittool.getElementPos(cp_grad_div);
				var event = event || window.event;
				saturation = Math
						.round((event.clientX - pos_sbbox.x - 1) / 1.3);
				saturation > 100 && (saturation = 100);
				saturation < 1 && (saturation = 1);
				brightness = -1
						* (Math.round((event.clientY - pos_sbbox.y - 1) / 1.3) - 100);
				brightness > 100 && (brightness = 100);
				brightness < 1 && (brightness = 1);
				$it('cp_sample').css("background-image", 'none');
				color = '#' + $ittool.hsb2rgb_hex(-1 * (hue - 119) * 3,
						saturation, brightness, 'hex');
				$it('cp_sample').css("background-color", color);
				init_positions();
				update_inputs();
				//pos_sel = $ittool.getElementPos($it('cp_picker'));
				//correct_x = event.clientX - pos_sel.x;
				//correct_y = event.clientY - pos_sel.y;
			};

			var cp_hue_wrap = $ittool.createTagNoId("div", "colorp_hue_wrap");
			cp_hue_wrap.attr("id", "cp_hue_wrap");
			var cp_hue = $ittool.createTagNoId("div", "colorp_hue");
			cp_hue.attr("id", "cp_hue");
			cp_hue_wrap.insert(cp_hue);

			cp_hue.onmousedown = function(event) {
				show = false;
				$it('cp_switcher').removeClass('colorp_switcher_h').addClass(
						'colorp_switcher_s');
				var pos_huebox = $ittool.getElementPos(cp_hue);
				var event = event || window.event;
				hue = event.clientY - pos_huebox.y - 5;
				hue < 0 && (hue = 0);
				hue > 119 && (hue = 119);
				init_positions();
				init_colors();
				update_inputs();
				//pos_slider = $ittool.getElementPos($it('cp_slider'));
				//correct_x = event.clientX - pos_slider.x;
				//correct_y = event.clientY - pos_slider.y;
			};

			var cp_data = $ittool.createTagNoId("div", "colorp_data");
			cp_data.attr("id", "colorp_data");
			var ht = "<div class='colorp_sample_wrapper' id='colorp_sample_wrapper'><div id='cp_sample' title='\u70B9\u51FB\u52A0\u5165\u989C\u8272\u5BF9\u6BD4\u533A' class='colorp_sample'></div></div>"
					+ "<div class='colorp_dataitem' style='padding-top:6px;'><b>R</b><input type='text' id='cp_r' class='colorp_input colorp_rgb' size='3' maxLength='3'></div>"
					+ "<div class='colorp_dataitem' style='padding-top:3px;'><b>G</b><input type='text' id='cp_g' class='colorp_input colorp_rgb' size='3' maxLength='3'></div>"
					+ "<div class='colorp_dataitem' style='padding-top:3px;'><b>B</b><input type='text' id='cp_b' class='colorp_input colorp_rgb' size='3' maxLength='3'></div>"
					+ "<div class='colorp_dataitem' style='padding:6px 0;'><b>#</b><input type='text' id='cp_hex' class='colorp_rgb' size='6' maxLength='6'></div>"
					+ "<div class='colorp_action'><div id='cp_ok' class='colorp_ok'></div><div id='cp_close' class='colorp_close'></div></div>";
			cp_data.innerHTML = ht;

			var s_ht = "<div id='ch1'style='background-color:#8064a2'></div><div id='ch2' style='background-color:#4f81bd'></div>"
					+ "<div id='ch3' style='background-color:#c0504d'></div><div id='ch4' style='background-color:#9bbb59'></div>"
					+ "<div id='ch5' style='background-color:#f79646'></div>"

			var cp_standard = $ittool.createTagNoId("div", "colorp_standard",
					s_ht);
			cp_standard.attr("id", "cp_standard")
			cp_standard.attr("title", "\u989C\u8272\u53C2\u7167\u533A")

			var h_ht = "<div id='cs1'style=''></div><div id='cs2' style=''></div>"
					+ "<div id='cs3' style=''></div><div id='cs4' style=''></div>"
					+ "<div id='cs5' style=''></div>"

			var cp_history = $ittool.createTagNoId("div", "colorp_history",
					h_ht);
			cp_history.attr("id", "cp_history")
			cp_history.attr("title", "\u989C\u8272\u5BF9\u6BD4\u533A")

			cp_wrapper.insert(cp_grad_wrap);
			cp_wrapper.insert(cp_hue_wrap);
			cp_wrapper.insert(cp_data);
			cp_wrapper.insert(cp_standard);
			cp_wrapper.insert(cp_history);
			colorPicker.insert(cp_wrapper);

			var cp_picker = $ittool.createTagNoId("div", "colorp_picker");
			cp_picker.attr("id", "cp_picker");
			top = (parseFloat(bandPos.y + bandObj.offsetHeight + 10 + 130)) + 'px';
			cp_picker.css("left", (bandPos.x + 10) + 'px');
			cp_picker.css("top", top);

			var mah_p = bandPos.y + bandObj.offsetHeight + 10 + 130 - 2;
			var mih_p = bandPos.y + bandObj.offsetHeight + 10;
			var maw_p = bandPos.x + bandObj.offsetHeight + 10 + 110;
			var miw_p = bandPos.x + 10 - 2;

			cp_grad_div.onclick = function(event) {
				$it('cp_switcher').removeClass('colorp_switcher_h').addClass(
						'colorp_switcher_s');
				var event = event || window.event;
				disX = event.clientX - 4;
				disY = event.clientY - 4;
				if (disX < maw_p && disX > miw_p)
					cp_picker.css("left", disX + "px");
				if (disY < mah_p && disY > mih_p)
					cp_picker.css("top", disY + "px");
			};

			cp_picker.onmousedown = function(event) {
				var event = event || window.event;
				disX = event.clientX - cp_picker.offsetLeft;
				disY = event.clientY - cp_picker.offsetTop;

				document.onmousemove = function(event) {
					$it('cp_switcher').removeClass('colorp_switcher_h')
							.addClass('colorp_switcher_s');
					var event = event || window.event;
					var iL = event.clientX - disX;
					var iT = event.clientY - disY;
					iL <= miw_p && (iL = miw_p);
					iT <= mih_p && (iT = mih_p - 5);
					iL >= maw_p && (iL = maw_p);
					iT >= mah_p && (iT = mah_p);
					cp_picker.css("left", iL + 'px');
					cp_picker.css("top", iT + 'px');

					var cp_sample_temp = $it('cp_sample');
					cp_sample_temp.css("background-image", 'none');

					var pos_sbbox = $ittool.getElementPos(cp_grad_div);
					correct_x = event.clientX - iL;
					correct_y = event.clientY - iT;
					var a = 0, tty = 0;
					a = event.clientX - correct_x;
					if (a < (pos_sbbox.x - 4)) {
						a = pos_sbbox.x - 4;
					}
					if (a > (pos_sbbox.x + 125)) {
						a = pos_sbbox.x + 125;
					}
					tty = event.clientY - correct_y;
					if (tty < (pos_sbbox.y - 4)) {
						tty = pos_sbbox.y - 4;
					}
					if (tty > (pos_sbbox.y + 125)) {
						tty = pos_sbbox.y + 125;
					}
					brightness = -1
							* (Math.round((tty - pos_sbbox.y + 5) / 1.3) - 100)
							+ 1;
					saturation = Math.round((a - pos_sbbox.x + 5) / 1.3);
					if (brightness == 1) {
						brightness = 0;
					}
					if (saturation == 1) {
						saturation = 0;
					}
					$it('cp_sample').css("background-image", 'none');
					color = '#' + $ittool.hsb2rgb_hex(-1 * (hue - 119) * 3,
							saturation, brightness, 'hex');
					$it('cp_sample').css("background-color", color);
					init_positions();
					update_inputs();

					//pos_sel = $ittool.getElementPos($it('cp_picker'));
					//correct_x = event.clientX - pos_sel.x;
					//correct_y = event.clientY - pos_sel.y;
					return true;
				};

				document.onmouseup = function() {
					document.onmousemove = null;
					document.onmouseup = null;
					this.releaseCapture && this.releaseCapture();
				};
				return true;
			};

			var cp_slider = $ittool.createTagNoId("div", "colorp_slider");
			cp_slider.attr("id", "cp_slider");
			top = parseFloat((bandPos.y + bandObj.offsetHeight + 10 + 130 + 4)) + 'px';
			left = parseFloat((bandPos.x + 10 + 130 + 21)) + 'px';
			cp_slider.css("top", top);
			cp_slider.css("left", left);

			var mah_s = bandPos.y + bandObj.offsetHeight + 10 + 130 + 4;
			var mih_s = bandPos.y + bandObj.offsetHeight + 10 + 4 + 10;

			cp_hue.onclick = function(event) {
				$it('cp_switcher').removeClass('colorp_switcher_h').addClass(
						'colorp_switcher_s');
				var event = event || window.event;
				disY = event.clientY - 4;
				if (disY < mah_s && disY > mih_s) {
					cp_slider.css("top", disY + "px");
				}
			};
			cp_slider.onmousedown = function(event) {
				var event = event || window.event;
				disY = event.clientY - cp_slider.offsetTop;
				document.onmousemove = function(event) {
					$it('cp_switcher').removeClass('colorp_switcher_h')
							.addClass('colorp_switcher_s');
					var event = event || window.event;
					var iT = event.clientY - disY;
					var maxT = document.documentElement.clientHeight
							- cp_slider.offsetHeight;

					iT <= 0 && (iT = 0);
					iT >= maxT && (iT = maxT);
					if (iT < mah_s && iT > mih_s)
						cp_slider.css("top", iT + "px");

					var pos_huebox = $ittool.getElementPos(cp_hue);
					hue = event.clientY - pos_huebox.y - 5;
					if (hue < 0)
						hue = 0;
					if (hue > 119)
						hue = 119;
					init_positions();
					init_colors();
					update_inputs();
					//pos_slider = $ittool.getElementPos($it('cp_slider'));
					//correct_x = event.clientX - pos_slider.x;
					//correct_y = event.clientY - pos_slider.y;
					return true;
				};

				document.onmouseup = function() {
					document.onmousemove = null;
					document.onmouseup = null;
					this.releaseCapture && this.releaseCapture();
				};
				return true;
			};

			var cp_switcher = $ittool.createTagNoId("div",
					"colorp_switcher colorp_switcher_s");
			cp_switcher.attr("id", "cp_switcher")
			top = parseFloat((bandPos.y + bandObj.offsetHeight + 10)) + 'px';
			left = parseFloat((bandPos.x + 10 + 130 + 21)) + 'px'
			cp_switcher.css("top", top)
			cp_switcher.css("left", left)

			document.body.appendChild(colorPicker);
			document.body.appendChild(cp_picker);
			document.body.appendChild(cp_slider);
			document.body.appendChild(cp_switcher);

			var chs = new Array();
			chs[0] = "ch1", chs[1] = "ch2", chs[2] = "ch3", chs[3] = "ch4",
					chs[4] = "ch5";
			chs[5] = "cs1", chs[6] = "cs2", chs[7] = "cs3", chs[8] = "cs4",
					chs[9] = "cs5";
			for ( var i = 0; i < chs.length; i++) {
				$ittool.addEvent($it(chs[i]), "click", function(o) {
					var h_c = o.css("background-color");
					if (h_c) {
						showColor = $ittool.rgb22hex(h_c);
						init_color();
						run_colorp_colorpicker();
					}
				});
			}

			var hIdx = 0;

			$ittool.addEvent($it('cp_sample'), "click", function(o) {
				var lis = $it('cp_history').getElementsByTagName("div");
				var liObj = $it(lis[hIdx++]);
				if (hIdx >= lis.length) {
					hIdx = 0;
				}
				liObj.css("background-color", '#' + $it('cp_hex').val());
			});
			$ittool.addEvent($it('cp_ok'), "click", function() {
				color = $it('cp_hex').val();
				var cc = color;
				if (mydata.get("symbol")) {
					color = '#' + color;
				}
				bandObj.val(color);
				var cb = mydata.get("callback");
				if (cb) {
					if (!mydata.get("text")) {
						var iconObj = $it(css_id + '_icon');
						if (iconObj) {
							iconObj.css('background-color', '#' + cc);
							$it(css_id + '_alpha').css("visibility", 'hidden');
						}
					}
					cb(color);
				}
				delColorPicker();
			});

			$ittool.addEvent($it('cp_close'), "click", function() {
				delColorPicker();
			});

			$ittool.addEvent($it('cp_switcher'), "click", function(o) {
				if (isNotBlank(o.attr("cp"))) {
					o.removeClass('colorp_switcher_h').addClass(
							'colorp_switcher_s');
					o.attr("cp", null);
					update_inputs();
				} else {
					o.removeClass('colorp_switcher_s').addClass(
							'colorp_switcher_h');
					o.attr("cp", $it('cp_hex').val());
					$it('cp_r').val('');
					$it('cp_g').val('');
					$it('cp_b').val('');
					$it('cp_hex').val('');
				}
			});

			colorPicker.onclick = cp_picker.onclick = cp_slider.onclick = cp_switcher.onclick = function() {
				show = false;
			};

			init_color();
			run_colorp_colorpicker();
		}
		var show = true;
		var delColorPicker = function() {
			if ($it('colorPicker'))
				document.body.removeChild($it('colorPicker'));
			if ($it('cp_picker'))
				document.body.removeChild($it('cp_picker'));
			if ($it('cp_slider'))
				document.body.removeChild($it('cp_slider'));
			if ($it('cp_switcher'))
				document.body.removeChild($it('cp_switcher'));
		};

		$ittool.addEvent(bandObj, "click", function() {
			if (document.onclick) {
				document.onclick();
			}
			if (!mydata.get("text")) {
				showColor = $ittool.rgb22hex($it(css_id + '_icon').css(
						'background-color'));
			} else {
				showColor = bandObj.val();
			}
			if (isUnDef(showColor)) {
				showColor = "";
			}
			correct_x = 0;
			correct_y = 0;
			init_color_picker();
			show = false;

			document.onclick = function() {
				if (show) {
					delColorPicker();
					this.releaseCapture && this.releaseCapture();
					document.onclick = null;
					document.onselectstart = null;
				}
				show = true;
			};
		});
	},
	tooltipMenu : function(data) {
		var dataMap = function() {
			var map = $ittool.map();
			var i, options = {}, defaults = {
				bindid : '',
				htmlid : null,
				content : null,
				event : "move",
				position : "left"

			};
			for (i in defaults) {
				options[i] = (!isNULL(data) && !isNULL(data[i])) ? data[i]
						: defaults[i];
			}
			for (i in options) {
				map.put(i, options[i]);
			}
			return map;
		};
		var mydata = dataMap();
		if (mydata.get("htmlid")) {
			var htmlObj = $it(mydata.get("htmlid"));
			htmlObj.setHidden();
		}
		var tipMenu = function() {
			this.flag = false;
		};
		var t = new tipMenu();
		tipMenu.prototype.initData = function() {
			var bandObj = $it(mydata.get("bindid"));
			if (mydata.get("event") == 'move') {
				$ittool.addEvent(bandObj, "mouseover", function() {
					var bandPos = $ittool.getElementPos(bandObj);
					var htmlObj = $it(mydata.get("htmlid"));
					htmlObj.addClass("tooltipmenu");
					htmlObj.css("position", "absolute");
					htmlObj.css("top",
							(bandPos.y + bandObj.offsetHeight) + 'px');
					htmlObj.css("left", bandPos.x + 'px');
					htmlObj.css("display", "block");
					$ittool.addEvent(htmlObj, "mouseover", function() {
						htmlObj.setShow();
					});
					$ittool.addEvent(htmlObj, "mouseout", function() {
						htmlObj.setHidden();
					});
				});
			} else {
				$ittool.addEvent(bandObj, "click", function() {
					var divObj = $ittool.createTagNoId("div", "tooltipmenu_d");
					document.body.appendChild(divObj);
					divObj.innerHTML = mydata.get("content");
					t.flag = false;
					var bandPos = $ittool.getElementPos(bandObj);
					divObj.css("position", "absolute");
					divObj
							.css("top",
									(bandPos.y + bandObj.offsetHeight) + 'px');
					if (mydata.get("position") == 'left') {
						divObj.css("left", bandPos.x + 'px');
					} else {
						var left = bandPos.x - divObj.offsetWidth
								+ bandObj.offsetWidth;
						divObj.css("left", left + 'px');
					}
					$ittool.addEvent(divObj, "click", function() {
						document.onclick = null;
						document.body.removeChild(divObj);
					});
					if (document.onclick) {
						document.onclick();
					}
					document.onclick = function() {
						if (t.flag) {
							document.body.removeChild(divObj);
							document.onclick = null;
						}
						t.flag = true;
					};
				});
			}
		};

		t.initData();
		return t;

	}
};
