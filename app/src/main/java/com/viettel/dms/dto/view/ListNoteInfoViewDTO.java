/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

/**
 *  list note for general statistics screen
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class ListNoteInfoViewDTO {
	
	
	private int noteNumber;
	private ArrayList<NoteInfoDTO> listNote = new ArrayList<NoteInfoDTO>();
	
	public ListNoteInfoViewDTO(){
		noteNumber = 0;
		listNote = new ArrayList<NoteInfoDTO>();
	}
	/**
	 * @return the noteNumber
	 */
	public int getNoteNumber() {
		return noteNumber;
	}
	/**
	 * @param noteNumber the noteNumber to set
	 */
	public void setNoteNumber(int noteNumber) {
		this.noteNumber = noteNumber;
	}
	/**
	 * @return the listNote
	 */
	public ArrayList<NoteInfoDTO> getListNote() {
		return listNote;
	}
	/**
	 * @param listNote the listNote to set
	 */
	public void setListNote(ArrayList<NoteInfoDTO> listNote) {
		this.listNote = listNote;
	}
	
}
