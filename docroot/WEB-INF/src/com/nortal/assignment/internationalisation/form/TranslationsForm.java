package com.nortal.assignment.internationalisation.form;

import java.io.Serializable;
import java.util.List;

import com.nortal.assignment.messagesource.Translation;

public class TranslationsForm implements Serializable {

	private List<Translation> translations;

	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}

}
