<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<form:form method="post" modelAttribute="money">
		<div class="d-inline p-2 bg-primary text-white">
			<h2 class="text-center">Welcome to CashMan 3001</h2>
		</div>
		<div style="height: 25px;"></div>
		<div class="text-center">No of $20 notes: ${money.twenties}</div>
		<div style="height: 15px;"></div>
		<div class="text-center">No of $50 notes: ${money.fifties}</div>
		<div style="height: 50px;"></div>
		<div class="text-center">What would you like to do today?</div>
		<div style="height: 25px;"></div>
		<div class="text-center">
			<a type="button" class="btn btn-success" href="/loadRem-notes">Load/Remove Notes</a>
			<a type="button" class="btn btn-success" href="/withdraw">Withdrawal</a>
		</div>
		
	</form:form>
</div>
<%@ include file="common/footer.jspf"%>