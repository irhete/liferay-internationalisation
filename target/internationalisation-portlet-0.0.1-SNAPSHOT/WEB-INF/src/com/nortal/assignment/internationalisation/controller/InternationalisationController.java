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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.nortal.assignment.internationalisation.data.LanguageDAO;
import com.nortal.assignment.internationalisation.form.TranslationsForm;
import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.validator.LanguageValidator;
import com.nortal.assignment.internationalisation.validator.TranslationValidator;
import com.nortal.assignment.messagesource.Translation;
import com.nortal.assignment.messagesource.TranslationDAO;
import com.nortal.assignment.messagesource.VerticalDatabaseMessageSource;

@Controller(value = "InternationalisationController")
@RequestMapping("VIEW")
@SessionAttributes({ "selectedLanguage", "translationsForm" })
public class InternationalisationController {

	@Resource
	private TranslationDAO translationDAO;

	@Resource
	private LanguageDAO languageDAO;

	@Resource
	private VerticalDatabaseMessageSource messageSource;

	@ModelAttribute("languages")
	public List<Language> getLanguages() {
		return languageDAO.getLanguages();
	}

	@RenderMapping
	public String handleRenderRequest(RenderRequest request,
			RenderResponse response, Model model) {
		model.addAttribute("selectedLanguage", new Language());
		return "defaultRender";
	}

	@ActionMapping(params = "action=addTranslation")
	public void addTranslationMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("newTranslation") Translation translation,
			BindingResult result,
			@ModelAttribute("selectedLanguage") Language selectedLanguage,
			Model model) throws ParseException {
		translation.setLocale(selectedLanguage.getLocale());
		TranslationValidator validator = new TranslationValidator();
		validator.validate(translation, result);

		if (!result.hasErrors()) {
			try {
				translationDAO.insert(translation);
				messageSource.init();
				model.addAttribute("success", "Translation successfully added!");
			} catch (DuplicateKeyException e) {
				result.reject("newTranslation", "Rejected");
				System.out.println("error");
				// result.reject("newTranslation",
				// "Translation for key '" + translation.getKey()
				// + "' and locale '" + translation.getLocale()
				// + "' already exists.");
			}
		}
		response.setRenderParameter("action", "showTranslations");
	}

	@RenderMapping(params = "action=showTranslations")
	public String showTranslationsMethod(RenderRequest request,
			RenderResponse response, Model model,
			@ModelAttribute("selectedLanguage") Language selectedLanguage) {
		try {
			selectedLanguage = languageDAO.getLanguage(selectedLanguage
					.getLocale());
			model.addAttribute("selectedLanguage", selectedLanguage);
			TranslationsForm form = new TranslationsForm();
			form.setTranslations(translationDAO
					.getTranslations(selectedLanguage.getLocale()));
			model.addAttribute("translationsForm", form);
		} catch (IndexOutOfBoundsException e) {
			model.addAttribute("error", "Choose language");
			return handleRenderRequest(request, response, model);
		}
		return "viewAndAddTranslationsForLanguage";
	}

	@ActionMapping(params = "action=addLanguage")
	public void addLanguageMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("newLanguage") Language newLanguage,
			BindingResult result, Model model) {

		LanguageValidator validator = new LanguageValidator();
		validator.validate(newLanguage, result);
		if (!result.hasErrors()) {
			try {
				languageDAO.addLanguage(newLanguage);
				model.addAttribute("success", "Language successfully added!");
				response.setRenderParameter("action", "renderEditLanguage");
			} catch (DuplicateKeyException e) {
				result.reject("newLanguage", "Rejected");
				// model.addAttribute("error", "Language with locale '"
				// + newLanguage.getLocale() + "' already exists.");
				response.setRenderParameter("action", "renderManageLanguages");
			}
		} else {
			response.setRenderParameter("action", "renderManageLanguages");
		}

	}

	@RenderMapping(params = "action=renderManageLanguages")
	public String renderManageLanguagesMethod(RenderRequest request,
			RenderResponse response, Model model) {
		return "manageLanguages";
	}

	@RenderMapping(params = "action=renderEditLanguage")
	public String renderEditLanguageMethod(RenderRequest request,
			RenderResponse response, Model model,
			@ModelAttribute("selectedLanguage") Language selectedLanguage) {
		try {
			selectedLanguage = languageDAO.getLanguage(selectedLanguage
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
		languageDAO.deleteLanguage(language.getId());
		response.setRenderParameter("action", "renderManageLanguages");
	}

	// TODO: selectedLanguage changing together with updatedLanguage
	@ActionMapping(params = "action=editLanguage")
	public void editLanguageMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("updatedLanguage") Language updatedLanguage,
			BindingResult result,
			@ModelAttribute("selectedLanguage") Language selectedLanguage,
			Model model) {
		updatedLanguage.setId(selectedLanguage.getId());
		LanguageValidator validator = new LanguageValidator();
		validator.validate(updatedLanguage, result);
		if (!result.hasErrors()) {
			try {
				languageDAO.editLanguage(updatedLanguage);
				model.addAttribute("success", "Language successfully updated!");
			} catch (DuplicateKeyException e) {
				model.addAttribute("error", "Language with locale '"
						+ updatedLanguage.getLocale() + "' already exists.");
			}
		}
		response.setRenderParameter("action", "renderEditLanguage");
	}

	@ActionMapping(params = "action=updateTranslations")
	public void updateTranslationsMethod(ActionRequest request,
			ActionResponse response,
			@ModelAttribute("selectedLanguage") Language selectedLanguage,
			@ModelAttribute("translationsForm") TranslationsForm form,
			BindingResult result, Model model) throws ParseException {
		List<Translation> translations = form.getTranslations();
		String currentLocale = selectedLanguage.getLocale();
		TranslationValidator validator = new TranslationValidator();
		int i = 0;
		for (Translation translation : translations) {
			BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
					translation, "translations[" + i + "]");
			validator.validate(translation, errors);
			i++;
			if (errors.hasErrors()) {
				System.out.println(errors.getAllErrors().get(0));
				result.addError(new ObjectError("translations[" + i + "]",
						"error.text"));
			} else {
				try {
					translationDAO.updateTranslation(translation);
					model.addAttribute("success",
							"Translation successfully updated!");
				} catch (DuplicateKeyException e) {
					model.addAttribute("error", "Translation for key '"
							+ translation.getKey() + "' and locale '"
							+ currentLocale + "' already exists.");
				}
			}

		}
		messageSource.init();
		response.setRenderParameter("action", "showTranslations");
	}

	@ActionMapping(params = "action=deleteTranslation")
	public void deleteTranslationMethod(ActionRequest request,
			ActionResponse response, @RequestParam("id") int translationId,
			Model model) {
		translationDAO.deleteTranslation(translationId);
		messageSource.init();
		model.addAttribute("success", "Translation successfully deleted!");
		response.setRenderParameter("action", "showTranslations");
	}
}