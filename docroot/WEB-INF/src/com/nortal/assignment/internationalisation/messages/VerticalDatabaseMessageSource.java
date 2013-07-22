package com.nortal.assignment.internationalisation.messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.springframework.dao.DataAccessException;

public class VerticalDatabaseMessageSource extends DatabaseMessageSourceBase {

	private static final String I18N_QUERY = "SELECT l.locale, t.key, t.value FROM translation t JOIN language l ON l.id = t.language";

	@Override
	public String getI18NSqlQuery() {
		return I18N_QUERY;
	}

	@Override
	protected Messages extractI18NData(ResultSet rs) throws SQLException, DataAccessException {
		Messages messages = new Messages();
		while (rs.next()) {
			Locale locale = new Locale(rs.getString("locale"));
			messages.addMessage(rs.getString("key"), locale, rs.getString("value"));
		}
		return messages;
	}
}