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

public class LanguageDAOtests {

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
		languageDAO = new LanguageDAOImpl();
		languageDAO.setJdbcTemplate(jdbcTemplate);
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
	public void testEditLanguage() {
		Language language = new Language("Francais", "FR");
		language.setId(3);
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
