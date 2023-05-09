package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserCreationRequest {
    @NonNull
    private String userName;

    private String password;

    private String phone;
}
