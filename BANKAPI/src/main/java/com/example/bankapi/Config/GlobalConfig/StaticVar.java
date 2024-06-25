package com.example.bankapi.Config.GlobalConfig;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StaticVar {
    public static final String DEPOSIT_RECEIPT ="DEPOSIT";
    public static final String WITHDRAWAL_RECEIPT="WITHDRAWAL";
    public static final String TRANSFER_RECEIPT = "TRANSFER";
    public static final String SAVING_ACCOUNT = "saving";
    public static final String CHECKING_ACCOUNT= "checking";
    public static final int ACCOUNT_LENGTH=16;
    public static final double INIT_BALANCE=50000;
    public static final String IN_SUFFICIENT_EXCEPTION_MESSAGE = "Balance is sufficient for transaction";
    public static final String INVALID_ACCOUNT_TYPE_EXCEPTION_MESSAGE = "Only Checking Account can have checking receipt";
    public static final String INSUFFICIENT_ROLE_EXCEPTION_MESSAGE="Permission denied: insufficient role privileges.";
    public static final String BLACKLISTED_TOKEN_MESSAGE = "Token is blacklisted";
    public static final String UNAUTHORIZED_EXCEPTION_MESSAGE="Authentication failure!";
    public static final String SIGNATURE_EXCEPTION_MESSAGE="Invalid Token!";
    public static final String EXPIRED_JWT_EXCEPTION_MESSAGE="Token has already expired!";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
    public static final String ACCESS_DENIED_PROPERTY_REASON="access_denied_reason";
    public static final String ACCOUNT_NOT_EXIST_EXCEPTION_MESSAGE="Account not found";
    public static final BigDecimal INTEREST_RATE = BigDecimal.valueOf(7.9/100);
    public static final long JWT_EXPIRATION = 1000 * 60 * 60;
}
