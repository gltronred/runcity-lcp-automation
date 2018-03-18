<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="runcity" uri="/WEB-INF/runcity.tld"%>

<c:set value="${teamProcessByVolunteerForm}" var="formVar"/>

<a id="formTop"></a>
<runcity:form bundle="${msg}" modal="${modal}" form="${formVar}" relatedTable="${currTable}" scroll="#formTop">	
	<runcity:form-body modal="${modal}">
		<div class="errorHolder">
			<form:errors cssClass="alert alert-danger" element="div"/>
		</div>		
		
		<c:set value="${formVar.volunteerIdColumn}" var="col"/>
		<spring:bind path="${col.name}">
			<runcity:input bundle="${msg}" column="${col}" status="${status.error}" />	
		</spring:bind>
		
		<c:set value="${formVar.numberColumn}" var="col"/>
		<spring:bind path="${col.name}">
			<runcity:input bundle="${msg}" column="${col}" status="${status.error}" autofocus="autofocus"/>
		</spring:bind>
		
		<runcity:keyboard bundle="${msg}" for="${col.htmlId}" rows="${'keyboard.team.row1,keyboard.team.row2,keyboard.team.row3,keyboard.team.row4,keyboard.team.row5'}"/>
	</runcity:form-body>
	<runcity:form-footer bundle="${msg}" modal="${modal}"/>
</runcity:form>