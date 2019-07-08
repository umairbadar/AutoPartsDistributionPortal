package com.example.autopartsdistributionportal;

public class Model_Bank {

    private String account_title;
    private String bank_name;
    private String branch_name;
    private String account_no;
    private String account_balance;

    public Model_Bank(String account_title, String bank_name, String branch_name, String account_no, String account_balance) {
        this.account_title = account_title;
        this.bank_name = bank_name;
        this.branch_name = branch_name;
        this.account_no = account_no;
        this.account_balance = account_balance;
    }

    public String getAccount_title() {
        return account_title;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public String getAccount_balance() {
        return account_balance;
    }
}
