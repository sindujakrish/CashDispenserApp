package com.zealousdev.cashdispenser.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zealousdev.cashdispenser.enums.Denomination;
import com.zealousdev.cashdispenser.jpa.NoteRepository;
import com.zealousdev.cashdispenser.model.NoteBean;
import com.zealousdev.cashdispenser.model.Note;

@Service
public class CashService {
	
	@Autowired
	NoteRepository noteRepository;
	
	
	public void initialiseDispenser() {
		if (noteRepository.countByType(Denomination.TWENTY) != 0 && noteRepository.countByType(Denomination.FIFTY) != 0) {
				//do nothing; the Dispenser is already pre-loaded with cash
		} else {
			//Load 100 Fifty dollar notes and 200 Twenty dollar notes onto the dispenser
			Note fifties = new Note(Denomination.FIFTY, 100);
			Note twenties = new Note(Denomination.TWENTY, 200);
			noteRepository.save(fifties);
			noteRepository.save(twenties);
		}
	}
	
	public NoteBean retrieveMoney() {
		
		Iterable<Note> cash = noteRepository.findAll();
		NoteBean money = new NoteBean();
		for (Note note : cash){
			if(note.getType() == Denomination.TWENTY) {
			money.setTwenties(note.getNumber());
			} else if(note.getType() == Denomination.FIFTY) {
				money.setFifties(note.getNumber());
			}
		}
		return money;
	}
	
	public void loadMoney(Denomination denomination, BigDecimal amount) {

		Note note = noteRepository.findByType(denomination);
		note.setNumber(note.getNumber()+amount.intValue());
		noteRepository.save(note);
	}
	
	public boolean removeMoney(Denomination denomination, BigDecimal amount) {
		boolean success = false;
		Note note = noteRepository.findByType(denomination);
		if(note.getNumber()-amount.intValue()>=00) {
			note.setNumber(note.getNumber()-amount.intValue());
			noteRepository.save(note);
			success = true;
		}
		
		return success;
	}
	
	private List<Note> dispense(BigDecimal requestedAmount, List<Note> result) {
		Note fiftyEntity = noteRepository.findByType(Denomination.FIFTY);
		Note twentyEntity = noteRepository.findByType(Denomination.TWENTY);
		int fifties = fiftyEntity.getNumber();
		int twenties = twentyEntity.getNumber();
		int tempamount;
		int tempFifties;
		int dispensedFifties = 0;
		int dispensedTwenties = 0;
		boolean cashMatchFound = false;
		int amount = requestedAmount.intValue();
		
		if (amount % 50 == 0) {
			if (amount / 50 <= fifties) {
				dispensedFifties = amount / 50;
				fifties = fifties - dispensedFifties;
				cashMatchFound = true;
			} else {
				tempamount = amount;
				tempFifties = fifties;
				dispensedFifties = 0;
				while (true && tempFifties > 0) {
					tempamount = amount - (tempFifties * 50);
					dispensedFifties = tempFifties;
					if (tempamount % 20 == 0) {
						if (tempamount / 20 <= twenties) {
							dispensedTwenties = tempamount / 20;
							fifties = fifties - dispensedFifties;
							twenties = twenties - dispensedTwenties;
							cashMatchFound = true;
							break;
						}
					}
					tempFifties--;
				}
			}
		}
		if (!cashMatchFound) {
			tempFifties = 0;
			dispensedFifties = 0;
			dispensedTwenties = 0;
			tempamount = amount;
			if (amount % 20 == 0) {
				if (amount / 50 > 0) {
					if (amount / 50 <= fifties) {
						tempFifties = amount / 50;

					} else {
						tempFifties = fifties;
					}
					tempamount = amount;

					while (true && tempFifties >= 0) {
						tempamount = amount - (tempFifties * 50);
						dispensedFifties = tempFifties;
						if (tempamount % 20 == 0) {
							if (tempamount / 20 <= twenties) {
								dispensedTwenties = tempamount / 20;
								twenties = twenties - dispensedTwenties;
								fifties = fifties - dispensedFifties;
								cashMatchFound = true;
								break;
							}
						}
						tempFifties--;
					}
				} else if (amount / 50 <= 0) {
					if (tempamount / 20 <= twenties) {
						dispensedTwenties = tempamount / 20;
						twenties = twenties - dispensedTwenties;
						fifties = fifties - dispensedFifties;
						cashMatchFound = true;
					}
				}
			} else {
				tempFifties = 0;
				dispensedFifties = 0;
				dispensedTwenties = 0;
				tempamount = amount;
				int remAmount;
				while (true && tempamount > 0) {
					tempamount = tempamount + (50 / 2);
					tempamount = tempamount - (tempamount % 50);
					if (tempamount / 50 <= fifties) {
						remAmount = amount - tempamount;
						if (remAmount % 20 == 0) {
							if (remAmount / 20 <= twenties) {
								dispensedTwenties = remAmount / 20;
								twenties = twenties - dispensedTwenties;
								dispensedFifties = tempamount / 50;
								fifties = fifties - dispensedFifties;
								cashMatchFound = true;
								break;
							}
						}
					}
					tempamount = tempamount - 50;
				}
			}
		}

		result = dispenseNotes(dispensedFifties, dispensedTwenties);

		if(result.get(0).getNumber() != 0 || result.get(1).getNumber() != 0) {
			fiftyEntity.setNumber(fifties);
			twentyEntity.setNumber(twenties);
			saveBalance(fiftyEntity, twentyEntity);
		}
		return result;
	}

	private  List<Note> dispenseNotes(int fifties, int twenties){
		List<Note> result = new ArrayList<Note>(2);
		result.add(new Note (Denomination.FIFTY, fifties));
		result.add(new Note(Denomination.TWENTY, twenties));
		return result;
	}

	private void saveBalance(Note fifties, Note twenties){
		noteRepository.save(fifties);
		noteRepository.save(twenties);
	}


	private Boolean checkBorderLimits(BigDecimal amount){
		Boolean proceed = true;
		if(amount.compareTo(this.totalAmountAvailable()) > 0) {
			proceed = false;
		} else if (amount.compareTo(BigDecimal.valueOf(20.0)) < 0){
			proceed = false;
		}  else if (amount.compareTo(BigDecimal.valueOf(30.0)) == 0){
			proceed = false;
		}  else if (amount.remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) != 0)  {
			proceed = false;
		}
		return proceed;
	}

	public List<Note> withdraw(BigDecimal amount) {
		Note twenty = new Note(Denomination.TWENTY,0);
		Note fifty = new Note(Denomination.FIFTY,0);
		List<Note> result = new ArrayList<Note>(2);
		result.add(fifty);
		result.add(twenty);
		if (checkBorderLimits(amount)) {
			result = this.dispense(amount, result);
		}
		return result;
	}
	
	private BigDecimal totalAmountAvailable() {
		BigDecimal availableFunds = BigDecimal.ZERO;
		Iterable<Note> cash = noteRepository.findAll();
		for (Note note : cash){
			BigDecimal value1 = BigDecimal.valueOf(note.getNumber());
			BigDecimal value2 = note.getType().value();
			availableFunds = availableFunds.add(value1.multiply(value2));
		}
		return availableFunds;
	}
	
	public Boolean checkAmount(List<Note> money) {
		Boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		return success;
	}
	
	public int noOfNotes(Denomination denomination) {
		Note note = (noteRepository.findByType(denomination));
		return note.getNumber();
	}
	
}
