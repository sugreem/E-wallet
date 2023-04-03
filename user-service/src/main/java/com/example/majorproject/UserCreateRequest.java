package com.example.majorproject;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    private String userId;
    private String name;
    private int age;
    private String email;
    private String phone;

    //private String authorities;  //no need to pass as request...need to be appended by backend.
    private String password;

}
