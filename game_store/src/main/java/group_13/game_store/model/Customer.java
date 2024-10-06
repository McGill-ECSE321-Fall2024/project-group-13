package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 12 "model.ump"
// line 155 "model.ump"
public class Customer extends UserAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  private List<WishlistItem> wishlistItems;
  private PaymentInformation paymentInformation;
  private DeliveryInformation deliveryInformation;
  private List<Review> reviews;
  private List<Order> orderHistory;
  private List<CartItem> cartItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aName, String aUsername, String aEmail, String aPassword, String aPhoneNumber, int aPermissionLevel)
  {
    super(aName, aUsername, aEmail, aPassword, aPhoneNumber, aPermissionLevel);
    wishlistItems = new ArrayList<WishlistItem>();
    reviews = new ArrayList<Review>();
    orderHistory = new ArrayList<Order>();
    cartItems = new ArrayList<CartItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public WishlistItem getWishlistItem(int index)
  {
    WishlistItem aWishlistItem = wishlistItems.get(index);
    return aWishlistItem;
  }

  public List<WishlistItem> getWishlistItems()
  {
    List<WishlistItem> newWishlistItems = Collections.unmodifiableList(wishlistItems);
    return newWishlistItems;
  }

  public int numberOfWishlistItems()
  {
    int number = wishlistItems.size();
    return number;
  }

  public boolean hasWishlistItems()
  {
    boolean has = wishlistItems.size() > 0;
    return has;
  }

  public int indexOfWishlistItem(WishlistItem aWishlistItem)
  {
    int index = wishlistItems.indexOf(aWishlistItem);
    return index;
  }
  /* Code from template association_GetOne */
  public PaymentInformation getPaymentInformation()
  {
    return paymentInformation;
  }

  public boolean hasPaymentInformation()
  {
    boolean has = paymentInformation != null;
    return has;
  }
  /* Code from template association_GetOne */
  public DeliveryInformation getDeliveryInformation()
  {
    return deliveryInformation;
  }

  public boolean hasDeliveryInformation()
  {
    boolean has = deliveryInformation != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Review getReview(int index)
  {
    Review aReview = reviews.get(index);
    return aReview;
  }

  public List<Review> getReviews()
  {
    List<Review> newReviews = Collections.unmodifiableList(reviews);
    return newReviews;
  }

  public int numberOfReviews()
  {
    int number = reviews.size();
    return number;
  }

  public boolean hasReviews()
  {
    boolean has = reviews.size() > 0;
    return has;
  }

  public int indexOfReview(Review aReview)
  {
    int index = reviews.indexOf(aReview);
    return index;
  }
  /* Code from template association_GetMany */
  public Order getOrderHistory(int index)
  {
    Order aOrderHistory = orderHistory.get(index);
    return aOrderHistory;
  }

  public List<Order> getOrderHistory()
  {
    List<Order> newOrderHistory = Collections.unmodifiableList(orderHistory);
    return newOrderHistory;
  }

  public int numberOfOrderHistory()
  {
    int number = orderHistory.size();
    return number;
  }

  public boolean hasOrderHistory()
  {
    boolean has = orderHistory.size() > 0;
    return has;
  }

  public int indexOfOrderHistory(Order aOrderHistory)
  {
    int index = orderHistory.indexOf(aOrderHistory);
    return index;
  }
  /* Code from template association_GetMany */
  public CartItem getCartItem(int index)
  {
    CartItem aCartItem = cartItems.get(index);
    return aCartItem;
  }

  public List<CartItem> getCartItems()
  {
    List<CartItem> newCartItems = Collections.unmodifiableList(cartItems);
    return newCartItems;
  }

  public int numberOfCartItems()
  {
    int number = cartItems.size();
    return number;
  }

  public boolean hasCartItems()
  {
    boolean has = cartItems.size() > 0;
    return has;
  }

  public int indexOfCartItem(CartItem aCartItem)
  {
    int index = cartItems.indexOf(aCartItem);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWishlistItems()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addWishlistItem(WishlistItem aWishlistItem)
  {
    boolean wasAdded = false;
    if (wishlistItems.contains(aWishlistItem)) { return false; }
    wishlistItems.add(aWishlistItem);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWishlistItem(WishlistItem aWishlistItem)
  {
    boolean wasRemoved = false;
    if (wishlistItems.contains(aWishlistItem))
    {
      wishlistItems.remove(aWishlistItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishlistItemAt(WishlistItem aWishlistItem, int index)
  {  
    boolean wasAdded = false;
    if(addWishlistItem(aWishlistItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlistItems()) { index = numberOfWishlistItems() - 1; }
      wishlistItems.remove(aWishlistItem);
      wishlistItems.add(index, aWishlistItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishlistItemAt(WishlistItem aWishlistItem, int index)
  {
    boolean wasAdded = false;
    if(wishlistItems.contains(aWishlistItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlistItems()) { index = numberOfWishlistItems() - 1; }
      wishlistItems.remove(aWishlistItem);
      wishlistItems.add(index, aWishlistItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishlistItemAt(aWishlistItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPaymentInformation(PaymentInformation aNewPaymentInformation)
  {
    boolean wasSet = false;
    paymentInformation = aNewPaymentInformation;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setDeliveryInformation(DeliveryInformation aNewDeliveryInformation)
  {
    boolean wasSet = false;
    deliveryInformation = aNewDeliveryInformation;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviews()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addReview(Review aReview)
  {
    boolean wasAdded = false;
    if (reviews.contains(aReview)) { return false; }
    reviews.add(aReview);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReview(Review aReview)
  {
    boolean wasRemoved = false;
    if (reviews.contains(aReview))
    {
      reviews.remove(aReview);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewAt(Review aReview, int index)
  {  
    boolean wasAdded = false;
    if(addReview(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(reviews.contains(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReviewAt(aReview, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderHistory()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addOrderHistory(Order aOrderHistory)
  {
    boolean wasAdded = false;
    if (orderHistory.contains(aOrderHistory)) { return false; }
    orderHistory.add(aOrderHistory);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderHistory(Order aOrderHistory)
  {
    boolean wasRemoved = false;
    if (orderHistory.contains(aOrderHistory))
    {
      orderHistory.remove(aOrderHistory);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderHistoryAt(Order aOrderHistory, int index)
  {  
    boolean wasAdded = false;
    if(addOrderHistory(aOrderHistory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderHistory()) { index = numberOfOrderHistory() - 1; }
      orderHistory.remove(aOrderHistory);
      orderHistory.add(index, aOrderHistory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderHistoryAt(Order aOrderHistory, int index)
  {
    boolean wasAdded = false;
    if(orderHistory.contains(aOrderHistory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderHistory()) { index = numberOfOrderHistory() - 1; }
      orderHistory.remove(aOrderHistory);
      orderHistory.add(index, aOrderHistory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderHistoryAt(aOrderHistory, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCartItems()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCartItem(CartItem aCartItem)
  {
    boolean wasAdded = false;
    if (cartItems.contains(aCartItem)) { return false; }
    cartItems.add(aCartItem);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCartItem(CartItem aCartItem)
  {
    boolean wasRemoved = false;
    if (cartItems.contains(aCartItem))
    {
      cartItems.remove(aCartItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCartItemAt(CartItem aCartItem, int index)
  {  
    boolean wasAdded = false;
    if(addCartItem(aCartItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCartItems()) { index = numberOfCartItems() - 1; }
      cartItems.remove(aCartItem);
      cartItems.add(index, aCartItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCartItemAt(CartItem aCartItem, int index)
  {
    boolean wasAdded = false;
    if(cartItems.contains(aCartItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCartItems()) { index = numberOfCartItems() - 1; }
      cartItems.remove(aCartItem);
      cartItems.add(index, aCartItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCartItemAt(aCartItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    wishlistItems.clear();
    paymentInformation = null;
    deliveryInformation = null;
    reviews.clear();
    orderHistory.clear();
    cartItems.clear();
    super.delete();
  }

}


