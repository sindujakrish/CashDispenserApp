<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	<form:form method="post" modelAttribute ="amountBean">
		<div class="alert alert-light" role="alert">
			${message}
		</div>

		<caption>How much do you want to withdraw?</caption>
		
			<table>
			<tr>
			<td>
			<form:input type="text" path="amount" class="form-control"/>
			</td>
			<td style="color: red; font-style: italic;"><form:errors    
       path="amount" />    
     		</td>
			</tr>
			<tr><td><div style="height: 15px;"></div></td></tr>
			<tr>
			<td>
			<button type="submit" class="btn btn-success">Withdraw</button>
			</td>
			</tr>
			</table>	
		<div style="height: 25px;"></div>

		<fieldset class="form-group">
			<a type="button" class="btn btn-success" href="/withdrawal/20/">$20</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/40/">$40</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/50/">$50</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/60/">$60</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/70/">$70</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/80/">$80</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/100/">$100</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/110/">$110</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/150/">$150</a> 
			<a type="button" class="btn btn-success" href="/withdrawal/200/">$200</a> 
		</fieldset>
	</form:form>
</div>
<%@ include file="common/footer.jspf"%>