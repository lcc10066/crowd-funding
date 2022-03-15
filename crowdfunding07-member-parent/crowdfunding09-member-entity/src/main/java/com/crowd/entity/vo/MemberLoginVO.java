package com.crowd.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberLoginVO implements Serializable {

    private static final long serialVersionUID = 11111111L;

    private Integer id;
    private String username;
    private String email;
}
