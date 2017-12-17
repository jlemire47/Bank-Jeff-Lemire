package com.pkg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <h1>Bank Program</h1>
 * 
 * @author Jeff Lemire
 * @version 1.0
 * @since 2017-12-11
 */

public class Process {

	static String fileName = "C:\\log.html";
	static Document doc = null;

	/**
	 * This is the main method
	 * 
	 * @param String
	 *            [] args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		ArrayList<String> transactions = new ArrayList<String>();

		// Read log/html file - get transactions
		doc = Jsoup.parse(new File(fileName), "utf-8");
		// Get table
		Elements auditLog = doc.select("table#transactions");
		// Get rows and data
		Elements rows = auditLog.select("td");
		for (Element row : rows) {
			Elements rowItems = row.select("td");
			for (Element rowItem : rowItems) {
				transactions.add(rowItem.text());
			}
		}

		int numberOfTrx = transactions.size();

		// Process Input
		String command = "";
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);

		while (!command.equalsIgnoreCase("Exit")) {

			// Initial Prompt
			System.out.println("");
			System.out
					.println("Please enter a command (Deposit, Withdrawl, Balance, or Exit).");
			command = input.next();

			// Handle Deposit logic
			if (command.equalsIgnoreCase("Deposit")) {
				System.out.println("");
				System.out
						.println("Please enter a deposit amount, (format is X,XXX.XX).");
				String deposit = input.next();
				if (validateNumber(deposit)) {
					processDeposit(deposit, transactions);
					System.out.println("");
					System.out.println("Thank you.");
				}

				// Handle Withdrawl logic
			} else if (command.equalsIgnoreCase("Withdrawl")) {
				System.out.println("");
				System.out
						.println("Please enter a withdrawl amount, (format is X,XXX.XX).");
				String withdrawl = input.next();
				if (validateNumber(withdrawl)) {
					processWithdrawl(withdrawl, transactions);
					System.out.println("");
					System.out.println("Thank you.");
				}

				// Handle Balance logic
			} else if (command.equalsIgnoreCase("Balance")) {
				String balance = getBalance(transactions);
				System.out.println("");
				System.out.println("Your balance is:  " + balance);

				// Handle Exit logic
			} else if (command.equalsIgnoreCase("Exit")) {
				updateHTML(transactions, numberOfTrx);
				System.out.println("");
				System.out.println("Good Bye and thank you.");
				break;

				// Re-prompt if NOT Deposit, Withdrawl or Balance
			} else {
				System.out.println("");
				System.out
						.println("Your command is invalid (Deposit, Withdrawl, Balance, or Exit).");
			}
		}

	}

	/**
	 * This is the processWithdrawl method
	 * 
	 * @param String
	 *            withdrawl
	 * @param ArrayList
	 *            <String> transactions
	 */
	private static void processWithdrawl(String withdrawl,
			ArrayList<String> transactions) {
		
		transactions.add((String.valueOf(Double.valueOf(withdrawl)*-1)));

	}

	/**
	 * This is the processDeposit method
	 * 
	 * @param ArrayList
	 *            <String> transactions
	 * 
	 */
	private static void processDeposit(String deposit,
			ArrayList<String> transactions) {
		transactions.add(deposit);
	}

	/**
	 * This is the getBalance method
	 * 
	 * @param ArrayList
	 *            <String> transactions
	 * 
	 */
	private static String getBalance(ArrayList<String> transactions) {

		Double balance = 0.00;
		String rtnValue = "";
	 
		for (int j = 0; j < transactions.size(); j++) {
			balance = balance + Double.parseDouble(transactions.get(j));
		}
		
		rtnValue = String.valueOf(balance).trim();
		
		if (rtnValue.indexOf(".")!=rtnValue.length()-3) {	
				rtnValue = rtnValue +"0";			
	    }
	
		return rtnValue;
	}

	/**
	 * This is the getBalance method
	 * 
	 * @param String
	 * @return boolean
	 * 
	 */
	private static boolean validateNumber(String in) {

		if (in.matches("^\\d+\\.\\d{2}")) {
			return true;
		}

		System.out.println("");
		System.out
				.println("Your amount is invalid (format is X,XXX.XX), Thanks.");

		return false;

	}

	/**
	 * This is the updateHTML method
	 * 
	 * @throws IOException
	 * 
	 */
	private static void updateHTML(ArrayList<String> transactions,
			int numberOfTrx) throws IOException {

		FileWriter fileWriter = new FileWriter(fileName);

		final int offSet = 5;
		String newRows = "";
		String htmlString = doc.toString();
		int positionOfLastTabRow = htmlString.lastIndexOf("</tr>");

		for (int j = numberOfTrx; j < transactions.size(); j++) {
			newRows = newRows + "<tr><td>" + transactions.get(j) + "</td></tr>";
		}

		htmlString = htmlString.substring(0, positionOfLastTabRow + offSet)
				+ newRows
				+ htmlString.substring(positionOfLastTabRow + offSet,
						htmlString.length());

		fileWriter.write(htmlString);
		fileWriter.flush();
		// Close file after write operation.
		fileWriter.close();

	}

}
