package org.example;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "USER_TABLE")
public class User {
    @NonNull
    @DynamoDBHashKey
    private String username;

    private String password;

    private String email;

    private List<String> addresses;

    private String phone;

    private String subscriptionId;

    private Double accountBalance;

    private boolean isActive;
}
