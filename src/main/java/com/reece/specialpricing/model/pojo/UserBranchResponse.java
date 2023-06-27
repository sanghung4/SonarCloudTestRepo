package com.reece.specialpricing.model.pojo;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBranchResponse {
    private String username;
    private List<String> branch;
}
