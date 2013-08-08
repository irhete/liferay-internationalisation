<%@ include file="common.jsp"%>

<portlet:actionURL var="editLanguageMethodURL">
	<portlet:param name="action" value="editLanguage"></portlet:param>
</portlet:actionURL>

<portlet:actionURL var="deleteLanguageMethodURL">
	<portlet:param name="action" value="deleteLanguage"></portlet:param>
</portlet:actionURL>

<form:errors path="updatedLanguage" cssClass="error" />
<form:form method="post" action="${editLanguageMethodURL}" commandName="updatedLanguage">
<table>
		<tr>
			<th><spring:message code="language.text"/>:</th>
			<td><input name="name" type="text" value="${selectedLanguage.name}" /> </td>
			<td><form:errors path="name" cssClass="error" /></td>
		</tr>
		<tr>
			<th><spring:message code="locale.text"/>:</th>
			<td><input name="locale" type="text" value="${selectedLanguage.locale}" /></td>
			<td><form:errors path="locale" cssClass="error" /></td>
		</tr>
		<tr>
			<th></th>
			<td><input type="submit" value="<spring:message code="save.text"/>" /></td>
		</tr>
	</table>
</form:form>

<form method="post" action="${deleteLanguageMethodURL}">
<input type="submit" value="<spring:message code="delete.text"/>" />
</form>

<a href="${renderManageLanguagesMethodURL}"><spring:message code="back.text"/></a>