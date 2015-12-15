package com.mililu.moneypower.classobject;

public class Wallet {
	private int id_wallet;
	private String name;
	private long money;
	private long org_money;
	private String descrip;
	
	public int getId_wallet() {
		return id_wallet;
	}
	public void setId_wallet(int id_wallet) {
		this.id_wallet = id_wallet;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public long getOrg_money() {
		return org_money;
	}
	public void setOrg_money(long org_money) {
		this.org_money = org_money;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
}
