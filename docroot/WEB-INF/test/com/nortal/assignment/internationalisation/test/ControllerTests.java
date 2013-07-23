package com.nortal.assignment.internationalisation.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.nortal.assignment.internationalisation.controller.InternationalisationController;
import com.nortal.assignment.internationalisation.data.TranslationDAO;
import com.nortal.assignment.internationalisation.messages.VerticalDatabaseMessageSource;
import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.model.Translation;
import com.nortal.assignment.internationalisation.validator.LanguageValidator;
import com.nortal.assignment.internationalisation.validator.TranslationValidator;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTests {
	
	@Mock
	private RenderRequest request;
	@Mock
	private RenderResponse response;
	@Mock
	private Model model;
	@Mock
	private ActionRequest actionRequest;
	@Mock
	private ActionResponse actionResponse;
	@Mock
	private TranslationDAO translationDAO;
	@Mock  
    private VerticalDatabaseMessageSource messageSource;
	@InjectMocks
	private InternationalisationController controller;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(messageSource).init();
	}
	
	@Test
	public void testHandleRenderRequest() throws IOException {
		String viewName = controller.handleRenderRequest(request, response, model);
		assertEquals("defaultRender", viewName);
	}

	@Test
	public void testAddTranslationMethod() throws ParseException {
		String key = "edit";
		String value = "Muuda";
		String locale = "ET";
		Translation translation = new Translation(locale, key, value);
		controller.addTranslationMethod(actionRequest, actionResponse, key, value, locale);
		Mockito.verify(translationDAO).insert(translation);
		Mockito.verify(actionRequest).setAttribute("success",
				"Translation successfully added!");
	}
	
	@Test
	public void testAddTranslationKeyNull() throws ParseException {
		String key = "";
		String value = "Muuda";
		String locale = "ET";
		Translation translation = new Translation(locale, key, value);
		controller.addTranslationMethod(actionRequest, actionResponse, key, value, locale);
		TranslationValidator validator = new TranslationValidator();
		Errors errors = new BeanPropertyBindingResult(translation,
				"translation");
		validator.validate(translation, errors);
		Mockito.verify(actionRequest).setAttribute("errors", errors.getAllErrors());
	}
	
	@Test
	public void testAddTranslationDuplicateKey() throws ParseException {
		String key = "edit";
		String value = "Muuda";
		String locale = "ET";
		Translation translation = new Translation(locale, key, value);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO).insert(translation);
		
		controller.addTranslationMethod(actionRequest, actionResponse, key, value, locale);
		Mockito.verify(actionRequest).setAttribute("error", "Translation for key '" + key
				+ "' and locale '" + locale + "' already exists.");
	}

	@Test
	public void testShowTranslationsMethodAfterSuccessfulInsertion() {
		String successMessage = "Translation successfully added!";
		Mockito.when(request.getAttribute("success")).thenReturn(successMessage);
		String viewName = controller.showTranslationsMethod(request, response, model, "EN");
		assertEquals("viewAndAddTranslationsForLanguage", viewName);
		Mockito.verify(model).addAttribute("success", successMessage);
	}
	
	@Test
	public void testShowTranslationsMethodNoLanguageSelected() {
		String locale = "language";
		Mockito.doThrow(new IndexOutOfBoundsException("")).when(translationDAO).getLanguage(locale);
		String viewName = controller.showTranslationsMethod(request, response, model, locale);
		assertEquals("defaultRender", viewName);
		Mockito.verify(model).addAttribute("error", "Choose language");
	}
	
	@Test
	public void testShowTranslationsMethodValidationErrors() {
		List<ObjectError> errors = new ArrayList<ObjectError>();
		Mockito.when(request.getAttribute("errors")).thenReturn(errors);
		String viewName = controller.showTranslationsMethod(request, response, model, "EN");
		assertEquals("viewAndAddTranslationsForLanguage", viewName);
		Mockito.verify(model).addAttribute("errors", errors);
	}
	
	@Test
	public void testShowTranslationsMethodDuplicateKeyError() {
		String errorMessage = "Key not unique!";
		Mockito.when(request.getAttribute("error")).thenReturn(errorMessage);
		String viewName = controller.showTranslationsMethod(request, response, model, "EN");
		assertEquals("viewAndAddTranslationsForLanguage", viewName);
		Mockito.verify(model).addAttribute("error", errorMessage);
	}

	@Test
	public void testAddLanguageMethod() {
		String language = "Russian";
		String locale = "RU";
		Language newLanguage = new Language(language, locale);
		controller.addLanguageMethod(actionRequest, actionResponse, language, locale);
		Mockito.verify(translationDAO).addLanguage(newLanguage);
		Mockito.verify(actionRequest).setAttribute("success", "Language successfully added!");
	}
	
	@Test
	public void testAddLanguageMethodDuplicateKey() throws ParseException {
		String language = "Russian";
		String locale = "RU";
		Language newLanguage = new Language(language, locale);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO).addLanguage(newLanguage);
		controller.addLanguageMethod(actionRequest, actionResponse, language, locale);
		Mockito.verify(actionRequest).setAttribute("error", "Language with locale '" + locale + "' already exists.");
	}
	
	@Test
	public void testAddLanguageMethodLocaleNull() throws ParseException {
		String language = "Russian";
		String locale = "";
		Language newLanguage = new Language(language, locale);
		LanguageValidator validator = new LanguageValidator();
		Errors errors = new BeanPropertyBindingResult(newLanguage,
				"newLanguage");
		validator.validate(newLanguage, errors);
		controller.addLanguageMethod(actionRequest, actionResponse, language, locale);
		Mockito.verify(actionRequest).setAttribute("errors", errors.getAllErrors());
		}

	
	@Test
	public void testRenderManageLanguagesMethod() {
		String viewName = controller.renderManageLanguagesMethod(request, response, model);
		assertEquals("manageLanguages", viewName);
	}

	@Test
	public void testRenderEditLanguageMethodAfterSuccessfulUpdate() {
		String successMessage = "Language successfully updated!";
		Mockito.when(request.getAttribute("success")).thenReturn(successMessage);
		String viewName = controller.renderEditLanguageMethod(request, response, model, "ET");
		assertEquals("editLanguage", viewName);
		Mockito.verify(model).addAttribute("success", successMessage);
	}
	
	@Test
	public void testRenderEditLanguageMethodNoLanguageSelected() {
		String locale = "language";
		Mockito.doThrow(new IndexOutOfBoundsException("")).when(translationDAO).getLanguage(locale);
		String viewName = controller.renderEditLanguageMethod(request, response, model, locale);
		assertEquals("manageLanguages", viewName);
		Mockito.verify(model).addAttribute("error", "Choose language");
	}
	
	@Test
	public void testRenderEditLanguageMethodOnUpdateLanguageNull() {
		String locale = "RU";
		List<ObjectError> errors = new ArrayList<ObjectError>();
		Mockito.when(request.getAttribute("errors")).thenReturn(errors);
		String viewName = controller.renderEditLanguageMethod(request, response, model, locale);
		assertEquals("editLanguage", viewName);
		Mockito.verify(model).addAttribute("errors", errors);
	}

	@Test
	public void testDeleteLanguageMethod() {
		String locale = "FR";
		controller.deleteLanguageMethod(actionRequest, actionResponse, locale);
		Mockito.verify(translationDAO).deleteLanguage(locale);
	}

	@Test
	public void testEditLanguageMethod() {
		String oldLanguage = "FR";
		String newLanguage = "Russian";
		String newLocale = "RU";
		Language language = new Language(newLanguage, newLocale);
		controller.editLanguageMethod(actionRequest, actionResponse, oldLanguage, newLanguage, newLocale);
		Mockito.verify(translationDAO).editLanguage(oldLanguage, language);
		Mockito.verify(actionRequest).setAttribute("success", "Language successfully updated!");
	}
	
	@Test
	public void testEditLanguageMethodDuplicateLocale() {
		String oldLanguage = "FR";
		String newLanguage = "Russian";
		String newLocale = "RU";
		Language language = new Language(newLanguage, newLocale);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO).editLanguage(oldLanguage, language);
		controller.editLanguageMethod(actionRequest, actionResponse, oldLanguage, newLanguage, newLocale);
		Mockito.verify(actionRequest).setAttribute("error", "Language with locale '" + newLocale + "' already exists.");
	}
	
	@Test
	public void testEditLanguageMethodLocaleNull() {
		String oldLanguage = "FR";
		String newLanguage = "Russian";
		String newLocale = "";
		Language language = new Language(newLanguage, newLocale);
		LanguageValidator validator = new LanguageValidator();
		Errors errors = new BeanPropertyBindingResult(language,
				"language");
		validator.validate(newLanguage, errors);
		controller.editLanguageMethod(actionRequest, actionResponse, oldLanguage, newLanguage, newLocale);
		Mockito.verify(actionRequest).setAttribute("errors", errors.getAllErrors());
	}

	@Test
	public void testUpdateTranslationsOnlyValueMethod() throws ParseException {
		String currentLocale = "EN";
		String[] keys = {"delete", "save"};
		String[] values = {"Remove", "Save"};
		Translation t1 = new Translation("EN", "delete", "Delete");
		Translation t2 = new Translation("EN", "salvesta", "Salvesta");
		Translation newT1 = new Translation("EN", "delete", "Remove");
		List<Translation> oldTranslations = new ArrayList<Translation>(Arrays.asList(t1, t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(oldTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse, currentLocale, keys, values);
		Mockito.verify(translationDAO).updateTranslation(newT1);
	}
	
	@Test
	public void testUpdateTranslationsKeyAndValueMethod() throws ParseException {
		String currentLocale = "EN";
		String[] keys = {"delete", "save"};
		String[] values = {"Remove", "Save"};
		Translation t1 = new Translation("EN", "delete", "Delete");
		Translation t2 = new Translation("EN", "salvesta", "Salvesta");
		Translation newT2 = new Translation("EN", "save", "Save");
		List<Translation> oldTranslations = new ArrayList<Translation>(Arrays.asList(t1, t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(oldTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse, currentLocale, keys, values);
		Mockito.verify(translationDAO).updateTranslationKeyAndValue("salvesta", newT2);
	}
	
	@Test
	public void testUpdateTranslationsKeyAndValueMethodDuplicateKey() throws ParseException {
		String oldKey = "salvesta";
		String newKey = "save";
		String oldValue = "Salvesta";
		String newValue = "Save";
		String currentLocale = "EN";
		String[] keys = {"delete", newKey};
		String[] values = {"Remove", newValue};
		Translation t1 = new Translation(currentLocale, "delete", "Delete");
		Translation t2 = new Translation(currentLocale, oldKey, oldValue);
		Translation newT2 = new Translation(currentLocale, newKey, newValue);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO).updateTranslationKeyAndValue(oldKey, newT2);
		List<Translation> oldTranslations = new ArrayList<Translation>(Arrays.asList(t1, t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(oldTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse, currentLocale, keys, values);
		Mockito.verify(actionRequest).setAttribute("error", "Translation for key '"
				+ newKey + "' and locale '" + currentLocale
				+ "' already exists.");
	}
	
	@Test
	public void testUpdateTranslationsMethodValueNull() throws ParseException {
		String oldKey = "salvesta";
		String newKey = "save";
		String oldValue = "Salvesta";
		String newValue = "";
		String currentLocale = "EN";
		String[] keys = {newKey};
		String[] values = {newValue};
		Translation t2 = new Translation(currentLocale, oldKey, oldValue);
		List<Translation> oldTranslations = new ArrayList<Translation>(Arrays.asList(t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(oldTranslations);
		Translation newT2 = new Translation(currentLocale, newKey, newValue);
		TranslationValidator validator = new TranslationValidator();
		Errors errors = new BeanPropertyBindingResult(newT2,
				"newTranslation");
		validator.validate(newT2, errors);
		controller.updateTranslationsMethod(actionRequest, actionResponse, currentLocale, keys, values);
		Mockito.verify(actionRequest).setAttribute("errors", errors.getAllErrors());
	}

	@Test
	public void testDeleteTranslationMethod() {
		String key = "delete";
		String locale = "RU";
		controller.deleteTranslationMethod(actionRequest, actionResponse, key, locale);
		Mockito.verify(translationDAO).deleteTranslation(key, locale);
	}

}
