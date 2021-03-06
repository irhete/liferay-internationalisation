package com.nortal.assignment.internationalisation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.nortal.assignment.internationalisation.model.Language;

public class LanguageValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return Language.class.equals(clazz);
	}

	@Override
	public void validate(Object language, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "locale", "empty",
				"Locale can not be empty");
		ValidationUtils.rejectIfEmpty(e, "name", "empty",
				"Language display name can not be empty");
	}

}
