package com.example.autopartsdistributionportal;

public class Model_ViewTransactions {

    private String Ref_num;
    private String Previous_balance;
    private String Added_Amount;
    private String Updated_Balance;
    private String Payment_Method;
    private String Payment_Mode;

    public Model_ViewTransactions(String ref_num, String previous_balance, String added_Amount, String updated_Balance, String payment_Method, String payment_Mode) {
        Ref_num = ref_num;
        Previous_balance = previous_balance;
        Added_Amount = added_Amount;
        Updated_Balance = updated_Balance;
        Payment_Method = payment_Method;
        Payment_Mode = payment_Mode;
    }

    public String getRef_num() {
        return Ref_num;
    }

    public String getPrevious_balance() {
        return Previous_balance;
    }

    public String getAdded_Amount() {
        return Added_Amount;
    }

    public String getUpdated_Balance() {
        return Updated_Balance;
    }

    public String getPayment_Method() {
        return Payment_Method;
    }

    public String getPayment_Mode() {
        return Payment_Mode;
    }
}
