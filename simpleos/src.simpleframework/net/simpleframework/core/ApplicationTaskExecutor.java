package net.simpleframework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.simpleframework.util.DateUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ApplicationTaskExecutor extends ALoggerAware implements ITaskExecutor {
	private int threadPoolSize = 20;

	public ApplicationTaskExecutor() {
		addScheduledTask(DateUtils.HOUR_PERIOD, DateUtils.HOUR_PERIOD, new ExecutorRunnable() {
			@Override
			public void task() {
				final ArrayList<Long> removes = new ArrayList<Long>();
				for (final Map.Entry<Long, ScheduledTask> entry : scheduledTasks.entrySet()) {
					final ScheduledTask scheduledTask = entry.getValue();
					if (scheduledTask.tasks.size() == 0) {
						scheduledTask.future.cancel(false);
						removes.add(entry.getKey());
					}
				}
				for (final Long r : removes) {
					scheduledTasks.remove(r);
				}
			}
		});
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(final int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	private ScheduledExecutorService executorService;

	public ScheduledExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newScheduledThreadPool(getThreadPoolSize());
		}
		return executorService;
	}

	@Override
	public void execute(final ExecutorRunnable task) {
		getExecutorService().execute(task);
	}

	private final Map<Long, ScheduledTask> scheduledTasks = new ConcurrentHashMap<Long, ScheduledTask>();

	@Override
	public void addScheduledTask(final long initialDelay, final long period,
			final ExecutorRunnable task) {
		ScheduledTask scheduledTask = scheduledTasks.get(period);
		if (scheduledTask == null) {
			scheduledTasks.put(period, scheduledTask = new ScheduledTask());
		}
		scheduledTask.tasks.add(task);
		if (scheduledTask.future == null) {
			scheduledTask.future = getExecutorService().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					final ScheduledTask scheduledTask = scheduledTasks.get(period);
					if (scheduledTask != null) {
						for (final ExecutorRunnable task : scheduledTask.tasks) {
							task.run();
						}
					}
				}
			}, initialDelay, period, TimeUnit.SECONDS);
		}
	}

	@Override
	public void removeScheduledTask(final long period, final String taskname) {
		if (taskname == null) {
			return;
		}
		final ScheduledTask scheduledTask = scheduledTasks.get(period);
		if (scheduledTask != null) {
			for (final ExecutorRunnable task : scheduledTask.tasks) {
				if (taskname.equals(task.getTaskname())) {
					scheduledTask.tasks.remove(task);
					break;
				}
			}
		}
	}

	class ScheduledTask {
		Collection<ExecutorRunnable> tasks = new ArrayList<ExecutorRunnable>();

		ScheduledFuture<?> future;
	}

	@Override
	public void close() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}
}
