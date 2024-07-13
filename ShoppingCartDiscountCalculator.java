package com.shop.cart;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * The ShoppingCartDiscountCalculator program is used to simply calculate
 * Discount and Saved Amount and Prints the output on the screen as per user
 * input.
 * 
 * @author Hariharan Jayakumar
 * @version 1.0
 * @since 2024-07-13
 */

interface ShoppingCartInterface {
	public static final double book_dis_price = 0.05;
	public static final double cloth_dis_price = 0.20;
	public static final double other_dis_price = 0.03;
	public static final double clearance_dis_price = 0.20;

	@SuppressWarnings("serial")
	static List<String> foodItemList = Collections.unmodifiableList(new ArrayList<String>() {
		{
			add("drink");
			add("milk");
			add("food");
			add("water");
			add("choco");
			add("cake");
			add("wine");
		}
	});

	@SuppressWarnings("serial")
	static List<String> clothItemList = Collections.unmodifiableList(new ArrayList<String>() {
		{
			add("shirt");
			add("pant");
			add("wear");
			add("coat");
			add("suit");
			add("vesti");
			add("saree");
			add("kurti");
			add("kurta");
			add("kerchief");
			add("towel");
			add("cloth");
			add("dress");
			add("baniyan");
			add("frock");
		}
	});

	ArrayList<String> inputItemList = new ArrayList<String>();
	List<ShoppingBasket> shoppingBasketInputList = new ArrayList<ShoppingBasket>();
	List<ShoppingBasket> shoppingBasketOutputList = new ArrayList<ShoppingBasket>();

	void getUserInput() throws IOException;

	boolean loadShoppingBasketList();

	void calculateDiscountAndSavedPrice();

	void displayShoppingCart();
}

class ShoppingCart implements ShoppingCartInterface {

	double totalDiscountedAmount = 0.00;
	double totalActualPrice = 0.00;
	double totalSavedAmount = 0.00;
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	/**
	 * This method implements using ShoppingCartInterface to get user input and
	 * store it in inputItemList
	 */
	@Override
	public void getUserInput() throws IOException {
		Scanner sc = new Scanner(System.in);
		int totalNumofItems = 0;
		try {
			totalNumofItems = sc.nextInt();
			if (!validateInput(totalNumofItems)) {
				System.out.println("Please enter valid input!");
			} else {
				for (int i = 0; i < totalNumofItems; i++) {
					sc = new Scanner(System.in);
					String inputString = sc.nextLine();
					inputItemList.add(inputString);
				}
			}
			sc.close();
		} catch (Exception ex) {
			System.out.println("Please enter valid input!");
		}

	}

	/**
	 * This method implements using ShoppingCartInterface to calculate discount and
	 * saved price and store it in shoppingBasketOutputList
	 */
	@Override
	public void calculateDiscountAndSavedPrice() {
		List<ShoppingBasket> shoppingBasketListFinal = new ArrayList<ShoppingBasket>();
		ShoppingBasket shoppingBasket = null;
		for (final ShoppingBasket shoppingBasketItem : shoppingBasketInputList) {
			shoppingBasket = new ShoppingBasket();
			int noOfQuantity = shoppingBasketItem.getNoOfQuantity();
			String item = shoppingBasketItem.getItem();
			double price = shoppingBasketItem.getPrice();
			double discountAmount = price - calulateDiscountPrice(item, price);

			// check if item is clearance
			double clearanceDisAmount = 0.00;
			if (isClearance(item)) {
				clearanceDisAmount = calulateClearanceDiscountPrice(item, discountAmount);
				discountAmount = discountAmount - clearanceDisAmount;
			}

			double discountAmountWithQuantity = Double.valueOf(decimalFormat.format(discountAmount * noOfQuantity));
			double actualAmountWithQuantity = Double.valueOf(decimalFormat.format(price * noOfQuantity));
			shoppingBasket.setItem(item);
			shoppingBasket.setNoOfQuantity(noOfQuantity);
			shoppingBasket.setPrice(discountAmountWithQuantity);
			shoppingBasketListFinal.add(shoppingBasket);
			totalDiscountedAmount = totalDiscountedAmount + discountAmountWithQuantity;
			totalActualPrice = totalActualPrice + actualAmountWithQuantity;
		}
		shoppingBasketOutputList.addAll(shoppingBasketListFinal);
	}

	/**
	 * This method implements using ShoppingCartInterface to display the output
	 * using shoppingBasketOutputList class.
	 */
	@Override
	public void displayShoppingCart() {
		for (final ShoppingBasket shoppingBasketItem : shoppingBasketOutputList) {
			System.out.println(shoppingBasketItem.getNoOfQuantity() + " " + shoppingBasketItem.getItem() + " "
					+ String.format("%.2f", shoppingBasketItem.getPrice()));
		}
		System.out.println("Total: " + String.format("%.2f", totalDiscountedAmount));
		System.out.println("You saved: " + String.format("%.2f", totalSavedAmount));

	}

	/**
	 * This method implements using ShoppingCartInterface to fetching input from
	 * inputItemList and store it in shoppingBasketInputList
	 */
	@Override
	public boolean loadShoppingBasketList() {
		List<ShoppingBasket> shoppingBasketList = new ArrayList<ShoppingBasket>();
		ShoppingBasket shoppingBasket = null;
		for (String inputItem : inputItemList) {
			shoppingBasket = new ShoppingBasket();

			int atFirstIndex = 0;
			int noOfQuantity = 0;
			int atLastIndex = 0;
			double price = 0.00;
			String item = "";
			try {
				atFirstIndex = inputItem.indexOf(" ");
				noOfQuantity = Integer.valueOf(inputItem.substring(0, atFirstIndex));

				if (inputItem.contains(" at ")) {
					atLastIndex = inputItem.lastIndexOf(" at ") + 4;

				} else {
					atLastIndex = inputItem.lastIndexOf(" ") + 1;
				}

				price = Double.valueOf(inputItem.substring(atLastIndex, inputItem.length()));

				if (!validateInput(price)) {
					shoppingBasketList = new ArrayList<ShoppingBasket>();
					System.out.println("Please enter valid input!");
					return false;
				}

				if (!validateInput(noOfQuantity)) {
					shoppingBasketList = new ArrayList<ShoppingBasket>();
					System.out.println("Please enter valid input!");
					return false;
				}

				item = inputItem.substring(atFirstIndex + 1, atLastIndex - 1);
				shoppingBasket.setItem(item);
				shoppingBasket.setNoOfQuantity(noOfQuantity);
				shoppingBasket.setPrice(price);
				shoppingBasketList.add(shoppingBasket);
			} catch (Exception ex) {
				System.out.println("Please enter valid input!");
			}
		}
		shoppingBasketInputList.addAll(shoppingBasketList);
		return true;
	}

	/*
	 * Validate the input is zero or negative
	 * 
	 * @input type as int
	 * 
	 * @return boolean true or false
	 */
	private boolean validateInput(int input) {
		if (input == 0 || input < 0) {
			return false;
		}
		return true;
	}

	/*
	 * Validate the input is zero or negative
	 * 
	 * @input type as double
	 * 
	 * @return boolean true or false
	 */
	private boolean validateInput(double input) {
		if (input == 0 || input == 0.0 || input < 0) {
			return false;
		}
		return true;
	}

	/*
	 * Calculate Discount Price for Clearance Items
	 */
	private static double calulateClearanceDiscountPrice(String item, double price) {
		double discountedPrice = getClearanceDiscountPriceByItem(item);
		double discountAmount = price * discountedPrice;
		return discountAmount;
	}

	/*
	 * Validate the item is Clearance
	 * 
	 * @return boolean true or false
	 */
	private static boolean isClearance(String item) {
		String itemLowerCase = item.toLowerCase();
		if (itemLowerCase.contains("clearance")) {
			return true;
		}
		return false;
	}

	/*
	 * Calculate Discount Price for Books, Food Items ,Drinks, Clothes and Other
	 * Items
	 */
	private static double calulateDiscountPrice(String item, double price) {
		double discountedPrice = getDiscountPriceByItem(item);
		double discountAmount = price * discountedPrice;
		return discountAmount;

	}

	/*
	 * Fetching Discount Price for Item added in cart
	 */
	private static double getDiscountPriceByItem(String item) {
		Optional<String> itemStr = Optional.ofNullable(item);
		if (itemStr.isPresent()) {
			String itemLowerCase = itemStr.get().toLowerCase();
			for (String foodItem : foodItemList) {
				if (itemLowerCase.contains("book") || itemLowerCase.contains(foodItem)) {
					return book_dis_price;
				}
			}
			for (String clothItem : clothItemList) {
				if (itemLowerCase.contains(clothItem)) {
					return cloth_dis_price;
				}
			}
		}
		return other_dis_price;
	}

	/*
	 * Fetching Discount Price for Clearance Item added in cart
	 */
	private static double getClearanceDiscountPriceByItem(String item) {
		Optional<String> itemStr = Optional.ofNullable(item);
		if (itemStr.isPresent()) {
			String itemLowerCase = itemStr.get().toLowerCase();
			if (itemLowerCase.contains("clearance")) {
				return clearance_dis_price;
			}
		}
		return 0.00;
	}
}

public class ShoppingCartDiscountCalculator {
	/**
	 * This is the main method to call get user input, calculate discount and saved
	 * price and display the output using ShoppingCartDetails class.
	 * 
	 * @param args Unused.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static void main(String[] args) throws IOException {
		// creating an instance of ShoppingCart
		// doing some operations
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.getUserInput();
		boolean isLoaded = shoppingCart.loadShoppingBasketList();

		if (!ShoppingCartInterface.inputItemList.isEmpty() && isLoaded) {
			shoppingCart.calculateDiscountAndSavedPrice();
			shoppingCart.totalSavedAmount = shoppingCart.totalActualPrice - shoppingCart.totalDiscountedAmount;
			shoppingCart.displayShoppingCart();
		}

	}

}

/**
 * The ShoppingBasket program implements Getter and Setter for noOfQuantity ,
 * item and price.
 */
class ShoppingBasket {
	int noOfQuantity;
	String item;
	double price;

	public int getNoOfQuantity() {
		return noOfQuantity;
	}

	public String getItem() {
		return item;
	}

	public double getPrice() {
		return price;
	}

	public void setNoOfQuantity(int noOfQuantity) {
		this.noOfQuantity = noOfQuantity;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
