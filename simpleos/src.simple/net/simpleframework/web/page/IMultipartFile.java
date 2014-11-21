package net.simpleframework.web.page;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IMultipartFile {
	String getOriginalFilename();

	InputStream getInputStream() throws IOException;

	byte[] getBytes() throws IOException;

	long getSize();

	void transferTo(File file) throws IOException;
}
