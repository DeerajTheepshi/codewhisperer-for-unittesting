package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Inject
    DynamoDbDao dynamoDbDao;

    @InjectMocks
    UserService userService;

    /**
     * Test class to test activateUser method
     * when user with given username is present in database with isActive as false
     * assert if method returns true
     * verify if dynamoDb save was called with updated user object
     */
    @Test
    public void testActivateUser_whenUserIsPresent_withIsActiveFalse() {
        //given
        //construct user object using builder pattern

        User user = User.builder()
                .username("XXXX")
                .isActive(false)
                .build();

        //when
        when(dynamoDbDao.getRecord(user.getUsername())).thenReturn(user);

        boolean result = userService.activateUser(user.getUsername());


        //then
        Assertions.assertTrue(result);
        verify(dynamoDbDao).saveRecord(user);
    }

    /**
     * Test for updateBalance method
     * when user with username is present in database with accountBalance double more than price
     * assert if method returns updated balance
     * verify if dynamoDb save was called with updated user object
     */
    @Test
    public void testUpdateBalance_whenUserIsPresent_withBalanceMoreThanPrice() {
        //given
        //construct user object using builder pattern

        User user = User.builder()
                .username("XXXX")
                .accountBalance(100.0)
                .build();

        //when
        when(dynamoDbDao.getRecord(user.getUsername())).thenReturn(user);

        double result = userService.updateBalance(user.getUsername(), 50.0);

        //then
        Assertions.assertEquals(50.0, result);
        verify(dynamoDbDao).saveRecord(user);
    }

    /**
     * Test for updateBalance method
     * when user with username is present in database with not enough balance
     * assert if exception is thrown
     * verify if dynamoDb save operation is not called
     */
    @Test
    public void testUpdateBalance_whenUserIsPresent_withBalanceLessThanPrice() {
        //given
        //construct user object using builder pattern

        User user = User.builder()
                .username("XXXX")
                .accountBalance(100.0)
                .build();

        //when
        when(dynamoDbDao.getRecord(user.getUsername())).thenReturn(user);

        //then
        Assertions.assertThrows(RuntimeException.class, () -> userService.updateBalance(user.getUsername(), 150.0));
        verify(dynamoDbDao, never()).saveRecord(user);
    }   

}
