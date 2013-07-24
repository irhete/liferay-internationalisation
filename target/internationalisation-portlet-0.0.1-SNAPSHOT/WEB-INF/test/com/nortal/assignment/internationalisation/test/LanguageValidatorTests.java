package com.nortal.assignment.internationalisation.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.validator.LanguageValidator;

public class LanguageValidatorTests {
	private LanguageValidator validator;
	private Errors errors;
	private Language language;

	@Before
	public void setUp() {
		language = new Language();
		validator = new LanguageValidator();
		errors = new BeanPropertyBindingResult(language, "language");
	}

	@Test
	public void validateSuccessfulTest() {
		language.setLocale("EN");
		language.setName("English");

		validator.validate(language, errors);
		assertEquals(false, errors.hasErrors());
	}

	@Test
	public void validateLocaleNullTest() {
		language.setName("English");

		validator.validate(language, errors);
		assertEquals("Locale can not be empty", errors.getAllErrors().get(0)
				.getCode());
	}

	@Test
	public void validateDisplayLanguageNullTest() {
		language.setLocale("EN");

		validator.validate(language, errors);
		assertEquals("Language display name can not be empty", errors
				.getAllErrors().get(0).getCode());
	}
}
