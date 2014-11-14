function selectTag(id, t) {
	if (id == '') {
		if ($F("docu_keyworks") == '')
			$("docu_keyworks").value = t.innerHTML;
		else
			$("docu_keyworks").value += "," + t.innerHTML;
	} else {
		if ($F("docu_keyworks_" + id) == '')
			$("docu_keyworks_" + id).value = t.innerHTML;
		else
			$("docu_keyworks_" + id).value += "," + t.innerHTML;
	}
	t.$toggle();
}
function refreshDocu(tid) {
	$$('#documentNav .nav_arrow').each(function(c) {
		c.removeClassName('nav_arrow');
		c.removeClassName('a2');
	});
	if ($(tid))
		$(tid).addClassName('nav_arrow');
		$(tid).addClassName('a2');
}