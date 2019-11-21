package com.alibaba.service;

import com.alibaba.bean.Result;
import com.alibaba.bean.User;
import com.alibaba.mapper.UserMapper;
import com.alibaba.utiles.MD5;
import com.alibaba.utiles.RandomValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 注册
     * @param user 参数封装
     * @return Result
     */
    public Result regist(User user) {
        Result result = new Result();
        result.setCode(null);
        result.setMsg(null);
        result.setData(null);
        try {
            User existUser = userMapper.findUserByName(user.getUsername());
            if(existUser != null){
                //如果用户名已存在
                result.setCode("5");
                result.setMsg("用户名已存在");
            }else{
                //把正常密码转为 Md5密文
                user.setPassword(MD5.getMd5(user.getPassword()));
                userMapper.regist(user);
                //System.out.println(user.getId());
                result.setCode("4");
                result.setMsg("注册成功");
                result.setData(user);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 登录
     * @param user 用户名和密码
     * @return Result
     */
    public Result login(User user, HttpServletRequest res) {
        Result result = new Result();
        result.setCode(null);
        result.setMsg(null);
       result.setData(null);
        try {
            //验证码的检验
            HttpSession session = res.getSession();
            String random = (String) session.getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            String code = user.getYanzheng();
            System.out.println("Codeqian"+code+"后"+random);

            if (!random.equals(code)) {
                result.setCode("2");
                result.setMsg("验证码错误!");
            }else {

                String yanzheng = user.getYanzheng();
                //把Md5密文 转为正常密码
                user.setPassword(MD5.getMd5(user.getPassword()));
                Long userId = userMapper.login(user);
                if (userId == null ||userId.equals("")) {
                    result.setCode("1");
                    result.setMsg("用户名或密码错误");
                } else {
                    result.setCode("0");
                    result.setMsg("登录成功");
                    user.setId(userId);
                    result.setData(user);
                }
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
