package com.mililu.moneypower.classobject;

public class Diary {
	private int id_diary;
	private int id_category;
	private int id_parent_category;
	private String name_categoy;
	private String name_income;
	private String name_expen;
	private int id_wallet;
	private String name_wallet;
	private int id_account;
	private int day;
	private int month;
	private int year;
	private String time;
	private long amount;
	private int type;
	private String notice;
	
	public int getId_diary() {
		return id_diary;
	}
	public void setId_diary(int id_diary) {
		this.id_diary = id_diary;
	}
	public int getId_category() {
		return id_category;
	}
	public void setId_category(int id_category) {
		this.id_category = id_category;
	}
	public String getName_categoy() {
		return name_categoy;
	}
	public void setName_categoy(String name_categoy) {
		this.name_categoy = name_categoy;
	}
	public String getName_income() {
		return name_income;
	}
	public void setName_income(String name_income) {
		this.name_income = name_income;
	}
	public String getName_expen() {
		return name_expen;
	}
	public void setName_expen(String name_expen) {
		this.name_expen = name_expen;
	}
	public int getId_wallet() {
		return id_wallet;
	}
	public void setId_wallet(int id_wallet) {
		this.id_wallet = id_wallet;
	}
	public String getName_wallet() {
		return name_wallet;
	}
	public void setName_wallet(String name_wallet) {
		this.name_wallet = name_wallet;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public int getId_account() {
		return id_account;
	}
	public void setId_account(int id_account) {
		this.id_account = id_account;
	}
	public int getId_parent_category() {
		return id_parent_category;
	}
	public void setId_parent_category(int id_parent_category) {
		this.id_parent_category = id_parent_category;
	}
}
