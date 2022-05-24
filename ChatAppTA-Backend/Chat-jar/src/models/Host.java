package models;

import java.io.Serializable;

public class Host implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String alias;
	public String address;
	public Host() {
		// TODO Auto-generated constructor stub
	}
	public Host(String alias, String address) {
		super();
		this.alias = alias;
		this.address = address;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
