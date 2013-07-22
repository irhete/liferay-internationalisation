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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.nortal.assignment.internationalisation.data.TranslationDAO;
import com.nortal.assignment.internationalisation.messages.VerticalDatabaseMessageSource;
import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.model.Translation;
import com.nortal.assignment.internationalisation.validator.LanguageValidator;
import com.nortal.assignment.internationalisation.validator.TranslationValidator;

@Controller(value = "InternationalisationController")
@RequestMapping("VIEW")
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
		return "defaultRender";
	}

	@RenderMapping(params = "action=defaultPage")
	public String renderDefaultPage(RenderRequest request,
			RenderResponse response, Model model) {
		return handleRenderRequest(request, response, model);
	}

	@ActionMapping(params = "action=addTranslation")
	public void addTranslationMethod(ActionRequest request,
			ActionResponse response, @RequestParam("newKey") String key,
			@RequestParam("newValue") String value,
			@RequestParam("locale") String locale) throws ParseException {

		Translation translation = new Translation(locale, key, value);
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
				request.setAttribute("error", "Translation for key '" + key
						+ "' and locale '" + locale + "' already exists.");
			}
		}
		response.setRenderParameter("languageSelect", locale);
		response.setRenderParameter("action", "showTranslations");
	}

	@RenderMapping(params = "action=showTranslations")
	public String showTranslationsMethod(RenderRequest request,
			RenderResponse response, Model model,
			@RequestParam("languageSelect") String locale) {
		// response.setTitle(messageSourceVertical.getMessage("internationalisation",
		// null,
		// LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request).substring(0,2))));
		if (request.getAttribute("errors") != null) {
			model.addAttribute("errors", request.getAttribute("errors"));
		} else if (request.getAttribute("error") != null) {
			model.addAttribute("error", request.getAttribute("error"));
		} else if (request.getAttribute("success") != null) {
			model.addAttribute("success", request.getAttribute("success"));
		}
		List<Translation> translations = translationDAO.getTranslations(locale);
		model.addAttribute("translations", translations);
		try {
			Language language = translationDAO.getLanguage(locale);
			model.addAttribute("currentLocale", language);
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
			@RequestParam("newLanguage") String language,
			@RequestParam("newLocale") String locale) {
		Language newLanguage = new Language(language, locale);

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
				request.setAttribute("error", "Language with locale '" + locale
						+ "' already exists.");
			}
		}
		response.setRenderParameter("languageSelect", locale);
		response.setRenderParameter("action", "renderEditLanguage");
	}

	@RenderMapping(params = "action=renderManageLanguages")
	public String renderManageLanguagesMethod(RenderRequest request,
			RenderResponse response, Model model) {
		// response.setTitle(messageSourceVertical.getMessage("internationalisation",
		// null,
		// LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request).substring(0,2))));
		List<Language> languages = translationDAO.getLanguages();
		model.addAttribute("languages", languages);
		return "manageLanguages";
	}

	@RenderMapping(params = "action=renderEditLanguage")
	public String renderEditLanguageMethod(RenderRequest request,
			RenderResponse response, Model model,
			@RequestParam("languageSelect") String locale) {
		// response.setTitle(messageSourceVertical.getMessage("internationalisation",
		// null,
		// LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request).substring(0,2))));
		if (request.getAttribute("errors") != null) {
			model.addAttribute("errors", request.getAttribute("errors"));
		} else if (request.getAttribute("success") != null) {
			model.addAttribute("success", request.getAttribute("success"));
		}
		try {
			Language language = translationDAO.getLanguage(locale);
			model.addAttribute("language", language);
		} catch (IndexOutOfBoundsException e) {
			model.addAttribute("error", "Choose language");
			return renderManageLanguagesMethod(request, response, model);
		}
		return "editLanguage";
	}

	@ActionMapping(params = "action=deleteLanguage")
	public void deleteLanguageMethod(ActionRequest request,
			ActionResponse response, @RequestParam("language") String language) {
		translationDAO.deleteLanguage(language);
		response.setRenderParameter("action", "renderManageLanguages");
	}

	@ActionMapping(params = "action=editLanguage")
	public void editLanguageMethod(ActionRequest request,
			ActionResponse response,
			@RequestParam("language") String oldLanguage,
			@RequestParam("newLanguage") String newLanguage,
			@RequestParam("newLocale") String newLocale) {
		Language language = new Language(newLanguage, newLocale);
		LanguageValidator validator = new LanguageValidator();
		Errors errors = new BeanPropertyBindingResult(language, "language");
		validator.validate(language, errors);
		if (errors.hasErrors()) {
			request.setAttribute("errors", errors.getAllErrors());
			response.setRenderParameter("languageSelect", oldLanguage);
		} else {
			try {
				translationDAO.editLanguage(oldLanguage, language);
				request.setAttribute("success",
						"Language successfully updated!");
				response.setRenderParameter("languageSelect", newLocale);
			} catch (DuplicateKeyException e) {
				request.setAttribute("error", "Language with locale '"
						+ newLocale + "' already exists.");
				response.setRenderParameter("languageSelect", oldLanguage);
			}
		}
		response.setRenderParameter("action", "renderEditLanguage");
	}

	@ActionMapping(params = "action=updateTranslations")
	public void updateTranslationsMethod(ActionRequest request,
			ActionResponse response,
			@RequestParam("currentLocale") String currentLocale,
			@RequestParam(value = "keys[]") String[] keys,
			@RequestParam(value = "values[]") String[] values)
			throws ParseException {
		List<Translation> oldTranslations = translationDAO
				.getTranslations(currentLocale);
		TranslationValidator validator = new TranslationValidator();
		for (int i = 0; i < keys.length; i++) {
			String newKey = keys[i];
			String newValue = values[i];
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
			ActionResponse response, @RequestParam("key") String key,
			@RequestParam("locale") String locale) {
		translationDAO.deleteTranslation(key, locale);
		messageSource.init();
		request.setAttribute("success", "Translation successfully deleted!");
		response.setRenderParameter("languageSelect", locale);
		response.setRenderParameter("action", "showTranslations");
	}
}
