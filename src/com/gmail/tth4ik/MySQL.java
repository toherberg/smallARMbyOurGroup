package com.gmail.tth4ik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	private Connection con;
	private int currentId; // ����� ��� ������ � �� ��������� �� �����, ���
							// ������� ���� �������� �� ������ ����� ���
							// �������� �����

	/**
	 * ����������� ��. ������� 2 ������� ��� ����������� �� ���, ���� ����
	 * ��� �������. ������� ���� � ������� ������ � ���������� ������.
	 * ������� ��'���� �������� "�� �����".
	 */
	public void initialization(String name) {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + name);
			PreparedStatement st = con.prepareStatement(
					"create table if not exists 'Groups' ('groupID' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'info' text);");
			int result = st.executeUpdate();
			PreparedStatement st1 = con.prepareStatement(
					"create table if not exists 'Products' ('groupID' INTEGER REFERENCES Groups(groupID), 'name' text, 'info' text, 'manufacturer' text, 'quantity' REAL, 'price' REAL);");
			int result1 = st1.executeUpdate();
		} catch (ClassNotFoundException e) {
			System.out.println("�� ������� �������");
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			System.out.println("������������ �����");
			e.printStackTrace();
		}
	}

	/** ����� ��� �������� ���� ����� � ���� ����� */
	public void insertGroupData(String name, String info) {
		try {
			PreparedStatement statement = con.prepareStatement(
					"INSERT INTO Groups(name,info) VALUES ('" + name.toUpperCase() + "','" + info.toUpperCase() + "')");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �������");
			e.printStackTrace();
		}
	}

	/** �����, ���� ������� ���� ����� */
	private void deleteGroup(String name) {
		try {
			PreparedStatement statement = con
					.prepareStatement("DELETE FROM Groups where groupID = '" + getGroupID(name) + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ���������");
			e.printStackTrace();
		}
	}

	/** �����, ���� ������� ����� � ������ � �� */
	public void deleteGroupAndProducts(String name) {
		deleteGroup(name);
		deleteProductByID(getGroupID(name));
	}

	/**
	 * ����� ������� �� ����� �����. ³� ������� ��� ��� ����, ��� ��������
	 * ��� �������� ����� � �� ������, �� � ��� ����������
	 */
	public int getGroupID(String name) {
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT groupID FROM Groups where name ='" + name.toUpperCase() + "';");
			while (res.next()) {
				currentId = res.getInt("groupID");
				System.out.println();
			}
			res.close();
			st.close();

		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return currentId;
	}

	/** ��������� ���������� � ����� ����� */
	public void updateGroupData(String groupName, String newName, String info) {
		updateGroupInfo(groupName, info);
		updateGroupName(groupName, newName);
	}

	/** ����� �� ��������� ������� ���������� ��� ����� */
	public void updateGroupInfo(String groupName, String info) {
		if (info.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Groups set info = '" + info.toUpperCase()
					+ "' where name = '" + groupName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** ����� �������� ������ ��'� ����� */
	public void updateGroupName(String groupName, String newName) {
		if (newName.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Groups set name = '" + newName.toUpperCase()
					+ "' where name = '" + groupName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** ����� ������ ���������� ��� �� ����� ����� */
	public String getGroupsData() {
		String groupdata = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Groups");
			while (res.next()) {
				String name = res.getString("name");
				String info = res.getString("info");
				groupdata += res.getShort("groupID") + ";" + name + ";" + info + "\n";
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return groupdata;
	}

	public String getGroupNameByID(int groupID) {
		String groupName = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT name FROM Groups WHERE groupID = '" + groupID + "'");
			while (res.next()) {
				String name = res.getString("name");
				groupName += name;
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return groupName;
	}

	public String getGroupNames() {
		String groupNames = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT name FROM Groups");
			while (res.next()) {
				String name = res.getString("name");
				groupNames += name + ";";
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return groupNames;
	}

	/**
	 * ������� � �� ����� �������, �� �� ������� ����, ������� ����� �����
	 * � ������� ��������
	 */
	public void insertProductData(int groupID, String name, String info, String manufacturer, int quantity,
			double price) {
		try {
			PreparedStatement statement = con
					.prepareStatement("INSERT INTO Products(groupID,name,info,manufacturer,quantity,price) VALUES ('"
							+ groupID + "','" + name.toUpperCase() + "','" + info.toUpperCase() + "','"
							+ manufacturer.toUpperCase() + "','" + quantity + "','" + price + "')");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �������");
			e.printStackTrace();
		}
	}

	/** ������� ������� ������� �� ������ */
	public void deleteProduct(String name) {
		try {
			PreparedStatement statement = con
					.prepareStatement("DELETE FROM Products where name = '" + name.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ���������");
			e.printStackTrace();
		}
	}

	/**
	 * ������� �� �������� � ������ �� �����. ����� ��������������� ���
	 * �������� ��� �������� �������� �����
	 */
	private void deleteProductByID(int id) {
		try {
			PreparedStatement statement = con
					.prepareStatement("DELETE FROM Products WHERE groupID = '" + id + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ���������");
			e.printStackTrace();
		}
	}

	/** �������� ���������� ����� �������� */
	public void updateProductName(String productName, String newName) {
		if (newName.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set name = '" + newName.toUpperCase()
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** �������� ���������� ���������� ��� ������� */
	public void updateProductInfo(String productName, String info) {
		if (info.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set info = '" + info.toUpperCase()
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** �������� ���������� ���������� ��� ��������� */
	public void updateProductManufacturer(String productName, String manufacturer) {
		if (manufacturer.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set manufacturer = '"
					+ manufacturer.toUpperCase() + "'where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** �������� ���������� ���� �������� */
	public void updateProductPrice(String productName, double price) {
		if (price == -1)
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set price = '" + price
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** ����������� �񳺿 ���������� ���������, ����� � ��� 4 ������ */
	public void editAllProductInfo(String productName, String newName, String newInfo, String newManufacturer,
			double newPrice) {
		updateProductInfo(productName, newInfo);
		updateProductManufacturer(productName, newManufacturer);
		updateProductPrice(productName, newPrice);
		updateProductName(productName, newName);
	}

	/**
	 * �������� ���������� ������� ��������. ������������� ���� ���
	 * ��������� ���������/�������� ��������
	 */
	public void updateProductQuantity(String productName, int quantity) {
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set quantity = '" + quantity
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� �����������");
			e.printStackTrace();
		}
	}

	/** ������ ���������� ��� �� ��������, �� �� �� */
	public String getProductData() {
		String productData = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products");
			while (res.next()) {
				String name = res.getString("name");
				String info = res.getString("info");
				String manufacturer = res.getString("manufacturer");
				int quantity = res.getInt("quantity");
				double price = res.getDouble("price");
				productData += res.getShort("groupID") + ";" + name + ";" + info + ";" + manufacturer + ";" + quantity
						+ ";" + price + "\n";
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return productData;
	}

	public String getProductNames() {
		String productNames = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT name FROM Products");
			while (res.next()) {
				String name = res.getString("name");
				productNames += name + ";";
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return productNames;
	}

	/** ������ ���������� ��� �������� ����� ����� */
	public String getGroupProductData(int groupID) {
		String groupProductsData = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products WHERE groupID = '" + groupID + "';");
			while (res.next()) {
				String name = res.getString("name");
				String info = res.getString("info");
				String manufacturer = res.getString("manufacturer");
				int quantity = res.getInt("quantity");
				double price = res.getDouble("price");
				groupProductsData += name + ";" + info + ";" + manufacturer + ";" + quantity + ";" + price + "\n";
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return groupProductsData;
	}
	
	/** ������ ������ � ������� �������� ����� ����� */
	public String getGroupProductNames(int groupID) {
		String groupProductsNames = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products WHERE groupID = '" + groupID + "';");
			while (res.next()) {
				String name = res.getString("name");
				groupProductsNames += name + ";";
			}
			
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return groupProductsNames;
	}

	public String getGroupProductReport(int groupID) {
		String groupProductsData = "";
		double totalcost = 0;
		int counter = 1; // ��� ������� �������
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products WHERE groupID = '" + groupID + "';");
			while (res.next()) {
				String name = res.getString("name");
				String info = res.getString("info");
				String manufacturer = res.getString("manufacturer");
				int quantity = res.getInt("quantity");
				double price = res.getDouble("price");
				groupProductsData += counter++ + ". Product name: " + name + "; Info: " + info + "; Manufacturer: "
						+ manufacturer + "; Current quantity: " + quantity + "; Price: " + price + "\n";
				totalcost += price * quantity;
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		groupProductsData += "Total cost of product in group: " + totalcost;
		return groupProductsData;
	}

	public String getProductReport() {
		String reportText = "FULL REPORT ABOUT ALL PRODUCTS \n";
		int counter = 1; // ��� ������� �������� ������
		double totalcost = 0;
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products");
			while (res.next()) {
				String name = res.getString("name");
				String info = res.getString("info");
				String manufacturer = res.getString("manufacturer");
				int quantity = res.getInt("quantity");
				double price = res.getDouble("price");
				reportText += counter++ + ". Product name: " + name + "; Info: " + info + "; Manufacturer: "
						+ manufacturer + "; Current quantity: " + quantity + "; Price: " + price + "; Group: "
						+ getGroupNameByID(res.getInt("groupID")) + "\n";
				totalcost += price * quantity;
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		reportText += "Total cost of products: " + totalcost;
		return reportText;
	}

	public int getQuantityByName(String name) {
		int resquantity = 0;
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products WHERE name LIKE '" + name.toUpperCase() + "';");
			while (res.next()) {
				int quantity = res.getInt("quantity");
				resquantity = quantity;
			}

		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return resquantity;
	}

	/** ��������� ������� � �� �� ������ � ������ ��� ����� ���������� */
	public String searchProductByName(String name) {
		String product = "";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM Products WHERE name = '" + name.toUpperCase() + "';");
			if (res == null){
				product = "false";
				return product;
			}
				
			while (res.next()) {
				String realName = res.getString("name");
				int groupID = res.getInt("groupID");
				String info = res.getString("info");
				String manufacturer = res.getString("manufacturer");
				int quantity = res.getInt("quantity");
				double price = res.getDouble("price");
				product += groupID + ";" + realName + ";" + info + ";" + manufacturer + ";" + quantity + ";" + price;
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("�� ����� SQL ����� �� ������ �����");
			e.printStackTrace();
		}
		return product;
	}

}

