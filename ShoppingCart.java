import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * The ShoppingCart program implements an application that simply calculate
 * Discount and Saved Amount and Prints the output on the screen.
 * 
 * @author Hariharan Jayakumar
 * @version 1.0
 * @since 2024-07-13
 */
public class ShoppingCart {
	static final double book_dis_price = 0.05;
	static final double cloth_dis_price = 0.20;
	static final double other_dis_price = 0.03;
	static final double clearance_dis_price = 0.20;
	public static final List<String> foodItemList = Collections.unmodifiableList(new ArrayList<String>() {
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

	public static final List<String> clothItemList = Collections.unmodifiableList(new ArrayList<String>() {
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
	private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	static double totalDiscountedAmount = 0.00;
	static double totalActualPrice = 0.00;
	static double totalSavedAmount = 0.00;

	/**
	 * This is the main method which makes use of addNum method.
	 * 
	 * @param args Unused.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static void main(String[] args) throws IOException {
		ShoppingCartDetails shoppingCartDetails = new ShoppingCartDetails();
		ArrayList<String> inputItemList = new ArrayList<String>();
		List<ShoppingBasket> shoppingBasketList = new ArrayList<ShoppingBasket>();
		List<ShoppingBasket> shoppingBasketListFinal = new ArrayList<ShoppingBasket>();

		shoppingCartDetails.getUserInput(inputItemList);

		// Storing Shopping Cart Details into ShoppingBasket List
		shoppingBasketList = shoppingCartDetails.loadShoppingBasketList(inputItemList);
		if(!inputItemList.isEmpty() && !shoppingBasketList.isEmpty()) {
			shoppingBasketListFinal = shoppingCartDetails.calculateDiscountAndSavedPrice(shoppingBasketList);
	
			totalSavedAmount = totalActualPrice - totalDiscountedAmount;
	
			shoppingCartDetails.displayShoppingCart(shoppingBasketListFinal);
		}

	}

	public static class ShoppingCartDetails {

		// Getting Input from User
		public void getUserInput(ArrayList<String> inputItemList) {
			Scanner sc = new Scanner(System.in);
			int totalNumofItems = 0;
			try {
			 totalNumofItems = sc.nextInt();
			}catch(Exception ex) {
				System.out.println("Please enter valid input!");
			}
			if(!validateInput(totalNumofItems)) {
				System.out.println("Please enter valid input!");
			}else {
				for (int i = 0; i < totalNumofItems; i++) {
					sc = new Scanner(System.in);
					String inputString = sc.nextLine();
					inputItemList.add(inputString);
				}
			}
			sc.close();
		}

		/**
		 * This is the method to calculate Discount and Saved Price and store into
		 * ShoppingBasketList.
		 * 
		 * @param shoppingBasketList used.
		 */
		public List<ShoppingBasket> calculateDiscountAndSavedPrice(List<ShoppingBasket> shoppingBasketList) {
			List<ShoppingBasket> shoppingBasketListFinal = new ArrayList<ShoppingBasket>();
			ShoppingBasket shoppingBasket = null;
			for (final ShoppingBasket shoppingBasketItem : shoppingBasketList) {
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
			return shoppingBasketListFinal;

		}

		/**
		 * This is the method to insert the Number of Quantity, Item and Price into
		 * ShoppingBasketList.
		 * 
		 * @param inputItemList used.
		 * @return
		 */
		public List<ShoppingBasket> loadShoppingBasketList(ArrayList<String> inputItemList) {
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
					
					if(inputItem.contains(" at ")) {
						atLastIndex = inputItem.lastIndexOf(" at ") + 4;
						
					} else {
						atLastIndex = inputItem.lastIndexOf(" ") + 1;
					}
					
					price = Double.valueOf(inputItem.substring(atLastIndex, inputItem.length()));
					
					if(!validateInput(price)) {
						shoppingBasketList = new ArrayList<ShoppingBasket>();
						System.out.println("Please enter valid input!");
						return shoppingBasketList;
					}
					
					if(!validateInput(noOfQuantity)) {
						shoppingBasketList = new ArrayList<ShoppingBasket>();
						System.out.println("Please enter valid input!");
						return shoppingBasketList;
					}
					
					item = inputItem.substring(atFirstIndex + 1, atLastIndex-1);
					shoppingBasket.setItem(item);
					shoppingBasket.setNoOfQuantity(noOfQuantity);
					shoppingBasket.setPrice(price);
					shoppingBasketList.add(shoppingBasket);
				}catch(Exception ex) {
					System.out.println("Please enter valid input!");
				}
			}
			return shoppingBasketList;

		}

		private boolean validateInput(double input) {
			if(input==0 || input==0.0 || input<0) {
				return false;
			}
			return true;
		}
		
		private boolean validateInput(int input) {
			if(input==0 || input<0) {
				return false;
			}
			return true;
		}

		/**
		 * This is the method to display Final Shopping Cart with Discount and Saved
		 * Price.
		 * 
		 * @param shoppingBasketListFinal,totalDiscountedAmount and totalSavedAmount
		 *                                                      used.
		 */
		private void displayShoppingCart(List<ShoppingBasket> shoppingBasketListFinal) {
			for (final ShoppingBasket shoppingBasketItem : shoppingBasketListFinal) {
				System.out.println(shoppingBasketItem.getNoOfQuantity() + " " + shoppingBasketItem.getItem() + " "
						+ String.format("%.2f", shoppingBasketItem.getPrice()));
			}
			System.out.println("Total: " + String.format("%.2f", totalDiscountedAmount));
			System.out.println("You saved: " + String.format("%.2f", totalSavedAmount));

		}

		/*
		 * Calculate Discount Price for Clearance Items
		 */
		private static double calulateClearanceDiscountPrice(String item, double price) {
			double discountedPrice = getClearanceDiscountPriceByItem(item);
			double discountAmount = price * discountedPrice;
			return discountAmount;
		}

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
			// Get Discounted Price
			double discountedPrice = getDiscountPriceByItem(item);
			double discountAmount = price * discountedPrice;

			return discountAmount;

		}

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