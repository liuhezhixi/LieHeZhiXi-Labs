package com.itheima.security.dao;

import com.itheima.security.model.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuguanshen
 * @create 2023/7/3
 * @description
 */
//@Repository
@Component
public class UserDao {

    //@Autowired
    //JdbcTemplate jdbcTemplate;

    //根据账号查询用户信息
    public UserDto getUserByUsername(String username){
        //连接数据库查询用户
/*        String sql = "select id,username,password,fullname,mobile from t_user where username = ?";
        List<UserDto> list = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(UserDto.class));
        if(list !=null && list.size()==1){
            return list.get(0);
        }
        return null;*/

        List<UserDto> list = new ArrayList<>();
        list.add(UserDto.builder().id("1").username("abiao1").password("abiao1").fullname("阿标1").mobile("111").build());
        list.add(UserDto.builder().id("2").username("abiao2").password("abiao2").fullname("阿标2").mobile("222").build());

        for (UserDto userDto : list) {
            if (userDto.getUsername().equals(username)) {
                return userDto;
            }
        }
        return null;
    }

    //根据用户id查询用户权限
    public List<String> findPermissionsByUserId(String userId){
        //连接数据库查询权限
/*        String sql = "SELECT * FROM t_permission WHERE id IN(\n" +
                "\n" +
                "SELECT permission_id FROM t_role_permission WHERE role_id IN(\n" +
                "  SELECT role_id FROM t_user_role WHERE user_id = ? \n" +
                ")\n" +
                ")\n";

        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));*/


        return null;
    }
}
