<%@ include file="common.jsp"%>

<portlet:actionURL var="editLanguageMethodURL">
	<portlet:param name="action" value="editLanguage"></portlet:param>
</portlet:actionURL>

<portlet:actionURL var="deleteLanguageMethodURL">
	<portlet:param name="action" value="deleteLanguage"></portlet:param>
</portlet:actionURL>

<form method="post" action="${editLanguageMethodURL}">
<input name="language" type="hidden" value="${language.locale}" />
<table>
		<tr>
			<th><spring:message code="language.text"/>:</th>
			<td><input name="newLanguage" type="text" value="${language.displayLanguage}" /> </td>
		</tr>
		<tr>
			<th><spring:message code="locale.text"/>:</th>
			<td><input name="newLocale" type="text" value="${language.locale}" /></td>
		</tr>
		<tr>
			<th />
			<td><input type="submit" value="<spring:message code="save.text"/>" /></td>
		</tr>
	</table>
</form>

<form method="post" action="${deleteLanguageMethodURL}">
<input name="language" type="hidden" value="${language.locale}" />
<input type="submit" value="<spring:message code="delete.text"/>" />
</form>

<a href="${renderManageLanguagesMethodURL}"><spring:message code="back.text"/></a>