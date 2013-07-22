package com.nortal.assignment.internationalisation.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nortal.assignment.internationalisation.model.Language;
import com.nortal.assignment.internationalisation.model.Translation;

@Repository
public class TranslationDAOImpl implements TranslationDAO {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void insert(final Translation translation)
			throws DuplicateKeyException {
		final String sql = "INSERT INTO translation "
				+ "(language, key, value) VALUES ((SELECT id FROM language WHERE locale = ?), ?, ?)";
		jdbcTemplate.update(sql, translation.getLanguage(),
				translation.getKey(), translation.getValue());
	}

	@Override
	public List<Translation> getTranslations(final String locale) {
		String sql = "SELECT t.key, t.value, l.locale FROM translation t JOIN language l ON l.id = t.language WHERE l.locale = ?";
		List<Translation> translations = jdbcTemplate.query(sql,
				new TranslationRowMapper(), locale);
		return translations;
	}

	private static class TranslationRowMapper implements RowMapper<Translation> {

		@Override
		public Translation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Translation translation = new Translation();
			translation.setLanguage(rs.getString("locale"));
			translation.setKey(rs.getString("key"));
			translation.setValue(rs.getString("value"));
			return translation;
		}
	}

	private static class LanguageRowMapper implements RowMapper<Language> {

		@Override
		public Language mapRow(ResultSet rs, int rowNum) throws SQLException {
			Language language = new Language();
			language.setLocale(rs.getString("locale"));
			language.setDisplayLanguage(rs.getString("name"));
			language.setId(rs.getInt("id"));
			return language;
		}
	}

	@Override
	public List<Language> getLanguages() {
		String sql = "SELECT * FROM language";
		List<Language> languages = jdbcTemplate.query(sql,
				new LanguageRowMapper());
		return languages;
	}

	@Override
	public void addLanguage(Language language) throws DuplicateKeyException {
		final String sql = "INSERT INTO language (name, locale) VALUES (?,?)";
		jdbcTemplate.update(sql, language.getDisplayLanguage(),
				language.getLocale());
	}

	@Override
	public void updateTranslationValue(Translation translation) {
		final String sql = "UPDATE translation SET value=? WHERE key=? AND language=(SELECT id FROM language WHERE locale=?)";
		jdbcTemplate.update(sql, translation.getValue(), translation.getKey(),
				translation.getLanguage());
	}

	@Override
	public void updateTranslationKeyAndValue(String oldKey,
			Translation translation) throws DuplicateKeyException {
		insert(translation);
		final String sql = "DELETE FROM translation WHERE key=? AND language=(SELECT id FROM language WHERE locale=?)";
		jdbcTemplate.update(sql, oldKey, translation.getLanguage());
	}

	@Override
	public void deleteTranslation(Translation translation) {
		System.out.println(translation.getKey());
		System.out.println(translation.getLanguage());
		final String sql = "DELETE FROM translation WHERE key=? AND language=(SELECT id FROM language WHERE locale=?)";
		jdbcTemplate.update(sql, translation.getKey(),
				translation.getLanguage());
	}

	@Override
	public void editLanguage(Language language) throws DuplicateKeyException {
		final String sql = "UPDATE language SET name = ?, locale = ? WHERE id = ?";
		jdbcTemplate.update(sql, language.getDisplayLanguage(),
				language.getLocale(), language.getId());
	}

	@Override
	public void deleteLanguage(Language language) {
		final String sql = "DELETE FROM language WHERE locale=?";
		jdbcTemplate.update(sql, language.getLocale());
	}

	@Override
	public Language getLanguage(final String locale) {
		String sql = "SELECT * FROM language WHERE locale = ?";
		List<Language> languages = jdbcTemplate.query(sql,
				new LanguageRowMapper(), locale);
		return languages.get(0);
	}

	@Override
	public Language getLanguage(final int id) {
		String sql = "SELECT * FROM language WHERE id = ?";
		List<Language> languages = jdbcTemplate.query(sql,
				new LanguageRowMapper(), id);
		return languages.get(0);
	}

}
