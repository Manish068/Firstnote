package com.manish.firstnote.Data;

import java.io.Serializable;

public class UserNotes implements Serializable {
    String noteTitle="",notedesc="",notedate="",noteid="";

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotedesc() {
        return notedesc;
    }

    public void setNotedesc(String notedesc) {
        this.notedesc = notedesc;
    }

    public String getNotedate() {
        return notedate;
    }

    public void setNotedate(String notedate) {
        this.notedate = notedate;
    }

    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }

    public UserNotes(){

    }

    public UserNotes(String noteTitle, String notedesc, String notedate, String noteid) {
        this.noteTitle = noteTitle;
        this.notedesc = notedesc;
        this.notedate = notedate;
        this.noteid = noteid;
    }
}
