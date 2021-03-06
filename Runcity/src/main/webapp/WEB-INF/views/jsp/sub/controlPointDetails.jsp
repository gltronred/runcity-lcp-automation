<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#${prefix}volTab"><fmt:message key="${volunteerTableByCP.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${volunteerTableByCP}" var="currTable"/>
	<%@ include file="../forms/volunteerCreateEditByCPForm.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}volTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${volunteerTableByCP}" caption="false"/>
	</div>
</div>