<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form modelAttribute="orderForm" action="${flowExecutionUrl}">
	<table style="width: 100%">
		<tr>
			<td width="30%"><spring:message code="label.page.category" /></td>
			<td><form:select path="categoryId"
					items="${selectableCategories}"></form:select></td>
		</tr>
	</table>

	<div align="right" style="margin-bottom: 20px; margin-top: 10px">
		<button type="submit" id="return" name="_eventId_cancel">
			<spring:message code="button.cancel" />
		</button>
		<button type="submit" id="continue" name="_eventId_next">
			<spring:message code="button.next" />
		</button>
	</div>

</form:form>