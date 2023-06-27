package com.reece.specialpricing.controller;

import com.reece.specialpricing.model.pojo.*;
import com.reece.specialpricing.postgres.*;

import com.reece.specialpricing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserBranchResponse> getUserBranches(@PathVariable String userName) {
        List<String> userBranchIdList = userService.getUserBranchIdList(userName);
        ResponseEntity<UserBranchResponse> responseEntity;
        if (!CollectionUtils.isEmpty(userBranchIdList)) {
            UserBranchResponse response = new UserBranchResponse(userName, userBranchIdList);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping(value = "branchUsersList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<BranchUsersListResponse>> getBranchUsersList() {
        List<BranchUsersListResponse> branchUsersListResponses = userService.getBranchUsersListResponses();

        if (!CollectionUtils.isEmpty(branchUsersListResponses)) {
            return new ResponseEntity<>(branchUsersListResponses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{branchId}/{userEmail}")
    public ErrorBlock addUser(@PathVariable String branchId, @PathVariable String userEmail) {
        return userService.addUser(branchId, userEmail);
    }


    @PutMapping(value = "/{oldEmail}/{newEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorBlock updateUserEmail(@PathVariable String oldEmail,
                                      @PathVariable String newEmail) {
        return userService.updateUserEmail(oldEmail, newEmail);
    }


    @DeleteMapping(value = "/{branchId}/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorBlock removeUserBranch(@PathVariable String branchId, @PathVariable String userEmail) {
        return userService.removeUserBranch(branchId, userEmail);
    }


    @DeleteMapping(value = "/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorBlock deleteUser(@PathVariable String userEmail) {
        return userService.deleteUser(userEmail);
    }
}