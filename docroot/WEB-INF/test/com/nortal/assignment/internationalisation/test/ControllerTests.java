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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.nortal.assignment.internationalisation.controller.InternationalisationController;
import com.nortal.assignment.internationalisation.data.LanguageDAO;
import com.nortal.assignment.internationalisation.form.TranslationsForm;
import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.messagesource.VerticalDatabaseMessageSource;
import com.nortal.assignment.messagesource.data.TranslationDAO;
import com.nortal.assignment.messagesource.model.Translation;

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
	private BindingResult result;
	@Mock
	private TranslationDAO translationDAO;
	@Mock
	private LanguageDAO languageDAO;
	@Mock
	private VerticalDatabaseMessageSource messageSource;
	@InjectMocks
	private InternationalisationController controller;

	@Test
	public void testHandleRenderRequest() throws IOException {
		String viewName = controller.handleRenderRequest(request, response,
				model);
		assertEquals("defaultRender", viewName);
	}

	@Test
	public void testAddTranslationMethod() throws ParseException {
		String key = "edit";
		String value = "Muuda";
		String locale = "ET";
		Translation translation = new Translation(locale, key, value);
		controller.addTranslationMethod(actionRequest, actionResponse,
				translation, result, new Language(), model);
		Mockito.verify(translationDAO).insert(translation);
		Mockito.verify(model).addAttribute("success",
				"Translation successfully added!");
	}

	@Test
	public void testAddTranslationValidationErrors() throws ParseException {
		String key = "";
		String value = "Muuda";
		String locale = "ET";
		Translation translation = new Translation(locale, key, value);
		Mockito.when(result.hasErrors()).thenReturn(true);
		controller.addTranslationMethod(actionRequest, actionResponse,
				translation, result, new Language(), model);
		Mockito.verify(model, Mockito.never()).addAttribute("success");
	}

	@Test
	public void testAddTranslationDuplicateKey() throws ParseException {
		String key = "edit";
		String value = "Muuda";
		String locale = "ET";
		Translation translation = new Translation(locale, key, value);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO)
				.insert(translation);

		controller.addTranslationMethod(actionRequest, actionResponse,
				translation, result, new Language(), model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testShowTranslationsMethodNoLanguageSelected() {
		String locale = "language";
		Language language = new Language();
		language.setLocale(locale);
		Mockito.doThrow(new IndexOutOfBoundsException("")).when(languageDAO)
				.getLanguage(locale);
		String viewName = controller.showTranslationsMethod(request, response,
				model, language, result);
		assertEquals("defaultRender", viewName);
		Mockito.verify(result).reject("wrong.language");
	}

	@Test
	public void testAddLanguageMethod() {
		String language = "Russian";
		String locale = "RU";
		Language newLanguage = new Language(language, locale);
		controller.addLanguageMethod(actionRequest, actionResponse,
				newLanguage, result, model);
		Mockito.verify(languageDAO).addLanguage(newLanguage);
		Mockito.verify(model).addAttribute("success",
				"Language successfully added!");
	}

	@Test
	public void testAddLanguageMethodDuplicateKey() throws ParseException {
		String language = "Russian";
		String locale = "RU";
		Language newLanguage = new Language(language, locale);
		Mockito.doThrow(new DuplicateKeyException("")).when(languageDAO)
				.addLanguage(newLanguage);
		controller.addLanguageMethod(actionRequest, actionResponse,
				newLanguage, result, model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testAddLanguageMethodValidationErrors() throws ParseException {
		String language = "Russian";
		String locale = "";
		Language newLanguage = new Language(language, locale);
		Mockito.when(result.hasErrors()).thenReturn(true);
		controller.addLanguageMethod(actionRequest, actionResponse,
				newLanguage, result, model);
		Mockito.verify(model, Mockito.never()).addAttribute("success",
				"Language successfully added!");
	}

	@Test
	public void testRenderManageLanguagesMethod() {
		String viewName = controller.renderManageLanguagesMethod(request,
				response, model);
		assertEquals("manageLanguages", viewName);
	}

	@Test
	public void testRenderEditLanguageMethodNoLanguageSelected() {
		String locale = "language";
		Language language = new Language();
		language.setLocale(locale);
		Mockito.doThrow(new IndexOutOfBoundsException("")).when(languageDAO)
				.getLanguage(locale);
		String viewName = controller.renderEditLanguageMethod(request,
				response, model, language, result);
		assertEquals("manageLanguages", viewName);
		Mockito.verify(result).reject("wrong.language");
	}

	@Test
	public void testDeleteLanguageMethod() {
		int id = 1;
		Language language = new Language();
		language.setId(id);
		controller
				.deleteLanguageMethod(actionRequest, actionResponse, language);
		Mockito.verify(languageDAO).deleteLanguage(id);
	}

	@Test
	public void testEditLanguageMethod() {
		String oldLanguage = "FR";
		String newLanguage = "Russian";
		String newLocale = "RU";
		Language language = new Language(newLanguage, newLocale);
		controller.editLanguageMethod(actionRequest, actionResponse, language,
				result, language, model);
		Mockito.verify(languageDAO).editLanguage(language);
		Mockito.verify(model).addAttribute("success",
				"Language successfully updated!");
	}

	@Test
	public void testEditLanguageMethodDuplicateLocale() {
		String oldLanguage = "FR";
		String newLanguage = "Russian";
		String newLocale = "RU";
		Language language = new Language(newLanguage, newLocale);
		Mockito.doThrow(new DuplicateKeyException("")).when(languageDAO)
				.editLanguage(language);
		controller.editLanguageMethod(actionRequest, actionResponse, language,
				result, language, model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testEditLanguageMethodValidationErrors() {
		String oldLanguage = "FR";
		String newLanguage = "Russian";
		String newLocale = "";
		Language language = new Language(newLanguage, newLocale);
		Mockito.when(result.hasErrors()).thenReturn(true);
		controller.editLanguageMethod(actionRequest, actionResponse, language,
				result, language, model);
		Mockito.verify(model, Mockito.never()).addAttribute("success",
				"Language successfully added!");
	}

	@Test
	public void testUpdateTranslationsMethod() throws ParseException {
		String currentLocale = "EN";
		Translation t1 = new Translation("EN", "delete", "Delete");
		Translation t2 = new Translation("EN", "salvesta", "Salvesta");
		Translation newT1 = new Translation("EN", "delete", "Remove");
		List<Translation> oldTranslations = new ArrayList<Translation>(
				Arrays.asList(t1, t2));
		List<Translation> newTranslations = new ArrayList<Translation>(
				Arrays.asList(newT1, t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(
				oldTranslations);
		TranslationsForm form = new TranslationsForm();
		Language selectedLanguage = new Language("Eesti", "et_EE");
		form.setTranslations(newTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse,
				form, result, selectedLanguage, model);
		Mockito.verify(translationDAO).updateTranslation(newT1);
	}

	@Test
	public void testUpdateTranslationsKeyAndValueMethodDuplicateKey()
			throws ParseException {
		String currentLocale = "EN";
		Translation t1 = new Translation("EN", "delete", "Delete");
		Translation t2 = new Translation("EN", "salvesta", "Salvesta");
		Translation newT1 = new Translation("EN", "delete", "Remove");
		List<Translation> oldTranslations = new ArrayList<Translation>(
				Arrays.asList(t1, t2));
		List<Translation> newTranslations = new ArrayList<Translation>(
				Arrays.asList(newT1, t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(
				oldTranslations);
		TranslationsForm form = new TranslationsForm();
		Language selectedLanguage = new Language("Eesti", "et_EE");
		form.setTranslations(newTranslations);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO)
				.updateTranslation(newT1);
		controller.updateTranslationsMethod(actionRequest, actionResponse,
				form, result, selectedLanguage, model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testUpdateTranslationsMethodValidationErrors()
			throws ParseException {
		String currentLocale = "EN";
		String language = "English";
		Language selectedLanguage = new Language(language, currentLocale);
		Translation t1 = new Translation("EN", "delete", "Delete");
		Translation t2 = new Translation("EN", "salvesta", "Salvesta");
		Translation newT1 = new Translation("EN", "delete", "");
		List<Translation> oldTranslations = new ArrayList<Translation>(
				Arrays.asList(t1, t2));
		List<Translation> newTranslations = new ArrayList<Translation>(
				Arrays.asList(newT1, t2));
		Mockito.when(translationDAO.getTranslations(currentLocale)).thenReturn(
				oldTranslations);
		TranslationsForm form = new TranslationsForm();
		form.setTranslations(newTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse,
				form, result, selectedLanguage, model);
		Mockito.verify(translationDAO, Mockito.never())
				.updateTranslation(newT1);
	}

	@Test
	public void testDeleteTranslationMethod() {
		int id = 1;
		controller.deleteTranslationMethod(actionRequest, actionResponse, id,
				model);
		Mockito.verify(translationDAO).deleteTranslation(id);
	}

}
