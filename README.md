# Bank Priority Transaction System 🏦💳

A Java-based banking transaction management system that uses priority queues and heap data structures to efficiently process banking transactions. Features multi-tier priority handling, automatic priority assignment, and comprehensive transaction processing capabilities.

## 🌟 Features

- **Priority-Based Transaction Processing** - Four-tier priority system (Low, Normal, High, Urgent)
- **Heap-Based Priority Queues** - Efficient O(log n) insertion and extraction
- **Multi-Queue Architecture** - Separate heaps for different transaction amount ranges
- **Automatic Priority Assignment** - Smart priority calculation based on transaction type and amount
- **Comprehensive Transaction Types** - Support for deposits, withdrawals, and loan applications
- **Account Management** - Full account balance tracking with overdraft protection
- **Robust Testing Suite** - Comprehensive unit tests for all components

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Basic understanding of data structures (heaps, priority queues)

### Installation

1. **Download the project files:**
```bash
git clone https://github.com/yourusername/bank-priority-system.git
cd bank-priority-system
```

2. **Compile the project:**
```bash
javac *.java
```

3. **Run the test suite:**
```bash
java BankManagerTester
```

4. **Expected output:**
```
Transaction Constructor Tests: PASS
CompareTo Tests for Priority: PASS
CompareTo Tests for Account Balance: PASS
Testing Add Transaction to Heap: PASS
Testing Heapify: PASS
Testing Get Next Transaction: PASS
Testing Queue Transaction: PASS
Testing Perform Transaction: PASS
Testing Peek Highest Priority Transaction: PASS
```

## 🏗️ System Architecture

### Core Classes

#### 1. **Account.java** - Banking Account Management
```java
public class Account {
    private final long accountNumber;    // Unique account identifier
    private double balance;              // Current account balance
    
    public void deposit(double amount);  // Add funds to account
    public void withdraw(double amount); // Remove funds (with overdraft protection)
    public double getBalance();          // Get current balance
}
```

#### 2. **Transaction.java** - Transaction Representation
```java
public class Transaction implements Comparable<Transaction> {
    public enum Priority { LOW, NORMAL, HIGH, URGENT }
    public enum Type { LOAN_APPLICATION, WITHDRAWAL, DEPOSIT }
    
    private final Priority priority;     // Calculated priority
    private final Account user;          // Associated account
    private final double amount;         // Transaction amount
    private final Type type;            // Transaction type
    
    public int compareTo(Transaction other); // Priority comparison
}
```

#### 3. **TransactionHeap.java** - Priority Queue Implementation
```java
public class TransactionHeap {
    private Transaction[] transactions;  // Heap array
    private int size;                   // Current heap size
    
    public void addTransaction(Transaction transaction);    // Insert with heapify up
    public Transaction getNextTransaction();               // Extract max with heapify down
    public Transaction peek();                            // View max without removal
    public void heapifyUp(int index);                     // Maintain heap property
    public void heapifyDown(int index);                   // Maintain heap property
}
```

#### 4. **BankManager.java** - Multi-Tier Transaction Manager
```java
public class BankManager {
    protected TransactionHeap low;       // < $1,000
    protected TransactionHeap medium;    // $1,000 - $999,999
    protected TransactionHeap high;      // ≥ $1,000,000
    
    public void queueTransaction(Transaction transaction);     // Route to appropriate heap
    public Transaction getNextTransaction();                   // Get highest priority transaction
    public void performTransaction();                          // Execute next transaction
}
```

## 🎯 Priority System

### Priority Assignment Rules

#### Transaction Type Priority
```java
switch (type) {
    case DEPOSIT:
        priority = Priority.HIGH;        // Deposits are always high priority
        break;
    
    case WITHDRAWAL:
        priority = Priority.NORMAL;      // Withdrawals are normal priority
        break;
    
    case LOAN_APPLICATION:
        if (amount > account.getBalance() * 3) {
            priority = Priority.LOW;     // Large loans are low priority
        } else {
            priority = Priority.URGENT;  // Small loans are urgent
        }
        break;
}
```

#### Amount-Based Queue Routing
```java
public void queueTransaction(Transaction transaction) {
    double amount = transaction.getAmount();
    
    if (amount >= 1000000) {
        high.addTransaction(transaction);    // ≥ $1M → High queue
    } else if (amount >= 1000) {
        medium.addTransaction(transaction);  // $1K-$999K → Medium queue
    } else {
        low.addTransaction(transaction);     // < $1K → Low queue
    }
}
```

### Priority Comparison Logic
```java
public int compareTo(Transaction other) {
    // 1. Compare by priority level (URGENT > HIGH > NORMAL > LOW)
    int priorityComparison = this.priority.ordinal() - other.priority.ordinal();
    
    if (priorityComparison != 0) {
        return priorityComparison;
    }
    
    // 2. If same priority, compare by account balance (higher balance = higher priority)
    return Double.compare(this.user.getBalance(), other.user.getBalance());
}
```

## 📊 Data Structure Details

### Heap Implementation

#### Structure
```
       Root (Highest Priority)
            /        \
    Left Child    Right Child
      /    \        /    \
    ...    ...    ...    ...
```

#### Heap Properties
- **Max Heap**: Parent ≥ Children (highest priority at root)
- **Complete Binary Tree**: Filled left to right, level by level
- **Array Representation**: Parent at index i, children at 2i+1 and 2i+2

#### Heapify Operations

##### Heapify Up (After Insertion)
```java
public void heapifyUp(int index) {
    while (index > 0) {
        int parentIndex = (index - 1) / 2;
        
        if (transactions[parentIndex].compareTo(transactions[index]) < 0) {
            // Swap with parent if child has higher priority
            swap(parentIndex, index);
            index = parentIndex;
        } else {
            break; // Heap property satisfied
        }
    }
}
```

##### Heapify Down (After Extraction)
```java
public void heapifyDown(int index) {
    int maxIndex = index;
    int leftChild = 2 * index + 1;
    int rightChild = 2 * index + 2;
    
    // Find child with highest priority
    if (leftChild < size && transactions[leftChild].compareTo(transactions[maxIndex]) > 0) {
        maxIndex = leftChild;
    }
    
    if (rightChild < size && transactions[rightChild].compareTo(transactions[maxIndex]) > 0) {
        maxIndex = rightChild;
    }
    
    // Swap and continue if necessary
    if (maxIndex != index) {
        swap(index, maxIndex);
        heapifyDown(maxIndex); // Recursive call
    }
}
```

## 🔄 Transaction Processing Flow

### Transaction Lifecycle

```
Transaction Creation → Priority Assignment → Queue Routing → Heap Insertion → Processing → Account Update
```

#### Step-by-Step Process

1. **Transaction Creation**
   ```java
   Transaction deposit = new Transaction(account, 1500, Transaction.Type.DEPOSIT);
   // Priority automatically set to HIGH
   ```

2. **Queue Routing**
   ```java
   bankManager.queueTransaction(deposit);
   // Routes to medium queue (amount = $1500)
   ```

3. **Heap Insertion**
   ```java
   medium.addTransaction(deposit);
   // Inserted and heapified up to maintain priority order
   ```

4. **Transaction Processing**
   ```java
   bankManager.performTransaction();
   // Extracts highest priority transaction and executes it
   ```

5. **Account Update**
   ```java
   account.deposit(1500); // Balance updated
   ```

### Multi-Queue Priority Processing

```java
public Transaction getNextTransaction() {
    // Process in order: high → medium → low amount queues
    if (!high.isEmpty()) {
        return high.getNextTransaction();
    } else if (!medium.isEmpty()) {
        return medium.getNextTransaction();
    } else if (!low.isEmpty()) {
        return low.getNextTransaction();
    }
    return null; // No transactions available
}
```

## 🔧 Configuration

### Account Setup
```java
// Create account with specific number and balance
Account account = new Account(12345L, 5000.0);

// Create account with random number and zero balance
Account account = new Account();
```

### Transaction Creation
```java
// High priority deposit
Transaction deposit = new Transaction(account, 500, Transaction.Type.DEPOSIT);

// Normal priority withdrawal
Transaction withdrawal = new Transaction(account, 200, Transaction.Type.WITHDRAWAL);

// Urgent priority small loan
Transaction smallLoan = new Transaction(account, 10000, Transaction.Type.LOAN_APPLICATION);

// Low priority large loan
Transaction largeLoan = new Transaction(account, 50000, Transaction.Type.LOAN_APPLICATION);
```

### Bank Manager Configuration
```java
// Create bank manager with specified heap capacity
BankManager bankManager = new BankManager(100);

// Queue transactions
bankManager.queueTransaction(deposit);
bankManager.queueTransaction(withdrawal);
bankManager.queueTransaction(smallLoan);

// Process transactions in priority order
while (bankManager.peekHighestPriorityTransaction() != null) {
    bankManager.performTransaction();
}
```

## 🛠️ Development

### Testing Framework

#### Transaction Testing
```java
public static boolean testTransactionConstructor() {
    Account acc = new Account(1, 1000);
    
    // Test deposit transaction (should be HIGH priority)
    Transaction deposit = new Transaction(acc, 500, Transaction.Type.DEPOSIT);
    return deposit.getPriority() == Transaction.Priority.HIGH;
}

public static boolean testTransactionCompareToPriority() {
    // Test priority comparison between different transaction types
    Transaction urgent = new Transaction(acc, 2000, Transaction.Type.LOAN_APPLICATION);
    Transaction high = new Transaction(acc, 100, Transaction.Type.DEPOSIT);
    
    return urgent.compareTo(high) > 0; // URGENT > HIGH
}
```

#### Heap Testing
```java
public static boolean testHeapify() {
    TransactionHeap heap = new TransactionHeap(4);
    
    // Add transactions in random order
    heap.addTransaction(normalTransaction);
    heap.addTransaction(urgentTransaction);
    heap.addTransaction(highTransaction);
    
    // Verify highest priority is at root
    return heap.peek().getPriority() == Transaction.Priority.URGENT;
}
```

#### Integration Testing
```java
public static boolean testPerformTransaction() {
    BankManager manager = new BankManager(10);
    Account account = new Account(321, 2000);
    
    // Test deposit
    Transaction deposit = new Transaction(account, 800, Transaction.Type.DEPOSIT);
    manager.queueTransaction(deposit);
    manager.performTransaction();
    
    return account.getBalance() == 2800; // 2000 + 800
}
```

### Adding New Features

#### Custom Priority Strategies
```java
public enum CustomPriority {
    VIP_URGENT,    // VIP customers get highest priority
    BUSINESS_HIGH, // Business accounts get high priority
    STUDENT_LOW    // Student accounts get lower priority
}

public class PriorityStrategy {
    public static Transaction.Priority calculatePriority(
        Account account, 
        double amount, 
        Transaction.Type type
    ) {
        // Custom logic based on account type, amount, etc.
        if (isVipAccount(account)) {
            return Transaction.Priority.URGENT;
        }
        // ... existing logic
    }
}
```

#### Enhanced Transaction Types
```java
public enum ExtendedType {
    DEPOSIT, WITHDRAWAL, LOAN_APPLICATION,
    TRANSFER,           // Money transfer between accounts
    INVESTMENT,         // Investment transactions
    BILL_PAYMENT,       // Utility bill payments
    FOREIGN_EXCHANGE    // Currency exchange
}
```

#### Advanced Queue Management
```java
public class AdvancedBankManager extends BankManager {
    private TransactionHeap vip;        // VIP customer queue
    private TransactionHeap timed;      // Time-sensitive transactions
    
    @Override
    public Transaction getNextTransaction() {
        // Process VIP first, then time-sensitive, then regular queues
        if (!vip.isEmpty()) return vip.getNextTransaction();
        if (!timed.isEmpty()) return timed.getNextTransaction();
        return super.getNextTransaction();
    }
}
```

## 📈 Performance Characteristics

### Time Complexity

#### Heap Operations
- **Insert (addTransaction)**: O(log n) - Heapify up
- **Extract Max (getNextTransaction)**: O(log n) - Heapify down
- **Peek**: O(1) - Access root element
- **Build Heap**: O(n) - From unsorted array

#### Bank Manager Operations
- **Queue Transaction**: O(log n) - Heap insertion
- **Get Next Transaction**: O(log n) - Heap extraction from highest priority queue
- **Perform Transaction**: O(log n) - Get transaction + account operation

### Space Complexity
- **Per Heap**: O(capacity) - Fixed-size array
- **Per Transaction**: O(1) - Fixed metadata
- **Total System**: O(3 × capacity) - Three heaps

### Scalability Analysis

#### Strengths
- **Efficient Priority Processing**: O(log n) operations
- **Memory Efficient**: Array-based heap implementation
- **Predictable Performance**: No worst-case degradation

#### Considerations
- **Fixed Capacity**: Heaps have maximum size limits
- **Memory Pre-allocation**: Arrays allocated at creation
- **Three-Queue Overhead**: Multiple heap management

## 🎯 Real-World Applications

### Banking Scenarios

#### High-Volume Processing
```java
// Process thousands of transactions efficiently
BankManager highVolumeManager = new BankManager(10000);

// Batch processing
List<Transaction> transactions = loadTransactionsFromFile();
for (Transaction t : transactions) {
    highVolumeManager.queueTransaction(t);
}

// Process all in priority order
while (highVolumeManager.peekHighestPriorityTransaction() != null) {
    highVolumeManager.performTransaction();
}
```

#### Emergency Processing
```java
// Handle urgent transactions during system maintenance
Transaction emergencyWithdrawal = new Transaction(
    account, 500, Transaction.Type.WITHDRAWAL
);
emergencyWithdrawal.setPriority(Transaction.Priority.URGENT);
```

### Other Applications

- **Hospital Emergency Room**: Patient triage by severity
- **Tech Support System**: Ticket prioritization by customer tier
- **Manufacturing**: Job scheduling by deadline and importance
- **Network Routing**: Packet prioritization by QoS requirements

## 🐛 Troubleshooting

### Common Issues

1. **Heap Overflow**
   ```java
   public void addTransaction(Transaction transaction) {
       if (size >= transactions.length) {
           throw new IllegalStateException("TransactionHeap is full");
       }
       // ... rest of method
   }
   ```

2. **Invalid Transaction Amounts**
   ```java
   public Transaction(Account user, double amount, Type type) {
       if (amount <= 0) {
           throw new IllegalArgumentException("Amount must be positive");
       }
       // ... rest of constructor
   }
   ```

3. **Overdraft Prevention**
   ```java
   public void withdraw(double amount) {
       if (amount > this.balance) {
           throw new IllegalStateException("Insufficient funds");
       }
       this.balance -= amount;
   }
   ```

### Debug Techniques

#### Heap Visualization
```java
public void printHeap(TransactionHeap heap) {
    Transaction[] data = heap.getHeapData();
    System.out.println("Heap contents:");
    for (int i = 0; i < heap.getSize(); i++) {
        System.out.printf("Index %d: %s (Priority: %s)%n", 
                         i, data[i].getType(), data[i].getPriority());
    }
}
```

#### Transaction Tracking
```java
public class TransactionLogger {
    private static List<Transaction> processedTransactions = new ArrayList<>();
    
    public static void logTransaction(Transaction t) {
        processedTransactions.add(t);
        System.out.printf("Processed: %s for $%.2f (Priority: %s)%n",
                         t.getType(), t.getAmount(), t.getPriority());
    }
}
```

## 🔮 Future Enhancements

### Planned Features
- [ ] Real-time transaction monitoring dashboard
- [ ] Automatic fraud detection integration
- [ ] Multi-currency support
- [ ] Transaction scheduling and recurring payments
- [ ] Account hierarchy and delegation
- [ ] Audit trail and compliance reporting

### Advanced Features
- [ ] Machine learning for dynamic priority adjustment
- [ ] Distributed processing across multiple servers
- [ ] Real-time analytics and reporting
- [ ] Mobile API integration
- [ ] Blockchain transaction verification
- [ ] AI-powered risk assessment

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add your improvements
4. Run all tests to ensure functionality
5. Submit a pull request

### Contribution Guidelines
- Follow Java coding standards
- Maintain heap property invariants
- Add comprehensive tests for new features
- Document priority assignment logic
- Test with edge cases (empty heaps, full heaps)

## 🆘 Support

If you encounter issues:

1. Run the test suite to verify system integrity
2. Check heap capacity limits
3. Verify transaction amount validations
4. Test priority assignment logic
5. Open an issue with transaction details and error logs

---

**Process transactions with priority!** 🏦⚡

*Built with ❤️ by Rishabh Aggarwal*

### Academic Integrity Notice
This project was created for educational purposes as part of CS 300 coursework. Please respect academic integrity policies when using this code for learning or reference.
