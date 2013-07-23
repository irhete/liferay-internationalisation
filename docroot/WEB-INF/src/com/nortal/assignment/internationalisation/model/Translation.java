package com.nortal.assignment.internationalisation.model;

public class Translation {

	private String language;
	private String key;
	private String value;
	private int id;

	public Translation(String language, String key, String value) {
		setLanguage(language);
		setKey(key);
		setValue(value);
	}

	public Translation() {
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Translation)) {
			return false;
		}
		Translation t1 = (Translation) other;
		return t1.language.equals(language) && t1.key.equals(key)
				&& t1.value.equals(value);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
