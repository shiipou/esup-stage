package fr.esupportail.esupstage.controllers.jsf.beans;

import javax.inject.Named;

@Named(value = "helloWorld")
public class HelloWorldBean {

	private String firstName = "John";

	private String lastName = "Doe";

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String showGreeting() {
		return "Hello " + firstName + " " + lastName + "!";
	}

}
