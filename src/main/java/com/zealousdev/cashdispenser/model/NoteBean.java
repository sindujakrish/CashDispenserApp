package com.zealousdev.cashdispenser.model;

/**
 * This is a form bean object that binds data back and forth between View and Controller for Notes
 */
public class NoteBean {
	int twenties;
	int fifties;

	public NoteBean(int twenties, int fifties) {
		this.twenties = twenties;
		this.fifties = fifties;
	}

	public NoteBean() {
	}

	public int getTwenties() {
		return twenties;
	}

	public void setTwenties(int twenties) {
		this.twenties = twenties;
	}

	public int getFifties() {
		return fifties;
	}

	public void setFifties(int fifties) {
		this.fifties = fifties;
	}
}
