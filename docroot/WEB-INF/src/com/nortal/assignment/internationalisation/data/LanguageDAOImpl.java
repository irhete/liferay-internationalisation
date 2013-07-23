package com.nortal.assignment.internationalisation.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.nortal.assignment.internationalisation.model.Language;

@Repository
public class LanguageDAOImpl implements LanguageDAO {

	@Resource(name = "dataSource")
	private DriverManagerDataSource dataSource;

	private SimpleJdbcTemplate jdbcTemplate;

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void init() {
		jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	@Override
	public List<Language> getLanguages() {
		String sql = "SELECT * FROM language";
		List<Language> languages = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Language>(Language.class));
		return languages;
	}

	@Override
	public void addLanguage(Language language) throws DuplicateKeyException {
		final String sql = "INSERT INTO language (name, locale) VALUES (:name, :locale)";
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(
				language);
		jdbcTemplate.update(sql, parameters);
	}

	@Override
	public void editLanguage(Language language) throws DuplicateKeyException {
		final String sql = "UPDATE language SET name = :name, locale = :locale WHERE id = :id";
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(
				language);
		jdbcTemplate.update(sql, parameters);
	}

	@Override
	public void deleteLanguage(int id) {
		final String sql = "DELETE FROM language WHERE id = :id";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public Language getLanguage(final String locale) {
		String sql = "SELECT * FROM language WHERE locale = :locale";
		List<Language> languages = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Language>(Language.class), locale);
		return languages.get(0);
	}

	@Override
	public Language getLanguage(final int id) {
		String sql = "SELECT * FROM language WHERE id = :id";
		List<Language> languages = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Language>(Language.class), id);
		return languages.get(0);
	}
}
