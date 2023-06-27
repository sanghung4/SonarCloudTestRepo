package com.reece.specialpricing.service;



import com.reece.specialpricing.model.pojo.BranchUsersListResponse;
import com.reece.specialpricing.model.pojo.BranchUsersResponse;
import com.reece.specialpricing.model.pojo.ErrorBlock;
import com.reece.specialpricing.postgres.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserBranchRepository userBranchRepository;

    ErrorBlock errorBlock=new ErrorBlock();
    ErrorBlock.Error e=errorBlock.new Error("ERPCP-1001","UserEmail does not exist",null);
    ErrorBlock.Error e1=errorBlock.new Error("ERPCP-1002","UserEmail not valid",null);
    ErrorBlock.Error e2=errorBlock.new Error("ERPCP-1003","User already exist with given branchId",null);
    ErrorBlock.Error e3=errorBlock.new Error("ERPCP-1004","BranchId does not exist",null);
    ErrorBlock.Error e4=errorBlock.new Error("ERPCP-1005","UserEmail and branchId not exist",null);
    ErrorBlock.Error e5=errorBlock.new Error("ERPCP-1006","NewUserEmail already exist",null);

    static final String MORSCO_DOMAIN="@morsco.com";
    static final String REECE_DOMAIN="@reece.com";
    static final String DIALEXA_DOMAIN="@dialexa.com";
    @Override
    public ErrorBlock addUser(String branchId, String userEmail) {
        if(userEmail.endsWith(MORSCO_DOMAIN) ||userEmail.endsWith(REECE_DOMAIN) || userEmail.endsWith(DIALEXA_DOMAIN))
        {
            User user = new User();
            UserBranch userBranch = new UserBranch();

            if (userRepository.findByUserName(userEmail) == null) {
                user.setUserName(userEmail);
                userRepository.save(user);
            }
            User userName = userRepository.findByUserName(userEmail);
            if(branchRepository.existsById(branchId)){
                if (userBranchRepository.findByBranchIdAndUserId(branchId, userName.getUserId()) == null) {
                    userBranch.setUserId(userName.getUserId());
                    userBranch.setBranchId(branchId);
                    userBranchRepository.save(userBranch);

                } else {
                    return new ErrorBlock(false,e2);
                }
            } else {
                return new ErrorBlock(false,e3);
            }
            return new ErrorBlock(true,null);
        }
        else
        {
            return new ErrorBlock(false,e1);
        }

    }

    @Override
    public ErrorBlock updateUserEmail(String oldEmail, String newEmail) {
        if((oldEmail.endsWith(MORSCO_DOMAIN) || oldEmail.endsWith(REECE_DOMAIN) || oldEmail.endsWith(DIALEXA_DOMAIN)) &&
                (newEmail.endsWith(MORSCO_DOMAIN) || newEmail.endsWith(REECE_DOMAIN) || newEmail.endsWith(DIALEXA_DOMAIN))) {
            User updateEmployee = userRepository.findByUserName(oldEmail);
            User updateEmployee1 =userRepository.findByUserName(newEmail);
            if (updateEmployee == null) {
                return new ErrorBlock(false, e);
            }

            else if(updateEmployee1==null) {
                updateEmployee.setUserName(newEmail);
                userRepository.save(updateEmployee);

                return new ErrorBlock(true, null);
            }
            else {
                return new ErrorBlock(false,e5);
            }
        }
        else {
            return new ErrorBlock(false,e1);
        }
    }

    @Override
    public ErrorBlock removeUserBranch(String branchId, String userEmail) {
        User user=new User();
        User deleteEmployee = userRepository.findByUserName(userEmail);

        if(deleteEmployee==null)
        {
            return new ErrorBlock(false,e);
        }
        else if(!branchRepository.existsById(branchId))
        {
            return new ErrorBlock(false,e3);
        } else if (userBranchRepository.findByBranchIdAndUserId(branchId, deleteEmployee.getUserId()) == null) {
            return new ErrorBlock(false,e4);
        } else {
            userBranchRepository.deleteById(new UserBranchId(deleteEmployee.getUserId(), branchId));
        }

        return new ErrorBlock(true,null);
    }

    @Override
    public ErrorBlock deleteUser(String userEmail) {
        User user=userRepository.findByUserName(userEmail);

        if(user==null)
        {
            return new ErrorBlock(false,e);
        }
        else {

            userBranchRepository.deleteByUserId(user.getUserId());
            userRepository.deleteById(user.getUserId());
        }

        return new ErrorBlock(true,null);
    }

    @Override
    public List<String> getUserBranchIdList(String userName) {
        List<String> userBranchIdList = null;
        Optional<User> user = userRepository.getOneByUserName(userName);
        if (user.isPresent()) {
            List<UserBranch> branches = user.get().getBranches();
            if (!branches.isEmpty()) {
                userBranchIdList = branches.stream()
                        .map(UserBranch::getBranchId).collect(Collectors.toList());
            }
        }
        return userBranchIdList;
    }

    @Override
    public List<BranchUsersListResponse> getBranchUsersListResponses() {
        List<BranchUsersListResponse> branchUsersListResponses = new ArrayList<>();
        List<User> users = userRepository.findAll();

        if (!CollectionUtils.isEmpty(users)) {
            List<BranchUsersResponse> branchUsersResponses = users.stream()
                    .flatMap(user -> user.getBranches()
                            .stream()
                            .filter(Objects::nonNull)
                            .map(userBranch -> new BranchUsersResponse(userBranch.getBranchId(), user.getUserName())))
                    .collect(Collectors.toList());


            Map<String, List<String>> stringListMap = branchUsersResponses.stream()
                    .collect(Collectors.groupingBy(BranchUsersResponse::getBranchId,
                            Collectors.mapping(BranchUsersResponse::getUserName, Collectors.toList())));

            branchUsersListResponses = stringListMap.entrySet()
                    .stream()
                    .map(e -> new BranchUsersListResponse(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }
        return branchUsersListResponses;
    }
}
