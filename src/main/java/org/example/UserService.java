package org.example;

import lombok.RequiredArgsConstructor;
import org.example.model.UserCreationRequest;

import javax.inject.Inject;

@RequiredArgsConstructor
public class UserService {
    @Inject
    private final DynamoDbDao dynmaoDbDao;

    public void createUser(UserCreationRequest userCreationRequest) {
        //Create user object from userCreationRequest
        User user = User.builder()
                .username(userCreationRequest.getUserName())
                .phone(userCreationRequest.getPhone())
                .password(userCreationRequest.getPassword())
                .build();

        dynmaoDbDao.saveRecord(user);
    }

    public void initialiseBalance(String username) {
        //get user from dynamoDb
        User user = dynmaoDbDao.getRecord(username);

        //get users current accountBalance
        Double currentBalance = user.getAccountBalance();

        //check if accountBalance is equal to zero
        //then check if account is activated
        //if so add 100 to accountBalance
        //else throw exception
        if(currentBalance == 0) {
            if (user.isActive()) {
                currentBalance += 100;
            } else {
                throw new RuntimeException("Account is not activated");
            }
        }
    }

    /**
     * Method to update balance of the user given username and price of purchase
     * get user from dynamoDb and check if accountBalance has enough balance for purchase
     * if yes, reduce the balance by price
     * else throw Insufficient funds exception
     * update the balance to user object
     * save updated user to dynamoDb and return the updated balance
     * @param username
     * @param price
     * @return
     */
    public Double updateBalance(String username, Double price) {
        //get user from dynamoDb
        User user = dynmaoDbDao.getRecord(username);

        //get users current accountBalance
        Double currentBalance = user.getAccountBalance();

        //check if accountBalance has enough balance for purchase
        //if yes, reduce the balance by price
        //else throw Insufficient funds exception
        if(currentBalance >= price) {
            currentBalance -= price;
        } else {
            throw new RuntimeException("Insufficient funds");
        }

        //update the balance to user object
        user.setAccountBalance(currentBalance);

        //save updated user to dynamoDb and return the updated balance
        dynmaoDbDao.saveRecord(user);
        return currentBalance;
    }

    /**
     * Method to activate the user for a given username
     * Get the user from dynamoDb and update isActive to true
     * if isActive is already true - return Already activated exception
     * update the record back to database and return true
     * @param username
     * @return
     */
    public Boolean activateUser(String username) {
        //get user from dynamoDb
        User user = dynmaoDbDao.getRecord(username);

        //check if isActive is already true - return Already activated exception
        if(user.isActive()) {
            throw new RuntimeException("Already activated");
        }

        //update the record back to database and return true
        user.setActive(true);
        dynmaoDbDao.saveRecord(user);
        return true;
    }
}
