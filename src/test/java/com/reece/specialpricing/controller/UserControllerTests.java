package com.reece.specialpricing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.specialpricing.model.pojo.BranchUsersListResponse;
import com.reece.specialpricing.model.pojo.ErrorBlock;
import com.reece.specialpricing.postgres.*;
import com.reece.specialpricing.service.UserService;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests extends TestCommon {
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserBranchRepository userBranchRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;


    @Before
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new CustomExceptionHandler()).build();
    }

    @BeforeEach
    public void setUp() {
        reset(userService);
    }


    @Test
    public void GET_getUserBranchReturnsOk() throws Exception {
        final String USERS_URL = "/users/" + USER_TEST_1.get().getUserName();
        List<String> branchIdList = new ArrayList<String>();
        branchIdList.add(USER_TEST_1.get().getBranches().get(0).getBranchId());
        when(userService.getUserBranchIdList(any())).thenReturn(branchIdList);
        mockMvc.perform(get(USERS_URL)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(USER_TEST_1.get().getUserName()))
                .andExpect(jsonPath("$.branch").value(USER_TEST_1.get().getBranches().get(0).getBranchId()));

        verify(userService, times(1)).getUserBranchIdList(eq(USER_TEST_1.get().getUserName()));
    }

    @Test
    public void POST_addUserEmailSuccess() throws Exception {
        User user = new User();
        final String ADDUSER_URL = "/users/1001/test@morsco.com/";
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
        mockMvc.perform(post(ADDUSER_URL).accept(contentType))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verify(userService, times(1)).addUser("1001","test@morsco.com");
    }

    @Test
    public void POST_addUserEmailFailure() throws Exception {
        ErrorBlock errorBlock = new ErrorBlock();
        when(userService.addUser(anyString(),anyString())).thenReturn(errorBlock);
        mockMvc.perform(post("/users/{branchId}/{userEmail}", "1001","test@rece.com"))
                .andExpect(status().isNotAcceptable()).andReturn();
        verify(userService, times(1)).addUser("1001","test@rece");

    }

    @Test
    public void PUT_updateUserEmailSuccess() throws Exception {
        User user = new User();
        final String UPDATE_USER_EMAIL = "/users/test@morsco.com/test1@morsco.com/";
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
        mockMvc.perform(put(UPDATE_USER_EMAIL).accept(contentType))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verify(userService, times(1)).updateUserEmail("test@morsco.com","test1@morsco.com");
    }

    @Test
    public void PUT_updateUserEmailFailure() throws Exception {
        ErrorBlock errorBlock = new ErrorBlock();
        when(userService.updateUserEmail(anyString(),anyString())).thenReturn(errorBlock);
        mockMvc.perform(put("/users/{oldUserEmail}/{NewUserEmail}", "test1@morsco.com","test@rece.com"))
                .andExpect(status().isNotAcceptable()).andReturn();
        verify(userService, times(1)).updateUserEmail("test1@morsco.com","test@rece");

    }

    @Test
    public void DELETE_removeUserBranchSuccess() throws Exception {
        UserBranch userBranch=new UserBranch();
        final String REMOVE_USERBRANCH_URL = "/users/1001/test@morsco.com/";
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
        mockMvc.perform(delete(REMOVE_USERBRANCH_URL).accept(contentType))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verify(userService, times(1)).removeUserBranch("1001","test@morsco.com");
    }

    @Test
    public void DELETE_removeUserBranchFailure() throws Exception {
        ErrorBlock errorBlock = new ErrorBlock();
        when(userService.removeUserBranch(anyString(),anyString())).thenReturn(errorBlock);
        mockMvc.perform(delete("/users/{branchId}/{userEmail}", "1001","test@reece.com"))
                .andExpect(status().isNotAcceptable()).andReturn();
        verify(userService, times(1)).removeUserBranch("1001","test@reece");

    }

    @Test
    public void DELETE_deleteUserSuccess() throws Exception {
        User user=new User();
        UserBranch userBranch=new UserBranch();
        final String DELETE_USER_URL = "/users/test@morsco.com/";
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
        userRepository.deleteById(user.getUserId());
        mockMvc.perform(delete(DELETE_USER_URL).accept(contentType))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        verify(userService, times(1)).deleteUser("test@morsco.com");
    }

    @Test
    public void DELETE_deleteUserFailure() throws Exception {
        ErrorBlock errorBlock = new ErrorBlock();
        when(userService.deleteUser(anyString())).thenReturn(errorBlock);
        mockMvc.perform(delete("/users/{userEmail}", "test@reece.com"))
                .andExpect(status().isNotAcceptable()).andReturn();
        verify(userService, times(1)).deleteUser("test@reece");

    }

    @Test
    public void GET_getBranchUsersListReturnsOk() throws Exception {
        final String BRANCH_USER_URL = "/users/branchUsersList";
        BranchUsersListResponse branchUsersListResponse = new BranchUsersListResponse("1039", List.of("malvarez"));
        List<BranchUsersListResponse> branchUsersListResponseList = new ArrayList<>();
        branchUsersListResponseList.add(branchUsersListResponse);
        when(userService.getBranchUsersListResponses()).thenReturn(branchUsersListResponseList);
        mockMvc.perform(get(BRANCH_USER_URL)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).getBranchUsersListResponses();
    }

}
