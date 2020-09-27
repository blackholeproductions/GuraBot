package net.celestialgaze.GuraBot.util;

public abstract class ArgRunnable<T> implements Runnable {
	private T arg;
	public ArgRunnable(T arg) {
		this.arg = arg;
	}
	public ArgRunnable() {}
	public void run(T arg) {
		this.arg = arg;
		run();
	}
	public T getArg() {
		return arg;
	}
}
