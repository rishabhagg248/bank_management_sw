//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: TransactionHeap
// Course: CS 300 Fall 2024
//
// Author: Swati Banwani
// Email: banwani@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// N/A
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// N/A
//
///////////////////////////////////////////////////////////////////////////////

import java.util.NoSuchElementException;

/**
 * Class for implementing a heap/priority queue on Transactions
 */
public class TransactionHeap {

  private Transaction[] transactions;

  private int size;

  /**
   * Initializes transactions array with size capacity
   * 
   * @param capacity the length of the transactions heap array
   */
  public TransactionHeap(int capacity) {

    transactions = new Transaction[capacity];
    size = 0;

  }

  /**
   * This method adds a transaction to the heap if space allows.
   * 
   * @param transaction the transaction to add to the heap
   * @throws IllegalStateException if the TransactionHeap is full.
   */
  public void addTransaction(Transaction transaction) {

    if (size >= transactions.length) {
      throw new IllegalStateException("TransactionHeap is full");
    }

    transactions[size] = transaction;

    heapifyUp(size);

    size++;

  }

  /**
   * Reinforces the heap rules after adding a Transaction to the end
   * 
   * @param index the index of the new Transaction
   */
  public void heapifyUp(int index) {

    while (index > 0) {

      int parentIndex = (index - 1) / 2;

      if (transactions[parentIndex].compareTo(transactions[index]) < 0) {

        Transaction temp = transactions[parentIndex];
        transactions[parentIndex] = transactions[index];
        transactions[index] = temp;
        index = parentIndex;

      } else {
        break;
      }
    }
  }

  /**
   * Removes the next transaction from the priority queue
   * 
   * @return the next transaction in the priority queue
   * @throws NoSuchElementException if there are no transactions in the heap
   */
  public Transaction getNextTransaction() {

    if (size == 0) {
      throw new NoSuchElementException("Heap is empty");
    }

    Transaction nextTransaction = transactions[0];
    transactions[0] = transactions[size - 1];
    transactions[size - 1] = null;
    size--;

    if (size > 0) {
      heapifyDown(0);
    }

    return nextTransaction;

  }

  /**
   * Enforces the heap conditions after removing a Transaction from the heap
   * 
   * @param index the index whose subtree needs to be heapified
   */
  public void heapifyDown(int index) {

    int maxIndex = index;
    int leftChild = 2 * index + 1;
    int rightChild = 2 * index + 2;

    if (leftChild < size && transactions[leftChild].compareTo(transactions[maxIndex]) > 0) {

      maxIndex = leftChild;

    }

    if (rightChild < size && transactions[rightChild].compareTo(transactions[maxIndex]) > 0) {

      maxIndex = rightChild;

    }

    if (maxIndex != index) {

      Transaction temp = transactions[index];
      transactions[index] = transactions[maxIndex];
      transactions[maxIndex] = temp;
      heapifyDown(maxIndex);

    }

  }

  /**
   * Returns the highest priority transaction without removing it from the heap.
   * 
   * @return the highest priority transaction without removing it from the heap.
   * @throws NoSuchElementException if there are no transactions in the heap
   */
  public Transaction peek() {

    if (size == 0) {
      throw new NoSuchElementException("Heap is empty");
    }

    return transactions[0];

  }

  /**
   * Getter method for the heap size
   * 
   * @return the size
   */
  public int getSize() {

    return size;

  }

  /**
   * Tells if the heap has any elements in it
   * 
   * @return whether or not the heap is empty
   */
  public boolean isEmpty() {

    return size == 0;

  }

  /**
   * PROVIDED Creates and returns a deep copy of the heap's array of data.
   * 
   * @return the deep copy of the array holding the heap's data
   */
  public Transaction[] getHeapData() {

    Transaction[] list = new Transaction[this.transactions.length];

    for (int i = 0; i < list.length; i++)
      list[i] = this.transactions[i];

    return list;

  }

}
