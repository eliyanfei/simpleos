package net.simpleframework.web.page.component.ui.htmleditor;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.util.ArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Toolbar extends ArrayListEx<Toolbar, String[]> {

	public static Map<String, Toolbar> map = new HashMap<String, Toolbar>();

	public static Toolbar of(final String[]... items) {
		return new Toolbar().append(items);
	}

	public static Toolbar SMALL = Toolbar.of(new String[] { "Bold", "Italic" }, new String[] { "Link", "Unlink" }, new String[] { "FontSize" },
			new String[] { "TextColor", "BGColor" });

	public static Toolbar BASIC = Toolbar.of(new String[] { "Bold", "Italic", "Underline", "Strike" },
			new String[] { "NumberedList", "BulletedList" }, new String[] { "Link", "Unlink" }, new String[] { "Font", "FontSize" }, new String[] {
					"TextColor", "BGColor" }, new String[] { "SpecialChar" });

	public static Toolbar STANDARD = Toolbar.of(new String[] { "Source" }, new String[] { "Bold", "Italic", "Underline", "Strike" }, new String[] {
			"PasteText", "PasteFromWord" }, new String[] { "Find", "Replace", "-", "RemoveFormat" }, new String[] { "NumberedList", "BulletedList",
			"-", "Outdent", "Indent", "Blockquote" }, new String[] { "JustifyLeft", "JustifyCenter", "JustifyRight", "JustifyBlock" }, new String[] {
			"Link", "Unlink", "Anchor" }, new String[] {}, new String[] { "Styles", "Format", "Font", "FontSize" }, new String[] { "TextColor",
			"BGColor" }, new String[] { "Image", "Table", "HorizontalRule", "Smiley", "SpecialChar" }, new String[] { "Maximize" });

	public static Toolbar FULL = Toolbar.of(new String[] { "Source", "-", "Save", "NewPage", "Preview", "-", "Templates" }, new String[] { "Cut",
			"Copy", "Paste", "PasteText", "PasteFromWord", "-", "Print", "SpellChecker", "Scayt" }, new String[] { "Undo", "Redo", "-", "Find",
			"Replace", "-", "SelectAll", "RemoveFormat" }, new String[] { "Form", "Checkbox", "Radio", "TextField", "Textarea", "Select", "Button",
			"ImageButton", "HiddenField" }, new String[] {},
			new String[] { "Bold", "Italic", "Underline", "Strike", "-", "Subscript", "Superscript" }, new String[] { "NumberedList", "BulletedList",
					"-", "Outdent", "Indent", "Blockquote", "CreateDiv", "-", "BidiLtr", "BidiRtl" }, new String[] { "JustifyLeft", "JustifyCenter",
					"JustifyRight", "JustifyBlock" }, new String[] { "Link", "Unlink", "Anchor" }, new String[] { "Image", "Flash", "Table",
					"HorizontalRule", "Smiley", "SpecialChar", "PageBreak" }, new String[] {},
			new String[] { "Styles", "Format", "Font", "FontSize" }, new String[] { "TextColor", "BGColor" },
			new String[] { "Maximize", "ShowBlocks" });
	static {
		map.put("Simple", Toolbar.SMALL);
		map.put("Basic", Toolbar.BASIC);
		map.put("News", Toolbar.STANDARD);
		map.put("Full", Toolbar.FULL);
	}
	private static final long serialVersionUID = -1226808677781921297L;
}
