var __vote_element = function(o) {
	o = $target(o);
	return o.hasClassName("vote_c") ? o : o.up(".vote_c");
};

var __vote_action = function(o) {
	return __vote_element(o).action;
};

function __vote_cb_click(o) {
	var vg = o.up(".vg");
	var n = parseInt($F(vg.down()));
	if (n == 0)
		return;
	var n2 = 0;
	vg.select("INPUT").each(function(cb) {
		if (cb.checked)
			n2++;
	});
	if (n2 > n) {
		alert(VOTE_CLICK_MSG);
		o.checked = false;
	}
}

function __vote_addMethods(pa, beanId) {
	pa.refresh = function(params) {
		pa(params);
	};

	pa._create = function(params) {
		var ea = $Actions["ajaxVoteCreate"];
		ea.selector = pa.selector;
		ea(params);
	};

	pa._edit = function(params) {
		$Actions["ajaxVoteEditPage"].selector = pa.selector;
		$Actions["voteEditWindow"](params);
	};

	pa._delete = function(params) {
		var da = $Actions["ajaxVoteDelete"];
		da.selector = pa.selector;
		da(params);
	};

	pa._submit = function(params) {
		var va = $Actions["ajaxVoteSubmit"];
		va.selector = pa.selector;
		va($$Form("vote_" + beanId).addParameter(params));
	};

	pa._view = function(params) {
		var va = $Actions["ajaxVoteView"];
		va.selector = pa.selector;
		var w = $Actions["voteViewWindow"];
		var vt = pa.vote.select(".vt")[0];
		if (vt) {
			w.options.title = vt.innerHTML.truncate(20);
		}
		w(params);
	};
};