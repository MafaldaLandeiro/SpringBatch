package org.springBatch.entity;

public class Country {

	private int id;

	private String name;

	private String currency;

	/**
	 * 
	 */
	public Country() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param currency
	 */
	public Country(int id, String name, String currency) {
		super();
		this.id = id;
		this.name = name;
		this.currency = currency;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", currency="
				+ currency + "]";
	}

}
