<%@ include file="common.jsp"%>

<portlet:actionURL var="addLanguageMethodURL">
	<portlet:param name="action" value="addLanguage"></portlet:param>
</portlet:actionURL>

<portlet:renderURL var="renderEditLanguageMethodURL">
	<portlet:param name="action" value="renderEditLanguage"></portlet:param>
</portlet:renderURL>

<form:form method="post" action="${renderEditLanguageMethodURL}"
	modelAttribute="selectedLanguage">
	<form:select path="locale">
		<option value="language">
			--
			<spring:message code="language.text" />
			--
		</option>
		<form:options items="${languages}" itemValue="locale"
			itemLabel="displayLanguage" />
	</form:select>
	<input type="submit"
		value="<spring:message code="edit.text"/>" />

</form:form>


<form:form method="post" action="${addLanguageMethodURL}" commandName="newLanguage">
<table>
		<tr>
			<th><spring:message code="language.text"/>:</th>
			<td><input name="displayLanguage" type="text" /> </td>
		</tr>
		<tr>
			<th><spring:message code="locale.text"/>:</th>
			<td><input name="locale" type="text" /></td>
		</tr>
		<tr>
			<th />
			<td><input type="submit" value="<spring:message code="add.language.text"/>" /></td>
		</tr>
	</table>
</form:form>

<a href="${handleRenderRequestMethodURL}"><spring:message code="back.text"/></a>
