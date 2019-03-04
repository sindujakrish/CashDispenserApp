package com.zealousdev.cashdispenser.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zealousdev.cashdispenser.enums.Denomination;
import com.zealousdev.cashdispenser.model.AmountBean;
import com.zealousdev.cashdispenser.model.Note;
import com.zealousdev.cashdispenser.service.CashService;
import com.zealousdev.cashdispenser.validator.FormValidation;

@Controller
public class CashController {

	@Autowired
	CashService cashService;

	private String message = "";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showWelcomePage(ModelMap model) {
		message = "";
		model.addAttribute("message", message);
		cashService.initialiseDispenser();
		model.put("money", cashService.retrieveMoney());
		return "welcome";
	}

	@RequestMapping(value = "/loadRem-notes", method = RequestMethod.GET)
	public String showLoadRemNotesPage(ModelMap model) {
		model.addAttribute("message", message);
		return "loadRemovenotes";
	}

	@RequestMapping(value = "/load/{type}/{amount}", method = RequestMethod.GET)
	public String loadMoney(@PathVariable int type, @PathVariable BigDecimal amount) {
		Denomination denomination = Denomination.FIFTY;
		if (type == 20) {
			denomination = Denomination.TWENTY;
		}
		cashService.loadMoney(denomination, amount);
		message = "Notes have been loaded successfully";
		return "redirect:/loadRem-notes";

	}

	@RequestMapping(value = "/remove/{type}/{amount}", method = RequestMethod.GET)
	public String removeMoney(@PathVariable int type, @PathVariable BigDecimal amount) {
		Denomination denomination = Denomination.FIFTY;
		if (type == 20) {
			denomination = Denomination.TWENTY;
		}
		boolean success = cashService.removeMoney(denomination, amount);
		if (success) {
			message = "Notes have been removed successfully";
		} else {
			message = "Not enough notes to dispense!! Notes have not been removed!!!";
		}

		return "redirect:/loadRem-notes";

	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String showWithdrawalPage(ModelMap model) {
		model.addAttribute("message", message);
		model.put("amountBean", new AmountBean(""));
		return "withdrawal";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdrawal(@ModelAttribute AmountBean amountBean, BindingResult result, ModelMap model) {
		FormValidation formValidation = new FormValidation();

		formValidation.validate(amountBean, result);

		if (result.hasErrors()) {
			return "withdrawal";
		}
		List<Note> money = cashService.withdraw(new BigDecimal(amountBean.getAmount()));
		Boolean amountWasDispensed = cashService.checkAmount(money);
		model.addAttribute("twenties", money.get(1).getNumber());
		model.addAttribute("fifties", money.get(0).getNumber());
		if (amountWasDispensed) {
			message = "Thank You!!! Cash has been dispensed successfully";
		} else {
			message = "We are currently unable to dispense the requested amount!! Click on Home to try a different amount!!!";
		}
		model.addAttribute("message", message);

		return "redirect:/thankYou";

	}

	@RequestMapping(value = "/withdrawal/{amount}", method = RequestMethod.GET)
	public String withdraw(@PathVariable BigDecimal amount, ModelMap model) {
		List<Note> money = cashService.withdraw(amount);
		Boolean amountWasDispensed = cashService.checkAmount(money);
		model.addAttribute("twenties", money.get(1).getNumber());
		model.addAttribute("fifties", money.get(0).getNumber());
		if (amountWasDispensed) {
			message = "Thank You!!! Cash has been dispensed successfully";
		} else {
			message = "We are currently unable to dispense the requested amount!! CLick on Home to try again!!!";
		}
		model.addAttribute("message", message);

		return "redirect:/thankYou";

	}

	@RequestMapping(value = "/thankYou", method = RequestMethod.GET)
	public String showThankYouPage(ModelMap model) {
		model.addAttribute("message", message);
		return "thankYou";
	}

	@RequestMapping(value = "/logmeout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(req, resp, auth);
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/numberofNotes/{type}", method = RequestMethod.GET)
	public ResponseEntity<Integer> checkNumberOfNotesAvailable(@PathVariable int type ) {
		Denomination denomination = Denomination.FIFTY;
		if (type == 20){
			denomination = Denomination.TWENTY;
		}
		int numberOfNotes = cashService.noOfNotes(denomination);
		return  new ResponseEntity<Integer>(numberOfNotes, HttpStatus.OK);
	}
}
