import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Main {
	
	static String[] tableNames = {"login", "wishList", "shareList"};
	static String[] dummyItems = {"Pizza", "Godis", "Burgare", "Kex", "Kebab"};
	static String[] dummyItems2 = {"Linser", "Lax", "Potatis", "Ris"};
	static String[] dummyItems3 = {"Vodka", "Gin", "Whisky", "Beer"};
	static Map<String, String> dummyUsers = new HashMap<String, String>();

	public static void main(String[] args) {
		setup();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI person1 = new GUI("duds");
				GUI person2 = new GUI("otto");
				GUI person3 = new GUI("alex");
			}
		});	
	}
	
	public static void setup() {
		Connection conn = null;
		Statement stmt = null;	
		dummyUsers.put("duds", "bla");
		dummyUsers.put("otto", "123");
		dummyUsers.put("alex", "qwe");
		
		try {
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection established");
			
			stmt = conn.createStatement();
			dropTable(stmt, tableNames);
			setupTable(stmt);
			addDummyData(stmt, dummyUsers, dummyItems, dummyItems2, dummyItems3);
			conn.close();
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		}
	}
	
	public static void setupTable(Statement stmt) {
		String createLogin = "create table if not exists login (id INTEGER PRIMARY KEY, user text UNIQUE NOT NULL, "
				+ "password text NOT NULL);";
		String createWishList = "create table if not exists wishList (id INTEGER PRIMARY KEY, user test NOT NULL,"
				+ "item text NOT NULL);";
		String createShareList = "create table if not exists shareList (id INTEGER PRIMARY KEY, user text NOT NULL, "
				+ "shareWith text NOT NULL,"
				+ "item text NOT NULL,"
				+ "bought INTEGER NOT NULL);";

		try {
			stmt.executeUpdate(createLogin);
			System.out.println("Table login has been created");
			stmt.executeUpdate(createWishList);
			System.out.println("Table wishList has been created");
			stmt.executeUpdate(createShareList);
			System.out.println("Table shareList has been created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void dropTable(Statement stmt, String[] tableNames) {
		for (String tableName : tableNames) {
			String dropTable = "drop table if exists '" + tableName + "';";
			
			try {
				stmt.executeUpdate(dropTable);
				System.out.println("Table " + tableName + " has been dropped");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void addDummyData(Statement stmt, Map<String, String> users, String[] items, String[] items2, String[] items3) {
		users.forEach((un,pw) -> {
			try {
				System.out.println("Namn: " + un);
				stmt.executeUpdate("insert into login (user, password) values "
						+ "('" + un + "', '" + pw + "');");
				if ("duds".equals(un)) {
					for (String item : items) {
						String dummyData = "insert into wishList (user, item) values ('" + un +"', '" + item + "');";
						stmt.executeUpdate(dummyData);
						dummyData = "insert into shareList (user, shareWith, item, bought) values "
								+ "('" + un + "', 'otto', '" + item + "', 0);";
						stmt.executeUpdate(dummyData);
					}
				}
				if ("otto".equals(un)) {
					for (String item : items2) {
						String dummyData = "insert into wishList (user, item) values ('" + un +"', '" + item + "');";
						stmt.executeUpdate(dummyData);
						dummyData = "insert into shareList (user, shareWith, item, bought) values "
								+ "('" + un + "', 'duds', '" + item + "', 0);";
						stmt.executeUpdate(dummyData);
					}
				}
				if ("alex".equals(un)) {
					for (String item : items3) {
						String dummyData = "insert into wishList (user, item) values ('" + un +"', '" + item + "');";
						stmt.executeUpdate(dummyData);
						dummyData = "insert into shareList (user, shareWith, item, bought) values "
								+ "('" + un + "', 'duds', '" + item + "', 0);";
						stmt.executeUpdate(dummyData);
						dummyData = "insert into shareList (user, shareWith, item, bought) values "
								+ "('" + un + "', 'otto', '" + item + "', 0);";
						stmt.executeUpdate(dummyData);
					}
				}
			} catch (SQLException e) {
				e.getMessage();
			}
		});
	}
	
	public static void addItem(String user, String item) {
		Connection conn = null;
		PreparedStatement pstmt = null;		
		String addItem = "insert into wishList (user, item) values (?, ?);";
		
		try {
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(addItem);
			pstmt.setString(1, user);
			pstmt.setString(2, item);

			pstmt.executeUpdate();
			conn.close();
			System.out.println("Item added");
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	
	public static void shareItem(String user, String shareWith, String item) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String shareItem = "insert into shareList (user, shareWith, item, bought) values (?, ?, ?, ?);";
		try {
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(shareItem);
			pstmt.setString(1, user);
			pstmt.setString(2, shareWith);
			pstmt.setString(3, item);
			pstmt.setBoolean(4, false);
			
			pstmt.executeUpdate();
			conn.close();
			System.out.println("Item: " + item + " shared");
		} catch (SQLException e) {
			e.getMessage();
		}
	}

	public static void updateSharedItem(String sharedBy, String item, boolean bought) {
		Connection conn = null;
		PreparedStatement pstmt = null;		
		String updateItem = "update shareList set bought =? where user =? and item =?;";
		
		try {
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(updateItem);
			pstmt.setBoolean(1, bought);
			pstmt.setString(2, sharedBy);
			pstmt.setString(3, item);

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			System.out.println("Shared item info updated");
		} catch (SQLException e) {
			e.getMessage();
		}		
	}
	
	public static DefaultListModel sharedItems(String user) {
		Connection conn = null;
		Statement stmt = null;
		DefaultListModel listModel = null;
		String showItems = "select * from shareList where shareWith ='" + user + "';";
		try {
			listModel = new DefaultListModel();
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(showItems);
			while (rs.next()) {
				listModel.addElement(rs.getString("item"));
				//System.out.println(rs.getString("item") + " found for user: " + user);
			}
			conn.close();
			return listModel;
		} catch (SQLException e) {
			e.getMessage();
		}
		return listModel;
	}
	
	public static DefaultListModel userItems(String user) {
		Connection conn = null;
		Statement stmt = null;
		DefaultListModel listModel = null;
		String showItems = "select * from wishList where user ='" + user + "';";
		try {
			listModel = new DefaultListModel();
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(showItems);
			while (rs.next()) {
				listModel.addElement(rs.getString("item"));
				//System.out.println(rs.getString("item") + " found for user: " + user);
			}
			conn.close();
			return listModel;
		} catch (SQLException e) {
			e.getMessage();
		}
		return listModel;
	}
	
	public static DefaultListModel updateMyList(String user) {
		Connection conn = null;
		Statement stmt = null;
		DefaultListModel listModel = null;
		String showItems = "select * from wishList where user ='" + user + "';";
		try {
			listModel = new DefaultListModel();
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(showItems);
			while (rs.next()) {
				listModel.addElement(rs.getString("item"));
				//System.out.println(rs.getString("item") + " found for user: " + user);
			}
			conn.close();
			return listModel;
		} catch (SQLException e) {
			e.getMessage();
		}
		return listModel;
	}
	
	public static DefaultTableModel userItems2(String user) {
		Connection conn = null;
		Statement stmt = null;
		DefaultTableModel mdl = null;
		String showItems = "select * from wishList where user ='" + user + "';";
		try {
			mdl = new DefaultTableModel();
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(showItems);
			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
		    Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();

		    columnNames.add("items");
		    
		    // data of rows
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getString("item"));
				}
				data.add(row);
				System.out.println(rs.getString("item") + " in user list");
			}
			conn.close();
			return new DefaultTableModel(data, columnNames);
		} catch (SQLException e) {
			e.getMessage();
		}
		return mdl;
	}

	public static DefaultTableModel sharedTable(String user) {
		Connection conn = null;
		Statement stmt = null;
		DefaultTableModel mdl = null;
		String showItems = "select * from shareList where shareWith ='" + user + "';";
		try {
			mdl = new DefaultTableModel();
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(showItems);
			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
		    Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();

		    columnNames.add("item");
		    columnNames.add("user");
		    columnNames.add("bought");
//		    for (int column = 1; column <= columnCount; column++) {
//	        columnNames.add(metaData.getColumnName(column));
//	    }
		    
		    // data of rows
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>();
				row.add(rs.getString("item"));
				row.add(rs.getString("user"));
				row.add(rs.getBoolean("bought"));
				//vector.add(rs.getObject(i));
				data.add(row);
				System.out.println(rs.getString("item") + " shared by user: " + rs.getString("user") + ", bought: " + rs.getInt("bought"));
			}
			conn.close();
			return new DefaultTableModel(data, columnNames);
		} catch (SQLException e) {
			e.getMessage();
		}
		return mdl;
	}

	public static DefaultTableModel updateMyTable(String user) {
		Connection conn = null;
		Statement stmt = null;
		DefaultTableModel mdl = null;
		String showItems = "select * from wishList where user ='" + user + "';";
		try {
			mdl = new DefaultTableModel();
			String url = "jdbc:sqlite:test.db";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(showItems);
			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
		    Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();
		    columnNames.add("item");
		    // data of rows
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>();
				row.add(rs.getString("item"));
				data.add(row);
			}
			conn.close();
			return new DefaultTableModel(data, columnNames);
		} catch (SQLException e) {
			e.getMessage();
		}
		return mdl;
	}
}
