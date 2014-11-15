package net.itsite.ttop;

public enum ETtop {
	ppriority0(1, 50), ppriority1(2, 80), ppriority2(3, 150);
	public int ppriority;
	public int points;

	ETtop(int ppriority, int points) {
		this.ppriority = ppriority;
		this.points = points;
	}

	public static int neededPoints(final int ppriority, int ddays) {
		for (final ETtop ttop : ETtop.values()) {
			if (ttop.ppriority == ppriority) {
				return ttop.points * ddays;
			}
		}
		return 0;
	}
}
