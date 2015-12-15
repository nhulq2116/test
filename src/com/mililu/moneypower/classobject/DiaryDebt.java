package com.mililu.moneypower.classobject;

public class DiaryDebt {
	int id_debt;
	int id_debtor;
	int id_account;
	int id_wallet;
	int type;
	long amount;
	int day;
	int month;
	int year;
	String notice;
	public int getId_debt() {
		return id_debt;
	}
	public void setId_debt(int id_debt) {
		this.id_debt = id_debt;
	}
	public int getId_debtor() {
		return id_debtor;
	}
	public void setId_debtor(int id_debtor) {
		this.id_debtor = id_debtor;
	}
	public int getId_account() {
		return id_account;
	}
	public void setId_account(int id_account) {
		this.id_account = id_account;
	}
	public int getId_wallet() {
		return id_wallet;
	}
	public void setId_wallet(int id_wallet) {
		this.id_wallet = id_wallet;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
}
