package com.example.majorproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {


    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public String initiateTransaction(@RequestBody TransactionRequest transactionRequest) throws JsonProcessingException {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return transactionService.initiateTransaction(transactionRequest);
    }

    @GetMapping("/welcome")
    public String greetCustomer(){
        return "Welcome!! to Transaction Service";
    }
}

// quartz cron job -> {fetch all the transaction db entries where start time > 1}