package com.gmail.tth4ik;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Тестовий клієнт, створений для тестування роботи сервера з базою даних.
 * Зчитує з клавіатури команди - надсилає їх серверу та отримує від нього
 * відповідні повідомлення
 */

public class ARMClient {
	
	String fromuser = "";
	String fromserver = "";
	DataOutputStream dos;
	DataInputStream dis;
	
	public ARMClient() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 8315);
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
	}
	
	public boolean sendMessageToServer(String s) throws IOException{
		fromuser = s;
		dos.writeUTF(fromuser);
		fromserver = dis.readUTF();
		if (fromserver.equals("true")){
			return true;
		}
		return false;
	}
	

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Welcome to Client!");
		Socket socket = new Socket("localhost", 8315);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		BufferedReader inu = new BufferedReader(new InputStreamReader(System.in));
		String fromuser;
		String fromserver = "";
		while (true) {
			System.out.println("Enter your command: ");
			fromuser = inu.readLine();
			dos.writeUTF(fromuser);
			fromserver = dis.readUTF();
			switch (fromserver) {
			case "end":
				System.out.println("received command to end process");
				return;
			default:
				System.out.println(fromserver);
				break;
			}

		}

	}

}
