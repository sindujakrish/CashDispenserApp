<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	<form:form method="post" modelAttribute="money">
		<div class="alert alert-light" role="alert">
			${message}
		</div>

		<caption>Load More Notes</caption>
		<fieldset class="form-group">
			<a type="button" class="btn btn-success" href="/load/20/10">Load 10 more $20s</a> 
			<a type="button" class="btn btn-success" href="/load/50/10">Load 10 more $50s</a>
		</fieldset>

		<caption>Remove Notes</caption>
		<fieldset class="form-group">
			<a type="button" class="btn btn-success" href="/remove/20/10">Remove 10 $20s</a> 
			<a type="button" class="btn btn-success" href="/remove/50/10">Remove 10 $50s</a>
		</fieldset>
	</form:form>
</div>
<%@ include file="common/footer.jspf"%>