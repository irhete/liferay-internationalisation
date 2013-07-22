<%@ include file="common.jsp"%>

<form method="post" action="${showTranslationsMethodURL}">
	<select name="languageSelect">
		<option value="language">
			--<spring:message code="language.text"/>--
		</option>
		<c:forEach items="${languages}" var="language">
			<option value="${language.locale}">${language.displayLanguage}</option>
		</c:forEach>
	</select> <input type="submit"
		value="<spring:message code="show.translations.text"/>" />
</form>

<form method="post" action="${renderManageLanguagesMethodURL}">
	<input type="submit" value="<spring:message code="manage.languages.text"/>" />
</form>