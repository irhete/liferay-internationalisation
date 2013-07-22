package com.nortal.assignment.internationalisation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.nortal.assignment.internationalisation.model.Translation;

public class TranslationValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return Translation.class.equals(clazz);
	}

	@Override
	public void validate(Object arg0, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "key", "Key can not be empty");
		ValidationUtils.rejectIfEmpty(e, "value", "Value can not be empty");
		ValidationUtils.rejectIfEmpty(e, "language", "Language can not be empty");
	}

}
