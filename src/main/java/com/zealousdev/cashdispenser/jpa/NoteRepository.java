package com.zealousdev.cashdispenser.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zealousdev.cashdispenser.enums.Denomination;
import com.zealousdev.cashdispenser.model.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

	Note findByType(Denomination denomination);

	int countByType(Denomination denomination);
	
	
}