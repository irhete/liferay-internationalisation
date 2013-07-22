package com.nortal.assignment.internationalisation.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.nortal.assignment.internationalisation.model.Translation;
import com.nortal.assignment.internationalisation.validator.TranslationValidator;

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
		translation.setLanguage("EN");
		translation.setValue("value");
		
		validator.validate(translation, errors);
		assertEquals(false, errors.hasErrors());
	}

	@Test
	public void validateKeyNullTest() {
		translation.setLanguage("EN");
		translation.setValue("value");
		
		validator.validate(translation, errors);
		assertEquals("Key can not be empty", errors.getAllErrors().get(0).getCode());
	}
	
	@Test
	public void validateLanguageNullTest() {
		translation.setKey("key");
		translation.setValue("value");
		
		validator.validate(translation, errors);
		assertEquals("Language can not be empty", errors.getAllErrors().get(0).getCode());
	}
	
	@Test
	public void validateValueNullTest() {
		translation.setKey("key");
		translation.setLanguage("EN");
		
		validator.validate(translation, errors);
		assertEquals("Value can not be empty", errors.getAllErrors().get(0).getCode());
	}
}
