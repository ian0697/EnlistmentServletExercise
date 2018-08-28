package com.orangeandbronze.enlistment.dao;

import java.util.*;

import com.orangeandbronze.enlistment.domain.*;

public interface SectionDAO {

	/**
	 * @return The section that has Section ID @param sectionId . If none found,
	 *         returns Section.NONE.
	 * For Room, does not fetch the collection of Sections of the Room.
 	 * For Faculty, does not fetch the collection of Sections of the Faculty.
	 * Fetches the collection of Students of the section, but not the collection of Sections of each Student.
	 */
	Section findBy(String sectionId);

	/**
	 * @return All the sections in the database, but does not fetch the collection of Students
	 * of each Section.
	 */
	Collection<Section> findAll();

	/** Retrieve all the Sections associated with the given student number, but does not
	* fetch the collection of Students of the Section.
	*/
	Collection<Section> findByStudentNo(int studentNumber);

	/** Retrieve all Sections NOT associated with the given student number, but does not
	* fetch the collection of Students of the Section.
	*/
	Collection<Section> findByNotStudentNo(int studentNumber);

	/** Saves a NEW section. **/
	void create(Section section);
}
