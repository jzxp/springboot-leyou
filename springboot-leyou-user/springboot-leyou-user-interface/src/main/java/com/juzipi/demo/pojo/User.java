package com.juzipi.demo.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Data
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4,max = 30,message = "用户名长度在4-30为之间")
    private String username;// 用户名

    @Length(min = 4,max = 30,message = "密码长度在4-30为之间")
    @JsonIgnore//对象序列化为json字符串时，忽略该属性
    private String password;// 密码

    //专业: ^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[6-7])|(17[1-8])|(18[0-9])|(19[1|3])|(19[5|6])|(19[8|9]))\d{8}$
    @Pattern(regexp = "^1[356789]\\d{9}$",message = "手机号不合法")
    private String phone;// 电话

    private Date created;// 创建时间

    @JsonIgnore
    private String salt;// 密码的盐值
}