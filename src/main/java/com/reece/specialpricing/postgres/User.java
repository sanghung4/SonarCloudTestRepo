package com.reece.specialpricing.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users", schema="special_pricing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false)
    private String userId;

    @Column(name = "username", nullable = false)
    private String userName;

    @OneToMany (fetch = FetchType.EAGER)
    @JoinColumn (name = "\"userId\"")
    List<UserBranch> branches;

}
