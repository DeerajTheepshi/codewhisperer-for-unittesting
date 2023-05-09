package org.example;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import static org.mockito.Mockito.*;


/**
 * Unit test class for DynamoDbDao
 */
@ExtendWith(MockitoExtension.class)
public class DynamoDbDaoTest {
    //Initialize mocks for DynamoDbDao

    @Inject
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private DynamoDbDao dynamoDbDao;

    //Test DynmaoDbDao.getRecord() with username as hashKey
    // when dynamoDbMapper returns a value
    @Test
    public void testGetRecord_whenDynamoDbMapperReturnsAValue() {
        //Given
        String username = "XXXXXXXX";
        User user = new User();
        when(dynamoDBMapper.load(User.class, username)).thenReturn(user);

        //When
        User result = dynamoDbDao.getRecord(username);

        //Then
        Assertions.assertEquals(user, result);
    }

    //Test DynmaoDbDao.getRecord() with username as hashKey
    // when dynamoDbMapper throws an exception
    @Test
    public void testGetRecord_whenDynamoDbMapperThrowsAnException() {
        //Given
        String username = "XXXXXXXX";
        when(dynamoDBMapper.load(User.class, username)).thenThrow(new RuntimeException());

        //When
        Assertions.assertThrows(RuntimeException.class, () -> dynamoDbDao.getRecord(username));

        //Then
        verify(dynamoDBMapper, times(1)).load(User.class, username);
    }

    //Test DynmaoDbDao.getRecord() with username
    // when dynamoDbMapper returns null
    // assert exception
    @Test
    public void testGetRecord_whenDynamoDbMapperReturnsNull() {
        //Given
        String username = "XXXXXXXX";
        when(dynamoDBMapper.load(User.class, username)).thenReturn(null);

        //When
        Assertions.assertThrows(RuntimeException.class, () -> dynamoDbDao.getRecord(username));

        //Then
        verify(dynamoDBMapper, times(1)).load(User.class, username);
    }

    //Test DynmaoDbDao.saveRecord() with user
    // when dynamoDbMapper runs successfully
    @Test
    public void testSaveRecord_whenDynamoDbMapperRunsSuccessfully() {
        //Given
        //Mock user with builder
        User user = User.builder()
                .username("XXXXXXXX")
                .password("XXXXXXXX")
                .build();

        //When
        dynamoDbDao.saveRecord(user);

        //Then
        verify(dynamoDBMapper, times(1)).save(user);
        //Assert username and password
        Assertions.assertEquals(user.getUsername(), "XXXXXXXX");
        Assertions.assertEquals(user.getPassword(), "XXXXXXXX");
    }

    //Test DynmaoDbDao.saveRecord() with user
    // when dynamoDbMapper throws exception
    @Test
    public void testSaveRecord_whenDynamoDbMapperThrowsException() {
        //Given
        //Mock user with builder
        User user = User.builder()
                .username("XXXXXXXX")
                .password("XXXXXXXX")
                .build();

        //When
        doThrow(new RuntimeException()).when(dynamoDBMapper).save(user);

        //Then
        Assertions.assertThrows(RuntimeException.class, () -> dynamoDbDao.saveRecord(user));
        verify(dynamoDBMapper, times(1)).save(user);
    }
}