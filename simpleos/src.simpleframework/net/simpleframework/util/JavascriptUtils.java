package net.simpleframework.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipInputStream;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class JavascriptUtils {
	static Logger logger = ALoggerAware.getLogger(JavascriptUtils.class);

	public static String escape(final String input) {
		return StringEscapeUtils.escapeEcmaScript(input);
	}

	static Constructor<?> jsCompressorConstructor;
	static Method jsCompressMethod;

	static Constructor<?> cssCompressorConstructor;
	static Method cssCompressMethod;
	static {
		try {
			final Class<?> classJsCompressor = Class.forName("com.yahoo.platform.yui.compressor.JavaScriptCompressor");
			jsCompressorConstructor = classJsCompressor.getConstructor(Reader.class, BeanUtils.forName("org.mozilla.javascript.ErrorReporter"));
			jsCompressMethod = classJsCompressor.getMethod("compress", Writer.class, int.class, boolean.class, boolean.class, boolean.class,
					boolean.class);

			final Class<?> classCssCompressor = Class.forName("com.yahoo.platform.yui.compressor.CssCompressor");
			cssCompressorConstructor = classCssCompressor.getConstructor(Reader.class);
			cssCompressMethod = classCssCompressor.getMethod("compress", Writer.class, int.class);
		} catch (final Exception ex) {
			logger.warn(ex);
		}
	}

	public static String jsCompress(final String js) {
		if (jsCompressorConstructor == null) {
			return js;
		}
		final StringWriter oWriter = new StringWriter();
		try {
			final Object jcObj = jsCompressorConstructor.newInstance(new StringReader(js), null);
			jsCompressMethod.invoke(jcObj, oWriter, 200, true, false, false, false);
			return oWriter.toString();
		} catch (final Throwable e) {
			return js;
		} finally {
			try {
				oWriter.close();
			} catch (final IOException e) {
			}
		}
	}

	public static String cssCompress(final String js) {
		if (cssCompressorConstructor == null) {
			return js;
		}
		final StringWriter oWriter = new StringWriter();
		try {
			final Object jcObj = cssCompressorConstructor.newInstance(new StringReader(js));
			cssCompressMethod.invoke(jcObj, oWriter, 200);
			return oWriter.toString();
		} catch (final Throwable e) {
			return js;
		} finally {
			try {
				oWriter.close();
			} catch (final IOException e) {
			}
		}
	}

	public static String wrapWhenReady(final String functionBody) {
		final StringBuilder sb = new StringBuilder();
		sb.append("$ready(function() {");
		sb.append(StringUtils.blank(functionBody));
		sb.append("});");
		return sb.toString();
	}

	public static String wrapFunction(final String functionBody) {
		final StringBuilder sb = new StringBuilder();
		sb.append("(function() {");
		sb.append(StringUtils.blank(functionBody));
		sb.append("})();");
		return sb.toString();
	}

	public static String wrapScriptTag(final String javascript) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">");
		sb.append(StringUtils.blank(javascript));
		sb.append("</script>");
		return sb.toString();
	}

	public static String wrapStyleTag(final String css) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<style type=\"text/css\">");
		sb.append(StringUtils.blank(css));
		sb.append("</style>");
		return sb.toString();
	}

	private static Map<String, Boolean> rewriteMap = new ConcurrentHashMap<String, Boolean>();

	private static boolean rewrite(final String target) {
		if (rewriteMap.get(target) == null) {
			rewriteMap.put(target, Boolean.TRUE);
			return true;
		} else {
			return false;
		}
	}

	public static void unzipJsAndCss(final InputStream in, final String target, final boolean compress) throws IOException {
		if (JavascriptUtils.jsCompressorConstructor == null) {
			IoUtils.unzip(in, target, rewrite(target));
			return;
		}
		IoUtils.unzip(in, target, rewrite(target), new IoUtils.IUnZipHandle() {
			@Override
			public void doFile(final ZipInputStream is, final File destFile) throws IOException {
				final String filename = destFile.getName();
				if (compress && filename.endsWith(".js")) {
					final OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(destFile));
					try {
						final Object jcObj = JavascriptUtils.jsCompressorConstructor.newInstance(new InputStreamReader(is), null);
						JavascriptUtils.jsCompressMethod.invoke(jcObj, oWriter, 200, true, false, false, false);
					} catch (final Throwable e) {
						copyFile(is, destFile);
					} finally {
						if (oWriter != null) {
							oWriter.close();
						}
					}
				} else if (compress && filename.endsWith(".css")) {
					final OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(destFile));
					try {
						final Object ccObj = JavascriptUtils.cssCompressorConstructor.newInstance(new InputStreamReader(is));
						JavascriptUtils.cssCompressMethod.invoke(ccObj, oWriter, 200);
					} catch (final Throwable e) {
						copyFile(is, destFile);
					} finally {
						if (oWriter != null) {
							oWriter.close();
						}
					}
				} else {
					copyFile(is, destFile);
				}
			}

			private void copyFile(final ZipInputStream is, final File destFile) throws IOException {
				final BufferedOutputStream oStream = new BufferedOutputStream(new FileOutputStream(destFile));
				try {
					IoUtils.copyStream(is, oStream);
				} finally {
					if (oStream != null) {
						oStream.close();
					}
				}
			}
		});
	}
}
