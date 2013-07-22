<%@ include file="common.jsp"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:actionURL var="addTranslationMethodURL">
	<portlet:param name="action" value="addTranslation"></portlet:param>
</portlet:actionURL>
<portlet:actionURL var="updateTranslationsMethodURL">
	<portlet:param name="action" value="updateTranslations"></portlet:param>
</portlet:actionURL>
<portlet:actionURL var="deleteTranslationMethodURL">
	<portlet:param name="action" value="deleteTranslation"></portlet:param>
</portlet:actionURL>

<liferay-ui:success key="success" message="Translation successfully added!" />
<liferay-ui:error key="error" message="Sorry, an error prevented saving your greeting" />

<form method="post" action="${showTranslationsMethodURL}">
	<select name="languageSelect">
		<option value="language">
			--<spring:message code="language.text"/>--
		</option>
		<c:forEach items="${languages}" var="language">
			<option value="${language.locale}">${language.displayLanguage}</option>
		</c:forEach>
	</select> <input type="submit" value="<spring:message code="show.translations.text"/>" />
</form>



<h1><spring:message code="existing.translations.text"/> (${currentLocale.displayLanguage})</h1>
<c:choose>
	<c:when test="${not empty translations}">
		<form id="updateTranslationsForm"
			action="${updateTranslationsMethodURL}" method="post">
			<input name="currentLocale" type="hidden" value="${currentLocale.locale}" />
			<table id="translationTable">
				<tr>
					<th><spring:message code="key.text"/></th>
					<th><spring:message code="value.text"/></th>
				</tr>
				<c:forEach items="${translations}" var="translation" varStatus="i">
					<tr id="row${i.index}">
						<td><input name="keys[]" type="text"
							value="${translation.key}" /></td>
						<td><input name="values[]" type="text"
							value="${translation.value}" /></td>
						<td><a href="${deleteTranslationMethodURL}&key=${translation.key}&locale=${currentLocale.locale}"><spring:message code="delete.text"/></a></td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" value="<spring:message code="save.text"/>" />
		</form>
	</c:when>
	<c:otherwise>
		<spring:message code="no.translations.text"/>.
	</c:otherwise>
</c:choose>
<h1><spring:message code="add.translation.text"/></h1>

<form id="addTranslationForm" action="${addTranslationMethodURL}" method="post">
	<input name="locale" type="hidden" value="${currentLocale.locale}" />
	<table>
		<tr>
			<th><spring:message code="key.text"/></th>
			<th><spring:message code="value.text"/></th>
		</tr>
		<tr>
			<td><input id="newKey" name="newKey" type="text" /></td>
			<td><input id="newValue" name="newValue" type="text" /></td>
			<td><input type="submit" value="<spring:message code="add.text"/>" /></td>
		</tr>
	</table>
</form>

<a href="${handleRenderRequestMethodURL}"><spring:message code="back.text"/></a>
