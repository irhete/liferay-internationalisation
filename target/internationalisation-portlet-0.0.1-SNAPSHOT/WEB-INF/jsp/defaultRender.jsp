<%@ include file="common.jsp"%>

<form:errors path="selectedLanguage" cssClass="error" />
<form:form method="post" action="${showTranslationsMethodURL}"
	modelAttribute="selectedLanguage">
	<form:select path="locale">
		<option value="language">
			--
			<spring:message code="language.text" />
			--
		</option>
		<form:options items="${languages}" itemValue="locale"
			itemLabel="name" />
	</form:select>
	<input type="submit"
		value="<spring:message code="show.translations.text"/>" />

</form:form>

<form method="post" action="${renderManageLanguagesMethodURL}">
	<input type="submit"
		value="<spring:message code="manage.languages.text"/>" />
</form>

