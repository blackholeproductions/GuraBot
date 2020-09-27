package net.celestialgaze.GuraBot.util;

public class BulletListBuilder {
	StringBuilder sb;
	boolean formatted = false;
	public BulletListBuilder() {
		sb = new StringBuilder();
	}
	public BulletListBuilder(boolean formatted) {
		sb = new StringBuilder();
		this.formatted = formatted;
	}
	public BulletListBuilder add(String title, String s) {
		return add(title, s, ": ");
	}
	public BulletListBuilder add(String title, String s, String seperator) {
		sb.append((formatted ? "**• " + title + seperator + "** " : "• " + title + seperator) + s + "\n");
		return this;
	}
	public String build() {
		return sb.toString();
	}
}
