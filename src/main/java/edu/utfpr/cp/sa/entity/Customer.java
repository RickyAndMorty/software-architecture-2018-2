package edu.utfpr.cp.sa.entity;

import lombok.Data;

@Data
public class Customer {
	
        private Long id;
	private String name;
	private String phone;
	private int age;
	private double creditLimit;
	
	private Country country;

	public void setName(String name) throws Exception {
		if (name.length() < 5)
			throw new Exception("Sorry, name must be 5 characters in length!");
		
		this.name = name;
	}

	public void setPhone(String phone) throws Exception {
		
		if (this.getCountry() == null)
			throw new Exception("Country must be defined!");
		
		if (phone.length() != this.getCountry().getPhoneDigits())
			throw new Exception("Phone does not conform to country!");
		
		this.phone = phone;
	}

	public void setAge(int age) {
		
		if (age <= 18)
			this.setCreditLimit(this.getCreditLimit() + 100.0);
		
		else if (age <= 35)
			this.setCreditLimit(this.getCreditLimit() + 300.0);
		
		else
			this.setCreditLimit(this.getCreditLimit() + 500.0);
			
		this.age = age;
	}

	public void setCountry(Country country) throws Exception {
		
		if (country == null || country.getName().length() < 1)
			throw new Exception("Country must be informed!");
		
		if (country.getName().equalsIgnoreCase("Brazil"))
			this.setCreditLimit(this.getCreditLimit() + 100.0);
			
		this.country = country;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Customer other = (Customer) obj;
		
		return this.getName().equalsIgnoreCase(other.getName());
	}

}
