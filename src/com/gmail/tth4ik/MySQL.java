package com.gmail.tth4ik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	private Connection con;
	private int currentId; // змінна для запису у неї поточного ІД групи, щоб
							// зручніше було видаляти усі товари групи при
							// видаленні групи

	/**
	 * Ініціалізація БД. Створює 2 таблиці або підключається до них, якщо вони
	 * вже існують. Таблиця груп і таблиця товарів з відповідними полями.
	 * Таблиці зв'язані колонкою "ІД групи".
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
			System.out.println("Не знайшли драйвер");
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			System.out.println("Неправильний запит");
			e.printStackTrace();
		}
	}

	/** Метод для внесення нової групи у базу даних */
	public void insertGroupData(String name, String info) {
		try {
			PreparedStatement statement = con.prepareStatement(
					"INSERT INTO Groups(name,info) VALUES ('" + name.toUpperCase() + "','" + info.toUpperCase() + "')");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на вставку");
			e.printStackTrace();
		}
	}

	/** метод, який видаляє лише групу */
	private void deleteGroup(String name) {
		try {
			PreparedStatement statement = con
					.prepareStatement("DELETE FROM Groups where groupID = '" + getGroupID(name) + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на видалення");
			e.printStackTrace();
		}
	}

	/** метод, який видаляє групу і товари з неї */
	public void deleteGroupAndProducts(String name) {
		deleteGroup(name);
		deleteProductByID(getGroupID(name));
	}

	/**
	 * метод повертає ІД номер групи. Він потрібен нам для того, аби видаляти
	 * при видаленні групи і усі товари, які з нею асоційовані
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return currentId;
	}

	/** оновлення інформації і назви групи */
	public void updateGroupData(String groupName, String newName, String info) {
		updateGroupInfo(groupName, info);
		updateGroupName(groupName, newName);
	}

	/** метод дає можливість оновити інформацію про групу */
	public void updateGroupInfo(String groupName, String info) {
		if (info.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Groups set info = '" + info.toUpperCase()
					+ "' where name = '" + groupName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** метод дозволяє змінити ім'я групи */
	public void updateGroupName(String groupName, String newName) {
		if (newName.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Groups set name = '" + newName.toUpperCase()
					+ "' where name = '" + groupName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** метод генерує інформацію про усі наявні групи */
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
			System.out.println("Не вірний SQL запит на вибірку даних");
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
			System.out.println("Не вірний SQL запит на вибірку даних");
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return groupNames;
	}

	/**
	 * Вносить у БД новий продукт, має усі відповідні поля, створює новий рядок
	 * у таблиці продуктів
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
			System.out.println("Не вірний SQL запит на вставку");
			e.printStackTrace();
		}
	}

	/** Видаляє окремий продукт за іменем */
	public void deleteProduct(String name) {
		try {
			PreparedStatement statement = con
					.prepareStatement("DELETE FROM Products where name = '" + name.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на видалення");
			e.printStackTrace();
		}
	}

	/**
	 * Видаляє усі продукти з певним ІД групи. Метод використовується при
	 * видаленні усіх продуктів відповідної групи
	 */
	private void deleteProductByID(int id) {
		try {
			PreparedStatement statement = con
					.prepareStatement("DELETE FROM Products WHERE groupID = '" + id + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на видалення");
			e.printStackTrace();
		}
	}

	/** Дозволяє редагувати назву продукта */
	public void updateProductName(String productName, String newName) {
		if (newName.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set name = '" + newName.toUpperCase()
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** Дозволяє редагувати інформацію про продукт */
	public void updateProductInfo(String productName, String info) {
		if (info.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set info = '" + info.toUpperCase()
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** дозволяє редагувати інформацію про виробника */
	public void updateProductManufacturer(String productName, String manufacturer) {
		if (manufacturer.equals(" "))
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set manufacturer = '"
					+ manufacturer.toUpperCase() + "'where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** Дозволяє редагувати ціну продукта */
	public void updateProductPrice(String productName, double price) {
		if (price == -1)
			return;
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set price = '" + price
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** Редагування усієї інформації одночасно, поєднує у собі 4 методи */
	public void editAllProductInfo(String productName, String newName, String newInfo, String newManufacturer,
			double newPrice) {
		updateProductInfo(productName, newInfo);
		updateProductManufacturer(productName, newManufacturer);
		updateProductPrice(productName, newPrice);
		updateProductName(productName, newName);
	}

	/**
	 * Дозволяє редагувати кількість продуктів. Використовуємо його для
	 * реалізації додавання/списання продуктів
	 */
	public void updateProductQuantity(String productName, int quantity) {
		try {
			PreparedStatement statement = con.prepareStatement("UPDATE Products set quantity = '" + quantity
					+ "' where name = '" + productName.toUpperCase() + "'; commit work;");
			int result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Не вірний SQL запит на редагування");
			e.printStackTrace();
		}
	}

	/** Генерує інформацію про усі продукти, які єу БД */
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
			System.out.println("Не вірний SQL запит на вибірку даних");
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return productNames;
	}

	/** Генерує інформацію про продукти певної групи */
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return groupProductsData;
	}
	
	/** Генерує стрічку з іменами продуктів певної групи */
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return groupProductsNames;
	}

	public String getGroupProductReport(int groupID) {
		String groupProductsData = "";
		double totalcost = 0;
		int counter = 1; // для красоти репорта
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		groupProductsData += "Total cost of product in group: " + totalcost;
		return groupProductsData;
	}

	public String getProductReport() {
		String reportText = "FULL REPORT ABOUT ALL PRODUCTS \n";
		int counter = 1; // щоб красиво виглядав список
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
			System.out.println("Не вірний SQL запит на вибірку даних");
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return resquantity;
	}

	/** Знаходить продукт у БД за іменем і генерує про нього інформацію */
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
			System.out.println("Не вірний SQL запит на вибірку даних");
			e.printStackTrace();
		}
		return product;
	}

}

