package com.nortal.assignment.internationalisation.model;

public class Language {

	private String name;
	private String locale;
	private int id;

	public Language(String name, String locale) {
		setName(name);
		setLocale(locale);
	}

	public Language() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Language)) {
			return false;
		}
		Language l1 = (Language) other;
		return l1.name.equals(name) && l1.locale.equals(locale) && l1.id == id;
	}
}
