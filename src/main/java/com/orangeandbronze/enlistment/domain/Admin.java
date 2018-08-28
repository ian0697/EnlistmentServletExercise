package com.orangeandbronze.enlistment.domain;

public class Admin {
	
	private Integer id;
	private String lastname;
	private String firstname;
	
	public Admin(Integer id, String lastname, String firstname) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	
	
}
