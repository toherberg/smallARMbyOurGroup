package com.gmail.tth4ik;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ARMServer implements Runnable {

	Socket connection;
	String input = "";
	int connected;
	MySQL sql;
	DataOutputStream dos;
	DataInputStream dis;
	String[] productNames;
	String[] groupNames;

	public ARMServer(Socket socket) {
		this.connection = socket;
		sql = new MySQL();
		sql.initialization("ARMDB");

	}

	public static void main(String[] args) throws IOException {
		System.out.println("Server was started!");
		ServerSocket servers = null;
		try {
			servers = new ServerSocket(8315);
		} catch (IOException e) {
			System.out.println("Can't connect to port");
			System.exit(-1);
		}
		/** Чекаємо підключення клієнта до сервера */
		while (true) {
			try {
				System.out.println("Waiting for a client...");
				Socket connection = servers.accept();
				Runnable runnable = new ARMServer(connection);
				Thread thread = new Thread(runnable);
				thread.start();
				System.out.println("Client connected");
			} catch (IOException e) {
				System.out.println("");
				System.exit(-1);
			}
		}
	}

	@Override
	public void run() {
		try {
			dos = new DataOutputStream(connection.getOutputStream());
			dis = new DataInputStream(connection.getInputStream());
			groupNames = sql.getGroupNames().split("§");
			productNames = sql.getProductNames().split("§");
			while (true) {
				if (input.equalsIgnoreCase("end")) {
					System.out.println("Client disconnected");
					return;
				}
				input = dis.readUTF();
				switch (input) {
				case "addgroup":
					dos.writeUTF("adding group");
					addGroup();
					continue;
				case "addproduct":
					dos.writeUTF("true");
					addProduct();
					continue;
				case "groupnames":
					dos.writeUTF("true");
					sendGroupNames();
					continue;
				case "groupnames1":
					dos.writeUTF("true");
					String s = dis.readUTF();
					System.out.println(s);
					dos.writeUTF(sql.getGroupProductNames(sql.getGroupID(s)));
					continue;
				case "editgroup":
					dos.writeUTF("editing group");
					editGroup();
					continue;
				case "editproduct":
					dos.writeUTF("editing product");
					editProduct();
					continue;
				case "deletegroup":
					dos.writeUTF("deletinggroup");
					deleteGroup();
					continue;
				case "deleteproduct":
					dos.writeUTF("deleting product");
					deleteProduct();
					continue;
				case "search":
					dos.writeUTF("searching");
					searchProduct();
					continue;
				case "changequantity":
					dos.writeUTF("changing quantity");
					changeQuantity();
					continue;
				case "groupreport":
					dos.writeUTF("true");
					makeGroupReport();
					continue;
				case "fullreport":
					dos.writeUTF(sql.getProductReport());
					continue;
				case "end":
					dos.writeUTF("end");
					break;
				default:
					dos.writeUTF("wrong code");
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendGroupNames() throws IOException {
		System.out.println(dis.readUTF());
		dos.writeUTF(sql.getGroupNames());
	}

	private void makeGroupReport() {
		String report = "";
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			report += "Group name: " + input.toUpperCase() + "\n";
			report += sql.getGroupProductReport(sql.getGroupID(input));
			dos.writeUTF(report);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void changeQuantity() {
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			String[] array = input.split("§");
			int currentquantity = sql.getQuantityByName(array[0]);
			if (array[1].charAt(0) == '+') {
				sql.updateProductQuantity(array[0], currentquantity + Integer.parseInt(array[1].substring(1)));
				dos.writeUTF("Sucessfully added product");
				return;
			}
			if (array[1].charAt(0) == '-') {
				if (currentquantity - Integer.parseInt(array[1].substring(1)) >= 0) {
					sql.updateProductQuantity(array[0], currentquantity - Integer.parseInt(array[1].substring(1)));
					dos.writeUTF("Sucessfully sold product");
					return;
				}
				dos.writeUTF("Can't change quantity,too much to delete");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void searchProduct() {
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			if (sql.searchProductByName(input).equalsIgnoreCase("false")){
			dos.writeUTF(input);
			return;
			}
			dos.writeUTF(sql.searchProductByName(input));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void editGroup() {
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			String[] array = input.split("§");
			if(isFreeName(array[1], groupNames)==false){
				array[1]=" "; // щоб відредагувало усе, крім імені, якщо воно зайняте
			}
			System.out.println(array.length);
			sql.updateGroupData(array[0], array[1], array[2]);
			dos.writeUTF("editing successfully ended");
			groupNames = sql.getGroupNames().split("§");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void editProduct() {
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			String[] array = input.split("§");
			if (isWhiteSpace(array[1])&&(!array[1].equalsIgnoreCase(" "))){
				input = "";
				dos.writeUTF("Can't use white space to rename");
				return;
			}
			if(isFreeName(array[1], productNames)==false){
				input = "";
				dos.writeUTF("Name is used");
				return;
			}
				sql.editAllProductInfo(array[0],array[1],array[2],array[3],Double.parseDouble(array[4]));
			dos.writeUTF("editing successfully ended");
			productNames = sql.getProductNames().split("§");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteGroup() {
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			sql.deleteGroupAndProducts(input);
			dos.writeUTF("Group with number " + sql.getGroupID(input) + " named " + input
					+ " with all products was sucessfully deleted from BD");
			productNames = sql.getProductNames().split("§");
			groupNames = sql.getGroupNames().split("§");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteProduct() {
		try {
			input = dis.readUTF();
			if (input.equalsIgnoreCase("end1")) {
				dos.writeUTF("process ended");
				return;
			}
			sql.deleteProduct(input);
			dos.writeUTF("Product named " + input + " was sucessfully deleted from BD");
			productNames = sql.getProductNames().split("§");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private void addGroup() {
			try {
				input = dis.readUTF();
				System.out.println(input);
				if (input.equalsIgnoreCase("end1")) {
					dos.writeUTF("process ended");
					return;
				}
				System.out.println(input);
				String[] array = input.split("§");
				if (isWhiteSpace(array[0])){
					input = "";
					dos.writeUTF("Can't create group with empty name");
					return;
				}
				if (isFreeName(array[0], groupNames) == false) {
					input = "";
					dos.writeUTF("Name is used, try to add it once more");
					return;
				}
				sql.insertGroupData(array[0], array[1]);
				dos.writeUTF("Group with name: " + array[0] + " and info: " + array[1] + " successfully added to DB");
				groupNames = sql.getGroupNames().split("§");
				return;

			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

	private boolean isFreeName(String string, String[] names) {
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase(string))
				return false;
		}
		return true;
	}
	
	private boolean isWhiteSpace(String s){
		char [] chrr = s.toCharArray();
		int chrrlength = chrr.length;
		int whiteSpaceChars = 0;
		for (char c : chrr){
			if (Character.isWhitespace(c))
				whiteSpaceChars++;
		}
		if (whiteSpaceChars == chrrlength)
			return true;
		return false;
	}
	

	private void addProduct() {
		while (true) {
			try {
				input = dis.readUTF();
				if (input.equalsIgnoreCase("end1")) {
					dos.writeUTF("process ended");
					return;
				}
				String[] array = input.split("§");
				if (array[1].isEmpty()||(isWhiteSpace(array[1]))){
					input = "";
					dos.writeUTF("Can't create product with empty name");
					continue;
				}
				if (isFreeName(array[1], productNames) == false) {
					input = "";
					dos.writeUTF("Name is used, try to add it once more");
					continue;
				}
				int groupID = sql.getGroupID(array[0]);
				sql.insertProductData(groupID, array[1], array[2], array[3],
						Integer.parseInt(array[4]), Double.parseDouble(array[5]));
				dos.writeUTF("Product from group № " + groupID + " named " + array[1] + " with info: " + array[2]
						+ " which manufatured by: " + array[3] + ", it's quantity: " + array[4] + " and price: "
						+ array[5] + " was added to product list");
				productNames = sql.getProductNames().split("§");
				return;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
