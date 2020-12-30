package net.celestialgaze.GuraBot.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class DocBuilder {
	Document document;
	List<DocBuilder> children = new ArrayList<DocBuilder>();
	public DocBuilder(Document document) {
		this.document = document;
	}
	/**
	 * @param name Name of the document to get
	 * @return The document in question, which is created if it didn't already exist
	 */
	public SubDocBuilder getSubDoc(String name) {
		if (!(document.get(name) instanceof Document)) {
			document.put(name, new Document());
		}
		return new SubDocBuilder(name, (Document) document.get(name), this);
	}
	public DocBuilder put(String key, Object value) {
		document.put(key, value);
		return this;
	}
	public DocBuilder remove(String key) {
		document.remove(key);
		return this;
	}
	public boolean has(String key) {
		return document.containsKey(key);
	}
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		if (document.get(key) == null) return defaultValue;
		return (T) document.get(key);
	}
	public Document build() {
		return document;
	}
}
