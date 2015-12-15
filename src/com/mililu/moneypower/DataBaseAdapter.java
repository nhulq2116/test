package com.mililu.moneypower;

import com.mililu.moneypower.classobject.Diary;
import com.mililu.moneypower.classobject.DiaryDebt;
import com.mililu.moneypower.classobject.Income;
import com.mililu.moneypower.classobject.Wallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAdapter {
	static final String DATABASE_NAME = "DARFTMONEYPOWER.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE_ACCOUNT = "create table tbl_ACCOUNT (ID_ACCOUNT integer primary key autoincrement, USERNAME  text,PASSWORD text, FULLNAME text); ";
	static final String DATABASE_CREATE_DEBTOR = "create table tbl_DEBTOR (ID_DEBTOR integer primary key autoincrement, NAME_DEBTOR  text, ID_ACCOUNT integer); ";
	static final String DATABASE_CREATE_WALLET = "create table tbl_WALLET (ID_WALLET integer primary key autoincrement, NAME_WALLET  text,ID_ACCOUNT integer, MONEY numeric, ORIGINAL_AMOUNT numeric, DESCRIPTION text); ";
	static final String DATABASE_CREATE_INCOME = "create table tbl_INCOME (ID_INC integer primary key autoincrement, NAME_INCOME  text); ";
	static final String DATABASE_CREATE_EXPENDITURE = "create table tbl_EXPENDITURE (ID_EXP integer primary key autoincrement, NAME_EXP text); ";
	static final String DATABASE_CREATE_EXP_DETAIL = "create table tbl_EXP_DETAIL (ID_EXP_DET integer primary key autoincrement, ID_EXP  integer, NAME_EXP_DET text); ";
	static final String DATABASE_CREATE_DIARY = "CREATE TABLE `tbl_DIARY` (`ID_DIARY` INTEGER PRIMARY KEY AUTOINCREMENT, `ID_PARENT_CATEGORY` INTEGER, `ID_CATEGORY` INTEGER, `ID_WALLET` INTEGER, `AMOUNT` NUMERIC, `ID_ACCOUNT` INTEGER, `DAY` INTEGER, `MONTH` INTEGER, `YEAR` INTEGER, `TIME` TEXT, `TYPE` INTEGER, `NOTICE` TEXT);";
	static final String DATABASE_CREATE_DEBT_DIARY = "CREATE TABLE `tbl_DEBT_DIARY` (`ID_DIARY_DEBT` INTEGER PRIMARY KEY AUTOINCREMENT, `ID_DEBTOR` INTEGER, `ID_ACCOUNT` INTEGER, `ID_WALLET` INTEGER, `AMOUNT` NUMERIC, `TYPE` INTEGER, `DAY` INTEGER, `MONTH` INTEGER, `YEAR` INTEGER, `NOTICE` TEXT);";
	static final String DATABASE_INSERT_EXPENDITURE = "insert into tbl_EXPENDITURE (ID_EXP, NAME_EXP) values (1, 'Ăn uống'), (2, 'Đi lại'), (3, 'Dịch vụ sinh hoạt'), (4, 'Hưởng thụ'); ";
	static final String DATABASE_INSERT_EXP_DET = "insert into tbl_EXP_DETAIL (ID_EXP_DET, ID_EXP, NAME_EXP_DET) values (1, 1, 'Đi chợ/siêu thị'), (2, 1, 'Cafe'), (3, 1, 'Cơm tiệm'), (4, 2, 'Gửi xe'), (5, 2, 'Xăng xe'), (6, 2, 'Rửa xe'), (7, 3, 'Điện thoại'), (8, 3, 'Điện'), (9, 3, 'Nước'), (10, 4, 'Du lịch'), (11, 4, 'Xem phim'); ";
	static final String DATABASE_INSERT_INCOME = "insert into tbl_INCOME (ID_INC, NAME_INCOME) values (1, 'Lương'), (2, 'Thưởng'), (3, 'Lãi'), (4, 'Lãi tiết kiệm'), (5, 'Được cho/tặng'), (6, 'Khác'); ";
	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  DataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public  DataBaseAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	public void close() 
	{
		db.close();
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}
	/**
	 * Insert account into database
	 * @param username
	 * @param password
	 */
	public void InsertAccount(String username,String password, String fullname)
	{
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("USERNAME", username);
		newValues.put("PASSWORD",password);
		newValues.put("FULLNAME", fullname);

		// Insert the row into your table
		db.insert("tbl_ACCOUNT", null, newValues);
	}
	public int deleteEntry(String UserName)
	{
		//String id=String.valueOf(ID);
	    String where="USERNAME=?";
	    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
       // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	
	/**
	 * Get password of account from database
	 * @param username
	 * @return
	 */
	public String getAccountPassword(String username)
	{
		Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return "NOT EXIST";
        }
	    cursor.moveToFirst();
		String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
		cursor.close();
		return password;				
	}
	/**
	 * Get ID of account from table ACCOUNT 
	 * @param username
	 * @return
	 */
	public int getAccountId(String username)
	{
		Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return -1;
        }
	    cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex("ID_ACCOUNT"));
		cursor.close();
		return id;				
	}
	
    public Cursor getAccountInfor(String username) {
        Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        return cursor;
    }
    public Cursor getAccountInfor(Integer id_account) {
        Cursor cursor=db.query("tbl_ACCOUNT", null, " ID_ACCOUNT=?", new String[]{String.valueOf(id_account)}, null, null, null);
        return cursor;
    }
	
	/**
	 * Check username is exit in table ACCOUNT or not 
	 * @param username
	 * @return
	 */
	public boolean isAccountExit(String username){
		Cursor cursor=db.query("tbl_ACCOUNT", null, " USERNAME=?", new String[]{username}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return false;
        }
        return true;
	}
	
	public void  updateEntry(String userName,String password){
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("USERNAME", userName);
		updatedValues.put("PASSWORD",password);

        String where="USERNAME = ?";
	    db.update("LOGIN",updatedValues, where, new String[]{userName});
	}	
	/**
	 * Insert wallet into table WALLET
	 * @param namewallet
	 * @param money
	 * @param id_user
	 */
	public void insertWallet(String namewallet, long money, int id_user, String description){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_WALLET", namewallet);
		newValues.put("ID_ACCOUNT",id_user);
		newValues.put("MONEY", money);
		newValues.put("ORIGINAL_AMOUNT", money);
		newValues.put("DESCRIPTION", description);
		// Insert the row into your table
		db.insert("tbl_WALLET", null, newValues);
	}

	public boolean updateWallet(int id_wallet, long money){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("MONEY", money);
		// Insert the row into your table
		return db.update("tbl_WALLET", newValues, "ID_WALLET =? ", new String[] {String.valueOf(id_wallet)}) > 0;
	}
	public boolean updateWallet(Wallet wallet){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_WALLET", wallet.getName());
		newValues.put("ORIGINAL_AMOUNT", wallet.getOrg_money());
		newValues.put("DESCRIPTION", wallet.getDescrip());
		// Insert the row into your table
		return db.update("tbl_WALLET", newValues, "ID_WALLET =?", new String[] {String.valueOf(wallet.getId_wallet())}) > 0;
	}
	
	/**
	 * Delete wallet from table WALLET
	 * @param id_wallet
	 */
	public boolean deleteWallet(int id_wallet){
		return db.delete("tbl_WALLET", "ID_WALLET =?", new String[] {String.valueOf(id_wallet)}) > 0;
	}
	
	public long getAmountOfWallet(int id_wallet){
		Cursor cursor=db.query("tbl_WALLET", null, " ID_WALLET=?", new String[]{String.valueOf(id_wallet)}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		long amount = cursor.getLong(cursor.getColumnIndex("MONEY"));
		cursor.close();
		return amount;
	}
	
	public long getOriginalAmountOfWallet(int id_wallet){
		Cursor cursor=db.query("tbl_WALLET", null, " ID_WALLET=?", new String[]{String.valueOf(id_wallet)}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		long amount = cursor.getLong(cursor.getColumnIndex("ORIGINAL_AMOUNT"));
		cursor.close();
		return amount;
	}
	
	public String getDecriptionOfWallet(int id_wallet){
		Cursor cursor=db.query("tbl_WALLET", null, " ID_WALLET=?", new String[]{String.valueOf(id_wallet)}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return " ";
        }
	    cursor.moveToFirst();
		String noti = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
		cursor.close();
		return noti;
	}
	
	public Cursor getInforWallet(int id_wallet){
		String selectQuery = "SELECT * from tbl_WALLET WHERE ID_WALLET = " + id_wallet + ";";
		db = dbHelper.getReadableDatabase();
		return db.rawQuery(selectQuery, null);
	}
	
	public long getTotalAmount(int id_user){
		Cursor cursor=db.query("tbl_WALLET", null, " ID_ACCOUNT=?", new String[]{String.valueOf(id_user)}, null, null, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("MONEY"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	   
    public Cursor getListWalletOfUser(int id_user) {
    	// Select All Query
        String selectQuery = "SELECT ID_WALLET AS _id, * FROM tbl_WALLET WHERE ID_ACCOUNT=" + id_user; //+ id_user;
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    
	public void insertCategoryIncome(String nameincome){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_INCOME", nameincome);
		// Insert the row into your table
		db.insert("tbl_INCOME", null, newValues);
	}
	
	public boolean deteleCategoryIncome(int id_income){
		return db.delete("tbl_INCOME", "ID_INC = " + id_income, null) > 0;
	}
	
	public boolean updateCategoryIncome(Income income){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_INCOME", income.getName());
		// Insert the row into your table
		return db.update("tbl_INCOME", newValues, "ID_INC = " + income.getId(), null) > 0;
	}
	
	public Cursor getInforCategoryIncome(int id_income){
		String selectQuery = "SELECT * from tbl_INCOME WHERE ID_INC = " + id_income + ";";
		db = dbHelper.getReadableDatabase();
		return db.rawQuery(selectQuery, null);
	}
	
    public Cursor getCategoryIncomeCursor() {
        // Select All Query
        String selectQuery = "SELECT ID_INC AS _id, * FROM tbl_INCOME";
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    
    public Cursor getCategoryExpenCursor() {
        // Select All Query
        String selectQuery = "SELECT ID_EXP AS _id, * FROM tbl_EXPENDITURE";
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    
    public Cursor getCategoryExpenDetail(int id_exp) {
        // Select All Query
        String selectQuery = "SELECT ID_EXP_DET AS _id, * FROM tbl_EXP_DETAIL WHERE ID_EXP =" + id_exp;
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    public Cursor getAllCategoryExpenDetail() {
        // Select All Query
        String selectQuery = "SELECT ID_EXP_DET AS _id, * FROM tbl_EXP_DETAIL ";
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
	
    public boolean insertDiary(Diary diary){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("ID_PARENT_CATEGORY", diary.getId_parent_category());
		newValues.put("ID_CATEGORY", diary.getId_category());
		newValues.put("ID_WALLET",diary.getId_wallet());
		newValues.put("ID_ACCOUNT",diary.getId_account());
		newValues.put("AMOUNT", diary.getAmount());
		newValues.put("DAY", diary.getDay());
		newValues.put("MONTH", diary.getMonth());
		newValues.put("YEAR", diary.getYear());
		newValues.put("TIME", diary.getTime().toString());
		newValues.put("TYPE", diary.getType());
		newValues.put("NOTICE", diary.getNotice().toString());
		// Insert the row into your table
		return db.insert("tbl_DIARY", null, newValues) > 0;
	}
    
	public Cursor getDiaryOfWallet(int id_wallet){
		String selectQuery = "SELECT tbl_DIARY.ID_DIARY, tbl_DIARY.AMOUNT, tbl_INCOME.NAME_INCOME, tbl_EXP_DETAIL.NAME_EXP_DET, tbl_DIARY.DAY, tbl_DIARY.MONTH, tbl_DIARY.YEAR, tbl_DIARY.TIME, tbl_DIARY.TYPE, tbl_DIARY.NOTICE FROM tbl_DIARY, tbl_EXP_DETAIL, tbl_INCOME WHERE (((tbl_DIARY.TYPE = 1 AND tbl_DIARY.ID_CATEGORY = tbl_INCOME.ID_INC) OR (tbl_DIARY.TYPE = 2 AND tbl_EXP_DETAIL.ID_EXP_DET = tbl_DIARY.ID_CATEGORY)) AND (tbl_DIARY.ID_WALLET = " + id_wallet +")) GROUP BY tbl_DIARY.ID_DIARY ORDER BY tbl_DIARY.YEAR DESC, tbl_DIARY.MONTH DESC, tbl_DIARY.DAY DESC, tbl_DIARY.TIME;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	
	public Cursor getListDateOfDiary(int id_account){ // get date from all wallet. result return day, month, year
		String selectQuery = "SELECT tbl_DIARY.DAY, tbl_DIARY.MONTH, tbl_DIARY.YEAR FROM tbl_DIARY WHERE tbl_DIARY.ID_ACCOUNT = " + id_account + " GROUP BY tbl_DIARY.DAY, tbl_DIARY.MONTH, tbl_DIARY.YEAR  ORDER BY tbl_DIARY.YEAR DESC, tbl_DIARY.MONTH DESC, tbl_DIARY.DAY DESC, tbl_DIARY.TIME;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	
	public Cursor getDiaryByDate(int day, int month, int year, int id_account){ // result return amount, name giao dich, name wallet, type , notice
		String selectQuery = "SELECT tbl_DIARY.ID_DIARY, tbl_DIARY.AMOUNT, tbl_INCOME.NAME_INCOME, tbl_EXP_DETAIL.NAME_EXP_DET,tbl_WALLET.NAME_WALLET, tbl_DIARY.TYPE, tbl_DIARY.NOTICE FROM tbl_DIARY, tbl_EXP_DETAIL, tbl_INCOME, tbl_WALLET WHERE (((tbl_DIARY.TYPE = 1 AND tbl_DIARY.ID_CATEGORY = tbl_INCOME.ID_INC) OR (tbl_DIARY.TYPE = 2 AND tbl_EXP_DETAIL.ID_EXP_DET = tbl_DIARY.ID_CATEGORY)) AND (tbl_DIARY.ID_WALLET = tbl_WALLET.ID_WALLET) AND (tbl_DIARY.DAY = "+ day + " AND tbl_DIARY.MONTH = "+month+" AND tbl_DIARY.YEAR = "+year+") AND (tbl_DIARY.ID_ACCOUNT = " + id_account + ")) GROUP BY tbl_DIARY.ID_DIARY ORDER BY  tbl_DIARY.TIME;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	
	public Cursor getInforDiary(int id_diary){
		String selectQuery = "SELECT * from tbl_DIARY WHERE ID_DIARY = " + id_diary + ";";
		db = dbHelper.getReadableDatabase();
		return db.rawQuery(selectQuery, null);
	}
	
	public boolean updateDiary (Diary diary){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("AMOUNT", diary.getAmount());
		newValues.put("NOTICE", diary.getNotice());
		newValues.put("ID_WALLET", diary.getId_wallet());
		newValues.put("ID_CATEGORY", diary.getId_category());
		newValues.put("ID_PARENT_CATEGORY", diary.getId_parent_category());
		newValues.put("DAY", diary.getDay());
		newValues.put("MONTH", diary.getMonth());
		newValues.put("YEAR", diary.getYear());
		newValues.put("TIME", diary.getTime());
		return db.update("tbl_DIARY", newValues, "ID_DIARY = " + diary.getId_diary(), null) > 0;
		
	}
	
	public boolean deteleDiary(int id_diary){
			return db.delete("tbl_DIARY", "ID_DIARY = " + id_diary, null) > 0;
	}
	
	public long getTotalIncome(int month, int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 1) AND (MONTH = "+month+" AND YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public long getTotalIncome(int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 1) AND (YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public long getTotalExpenditure(int month, int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 2) AND (MONTH = "+month+" AND YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public long getTotalExpenditure(int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 2) AND (YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public Cursor getListIncomeOfMonth(int month, int year, int id_account){ // get date from all wallet. result return day, month, year
		String selectQuery = "SELECT tbl_DIARY.ID_CATEGORY, tbl_INCOME.NAME_INCOME FROM tbl_DIARY, tbl_INCOME WHERE (((tbl_DIARY.TYPE = 1) AND (tbl_DIARY.ID_CATEGORY = tbl_INCOME.ID_INC)) AND (tbl_DIARY.MONTH = "+month+" AND tbl_DIARY.YEAR = "+year+") AND (tbl_DIARY.ID_ACCOUNT = " + id_account + ") ) GROUP BY tbl_DIARY.ID_CATEGORY;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	public Cursor getListIncomeOfYear(int year, int id_account){ // get date from all wallet. result return day, month, year
		String selectQuery = "SELECT tbl_DIARY.ID_CATEGORY, tbl_INCOME.NAME_INCOME FROM tbl_DIARY, tbl_INCOME WHERE (((tbl_DIARY.TYPE = 1) AND (tbl_DIARY.ID_CATEGORY = tbl_INCOME.ID_INC)) AND (tbl_DIARY.YEAR = "+year+") AND (tbl_DIARY.ID_ACCOUNT = " + id_account + ") ) GROUP BY tbl_DIARY.ID_CATEGORY;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	
	public Cursor getListExpenditureOfMonth(int month, int year, int id_account){ // get date from all wallet. result return day, month, year
		String selectQuery = "SELECT tbl_DIARY.ID_PARENT_CATEGORY, tbl_EXPENDITURE.NAME_EXP FROM tbl_DIARY, tbl_EXPENDITURE WHERE (((tbl_DIARY.TYPE = 2) AND (tbl_DIARY.ID_PARENT_CATEGORY = tbl_EXPENDITURE.ID_EXP)) AND (tbl_DIARY.MONTH = "+month+" AND tbl_DIARY.YEAR = "+year+") AND (tbl_DIARY.ID_ACCOUNT = " + id_account + ") ) GROUP BY tbl_DIARY.ID_PARENT_CATEGORY;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	public Cursor getListExpenditureOfYear(int year, int id_account){ // get date from all wallet. result return day, month, year
		String selectQuery = "SELECT tbl_DIARY.ID_PARENT_CATEGORY, tbl_EXPENDITURE.NAME_EXP FROM tbl_DIARY, tbl_EXPENDITURE WHERE (((tbl_DIARY.TYPE = 2) AND (tbl_DIARY.ID_PARENT_CATEGORY = tbl_EXPENDITURE.ID_EXP)) AND (tbl_DIARY.YEAR = "+year+") AND (tbl_DIARY.ID_ACCOUNT = " + id_account + ") ) GROUP BY tbl_DIARY.ID_PARENT_CATEGORY;";
		db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
	}
	
	public long CalculateIncomeByMonth(int id_category, int month, int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 1 AND ID_CATEGORY = " + id_category + ") AND (MONTH = "+month+" AND YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public long CalculateIncomeByYear(int id_category, int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 1 AND ID_CATEGORY = " + id_category + ") AND (YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public long CalculateExpendByMonth(int id_category, int month, int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 2 AND ID_PARENT_CATEGORY = " + id_category + ") AND (MONTH = "+month+" AND YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	public long CalculateExpendByYear(int id_category, int year, int id_account){
		String selectQuery = "SELECT AMOUNT FROM tbl_DIARY WHERE ((TYPE = 2 AND ID_PARENT_CATEGORY = " + id_category + ") AND (YEAR = "+year+") AND (ID_ACCOUNT = " + id_account + "))";
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
        long amount = 0;
		if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return amount;
        }
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	    	long money = cursor.getLong(cursor.getColumnIndex("AMOUNT"));
	    	amount += money;
			cursor.moveToNext();
	 	}
		cursor.close();
		return amount;
	}
	
	public boolean insertDebtDiary(DiaryDebt diary){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("ID_DEBTOR", diary.getId_debtor());
		newValues.put("ID_ACCOUNT", diary.getId_account());
		newValues.put("ID_WALLET",diary.getId_wallet());
		newValues.put("TYPE",diary.getId_account());
		newValues.put("AMOUNT", diary.getAmount());
		newValues.put("DAY", diary.getDay());
		newValues.put("MONTH", diary.getMonth());
		newValues.put("YEAR", diary.getYear());
		newValues.put("NOTICE", diary.getNotice());
		// Insert the row into your table
		return db.insert("tbl_DEBT_DIARY", null, newValues) > 0;
	}
	
    public Cursor getDebtorCursor(int id_user) {
        // Select All Query
        String selectQuery = "SELECT ID_DEBTOR as _id, * FROM tbl_DEBTOR WHERE ID_ACCOUNT =" + id_user;
        db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }
    
	public boolean insertDebtor(String namedebtor, int id_user){
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("NAME_DEBTOR", namedebtor);
		newValues.put("ID_ACCOUNT", id_user);
		// Insert the row into your table
		return db.insert("tbl_DEBTOR", null, newValues) > 0;
	}
}

