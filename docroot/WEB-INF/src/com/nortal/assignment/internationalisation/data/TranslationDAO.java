package com.nortal.assignment.internationalisation.data;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.model.Translation;

/**
 * Services for adding and retrieving translations
 */
public interface TranslationDAO {

	/**
	 * Inserts translation to database.
	 * 
	 * @param translation
	 *            Translation.
	 * @throws DuplicateKeyException
	 */
	public void insert(Translation translation) throws DuplicateKeyException;

	/**
	 * Retrieves translations for a language.
	 * 
	 * @param language
	 *            to query the translations for String.
	 * @return List of translations
	 */
	public List<Translation> getTranslations(String locale);

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
	 * Updates a translation's value by its key.
	 * 
	 * @param translation
	 *            . Key and value must not be null. Value is the updated value.
	 * @throws DuplicateKeyException
	 */
	public void updateTranslation(Translation translation)
			throws DuplicateKeyException;;

	/**
	 * Deletes a translation from the database.
	 * 
	 * @param id
	 *            . The id of the translation to be deleted.
	 */
	public void deleteTranslation(int translationId);

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
	public void deleteLanguage(Language language);

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
