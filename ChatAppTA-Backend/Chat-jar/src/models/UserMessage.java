package models;

import java.io.Serializable;
import java.util.Date;
import com.google.gson.Gson;

public class UserMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String recipient = "";
	public String sender = "";
	public Date date = new Date();
	public String subject = "";
	public String content = "";
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public UserMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserMessage(String recipient, String sender, Date date, String subject, String content) {
		super();
		this.recipient = recipient;
		this.sender = sender;
		this.date = date;
		this.subject = subject;
		this.content = content;
	}
	
	public String toJson() {
		Gson gson = new Gson();
	    String json = gson.toJson(this);
		return json;
	}
}
