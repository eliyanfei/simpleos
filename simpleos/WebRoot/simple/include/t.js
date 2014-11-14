$ready(function() {
	var header = $('site_header2');
	if (header) {
		var footer = $('site_footer');
		var m = $('site_content');
		var l = m.down('.category');
		var c = m.down('.Category_ListTemplatePage');
		var r = function() {
			var height = document.viewport.getHeight() - header.getHeight()
					- footer.getHeight();
			var style = 'height: ' + height + 'px';
			m.setStyle(style);
			if (l)
				l.setStyle(style);
			if (c)
				c.setStyle(style);

		};
		r.call();
		Event.observe(window, "resize", r);
	}
});