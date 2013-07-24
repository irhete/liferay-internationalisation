package com.nortal.assignment.internationalisation.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import com.nortal.assignment.internationalisation.data.LanguageDAOImpl;
import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.messagesource.Translation;
import com.nortal.assignment.messagesource.TranslationDAOImpl;

public class TranslationDAOtests {

	TranslationDAOImpl translationDAO;
	LanguageDAOImpl languageDAO;
	private EmbeddedDatabase database;

	@Before
	public void setUp() throws SQLException {
		database = new EmbeddedDatabaseBuilder().addScript("schema.sql")
				.addScript("test-data.sql").build();
		configureDB();
	}

	@After
	public void tearDown() throws Exception {
		database.shutdown();
	}

	private void configureDB() {
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(database);
		translationDAO = new TranslationDAOImpl();
		translationDAO.setJdbcTemplate(jdbcTemplate);
		languageDAO = new LanguageDAOImpl();
		languageDAO.setJdbcTemplate(jdbcTemplate);
	}

	@Test
	public void testInsert() {
		int beforeCount = translationDAO.getTranslations("FR").size();
		Translation translation = new Translation("FR", "delete", "Supprimer");
		translationDAO.insert(translation);
		int afterCount = translationDAO.getTranslations("FR").size();
		assertEquals(1, afterCount - beforeCount);
	}

	@Test
	public void testGetTranslations() {
		List<Translation> translations = translationDAO.getTranslations("ET");
		assertEquals("Näita tõlkeid", translations.get(1).getValue());
	}

	@Test
	public void testGetLanguages() {
		List<Language> languages = languageDAO.getLanguages();
		assertEquals(3, languages.size());
	}

	@Test
	public void testAddLanguage() {
		int beforeCount = languageDAO.getLanguages().size();
		Language language = new Language("Russian", "RU");
		languageDAO.addLanguage(language);
		int afterCount = languageDAO.getLanguages().size();
		assertEquals(1, afterCount - beforeCount);
	}

	@Test
	public void testUpdateTranslationValue() {
		Translation translation = new Translation("EN", "delete", "Remove");
		translationDAO.updateTranslation(translation);
		List<Translation> translations = translationDAO.getTranslations("EN");
		assertEquals(true, translations.contains(translation));
	}

	@Test
	public void testUpdateTranslationKeyAndValue() {
		int beforeCount = translationDAO.getTranslations("EN").size();
		Translation translation = new Translation("EN", "remove", "Remove");
		translationDAO.updateTranslation(translation);
		List<Translation> translations = translationDAO.getTranslations("EN");
		int afterCount = translations.size();
		assertEquals(0, afterCount - beforeCount);
		assertEquals(true, translations.contains(translation));
	}

	@Test
	public void testDeleteTranslation() {
		int beforeCount = translationDAO.getTranslations("EN").size();
		translationDAO.deleteTranslation(1);
		int afterCount = translationDAO.getTranslations("EN").size();
		assertEquals(-1, afterCount - beforeCount);
	}

	@Test
	public void testEditLanguage() {
		Language language = new Language("Francais", "FR");
		languageDAO.editLanguage(language);
		assertEquals("Francais", languageDAO.getLanguage("FR").getName());
	}

	@Test
	public void testDeleteLanguage() {
		int beforeCount = languageDAO.getLanguages().size();
		languageDAO.deleteLanguage(1);
		int afterCount = languageDAO.getLanguages().size();
		assertEquals(-1, afterCount - beforeCount);
	}

	@Test
	public void testGetLanguage() {
		Language language = languageDAO.getLanguage("EN");
		assertEquals("English", language.getName());
	}

}
