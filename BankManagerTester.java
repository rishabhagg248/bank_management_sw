import java.util.NoSuchElementException;

public class BankManagerTester {

  /**
   * Tests the constructor for the Transaction class.
   * 
   * @return true if the test passes
   */
  public static boolean testTransactionConstructor() {

    try {

      // Test valid deposit transaction
      Account acc1 = new Account(1, 1000);
      Transaction deposit = new Transaction(acc1, 500, Transaction.Type.DEPOSIT);
      if (deposit.getPriority() != Transaction.Priority.HIGH) {
        return false;
      }

      // Test valid withdrawal transaction
      Transaction withdrawal = new Transaction(acc1, 200, Transaction.Type.WITHDRAWAL);
      if (withdrawal.getPriority() != Transaction.Priority.NORMAL) {
        return false;
      }

      // Test loan application below 3x threshold
      Account acc2 = new Account(2, 2000);
      Transaction smallLoan = new Transaction(acc2, 5000, Transaction.Type.LOAN_APPLICATION);
      if (smallLoan.getPriority() != Transaction.Priority.URGENT) {
        return false;
      }

      // Test loan application above 3x threshold
      Transaction bigLoan = new Transaction(acc2, 7000, Transaction.Type.LOAN_APPLICATION);
      if (bigLoan.getPriority() != Transaction.Priority.LOW) {
        return false;
      }

      // Test invalid amount
      try {
        Transaction invalid = new Transaction(acc1, -100, Transaction.Type.DEPOSIT);
        return false; // Should not reach here
      } catch (IllegalArgumentException e) {
        // Expected exception
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * tests the Transaction.compareTo when the priorities are different
   * 
   * @return true if the test passes
   */
  public static boolean testTransactionCompareToPriority() {

    try {

      Account acc = new Account(1, 1000);

      // Create transactions with different priorities
      Transaction deposit = new Transaction(acc, 100, Transaction.Type.DEPOSIT); // HIGH
      Transaction withdrawal = new Transaction(acc, 50, Transaction.Type.WITHDRAWAL); // NORMAL
      Transaction bigLoan = new Transaction(acc, 4000, Transaction.Type.LOAN_APPLICATION); // LOW
      Transaction smallLoan = new Transaction(acc, 2000, Transaction.Type.LOAN_APPLICATION); // URGENT

      // Test different priority comparisons
      if (!(deposit.compareTo(withdrawal) > 0))
        return false; // HIGH > NORMAL
      if (!(withdrawal.compareTo(bigLoan) > 0))
        return false; // NORMAL > LOW
      if (!(smallLoan.compareTo(deposit) > 0))
        return false; // URGENT > HIGH
      if (!(smallLoan.compareTo(bigLoan) > 0))
        return false; // URGENT > LOW

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * tests the Transaction.compareTo when the priorities are the same
   * 
   * @return true if the test passes
   */
  public static boolean testTransactionCompareToAccountBalance() {

    try {

      Account richAcc = new Account(1, 2000);
      Account poorAcc = new Account(2, 1000);

      // Create transactions with same priority but different account balances
      Transaction richDeposit = new Transaction(richAcc, 100, Transaction.Type.DEPOSIT);
      Transaction poorDeposit = new Transaction(poorAcc, 100, Transaction.Type.DEPOSIT);

      // Higher balance should be more important
      if (richDeposit.compareTo(poorDeposit) <= 0)
        return false;
      if (poorDeposit.compareTo(richDeposit) >= 0)
        return false;

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Tests the TransactionHeap.addTransaction() method
   * 
   * @return true if the test passes
   */
  public static boolean testAddTransactionToHeap() {

    try {

      TransactionHeap heap = new TransactionHeap(3);
      Account acc = new Account(1, 1000);

      // Add transactions
      Transaction t1 = new Transaction(acc, 100, Transaction.Type.DEPOSIT);
      Transaction t2 = new Transaction(acc, 50, Transaction.Type.WITHDRAWAL);

      heap.addTransaction(t1);
      if (heap.getSize() != 1)
        return false;

      heap.addTransaction(t2);
      if (heap.getSize() != 2)
        return false;

      // Test full heap
      Transaction t3 = new Transaction(acc, 75, Transaction.Type.WITHDRAWAL);
      heap.addTransaction(t3);

      try {
        Transaction t4 = new Transaction(acc, 80, Transaction.Type.WITHDRAWAL);
        heap.addTransaction(t4);
        return false; // Should not reach here
      } catch (IllegalStateException e) {
        // Expected exception
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Tests the TransactionHeap.heapifyUp() and TransactionHeap.heapifyDown() methods
   * 
   * @return true if the test passes
   */
  public static boolean testHeapify() {

    try {

      TransactionHeap heap = new TransactionHeap(4);
      Account acc = new Account(1, 1000);

      // Add transactions in a way that requires heapifying
      Transaction t1 = new Transaction(acc, 100, Transaction.Type.WITHDRAWAL); // NORMAL
      Transaction t2 = new Transaction(acc, 200, Transaction.Type.DEPOSIT); // HIGH
      Transaction t3 = new Transaction(acc, 2000, Transaction.Type.LOAN_APPLICATION); // URGENT

      heap.addTransaction(t1);
      heap.addTransaction(t2);
      heap.addTransaction(t3);

      // Check if highest priority is at the root
      if (heap.peek().getPriority() != Transaction.Priority.URGENT)
        return false;

      // Remove highest priority and check if heap property is maintained
      heap.getNextTransaction();
      if (heap.peek().getPriority() != Transaction.Priority.HIGH)
        return false;

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Tests the TransactionHeap.getNextTransaction() method
   * 
   * @return true if the test passes
   */
  public static boolean testGetNextTransactionFromHeap() {

    try {

      TransactionHeap heap = new TransactionHeap(5);
      Account acc = new Account(1, 1000);

      // Test empty heap
      try {

        heap.getNextTransaction();
        return false; // Should not reach here

      } catch (NoSuchElementException e) {
        // Expected exception
      }

      // Add transactions in specific order to test heap property
      Transaction t1 = new Transaction(acc, 100, Transaction.Type.WITHDRAWAL); // NORMAL
      Transaction t2 = new Transaction(acc, 200, Transaction.Type.DEPOSIT); // HIGH
      Transaction t3 = new Transaction(acc, 2000, Transaction.Type.LOAN_APPLICATION); // URGENT
      Transaction t4 = new Transaction(acc, 300, Transaction.Type.WITHDRAWAL); // NORMAL

      heap.addTransaction(t1);
      heap.addTransaction(t2);
      heap.addTransaction(t3);
      heap.addTransaction(t4);

      // Check if we get transactions in correct priority order
      Transaction next1 = heap.getNextTransaction();
      Transaction next2 = heap.getNextTransaction();
      Transaction next3 = heap.getNextTransaction();

      // Verify priority order
      if (next1.getPriority() != Transaction.Priority.URGENT)
        return false;
      if (next2.getPriority() != Transaction.Priority.HIGH)
        return false;
      if (next3.getPriority() != Transaction.Priority.NORMAL)
        return false;

      // Verify heap size decreases
      if (heap.getSize() != 1)
        return false;

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Tests the BankManager.queueTransaction() method
   * 
   * @return true if the test passes
   */
  public static boolean testQueueTransaction() {

    try {

      BankManager manager = new BankManager(5);
      Account acc = new Account(1, 1000);

      // Test low priority queue (amount < 1000)
      Transaction lowTrans = new Transaction(acc, 500, Transaction.Type.DEPOSIT);
      manager.queueTransaction(lowTrans);
      if (manager.low.isEmpty())
        return false;

      // Test medium priority queue (1000 <= amount < 1000000)
      Transaction medTrans = new Transaction(acc, 5000, Transaction.Type.DEPOSIT);
      manager.queueTransaction(medTrans);
      if (manager.medium.isEmpty())
        return false;

      // Test high priority queue (amount >= 1000000)
      Transaction highTrans = new Transaction(acc, 1000000, Transaction.Type.DEPOSIT);
      manager.queueTransaction(highTrans);
      if (manager.high.isEmpty())
        return false;

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Tests the BankManager.performTransaction() method
   * 
   * @return true if the test passes
   */
  public static boolean testPerformTransaction() {

    BankManager bankManager = new BankManager(10);

    Account savingsAccount = new Account(321, 2000);
    Transaction depositTransaction = new Transaction(savingsAccount, 800, Transaction.Type.DEPOSIT);
    bankManager.queueTransaction(depositTransaction);
    bankManager.performTransaction();
    if (savingsAccount.getBalance() != 2800) {
      return false;
    }

    Account checkingAccount = new Account(654, 1500);
    Transaction withdrawalTransaction =
        new Transaction(checkingAccount, 700, Transaction.Type.WITHDRAWAL);
    bankManager.queueTransaction(withdrawalTransaction);
    bankManager.performTransaction();
    if (checkingAccount.getBalance() != 800) {
      return false;
    }

    Account businessAccount = new Account(987, 3000);
    Transaction loanTransaction =
        new Transaction(businessAccount, 4000, Transaction.Type.LOAN_APPLICATION);
    bankManager.queueTransaction(loanTransaction);
    bankManager.performTransaction();
    if (businessAccount.getBalance() != 7000) {
      return false;
    }

    Account invalidLoanAccount = new Account(202, 2500);
    Transaction invalidLoanTransaction =
        new Transaction(invalidLoanAccount, 30000, Transaction.Type.LOAN_APPLICATION);
    bankManager.queueTransaction(invalidLoanTransaction);
    bankManager.performTransaction();
    if (invalidLoanAccount.getBalance() != 2500) {
      return false;
    }

    BankManager emptyBankManager = new BankManager(10);
    try {
      emptyBankManager.performTransaction();
      return false;
    } catch (NoSuchElementException e) {
    }

    return true;

  }

  /**
   * Tests the BankManager.peekHighestPriorityTransaction() method
   * 
   * @return true if the test passes
   */
  public static boolean testPeekHighestPriorityTransaction() {
    try {
      BankManager manager = new BankManager(5);
      Account acc = new Account(1, 1000);

      // Create transactions with different PRIORITIES (not just amounts)
      Transaction urgentTrans = new Transaction(acc, 2000, Transaction.Type.LOAN_APPLICATION); // URGENT
                                                                                               // priority
      Transaction highTrans = new Transaction(acc, 100, Transaction.Type.DEPOSIT); // HIGH priority
      Transaction normalTrans = new Transaction(acc, 100, Transaction.Type.WITHDRAWAL); // NORMAL
                                                                                        // priority

      // Add in reverse priority order
      manager.queueTransaction(normalTrans);
      manager.queueTransaction(highTrans);
      manager.queueTransaction(urgentTrans);

      // First peek should be URGENT priority
      if (manager.peekHighestPriorityTransaction() != urgentTrans) {
        return false;
      }

      // Remove urgent, next should be HIGH priority
      manager.getNextTransaction();
      if (manager.peekHighestPriorityTransaction() != highTrans) {
        return false;
      }

      // Remove high, next should be NORMAL priority
      manager.getNextTransaction();
      if (manager.peekHighestPriorityTransaction() != normalTrans) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  public static void main(String[] args) {
    System.out.println(
        "Transaction Constructor Tests: " + (testTransactionConstructor() ? "PASS" : "FAIL"));
    System.out.println(
        "CompareTo Tests for Priority: " + (testTransactionCompareToPriority() ? "PASS" : "FAIL"));
    System.out.println("CompareTo Tests for Account Balance: "
        + (testTransactionCompareToAccountBalance() ? "PASS" : "FAIL"));
    System.out.println(
        "Testing Add Transaction to Heap: " + (testAddTransactionToHeap() ? "PASS" : "FAIL"));
    System.out.println("Testing Heapify: " + (testHeapify() ? "PASS" : "FAIL"));
    System.out.println(
        "Testing Get Next Transaction: " + (testGetNextTransactionFromHeap() ? "PASS" : "FAIL"));
    System.out.println("Testing Queue Transaction: " + (testQueueTransaction() ? "PASS" : "FAIL"));
    System.out
        .println("Testing Perform Transaction: " + (testPerformTransaction() ? "PASS" : "FAIL"));
    System.out.println("Testing Peek Highest Priority Transaction: "
        + (testPeekHighestPriorityTransaction() ? "PASS" : "FAIL"));
  }
}
