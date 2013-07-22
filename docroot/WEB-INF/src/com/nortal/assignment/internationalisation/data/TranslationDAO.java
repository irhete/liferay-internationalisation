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
	 */
	public void updateTranslationValue(Translation translation);

	/**
	 * Updates a translation's key and value by its old key.
	 * 
	 * @param oldKey
	 *            . The old key to retrieve the translation to be updated.
	 * @param translation
	 *            . The updated translation's key and value must not be null.
	 * @throws DuplicateKeyException
	 */
	public void updateTranslationKeyAndValue(String oldKey,
			Translation translation) throws DuplicateKeyException;

	/**
	 * Deletes a translation from the database.
	 * 
	 * @param key
	 *            . The key of the translation to be deleted.
	 * @param locale
	 *            . The locale of the translation to be deleted.
	 */
	public void deleteTranslation(Translation translation);

	/**
	 * Updates a language.
	 * 
	 * @param oldLanguage
	 *            . The locale of the language to be updated.
	 * @param language
	 *            . Display name and locale must not be null.
	 * @throws DuplicateKeyException
	 */
	public void editLanguage(String oldLanguage, Language language);

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
}
