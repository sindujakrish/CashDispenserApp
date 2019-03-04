package com.zealousdev.cashdispenser.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.zealousdev.cashdispenser.enums.Denomination;
import com.zealousdev.cashdispenser.jpa.NoteRepository;
import com.zealousdev.cashdispenser.model.Note;
import com.zealousdev.cashdispenser.model.NoteBean;

@RunWith(SpringRunner.class)
public class CashServiceTest {
	
	@TestConfiguration
    static class CashServiceImplTestContextConfiguration {
  
        @Bean
        public CashService cashService() {
            return new CashService();
        }
    }
	
	@Autowired
	private CashService cashService;
	
	@MockBean
	NoteRepository noteRepository;
	
	@Before
	public void setup() {
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(twenties);
	}
	
	@Test
	public void initialiseDispenserSuccess() {
		cashService.initialiseDispenser();
		Note fiftyNote = noteRepository.findByType(Denomination.FIFTY);
		Note twentyNote = noteRepository.findByType(Denomination.TWENTY);
		assertEquals(fiftyNote.getNumber(), 100);
		assertEquals(twentyNote.getNumber(), 200);
	}
		
	@Test
	public void retrieveMoneySuccess() {
		NoteBean noteBean = new NoteBean(0,0);
		NoteBean actualNoteBean = cashService.retrieveMoney();
		assertEquals(noteBean.getFifties(), actualNoteBean.getFifties());
		assertEquals(noteBean.getTwenties(), actualNoteBean.getTwenties());
	}
	
	@Test
	public void loadMoneyFiftySuccess() {
		cashService.loadMoney(Denomination.FIFTY, new BigDecimal(10));
		Note fiftyNote = noteRepository.findByType(Denomination.FIFTY);
		assertEquals(fiftyNote.getNumber(), 110);
	}
	
	@Test
	public void loadMoneyTwentySuccess() {
		cashService.loadMoney(Denomination.TWENTY, new BigDecimal(10));
		Note twentyNote = noteRepository.findByType(Denomination.TWENTY);
		assertEquals(twentyNote.getNumber(), 210);
	}
	
	@Test
	public void removeMoneyFiftySuccess() {
		boolean success = cashService.removeMoney(Denomination.FIFTY, new BigDecimal(10));
		Note fiftyNote = noteRepository.findByType(Denomination.FIFTY);
		assertEquals(fiftyNote.getNumber(), 90);
		assertTrue(success);
	}
	
	@Test
	public void removeMoneyTwentySuccess() {
		boolean success = cashService.removeMoney(Denomination.TWENTY, new BigDecimal(10));
		Note twentyNote = noteRepository.findByType(Denomination.TWENTY);
		assertEquals(twentyNote.getNumber(), 190);
		assertTrue(success);
	}
	
	@Test
	public void removeMoneyFiftyFailure() {
		Note fifties = new Note(Denomination.FIFTY, 5);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		boolean success = cashService.removeMoney(Denomination.FIFTY, new BigDecimal(10));
		assertFalse(success);
	}
	
	@Test
	public void removeMoneyTwentyFailure() {
		Note twenties = new Note(Denomination.TWENTY, 5);
		Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(twenties);
		boolean success = cashService.removeMoney(Denomination.TWENTY, new BigDecimal(10));
		assertFalse(success);
	}
	
	
	
	@Test
	public void withdraw20Success() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(20));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdraw20Failure() {
		Note twenties = new Note(Denomination.TWENTY, 0);
		Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(twenties);
		Note fifties = new Note(Denomination.FIFTY, 100);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(20));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdraw30Failure() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(30));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdrawExcessAmountFailure() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(9100));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdrawAmtNotMultipleof10Failure() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(91));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdrawMultipleof50NotesAvailableSuccess() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(100));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawMultipleof50NotesNotAvailableSuccess() {
		
		Note fifties = new Note(Denomination.FIFTY, 1);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		List<Note> money = cashService.withdraw(new BigDecimal(100));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawMultipleof50NotesNotAvailableFailure() {
		
		Note fifties = new Note(Denomination.FIFTY, 1);
		Note twenties = new Note(Denomination.TWENTY, 4);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(twenties);
		List<Note> money = cashService.withdraw(new BigDecimal(100));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdrawMultipleof20NotesAvailableLessThan50Success() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(40));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawMultipleof20NotesNotAvailableLessThan50Failure() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 1);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(twenties);
		List<Note> money = cashService.withdraw(new BigDecimal(40));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdrawMultipleof20Notes50NotAvailableSuccess() {
		
		Note fifties = new Note(Denomination.FIFTY, 0);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		List<Note> money = cashService.withdraw(new BigDecimal(80));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawMultipleof20Notes50AvailableSuccess() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(80));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawNotMultipleof20or50CombinationSuccess() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(70));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawNotMultipleof20or50CombinationFailure() {
		
		Note fifties = new Note(Denomination.FIFTY, 0);
		Note twenties = new Note(Denomination.TWENTY, 1);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		List<Note> money = cashService.withdraw(new BigDecimal(70));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertFalse(success);
	}
	
	@Test
	public void withdrawNotMultipleof20or50CombinationOne410Success() {
		
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		List<Note> money = cashService.withdraw(new BigDecimal(410));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawNotMultipleof20or50CombinationTwo410Success() {
		
		Note fifties = new Note(Denomination.FIFTY, 1);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		List<Note> money = cashService.withdraw(new BigDecimal(410));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void withdrawNotMultipleof20or50CombinationThree410Success() {
		
		Note fifties = new Note(Denomination.FIFTY, 8);
		Note twenties = new Note(Denomination.TWENTY, 3);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		Mockito.when(noteRepository.findAll()).thenReturn(notes);
		Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(fifties);
		Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(twenties);
		List<Note> money = cashService.withdraw(new BigDecimal(410));
		boolean success = false;
		int numberOfNotes = 0;
		for (Note note : money) {
			numberOfNotes += note.getNumber();
		}
		if(numberOfNotes > 0 ){
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void checkAmountSuccess() {
		Note fifties = new Note(Denomination.FIFTY, 100);
		Note twenties = new Note(Denomination.TWENTY, 200);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		boolean success = cashService.checkAmount(notes);
		assertTrue(success);
	}
	
	@Test
	public void checkAmountFailure() {
		Note fifties = new Note(Denomination.FIFTY, 0);
		Note twenties = new Note(Denomination.TWENTY, 0);
		List<Note> notes = new ArrayList<Note>();
		notes.add(fifties);
		notes.add(twenties);
		boolean success = cashService.checkAmount(notes);
		assertFalse(success);
	}
	
	@Test
	public void noOfNotes50Success() {
		int noOfNotes = cashService.noOfNotes(Denomination.FIFTY);
		assertEquals(100, noOfNotes);
	}
	
	@Test
	public void noOfNotes20Success() {
		int noOfNotes = cashService.noOfNotes(Denomination.TWENTY);
		assertEquals(200, noOfNotes);
	}
}
