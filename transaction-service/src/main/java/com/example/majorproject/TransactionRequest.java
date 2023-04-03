package com.example.majorproject;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private int amount;
    //private String fromUser;  //TODO: This fromUser needs to be removed
    private String toUser;
    private String purpose;
}
