package com.zealousdev.cashdispenser.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.zealousdev.cashdispenser.model.AmountBean;    
    
public class FormValidation implements Validator {    
    
 private Pattern pattern;    
 private Matcher matcher;    
    
 String AMT_PATTERN = "[0-9]+";    
 
    
 @Override    
 public void validate(Object target, Errors errors) {    
    
  AmountBean amtBean = (AmountBean) target;    
    
  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "required.amount",    
    "Amount is required.");    
    
// input string contains numeric values only    
  if (amtBean.getAmount() != null) {    
   pattern = Pattern.compile(AMT_PATTERN);    
   matcher = pattern.matcher(amtBean.getAmount().toString());    
   if (!matcher.matches()) {    
    errors.rejectValue("amount", "amount.incorrect",    
      "Enter a numeric value");    
   }
  }
    
 }

@Override
public boolean supports(Class<?> arg0) {
	// TODO Auto-generated method stub
	return false;
}    
}
