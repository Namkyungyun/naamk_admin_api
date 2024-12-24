package kr.co.naamkbank.domain;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class TbUserRoleIds implements Serializable {
    private Long user;
    private Long role;
}
