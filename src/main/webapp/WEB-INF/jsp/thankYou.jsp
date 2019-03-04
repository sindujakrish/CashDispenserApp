<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	<form:form method="post" modelAttribute="money">
		<div class="alert alert-success" role="alert">
			${message}
		</div>
	</form:form>
</div>
<%@ include file="common/footer.jspf"%>