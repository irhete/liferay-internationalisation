package com.nortal.assignment.internationalisation.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.nortal.assignment.internationalisation.validator.TranslationValidator;
import com.nortal.assignment.messagesource.model.Translation;

public class TranslationValidatorTests {
	private TranslationValidator validator;
	private Errors errors;
	private Translation translation;

	@Before
	public void setUp() {
		translation = new Translation();
		validator = new TranslationValidator();
		errors = new BeanPropertyBindingResult(translation, "translation");
	}

	@Test
	public void validateSuccessfulTest() {
		translation.setKey("key");
		translation.setLocale("EN");
		translation.setValue("value");

		validator.validate(translation, errors);
		assertEquals(false, errors.hasErrors());
	}

	@Test
	public void validateKeyNullTest() {
		translation.setLocale("EN");
		translation.setValue("value");

		validator.validate(translation, errors);
		assertEquals("empty", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void validateLanguageNullTest() {
		translation.setKey("key");
		translation.setValue("value");

		validator.validate(translation, errors);
		assertEquals("empty", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void validateValueNullTest() {
		translation.setKey("key");
		translation.setLocale("EN");

		validator.validate(translation, errors);
		assertEquals("empty", errors.getAllErrors().get(0).getCode());
	}
}
