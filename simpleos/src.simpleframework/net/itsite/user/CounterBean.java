package net.itsite.user;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 
 * @author 李岩飞
 *
 */
public class CounterBean extends AbstractIdDataObjectBean {
	private String name;
	private int counter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}
