package project;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class BankAccount {
    private String accountNo;
    private String password;
    private double balance;

    public BankAccount(String accountNo, String password, double balance) {
        this.accountNo = accountNo;
        this.password = password;
        this.balance = balance;
    }

    public boolean login(String accNo, String pwd) {
        return this.accountNo.equals(accNo) && this.password.equals(pwd);
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount + ", Current Balance: " + balance);
        recordTransaction("Deposit of " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + ", Current Balance: " + balance);
            recordTransaction("Withdrawal of " + amount);
        } else {
            System.out.println("Insufficient Balance!");
        }
    }

    public void transfer(BankAccount receiver, double amount) {
        if (amount <= balance) {
            int otp = generateOTP();
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter OTP sent to your device: ");
            int enteredOtp = sc.nextInt();

            if (enteredOtp == otp) {
                this.balance -= amount;
                receiver.balance += amount;
                System.out.println("Transferred " + amount + " successfully to Account: " + receiver.accountNo);
                recordTransaction("Transfer of " + amount + " to " + receiver.accountNo);
            } else {
                System.out.println("Invalid OTP! Transfer Failed.");
            }
        } else {
            System.out.println("Insufficient Balance for transfer!");
        }
    }

    public double getBalance() {
        return balance;
    }

    private int generateOTP() {
        Random rand = new Random();
        int otp = 1000 + rand.nextInt(9000);
        System.out.println("Generated OTP: " + otp); // In real system, OTP sent to phone/email
        return otp;
    }

    private void recordTransaction(String transaction) {
        try (FileWriter fw = new FileWriter("transactions.txt", true)) {
            fw.write(transaction + " | Balance: " + balance + "\n");
        } catch (IOException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
        }
    }
}

public class BankingApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
      
        BankAccount acc1 = new BankAccount("12345", "pass123", 10000);
        BankAccount acc2 = new BankAccount("67890", "pass456", 5000);

        System.out.print("Enter Account No: ");
        String accNo = sc.next();
        System.out.print("Enter Password: ");
        String pwd = sc.next();

        if (acc1.login(accNo, pwd)) {
            System.out.println("Login Successful!");
            int choice;
            do {
                System.out.println("\n1. Deposit\n2. Withdraw\n3. Transfer\n4. Logout");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter amount to deposit: ");
                        double dep = sc.nextDouble();
                        acc1.deposit(dep);
                        break;

                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double wd = sc.nextDouble();
                        acc1.withdraw(wd);
                        break;

                    case 3:
                        System.out.print("Enter amount to transfer: ");
                        double tr = sc.nextDouble();
                        acc1.transfer(acc2, tr);
                        break;

                    case 4:
                        System.out.println("Logging out...");
                        break;

                    default:
                        System.out.println("Invalid Choice!");
                }
            } while (choice != 4);

        } else {
            System.out.println("Invalid Credentials!");
        }

        sc.close();
    }
}


