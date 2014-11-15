package net.itsite.utils;

public class UID {
	protected static long COUNTER = 0;

	private static final Object lock = new Object();

	protected final long time;

	protected final long id;

	public UID() {
		time = System.currentTimeMillis();
		synchronized (UID.lock) {
			id = UID.COUNTER++;
		}
	}

	protected UID(final UID uid) {
		time = uid.time;
		id = uid.id;
	}

	public final long getTime() {
		return time;
	}

	public final long getID() {
		return id;
	}

	@Override
	public String toString() {
		return Long.toString(time, Character.MAX_RADIX) + Long.toString(id, Character.MAX_RADIX);
	}

	public String toString(final String dim) {
		return Long.toString(time, Character.MAX_RADIX) + dim + Long.toString(id, Character.MAX_RADIX);
	}

	@Override
	public int hashCode() {
		return (int) ((time ^ id) >> 32);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj != null && obj.getClass() == getClass()) {
			final UID uid = (UID) obj;
			return uid.time == time && uid.id == id;
		}
		return false;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	public static String asString() {
		return new UID().toString("-");
	}

	public static String asString(final String dim) {
		return new UID().toString(dim);
	}

	public static String get() {
		return new UID().toString();
	}

	public static void main(final String[] args) {
		for (int i = 0; i < 1000; i++) {
			System.out.println(UID.get());
		}
	}

}
