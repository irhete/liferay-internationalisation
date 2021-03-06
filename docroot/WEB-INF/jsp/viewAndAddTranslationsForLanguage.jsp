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



<h1><spring:message code="existing.translations.text"/> (${selectedLanguage.name})</h1>
<c:choose>
	<c:when test="${not empty translationsForm.translations}">
		<form:form id="updateTranslationsForm"
			action="${updateTranslationsMethodURL}" method="post" modelAttribute="translationsForm">
			<form:errors path="*" cssClass="error"/>
			<table id="translationTable">
				<tr>
					<th><spring:message code="key.text"/></th>
					<th><spring:message code="value.text"/></th>
				</tr>
				<c:forEach items="${translationsForm.translations}" var="translation" varStatus="status">
					<tr>
						<td><input name="translations[${status.index}].key" type="text"
							value="${translation.key}" /></td>
						<td><input name="translations[${status.index}].value" type="text"
							value="${translation.value}" /></td><td>
						<td><a href="${deleteTranslationMethodURL}&id=${translation.id}"><spring:message code="delete.text"/></a></td>
					<td><form:errors path="translations[${status.index}]" cssClass="error" text="err"/></td>
					</tr>
				</c:forEach>
			</table>
			<input type="submit" value="<spring:message code="save.text"/>" />
		</form:form>
	</c:when>
	<c:otherwise>
		<spring:message code="no.translations.text"/>.
	</c:otherwise>
</c:choose>
<h1><spring:message code="add.translation.text"/></h1>

<form:form id="addTranslationForm" action="${addTranslationMethodURL}" method="post" commandName="newTranslation">
	<form:errors path="*" cssClass="error"/>
	<table>
		<tr>
			<th><spring:message code="key.text"/></th>
			<th><spring:message code="value.text"/></th>
		</tr>
		<tr>
			<td><input name="key" type="text" /></td>
			<td><input name="value" type="text" /></td>
			<td><input type="submit" value="<spring:message code="add.text"/>" /></td>
		</tr>
	</table>
	
</form:form>

<a href="${handleRenderRequestMethodURL}"><spring:message code="back.text"/></a>
