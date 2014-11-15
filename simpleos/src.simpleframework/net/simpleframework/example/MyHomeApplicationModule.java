package net.simpleframework.example;

import java.io.IOException;
import java.net.URL;

import net.simpleframework.my.home.DefaultHomeApplicationModule;
import net.simpleframework.my.home.HomeTabsBean;
import net.simpleframework.my.home.IHomeApplicationModule;
import net.simpleframework.util.IoUtils;
import net.simpleframework.web.page.PageRequestResponse;

public class MyHomeApplicationModule extends DefaultHomeApplicationModule {

	@Override
	public String getTabUrl(final PageRequestResponse requestResponse, final HomeTabsBean homeTab) {
		return "/my/home.jsp?" + IHomeApplicationModule.MYTAB_ID + "=" + homeTab.getId();
	}

	public static void main(final String[] args) {
		for (int i = 0; i < 50; i++) {
			final int j = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					int k = 0;
					while (true) {
						try {
							IoUtils.getStringFromInputStream(new URL(
									"http://127.0.0.1:8080/site/blog.html").openStream());
							System.out.println("Thread" + j + ": " + k++);
							try {
								Thread.sleep(500);
							} catch (final InterruptedException e) {
							}
						} catch (final IOException e) {
						}
					}
				}
			}).start();
		}
	}
}
