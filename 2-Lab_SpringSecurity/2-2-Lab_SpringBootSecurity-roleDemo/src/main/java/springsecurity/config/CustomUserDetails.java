package springsecurity.config;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetails implements UserDetailsService {



    //根据 账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**
         * 以下这对段code是模拟真实场景：
         *
         *  //将来连接数据库根据账号查询用户信息
         *  UserDto userDto = userDao.getUserByUsername(username);
         *  if(userDto == null){
         *      //如果用户查不到，返回null，由provider来抛出异常
         *      return null;
         *  }
         *  //根据用户的id查询用户的权限
         *  List<String> permissions = userDao.findPermissionsByUserId(userDto.getId());
         *  //将permissions转成数组，重点！
         *  String[] permissionArray = new String[permissions.size()];
         *  permissions.toArray(permissionArray);
         *  UserDetails userDetails = User.withUsername(userDto.getUsername()).password(userDto.getPassword()).authorities(permissionArray).build();
         */

        //模拟已经从DB中查出的数据
        UserDetails userDetails = User
                .withUsername("abiao")
                .password("$2a$10$DWa.hJqYkfGUXFZjQggPhursXjSE4mEc9Jy9l1XPCT/4qLpwfkmk2")
                .authorities("ADMIN").build();
        return userDetails;
    }
}
