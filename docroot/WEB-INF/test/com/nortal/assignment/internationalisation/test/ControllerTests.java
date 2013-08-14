package com.nortal.assignment.internationalisation.test;

import static org.junit.Assert.assertEquals;

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

	private String locale = "RU";

	private Language language = new Language("Russian", locale);

	private Translation translation = new Translation("ET", "edit", "Muuda");

	private List<Translation> oldTranslations;

	private List<Translation> newTranslations;

	private TranslationsForm form;

	@Before
	public void setUp() {
		Translation t1 = new Translation("EN", "delete", "Delete");
		Translation t2 = new Translation("EN", "salvesta", "Salvesta");
		Translation newT1 = new Translation("EN", "delete", "Remove");
		oldTranslations = new ArrayList<Translation>(Arrays.asList(t1, t2));
		newTranslations = new ArrayList<Translation>(Arrays.asList(newT1, t2));
		form = new TranslationsForm();
		form.setTranslations(newTranslations);
	}

	@Test
	public void testHandleRenderRequest() {
		String viewName = controller.handleRenderRequest(request, response,
				model);
		assertEquals("defaultRender", viewName);
	}

	@Test
	public void testAddTranslationMethod() {
		controller.addTranslationMethod(actionRequest, actionResponse,
				translation, result, new Language(), model);
		Mockito.verify(translationDAO).insert(translation);
		Mockito.verify(model).addAttribute("success",
				"Translation successfully added!");
	}

	@Test
	public void testAddTranslationValidationErrors() {
		Mockito.when(result.hasErrors()).thenReturn(true);
		controller.addTranslationMethod(actionRequest, actionResponse,
				translation, result, new Language(), model);
		Mockito.verify(model, Mockito.never()).addAttribute("success");
	}

	@Test
	public void testAddTranslationDuplicateKey() {
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO)
				.insert(translation);

		controller.addTranslationMethod(actionRequest, actionResponse,
				translation, result, new Language(), model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testShowTranslationsMethodNoLanguageSelected() {
		Mockito.doThrow(new IndexOutOfBoundsException("")).when(languageDAO)
				.getLanguage(locale);
		String viewName = controller.showTranslationsMethod(request, response,
				model, language, result);
		assertEquals("defaultRender", viewName);
		Mockito.verify(result).reject("wrong.language");
	}

	@Test
	public void testAddLanguageMethod() {
		controller.addLanguageMethod(actionRequest, actionResponse, language,
				result, model);
		Mockito.verify(languageDAO).addLanguage(language);
		Mockito.verify(model).addAttribute("success",
				"Language successfully added!");
	}

	@Test
	public void testAddLanguageMethodDuplicateKey() {
		Mockito.doThrow(new DuplicateKeyException("")).when(languageDAO)
				.addLanguage(language);
		controller.addLanguageMethod(actionRequest, actionResponse, language,
				result, model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testAddLanguageMethodValidationErrors() {
		Mockito.when(result.hasErrors()).thenReturn(true);
		controller.addLanguageMethod(actionRequest, actionResponse, language,
				result, model);
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
		language.setId(id);
		controller
				.deleteLanguageMethod(actionRequest, actionResponse, language);
		Mockito.verify(languageDAO).deleteLanguage(id);
	}

	@Test
	public void testEditLanguageMethod() {
		controller.editLanguageMethod(actionRequest, actionResponse, language,
				result, language, model);
		Mockito.verify(languageDAO).editLanguage(language);
		Mockito.verify(model).addAttribute("success",
				"Language successfully updated!");
	}

	@Test
	public void testEditLanguageMethodDuplicateLocale() {
		Mockito.doThrow(new DuplicateKeyException("")).when(languageDAO)
				.editLanguage(language);
		controller.editLanguageMethod(actionRequest, actionResponse, language,
				result, language, model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testEditLanguageMethodValidationErrors() {
		Mockito.when(result.hasErrors()).thenReturn(true);
		controller.editLanguageMethod(actionRequest, actionResponse, language,
				result, language, model);
		Mockito.verify(model, Mockito.never()).addAttribute("success",
				"Language successfully added!");
	}

	@Test
	public void testUpdateTranslationsMethod() throws ParseException {
		Mockito.when(translationDAO.getTranslations(locale)).thenReturn(
				oldTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse,
				form, result, language, model);
		Mockito.verify(translationDAO)
				.updateTranslation(newTranslations.get(0));
	}

	@Test
	public void testUpdateTranslationsKeyAndValueMethodDuplicateKey()
			throws ParseException {
		Mockito.when(translationDAO.getTranslations(locale)).thenReturn(
				oldTranslations);
		Mockito.doThrow(new DuplicateKeyException("")).when(translationDAO)
				.updateTranslation(newTranslations.get(0));
		controller.updateTranslationsMethod(actionRequest, actionResponse,
				form, result, language, model);
		Mockito.verify(result).reject("duplicate.key");
	}

	@Test
	public void testUpdateTranslationsMethodValidationErrors()
			throws ParseException {
		Translation newT1 = new Translation("EN", "delete", "");
		form.getTranslations().set(0, newT1);
		Mockito.when(translationDAO.getTranslations(locale)).thenReturn(
				oldTranslations);
		controller.updateTranslationsMethod(actionRequest, actionResponse,
				form, result, language, model);
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
