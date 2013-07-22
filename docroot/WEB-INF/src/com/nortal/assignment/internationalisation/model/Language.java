package com.nortal.assignment.internationalisation.model;

public class Language {

	private String displayLanguage;
	private String locale;
	private int id;
	
	public Language(String displayName, String locale) {
		setDisplayLanguage(displayName);
		setLocale(locale);
	}
	
	public Language() {}

	public String getDisplayLanguage() {
		return displayLanguage;
	}

	public void setDisplayLanguage(String displayName) {
		displayLanguage = displayName;
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
		return l1.displayLanguage.equals(displayLanguage) && l1.locale.equals(locale) && l1.id == id;
	}
}
