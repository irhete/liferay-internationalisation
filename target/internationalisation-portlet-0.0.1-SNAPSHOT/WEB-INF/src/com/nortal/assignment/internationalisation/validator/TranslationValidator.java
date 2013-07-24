package com.nortal.assignment.internationalisation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.nortal.assignment.messagesource.Translation;

public class TranslationValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return Translation.class.equals(clazz);
	}

	@Override
	public void validate(Object translation, Errors e) {
		ValidationUtils
				.rejectIfEmpty(e, "key", "empty", "Key can not be empty");
		ValidationUtils.rejectIfEmpty(e, "value", "empty",
				"Value can not be empty");
	}

}
