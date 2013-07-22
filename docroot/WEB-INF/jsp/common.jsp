<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<portlet:renderURL var="showTranslationsMethodURL">
	<portlet:param name="action" value="showTranslations"></portlet:param>
</portlet:renderURL>
<portlet:renderURL var="renderManageLanguagesMethodURL">
	<portlet:param name="action" value="renderManageLanguages"></portlet:param>
</portlet:renderURL>
<portlet:renderURL var="handleRenderRequestMethodURL">
</portlet:renderURL>

<c:choose>
	<c:when test="${not empty errors}">
		<c:forEach items="${errors}" var="validationError">
			<p class="error">${validationError.code}</p>
	</c:forEach>
	</c:when>
	<c:when test="${not empty error}">
		<p class="error">${error}</p>
	</c:when>
	<c:when test="${not empty success}">
		<p class="success">${success}</p>
	</c:when>
</c:choose>