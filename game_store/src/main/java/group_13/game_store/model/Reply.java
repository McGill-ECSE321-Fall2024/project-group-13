package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// line 108 "model.ump"
// line 215 "model.ump"
@Entity
public class Reply
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reply Attributes
  @Id
  @GeneratedValue
  private int replyID;
  private String text;
  private Date date;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reply(String aText, Date aDate)
  {
    text = aText;
    date = aDate;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setReplyID(int aReplyID)
  {
    boolean wasSet = false;
    replyID = aReplyID;
    wasSet = true;
    return wasSet;
  }

  public boolean setText(String aText)
  {
    boolean wasSet = false;
    text = aText;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public int getReplyID()
  {
    return replyID;
  }

  public String getText()
  {
    return text;
  }

  public Date getDate()
  {
    return date;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "replyID" + ":" + getReplyID()+ "," +
            "text" + ":" + getText()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}



