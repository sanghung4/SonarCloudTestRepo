package com.reece.specialpricing.service;

import com.reece.specialpricing.model.pojo.ErrorBlock;
import com.reece.specialpricing.postgres.*;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserBranchRepository userBranchRepository;

    @BeforeEach
    public void setUp() {
        reset(userService);
    }


    ErrorBlock errorBlock = new ErrorBlock();
    ErrorBlock.Error e = errorBlock.new Error("ERPCP-1001", "UserEmail does not exist", null);
    ErrorBlock.Error e1 = errorBlock.new Error("ERPCP-1002", "UserEmail not valid", null);
    ErrorBlock.Error e2 = errorBlock.new Error("ERPCP-1003", "User already exist with given branchId", null);
    ErrorBlock.Error e3 = errorBlock.new Error("ERPCP-1004", "BranchId does not exist", null);
    ErrorBlock.Error e4 = errorBlock.new Error("ERPCP-1005", "UserEmail and branchId not exist", null);
    ErrorBlock.Error e5 = errorBlock.new Error("ERPCP-1006", "NewUserEmail already exist", null);


    @Test
    public void whenUserEmailIsWrong_addUser() {

        Assert.assertEquals(userServiceImpl.addUser("1009", "xyz"), new ErrorBlock(false, e1));

    }

    @Test
    public void whenBranchIdNotExist_addUser() {

        Assert.assertEquals(userServiceImpl.addUser("109", "xyz@morsco.com"), new ErrorBlock(false, e3));

    }

    @Test
    public void whenUserEmailIsWrong_updateUser() {
        Assert.assertEquals(userServiceImpl.addUser("xyz@morsco.com", "xyz"), new ErrorBlock(false, e1));
    }

    @Test
    public void whenUserEmailNotExist_updateUser() {
        Assert.assertEquals(userServiceImpl.updateUserEmail("xyz@morsco.com", "xyz1@morsco.com"), new ErrorBlock(false, e));
    }

    @Test
    public void whenUserEmailNotExist_removeUser() {
        Assert.assertEquals(userServiceImpl.removeUserBranch("1009", "xyz@morsco.com"), new ErrorBlock(false, e));
    }

    @Test
    public void whenUserEmailNotExist_deleteUser() {
        Assert.assertEquals(userServiceImpl.deleteUser("xyz@morsco.com"), new ErrorBlock(false, e));
    }

    @Test
    public void getUserBranchIdList_shouldReturnUserBranchIdList() {
        when(userRepository.getOneByUserName(any())).thenReturn(new TestCommon().USER_TEST_1);
        var result = userServiceImpl.getUserBranchIdList(new TestCommon().USER_TEST_1.get().getUserName());
        assert result.size() == 1;
    }

    @Test
    public void getBranchUsersListResponses_shouldReturnBranchUsersListResponses() {
        UserBranch userBranch = new UserBranch("1000", "1039");
        User user = new User("", "", List.of(userBranch));
        when(userRepository.findAll()).thenReturn(List.of(user));
        var result = userServiceImpl.getBranchUsersListResponses();
        assert result.size() == 1;
    }
}
