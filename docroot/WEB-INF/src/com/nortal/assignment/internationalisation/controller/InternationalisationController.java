package com.nortal.assignment.internationalisation.controller;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.nortal.assignment.internationalisation.data.TranslationDAO;
import com.nortal.assignment.internationalisation.form.TranslationsForm;
import com.nortal.assignment.internationalisation.messages.VerticalDatabaseMessageSource;
import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.model.Translation;
import com.nortal.assignment.internationalisation.validator.LanguageValidator;
import com.nortal.assignment.internationalisation.validator.TranslationValidator;

@Controller(value = "InternationalisationController")
@RequestMapping("VIEW")
@SessionAttributes("selectedLanguage")
public class InternationalisationController {

	@Resource
	private TranslationDAO translationDAO;

	@Resource
	private VerticalDatabaseMessageSource messageSource;

	@RenderMapping
	public String handleRenderRequest(RenderRequest request,
			RenderResponse response, Model model) {
		List<Language> languages = translationDAO.getLanguages();
		model.addAttribute("languages", languages);
		model.addAttribute("selectedLanguage", new Language());
		return "defaultRender";
	}

	@RenderMapping(params = "action=defaultPage")
	public String renderDefaultPage(RenderRequest request,
			RenderResponse response, Model model) {
		return handleRenderRequest(request, response, model);
	}

	@ActionMapping(params = "action=addTranslation")
	public void addTranslationMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("newTranslation") Translation translation,
			@ModelAttribute("selectedLanguage") Language selectedLanguage)
			throws ParseException {
		translation.setLanguage(selectedLanguage.getLocale());
		TranslationValidator validator = new TranslationValidator();
		Errors errors = new BeanPropertyBindingResult(translation,
				"translation");
		validator.validate(translation, errors);

		if (errors.hasErrors()) {
			request.setAttribute("errors", errors.getAllErrors());
		} else {
			try {
				translationDAO.insert(translation);
				messageSource.init();
				request.setAttribute("success",
						"Translation successfully added!");
			} catch (DuplicateKeyException e) {
				request.setAttribute("error",
						"Translation for key '" + translation.getKey()
								+ "' and locale '" + translation.getLanguage()
								+ "' already exists.");
			}
		}
		response.setRenderParameter("action", "showTranslations");
	}

	@RenderMapping(params = "action=showTranslations")
	public String showTranslationsMethod(RenderRequest request,
			RenderResponse response, Model model,
			@ModelAttribute("selectedLanguage") Language selectedLanguage) {
		TranslationsForm form = new TranslationsForm();
		String locale = selectedLanguage.getLocale();
		form.setTranslations(translationDAO.getTranslations(locale));
		model.addAttribute("translationsForm", form);

		if (request.getAttribute("errors") != null) {
			model.addAttribute("errors", request.getAttribute("errors"));
		} else if (request.getAttribute("error") != null) {
			model.addAttribute("error", request.getAttribute("error"));
		} else if (request.getAttribute("success") != null) {
			model.addAttribute("success", request.getAttribute("success"));
		}
		try {
			selectedLanguage = translationDAO.getLanguage(selectedLanguage
					.getLocale());
			model.addAttribute("selectedLanguage", selectedLanguage);
		} catch (IndexOutOfBoundsException e) {
			model.addAttribute("error", "Choose language");
			return handleRenderRequest(request, response, model);
		}

		List<Language> languages = translationDAO.getLanguages();
		model.addAttribute("languages", languages);
		return "viewAndAddTranslationsForLanguage";
	}

	@ActionMapping(params = "action=addLanguage")
	public void addLanguageMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("newLanguage") Language newLanguage) {

		LanguageValidator validator = new LanguageValidator();
		Errors errors = new BeanPropertyBindingResult(newLanguage,
				"newLanguage");
		validator.validate(newLanguage, errors);
		if (errors.hasErrors()) {
			request.setAttribute("errors", errors.getAllErrors());
		} else {
			try {
				translationDAO.addLanguage(newLanguage);
				request.setAttribute("success", "Language successfully added!");
			} catch (DuplicateKeyException e) {
				request.setAttribute("error", "Language with locale '"
						+ newLanguage.getLocale() + "' already exists.");
			}
		}
		response.setRenderParameter("action", "renderEditLanguage");
	}

	@RenderMapping(params = "action=renderManageLanguages")
	public String renderManageLanguagesMethod(RenderRequest request,
			RenderResponse response, Model model) {
		List<Language> languages = translationDAO.getLanguages();
		model.addAttribute("languages", languages);
		return "manageLanguages";
	}

	@RenderMapping(params = "action=renderEditLanguage")
	public String renderEditLanguageMethod(RenderRequest request,
			RenderResponse response, Model model,
			@ModelAttribute("selectedLanguage") Language selectedLanguage) {
		if (request.getAttribute("errors") != null) {
			model.addAttribute("errors", request.getAttribute("errors"));
		} else if (request.getAttribute("success") != null) {
			model.addAttribute("success", request.getAttribute("success"));
		}
		try {
			selectedLanguage = translationDAO.getLanguage(selectedLanguage
					.getLocale());
			model.addAttribute("selectedLanguage", selectedLanguage);
		} catch (IndexOutOfBoundsException e) {
			model.addAttribute("error", "Choose language");
			return renderManageLanguagesMethod(request, response, model);
		}
		return "editLanguage";
	}

	@ActionMapping(params = "action=deleteLanguage")
	public void deleteLanguageMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("selectedLanguage") Language language) {
		translationDAO.deleteLanguage(language);
		response.setRenderParameter("action", "renderManageLanguages");
	}

	@ActionMapping(params = "action=editLanguage")
	public void editLanguageMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("updatedLanguage") Language updatedLanguage,
			@ModelAttribute("selectedLanguage") Language selectedLanguage,
			Model model) {
		LanguageValidator validator = new LanguageValidator();
		Errors errors = new BeanPropertyBindingResult(updatedLanguage,
				"language");
		validator.validate(updatedLanguage, errors);
		if (errors.hasErrors()) {
			request.setAttribute("errors", errors.getAllErrors());
			response.setRenderParameter("languageSelect",
					selectedLanguage.getLocale());
		} else {
			try {
				translationDAO.editLanguage(selectedLanguage.getLocale(),
						updatedLanguage);
				request.setAttribute("success",
						"Language successfully updated!");
				model.addAttribute("selectedLanguage", updatedLanguage);
			} catch (DuplicateKeyException e) {
				request.setAttribute("error", "Language with locale '"
						+ updatedLanguage.getLocale() + "' already exists.");
			}
		}
		response.setRenderParameter("action", "renderEditLanguage");
	}

	@ActionMapping(params = "action=updateTranslations")
	public void updateTranslationsMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("selectedLanguage") Language selectedLanguage,
			@ModelAttribute("translationsForm") TranslationsForm form)
			throws ParseException {
		List<Translation> translations = form.getTranslations();
		String currentLocale = selectedLanguage.getLocale();
		List<Translation> oldTranslations = translationDAO
				.getTranslations(currentLocale);
		TranslationValidator validator = new TranslationValidator();
		for (int i = 0; i < translations.size(); i++) {
			String newKey = translations.get(i).getKey();
			String newValue = translations.get(i).getValue();
			Translation newTranslation = new Translation(currentLocale, newKey,
					newValue);
			Errors errors = new BeanPropertyBindingResult(newTranslation,
					"newTranslation");
			validator.validate(newTranslation, errors);

			if (errors.hasErrors()) {
				request.setAttribute("errors", errors.getAllErrors());
			} else if (oldTranslations.get(i).getKey().equals(newKey)) {
				translationDAO.updateTranslationValue(newTranslation);
				request.setAttribute("success",
						"Translation successfully updated!");
			} else {
				try {
					translationDAO.updateTranslationKeyAndValue(oldTranslations
							.get(i).getKey(), newTranslation);
					request.setAttribute("success",
							"Translation successfully updated!");
				} catch (DuplicateKeyException e) {
					request.setAttribute("error", "Translation for key '"
							+ newKey + "' and locale '" + currentLocale
							+ "' already exists.");
				}
			}

		}
		messageSource.init();
		response.setRenderParameter("languageSelect", currentLocale);
		response.setRenderParameter("action", "showTranslations");
	}

	@ActionMapping(params = "action=deleteTranslation")
	public void deleteTranslationMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("translation") Translation translation,
			@ModelAttribute("selectedLanguage") Language selectedLanguage) {
		translation.setLanguage(selectedLanguage.getLocale());
		translationDAO.deleteTranslation(translation);
		messageSource.init();
		request.setAttribute("success", "Translation successfully deleted!");
		response.setRenderParameter("action", "showTranslations");
	}
}
