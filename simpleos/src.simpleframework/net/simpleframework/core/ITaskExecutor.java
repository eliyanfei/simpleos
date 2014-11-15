package net.simpleframework.core;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITaskExecutor {

	void execute(ExecutorRunnable task);

	void addScheduledTask(long initialDelay, long period, ExecutorRunnable task);

	void removeScheduledTask(long period, String taskname);

	void close();
}
