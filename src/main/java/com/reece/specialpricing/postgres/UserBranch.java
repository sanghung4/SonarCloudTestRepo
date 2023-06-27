package com.reece.specialpricing.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_branch", schema="special_pricing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserBranchId.class)
public class UserBranch implements Serializable {

    @Id
    @Column(name = "\"userId\"", nullable = false)
    private String userId;

    @Id
    @Column(name = "\"branchId\"", nullable = false)
    private String branchId;


}
