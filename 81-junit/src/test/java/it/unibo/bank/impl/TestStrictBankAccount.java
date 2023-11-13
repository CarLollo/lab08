package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.unibo.bank.impl.SimpleBankAccount.*;
import static it.unibo.bank.impl.StrictBankAccount.TRANSACTION_FEE;
import static org.junit.jupiter.api.Assertions.*;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // 1. Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", INITIAL_AMOUNT);
        this.bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    // 2. Test the initial state of the StrictBankAccount
    @Test
    public void testInitialization() {
        Assertions.assertEquals(INITIAL_AMOUNT, bankAccount.getBalance());
        Assertions.assertEquals(0, bankAccount.getTransactionsCount());
        Assertions.assertEquals(mRossi, bankAccount.getAccountHolder());
    }


    // 3. Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
    @Test
    public void testManagementFees() {
        Assertions.assertFalse(bankAccount.getTransactionsCount() > 0);
        double expectedValue = 100.0;
        bankAccount.deposit(mRossi.getUserID(), expectedValue);
        bankAccount.chargeManagementFees(mRossi.getUserID());
        Assertions.assertEquals(INITIAL_AMOUNT + expectedValue - MANAGEMENT_FEE - TRANSACTION_FEE, bankAccount.getBalance());
    }

    // 4. Test the withdraw of a negative value
    @Test
    public void testNegativeWithdraw() {
        try {
            double negativeValue = -50.0;
            bankAccount.withdraw(mRossi.getUserID(), negativeValue);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    // 5. Test withdrawing more money than it is in the account
    @Test
    public void testWithdrawingTooMuch() {
        try {
            double moreValue = bankAccount.getBalance() + 50.0;
            bankAccount.withdraw(mRossi.getUserID(), moreValue);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
