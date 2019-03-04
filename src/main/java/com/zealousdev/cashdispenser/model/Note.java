package com.zealousdev.cashdispenser.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.zealousdev.cashdispenser.enums.Denomination;

@Entity
public class Note {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	Denomination type;

	int number;

	public Note(Denomination type, int number) {
		this.type = type;
		this.number = number;
	}

	public Note() {
	}

	public Denomination getType() {
		return type;
	}

	public void setType(Denomination type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
