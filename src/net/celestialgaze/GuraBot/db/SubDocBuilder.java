package net.celestialgaze.GuraBot.db;

import org.bson.Document;

public class SubDocBuilder extends DocBuilder {
	private DocBuilder parent;
	private String parentDocName;
	public SubDocBuilder(String parentDocName, Document document, DocBuilder parent) {
		super(document);
		this.parent = parent;
		this.parentDocName = parentDocName;
	}
	@Override
	public SubDocBuilder put(String key, Object value) {
		return (SubDocBuilder) super.put(key, value);
	}
	@Override
	public SubDocBuilder remove(String key) {
		return (SubDocBuilder) super.remove(key);
	}
	@Override
	public Document build() {
		parent.document.put(parentDocName, document);
		return parent.build();
	}
	public Document buildThis() {
		return document;
	}
}
