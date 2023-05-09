package org.example;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DynamoDbDao {
    @NonNull
    private final DynamoDBMapper dynamoDBMapper;

    public User getRecord(String hashKey) {
        User record = dynamoDBMapper.load(User.class,hashKey);
        if (record == null) {
            throw new RuntimeException("Record not found");
        }
        return record;
    }

    public void saveRecord(User employee) {
        dynamoDBMapper.save(employee);
    }
}
