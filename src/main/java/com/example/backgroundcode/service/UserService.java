package com.example.backgroundcode.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backgroundcode.Exception.ServiceException;
import com.example.backgroundcode.common.Constants;
import com.example.backgroundcode.dao.UserMapper;
import com.example.backgroundcode.entity.User;
import com.example.backgroundcode.entity.dto.UserDTO;
import com.example.backgroundcode.untils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class UserService extends ServiceImpl<UserMapper,User>  {
    private UserMapper userMapper;
    private static final Log LOG = Log.get();

    public List<User> getAll(){;
        return list();
    }
    public boolean deleteById(Integer id){
        return removeById(id);
    }

    //登录
    public UserDTO login(UserDTO userDTO){
        User loginUser = getUserInfo(userDTO);
        if(loginUser!=null){
            BeanUtil.copyProperties(loginUser, userDTO, true);
            String token = TokenUtils.genToken(loginUser.getId().toString(),loginUser.getPassword());
            userDTO.setToken(token);
            return userDTO;
        }else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }


    //注册
    public User register(UserDTO userDTO){
        User registerUser = getUserInfo(userDTO);
        if(registerUser==null){
            registerUser = new User();
            BeanUtil.copyProperties(userDTO,registerUser,true);
            save(registerUser);
        }else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return registerUser;

    }

    public User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userDTO.getUsername());
        wrapper.eq("password",userDTO.getPassword());
        try {
            User user = getOne(wrapper);
            return user;
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
    }

    public User finOneByName(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return getOne(wrapper);
    }




    public boolean insert(User user) {
        return saveOrUpdate(user);
    }
}
