String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, '');
};
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, '');
};
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, '');
};
String.prototype.trimNBSP = function() {
	return this.replace(/^[\s\u3000\xA0]+|[\s\u3000\xA0]+$/g, '');
};
String.prototype.ltrimNBSP = function() {
	return this.replace(/^[\s\u3000\xA0]+/g, '');
};
String.prototype.rtrimNBSP = function() {
	return this.replace(/[\s\u3000\xA0]+$/g, '');
};
String.prototype.startWith = function(str) {
	var reg = new RegExp("^" + str);
	return reg.test(this);
};
String.prototype.endWith = function(str) {
	var reg = new RegExp(str + "$");
	return reg.test(this);
};
Date.LZ = function(x) {
	return (x < 0 || x > 9 ? "" : "0") + x
};
// Utility function to append a 0 to single-digit numbers
Date.LZ = function(x) {
	return (x < 0 || x > 9 ? "" : "0") + x
};
// Full month names. Change this for local month names
Date.monthNames = new Array('January', 'February', 'March', 'April', 'May',
		'June', 'July', 'August', 'September', 'October', 'November',
		'December');
// Month abbreviations. Change this for local month names
Date.monthAbbreviations = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
		'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
// Full day names. Change this for local month names
Date.dayNames = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday',
		'Thursday', 'Friday', 'Saturday');
// Day abbreviations. Change this for local month names
Date.dayAbbreviations = new Array('Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri',
		'Sat');
// Used for parsing ambiguous dates like 1/2/2000 - default to preferring 'American' format meaning Jan 2.
// Set to false to prefer 'European' format meaning Feb 1
Date.preferAmericanFormat = true;
if (!Date.prototype.getFullYear) {
	Date.prototype.getFullYear = function() {
		var yy = this.getYear();
		return (yy < 1900 ? yy + 1900 : yy);
	};
}
Date.parseString = function(val, format) {
	// If no format is specified, try a few common formats
	if (typeof (format) == "undefined" || format == null || format == "") {
		var generalFormats = new Array('y-M-d', 'MMM d, y', 'MMM d,y',
				'y-MMM-d', 'd-MMM-y', 'MMM d', 'MMM-d', 'd-MMM');
		var monthFirst = new Array('M/d/y', 'M-d-y', 'M.d.y', 'M/d', 'M-d');
		var dateFirst = new Array('d/M/y', 'd-M-y', 'd.M.y', 'd/M', 'd-M');
		var checkList = new Array(generalFormats,
				Date.preferAmericanFormat ? monthFirst : dateFirst,
				Date.preferAmericanFormat ? dateFirst : monthFirst);
		for ( var i = 0; i < checkList.length; i++) {
			var l = checkList[i];
			for ( var j = 0; j < l.length; j++) {
				var d = Date.parseString(val, l[j]);
				if (d != null) {
					return d;
				}
			}
		}
		return null;
	}
	this.isInteger = function(val) {
		for ( var i = 0; i < val.length; i++) {
			if ("1234567890".indexOf(val.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	};
	this.getInt = function(str, i, minlength, maxlength) {
		for ( var x = maxlength; x >= minlength; x--) {
			var token = str.substring(i, i + x);
			if (token.length < minlength) {
				return null;
			}
			if (this.isInteger(token)) {
				return token;
			}
		}
		return null;
	};
	val = val + "";
	format = format + "";
	var i_val = 0;
	var i_format = 0;
	var c = "";
	var token = "";
	var token2 = "";
	var x, y;
	var year = new Date().getFullYear();
	var month = 1;
	var date = 1;
	var hh = 0;
	var mm = 0;
	var ss = 0;
	var ampm = "";
	while (i_format < format.length) {
		// Get next token from format string
		c = format.charAt(i_format);
		token = "";
		while ((format.charAt(i_format) == c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
		}
		// Extract contents of value based on format token
		if (token == "yyyy" || token == "yy" || token == "y") {
			if (token == "yyyy") {
				x = 4;
				y = 4;
			}
			if (token == "yy") {
				x = 2;
				y = 2;
			}
			if (token == "y") {
				x = 2;
				y = 4;
			}
			year = this.getInt(val, i_val, x, y);
			if (year == null) {
				return null;
			}
			i_val += year.length;
			if (year.length == 2) {
				if (year > 70) {
					year = 1900 + (year - 0);
				} else {
					year = 2000 + (year - 0);
				}
			}
		} else if (token == "MMM" || token == "NNN") {
			month = 0;
			var names = (token == "MMM" ? (Date.monthNames
					.concat(Date.monthAbbreviations)) : Date.monthAbbreviations);
			for ( var i = 0; i < names.length; i++) {
				var month_name = names[i];
				if (val.substring(i_val, i_val + month_name.length)
						.toLowerCase() == month_name.toLowerCase()) {
					month = (i % 12) + 1;
					i_val += month_name.length;
					break;
				}
			}
			if ((month < 1) || (month > 12)) {
				return null;
			}
		} else if (token == "EE" || token == "E") {
			var names = (token == "EE" ? Date.dayNames : Date.dayAbbreviations);
			for ( var i = 0; i < names.length; i++) {
				var day_name = names[i];
				if (val.substring(i_val, i_val + day_name.length).toLowerCase() == day_name
						.toLowerCase()) {
					i_val += day_name.length;
					break;
				}
			}
		} else if (token == "MM" || token == "M") {
			month = this.getInt(val, i_val, token.length, 2);
			if (month == null || (month < 1) || (month > 12)) {
				return null;
			}
			i_val += month.length;
		} else if (token == "dd" || token == "d") {
			date = this.getInt(val, i_val, token.length, 2);
			if (date == null || (date < 1) || (date > 31)) {
				return null;
			}
			i_val += date.length;
		} else if (token == "hh" || token == "h") {
			hh = this.getInt(val, i_val, token.length, 2);
			if (hh == null || (hh < 1) || (hh > 12)) {
				return null;
			}
			i_val += hh.length;
		} else if (token == "HH" || token == "H") {
			hh = this.getInt(val, i_val, token.length, 2);
			if (hh == null || (hh < 0) || (hh > 23)) {
				return null;
			}
			i_val += hh.length;
		} else if (token == "KK" || token == "K") {
			hh = this.getInt(val, i_val, token.length, 2);
			if (hh == null || (hh < 0) || (hh > 11)) {
				return null;
			}
			i_val += hh.length;
			hh++;
		} else if (token == "kk" || token == "k") {
			hh = this.getInt(val, i_val, token.length, 2);
			if (hh == null || (hh < 1) || (hh > 24)) {
				return null;
			}
			i_val += hh.length;
			hh--;
		} else if (token == "mm" || token == "m") {
			mm = this.getInt(val, i_val, token.length, 2);
			if (mm == null || (mm < 0) || (mm > 59)) {
				return null;
			}
			i_val += mm.length;
		} else if (token == "ss" || token == "s") {
			ss = this.getInt(val, i_val, token.length, 2);
			if (ss == null || (ss < 0) || (ss > 59)) {
				return null;
			}
			i_val += ss.length;
		} else if (token == "a") {
			if (val.substring(i_val, i_val + 2).toLowerCase() == "am") {
				ampm = "AM";
			} else if (val.substring(i_val, i_val + 2).toLowerCase() == "pm") {
				ampm = "PM";
			} else {
				return null;
			}
			i_val += 2;
		} else {
			if (val.substring(i_val, i_val + token.length) != token) {
				return null;
			} else {
				i_val += token.length;
			}
		}
	}
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val != val.length) {
		return null;
	}
	// Is date valid for month?
	if (month == 2) {
		// Check for leap year
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) { // leap year
			if (date > 29) {
				return null;
			}
		} else {
			if (date > 28) {
				return null;
			}
		}
	}
	if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
		if (date > 30) {
			return null;
		}
	}
	// Correct hours value
	if (hh < 12 && ampm == "PM") {
		hh = hh - 0 + 12;
	} else if (hh > 11 && ampm == "AM") {
		hh -= 12;
	}
	return new Date(year, month - 1, date, hh, mm, ss);
};
Date.prototype.newDate = function() {
	var date = new Date();
	date.setTime(this.getTime());
	return date;
};
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"H+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter 
		"S" : this.getMilliseconds()
	//millisecond 
	}
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
// If Push and pop is not implemented by the browser

if (!Array.prototype.push) {
	Array.prototype.push = function array_push() {
		for ( var i = 0; i < arguments.length; i++)
			this[this.length] = arguments[i];
		return this.length;
	}
};

if (!Array.prototype.pop) {
	Array.prototype.pop = function array_pop() {
		lastElement = this[this.length - 1];
		this.length = Math.max(this.length - 1, 0);
		return lastElement;
	}
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
window.$it = function(id) {
	if (isNULL(id))
		return null;
	var obj = (typeof (id) == 'string') ? document.getElementById(id) : id;
	if (!obj) {
		return;
	}
	obj.val = function(value) {
		if (!isNULL(value)) {
			obj.value = value;
			return obj;
		}
		return obj.value;
	};
	obj.attr = function(name, value) {
		if (isUnDef(value)) {
			if ('class' == name) {
				return obj.className;
			}
			return obj.getAttribute(name);
		}
		obj.setAttribute(name, value);
		return obj;
	};
	obj.css = function(name, value) {
		var cs = obj.style.cssText;
		if (!isNotBlank(cs)) {
			cs = '';
		}
		var arr = cs.split(";");
		var map = $ittool.map();
		for ( var i = 0; i < arr.length; i++) {
			var arr_v = arr[i].split(':');
			if (arr_v.length == 2) {
				map.put(arr_v[0].trim(), arr_v[1]);
			}

		}
		if (isUnDef(value)) {
			var c = map.get(name);
			return c == false ? '' : c;
		}
		if (value == null) {
			map.removeByKey(name);
		} else {
			map.put(name, value);
		}

		var keys_ = map.keys();
		var css = '';
		var i = 0;
		for ( var j = 0; j < keys_.length; j++) {
			css += keys_[j] + ':' + map.get(keys_[j]);
			if (i++ != keys_.length - 1) {
				css += ';';
			}
		}
		obj.style.cssText = css;
		return obj;
		var c = map.get(name);
		return c == false ? '' : c;
	};
	obj.wid = function(value) {
		if (!isNULL(value)) {
			obj.style.width = value;
			return obj;
		}
		return obj.style.width;
	};
	obj.hei = function(value) {
		if (!isNULL(value)) {
			obj.style.height = value;
			return obj;
		}
		return obj.style.height;
	};
	obj.htmlVal = function(value) {
		if (!isNULL(value)) {
			obj.innerHTML = value;
			return obj;
		}
		return obj.innerHTML;
	};
	obj.hasClass = function(className) {
		if (obj.className)
			return obj.className.indexOf(className) != -1;
		return false;
	};
	obj.getClass = function() {
		return obj.className;
	};
	obj.addClass = function(className, reset) {
		if (isNULL(reset) || !reset) {
			if (!obj.hasClass(className)) {
				obj.className = obj.className + ' ' + className;
			}
		} else
			obj.className = className;
		return obj;
	};
	obj.removeClass = function(className) {
		if (className) {
			obj.className = obj.className.replace(className, '');
		} else {
			obj.className = '';
		}
		return obj;
	};
	obj.insertAfter = function(newElement) {
		var parent = obj.parentNode;
		if (parent.lastChild == obj) {
			parent.appendChild(newElement);
		} else {
			parent.insertBefore(newElement, obj.nextSibling);
		}
	};
	obj.insert = function(newElement) {
		obj.appendChild(newElement);
	};
	obj.getObj = function() {
		return obj;
	};
	obj.isView = function() {
		return obj.style.display == '';
	};
	obj.toggle = function() {
		if (obj.style.display == '') {
			obj.style.display = 'none';
		} else {
			obj.style.display = '';
		}
		return obj;
	};
	obj.setShow = function() {
		obj.style.display = '';
		return obj;
	};
	obj.setHidden = function() {
		obj.style.display = 'none';
		return obj;
	};
	obj.nextNode = function(s) {
		return $ittool.nextNode(obj, s);
	};
	obj.upNode = function(s) {
		return $ittool.upNode(obj, s);
	};
	obj.innerNode = function(s) {
		return $ittool.innerNode(obj, s);
	};
	obj.outerNode = function(s) {
		return $ittool.outerNode(obj, s);
	};
	return obj;
};
var $nnum = 0;
var $ittool = {
	setCookies : function(name, value) {
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString();
	},
	getCookies : function(name, value) {
		var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
		if (arr = document.cookie.match(reg))
			return unescape(arr[2]);
		else
			return value;
	},
	delCookies : function(name) {
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = this.getCookies(name);
		if (cval != null)
			document.cookie = name + "=" + cval + ";expires="
					+ exp.toGMTString();
	},
	removeNodes : function(obj) {
		while (obj.lastChild) {
			obj.removeChild(obj.lastChild)
		}
	},
	getEleByClass : function(sClass, oParent) {
		var aClass = [];
		var reClass = new RegExp("(^| )" + sClass + "( |$)");
		var aElem = this.getEleTagName("*", oParent);
		for ( var i = 0; i < aElem.length; i++) {
			reClass.test(aElem[i].className) && aClass.push(aElem[i]);
		}
		return aClass;
	},
	getEleTagName : function(elem, obj) {
		return (obj || document).getElementsByTagName(elem);
	},
	getNnum : function(name) {
		$nnum = $nnum + 1;
		if (name) {
			return name + $nnum;
		}
		return 'itniwo_' + $nnum;
	},
	hiddenClass : function(className) {
		var aClass = this.getEleByClass(className, document.body);
		for ( var i = 0; i < aClass.length; i++) {
			aClass[i].style.display = 'none';
		}
	},
	createTag : function(tag, className, data) {
		var obj = document.createElement(tag);
		obj = $it(obj);
		obj.attr("id", 'div_' + this.getNnum());
		if (!isNULL(className)) {
			obj.addClass(className, true);
		}
		if (!isNULL(data)) {
			obj.innerHTML = data;
		}
		return obj;
	},
	createTagNoId : function(tag, className, data) {
		var obj = document.createElement(tag);
		obj = $it(obj);
		if (!isNULL(className)) {
			obj.addClass(className, true);
		}
		if (!isNULL(data)) {
			obj.innerHTML = data;
		}
		return obj;
	},
	formatNumber : function(src, pos) {
		return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
	},
	map : function() {
		var elements = new Array();
		elements.isMap = function() {
			return true;
		};
		elements.size = function() {
			return elements.length;
		};
		elements.isEmpty = function() {
			return (elements.length < 1);
		};
		elements.clear = function() {
			elements = new Array();
		};
		elements.put = function(_key, _value) {
			elements.removeByKey(_key);
			elements.push( {
				key : _key,
				value : _value
			});
		};
		elements.removeByKey = function(_key) {
			var bln = false;
			try {
				for (i = 0; i < elements.length; i++) {
					if (elements[i].key == _key) {
						elements.splice(i, 1);
						return true;
					}
				}
			} catch (e) {
				bln = false;
			}
			return bln;
		};

		elements.removeByValue = function(_value) {//removeByValueAndKey
			var bln = false;
			try {
				for (i = 0; i < elements.length; i++) {
					if (elements[i].value == _value) {
						elements.splice(i, 1);
						return true;
					}
				}
			} catch (e) {
				bln = false;
			}
			return bln;
		};
		elements.get = function(_key) {
			try {
				for (i = 0; i < elements.length; i++) {
					if (elements[i].key == _key) {
						return elements[i].value;
					}
				}
			} catch (e) {
				return false;
			}
			return false;
		};
		elements.containsKey = function(_key) {
			var bln = false;
			try {
				for (i = 0; i < elements.length; i++) {
					if (elements[i].key == _key) {
						bln = true;
					}
				}
			} catch (e) {
				bln = false;
			}
			return bln;
		};
		elements.containsValue = function(_value) {
			var bln = false;
			try {
				for (i = 0; i < elements.length; i++) {
					if (elements[i].value == _value) {
						bln = true;
					}
				}
			} catch (e) {
				bln = false;
			}
			return bln;
		};
		elements.values = function() {
			var arr = new Array();
			for (i = 0; i < elements.length; i++) {
				arr.push(elements[i].value);
			}
			return arr;
		};
		elements.valuesByKey = function(_key) {
			var arr = new Array();
			for (i = 0; i < elements.length; i++) {
				if (elements[i].key == _key) {
					arr.push(elements[i].value);
				}
			}
			return arr;
		};
		elements.keys = function() {
			var arr = new Array();
			for (i = 0; i < elements.length; i++) {
				arr.push(elements[i].key);
			}
			return arr;
		};
		elements.keysByValue = function(_value) {
			var arr = new Array();
			for (i = 0; i < elements.length; i++) {
				if (_value == elements[i].value) {
					arr.push(elements[i].key);
				}
			}
			return arr;
		};
		return elements;
	},
	/*********************add event*******************/
	addEvent : function(obj, type, callback) {
		obj = $it(obj);
		if (obj.addEventListener) {
			obj.addEventListener(type, function() {
				callback(obj);
			});
			return true;
		} else if (obj.attachEvent) {
			return obj.attachEvent("on" + type, function() {
				callback(obj);
			});
		} else {
			return false;
		}
	},
	/*********************delete event*******************/
	delEvent : function(obj, type, callback) {
		obj = $it(obj);
		if (obj.addEventListener) {
			obj.removeEventListener(type, function() {
				callback(obj);
			});
			return true;
		} else if (obj.attachEvent) {
			obj.detachEvent("on" + type, function() {
				callback(obj);
			});
			return true;
		} else {
			return false;
		}
	},
	getElementPos : function(el) {
		var ua = navigator.userAgent.toLowerCase();
		var isOpera = (ua.indexOf('opera') != -1);
		var isIE = (ua.indexOf('msie') != -1 && !isOpera); // not opera spoof
		if (el.parentNode === null || el.style.display == 'none') {
			return false;
		}
		var parent = null;
		var pos = [];
		var box;

		if (el.getBoundingClientRect) { //IE      
			box = el.getBoundingClientRect();
			var scrollTop = Math.max(document.documentElement.scrollTop,
					document.body.scrollTop);
			var scrollLeft = Math.max(document.documentElement.scrollLeft,
					document.body.scrollLeft);
			return {
				x : box.left + scrollLeft,
				y : box.top + scrollTop
			};
		} else if (document.getBoxObjectFor) {// gecko    
			box = document.getBoxObjectFor(el);
			var borderLeft = (el.style.borderLeftWidth) ? parseInt(el.style.borderLeftWidth)
					: 0;
			var borderTop = (el.style.borderTopWidth) ? parseInt(el.style.borderTopWidth)
					: 0;
			pos = [ box.x - borderLeft, box.y - borderTop ];
		} else { // safari & opera    
			pos = [ el.offsetLeft, el.offsetTop ];
			parent = el.offsetParent;

			if (parent != el) {
				while (parent) {
					pos[0] += parent.offsetLeft;
					pos[1] += parent.offsetTop;
					parent = parent.offsetParent;
				}
			}
			if (ua.indexOf('opera') != -1
					|| (ua.indexOf('safari') != -1 && el.style.position == 'absolute')) {
				pos[0] -= document.body.offsetLeft;
				pos[1] -= document.body.offsetTop;
			}
		}
		if (el.parentNode) {
			parent = el.parentNode;
		} else {
			parent = null;
		}
		while (parent && parent.tagName != 'BODY' && parent.tagName != 'HTML') { // account for any scrolled ancestors
			pos[0] -= parent.scrollLeft;
			pos[1] -= parent.scrollTop;

			if (parent.parentNode) {
				parent = parent.parentNode;
			} else {
				parent = null;
			}
		}
		return {
			x : pos[0],
			y : pos[1]
		};
	},
	scrollToBottom : function(obj1) {
		var obj = eval(obj1);
		var scrollHeight = obj.scrollHeight;
		var clientHeight = obj.clientHeight;
		var scrollTop = scrollHeight.scrollTop;
		if (scrollHeight > clientHeight) {
			scrollTop = parseInt(scrollHeight - clientHeight);
		}
		obj.scrollTop = scrollTop;
	},
	rgb2hsv : function(a, c, d) {
		var e = 0, maxrgb = 0, delta = 0, h = 0, s = 0, b = 0;
		var f = new Array();
		h = 0.0;
		e = Math.min(Math.min(a, c), d);
		maxrgb = Math.max(Math.max(a, c), d);
		delta = (maxrgb - e);
		b = maxrgb;
		if (maxrgb != 0.0) {
			s = 255.0 * delta / maxrgb;
		} else {
			s = 0.0;
		}
		if (s != 0.0) {
			if (a == maxrgb) {
				h = (c - d) / delta;
			} else {
				if (c == maxrgb) {
					h = 2.0 + (d - a) / delta;
				} else {
					if (d == maxrgb) {
						h = 4.0 + (a - c) / delta;
					}
				}
			}
		} else {
			h = -1.0;
		}
		h = h * 60;
		if (h < 0.0) {
			h = h + 360.0;
		}
		f['h'] = Math.round(h);
		f['s'] = Math.round(s * 100 / 255);
		f['v'] = Math.round(b * 100 / 255);
		if (f['h'] > 360) {
			f['h'] = 360;
		}
		if (f['s'] > 100) {
			f['s'] = 100;
		}
		if (f['v'] > 100) {
			f['v'] = 100;
		}
		return f;
	},
	hex2rgb : function(h) {
		var a = new String();
		a = h;
		a.toUpperCase();
		h = a;
		var i, x = '0123456789ABCDEF', c = '';
		var b = new Array();
		if (h) {
			h = h.toUpperCase();
			for (i = 0; i < 6; i += 2) {
				switch (i) {
				case 0:
					b['r'] = (16 * x.indexOf(h.charAt(i)) + x.indexOf(h
							.charAt(i + 1))) * 1;
					break;
				case 2:
					b['g'] = (16 * x.indexOf(h.charAt(i)) + x.indexOf(h
							.charAt(i + 1))) * 1;
					break;
				case 4:
					b['b'] = (16 * x.indexOf(h.charAt(i)) + x.indexOf(h
							.charAt(i + 1))) * 1;
					break;
				}
			}
		}
		return b;
	},
	rgb2hex : function(r, g, b) {
		var a = new Array();
		a[0] = r;
		a[1] = g;
		a[2] = b;
		var c = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
				'c', 'd', 'e', 'f' ];
		var d = '';
		for ( var i = 0; i < a.length; i++) {
			dec = parseInt(a[i]);
			d += c[parseInt(dec / 16)] + c[dec % 16];
		}
		return d;
	},
	hsb2rgb_hex : function(a, b, c, d) {
		var h = a / 360;
		var s = b / 100;
		var v = c / 100;
		var e = new Array();
		var f, var_i, var_1, var_2, var_3, var_r, var_g;
		if (s == 0) {
			e['r'] = v * 255;
			e['g'] = v * 255;
			e['b'] = v * 255
		} else {
			f = h * 6;
			var_i = Math.floor(f);
			var_1 = v * (1 - s);
			var_2 = v * (1 - s * (f - var_i));
			var_3 = v * (1 - s * (1 - (f - var_i)));
			if (var_i == 0) {
				var_r = v;
				var_g = var_3;
				var_b = var_1
			} else if (var_i == 1) {
				var_r = var_2;
				var_g = v;
				var_b = var_1
			} else if (var_i == 2) {
				var_r = var_1;
				var_g = v;
				var_b = var_3
			} else if (var_i == 3) {
				var_r = var_1;
				var_g = var_2;
				var_b = v
			} else if (var_i == 4) {
				var_r = var_3;
				var_g = var_1;
				var_b = v
			} else {
				var_r = v;
				var_g = var_1;
				var_b = var_2
			}
			e['r'] = Math.round(var_r * 255);
			e['g'] = Math.round(var_g * 255);
			e['b'] = Math.round(var_b * 255)
		}
		if (d == 'hex') {
			return this.rgb2hex(e['r'], e['g'], e['b'])
		} else if (d == 'rgb') {
			return (e);
		} else {
			return (e);
		}
	},
	rgb22hex : function(rgb) {
		if (!isNotBlank(rgb)) {
			return "#000000";
		}
		if (rgb.charAt(0) == '#') {
			return rgb;
		}
		var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
		var aColor = rgb.replace(/(?:\(|\)|rgb|RGB)*/g, "").split(",");
		var strHex = "#";
		for ( var i = 0; i < aColor.length; i++) {
			var hex = Number(aColor[i]).toString(16);
			if (hex === "0") {
				hex += hex;
			}
			strHex += hex;
		}
		if (strHex.length !== 7) {
			strHex = rgb;
		}
		return strHex;
	},
	createDrag : function(triggerObj, moveObj) {
		triggerObj.onmousedown = function(event) {
			var event = event || window.event;
			disX = event.clientX - moveObj.offsetLeft;
			disY = event.clientY - moveObj.offsetTop;

			document.onmousemove = function(event) {
				var event = event || window.event;
				var iL = event.clientX - disX;
				var iT = event.clientY - disY;
				var maxL = document.documentElement.clientWidth
						- moveObj.offsetWidth;
				var maxT = document.documentElement.clientHeight
						- moveObj.offsetHeight;

				iL <= 0 && (iL = 0);
				iT <= 0 && (iT = 0);
				iL >= maxL && (iL = maxL);
				iT >= maxT && (iT = maxT);

				moveObj.style.left = iL + "px";
				moveObj.style.top = iT + "px";

				return true;
			};

			document.onmouseup = function() {
				document.onmousemove = null;
				document.onmouseup = null;
				this.releaseCapture && this.releaseCapture();
			};
			return true;
		};
	},
	matchNode : function(type, obj, ss) {
		if (!obj) {
			return null;
		}
		if (type == 0) {
			if (obj.nodeName.toUpperCase() == ss.toUpperCase()) {
				return obj;
			}
		} else if (type == 1) {
			if (obj.attr("id") == ss) {
				return obj;
			}
		} else if (type == 2) {
			if (obj.hasClass(ss)) {
				return obj;
			}
		}
		return null;
	},
	nextNode : function(obj, s) {
		var pn = obj.nextSibling;
		var nownode = obj;
		pn = $it(pn);
		var type = 0;
		var ss = s.substring(1, s.length);
		if (s.charAt(0) == '#') {
			type = 1;
		} else if (s.charAt(0) == '.') {
			type = 2;
		} else {
			ss = s;
		}
		while (pn) {
			if (pn.nodeType == 1) {
				var mo = this.matchNode(type, pn, ss);
				if (mo) {
					return mo;
				}
				var oo = this.recursionNode(pn, type, ss);
				if (oo) {
					return oo;
				}
			}
			pn = $it(pn.nextSibling);
			if (pn == null) {
				pn = nownode.parentNode;
				nownode = pn;
				if (pn != null) {
					pn = pn.nextSibling;
				}
			}
		}
		return null;
	},
	upNode : function(obj, s) {
		var pn = obj.previousSibling;
		var nownode = obj;
		pn = $it(pn);
		var type = 0;
		var ss = s.substring(1, s.length);
		if (s.charAt(0) == '#') {
			type = 1;
		} else if (s.charAt(0) == '.') {
			type = 2;
		} else {
			ss = s;
		}
		while (pn) {
			if (pn.nodeType == 1) {
				var mo = this.matchNode(type, pn, ss);
				if (mo) {
					return mo;
				}
				var oo = this.recursionNode(pn, type, ss);
				if (oo) {
					return oo;
				}
			}
			pn = $it(pn.previousSibling);
			if (pn == null) {
				pn = nownode.parentNode;
				nownode = pn;
				if (pn != null) {
					pn = pn.previousSibling;
				}
			}
		}
		return null;
	},
	recursionNode : function(obj, type, s) {
		obj = $it(obj);
		var cs = obj.childNodes;
		for ( var i = 0; i < cs.length; i++) {
			var o = $it(cs[i]);
			if (o.nodeType == 1) {
				var mo = this.matchNode(type, o, s);
				if (mo) {
					return mo;
				}
				var oo = this.recursionNode(o, type, s);
				if (oo) {
					return oo;
				}
			}
		}
		return null;
	},
	innerNode : function(obj, s) {
		var cs = obj.childNodes;
		var type = 0;
		var ss = s.substring(1, s.length);
		if (s.charAt(0) == '#') {
			type = 1;
		} else if (s.charAt(0) == '.') {
			type = 2;
		} else {
			ss = s;
		}

		for ( var i = 0; i < cs.length; i++) {
			var o = cs[i];
			if (cs[i].nodeType == 1) {
				var oo = this.recursionNode(o, type, ss);
				if (oo) {
					return oo;
				}
			}
		}
	},
	outerNode : function(obj, s) {
		var pn = obj.parentNode;
		pn = $it(pn);
		var type = 0;
		var ss = s.substring(1, s.length);
		if (s.charAt(0) == '#') {
			type = 1;
		} else if (s.charAt(0) == '.') {
			type = 2;
		} else {
			ss = s;
		}
		while (pn) {
			var mo = this.matchNode(type, pn, ss);
			if (mo) {
				return mo;
			}
			pn = $it(pn.parentNode);
		}
		return null;
	}

};