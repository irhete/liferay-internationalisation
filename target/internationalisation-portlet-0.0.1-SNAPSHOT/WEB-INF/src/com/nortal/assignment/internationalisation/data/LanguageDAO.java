package com.nortal.assignment.internationalisation.data;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import com.nortal.assignment.internationalisation.model.Language;

public interface LanguageDAO {
	/**
	 * Inserts language to database
	 * 
	 * @param language
	 *            . Display name and locale must not be null.
	 * @throws DuplicateKeyException
	 */
	public void addLanguage(Language language);

	/**
	 * Retrieves currently available languages.
	 * 
	 * @return List of languages
	 */
	public List<Language> getLanguages();

	/**
	 * Updates a language.
	 * 
	 * @param language
	 *            . Display name and locale must not be null.
	 * @throws DuplicateKeyException
	 */
	public void editLanguage(Language language);

	/**
	 * Deletes a language from the database.
	 * 
	 * @param locale
	 *            . The locale of the language to be deleted.
	 */
	public void deleteLanguage(int id);

	/**
	 * Retrieves a language by its locale.
	 * 
	 * @param locale
	 *            . The locale of the language.
	 * 
	 * @return Language.
	 */
	public Language getLanguage(String locale);

	public Language getLanguage(int id);
}
