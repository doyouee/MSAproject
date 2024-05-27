package com.inzent.commonAPI.controller;

import com.inzent.commonAPI.service.UserService;
import com.inzent.commonAPI.vo.UserVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 사용자 관리 CRUD
 *
 * @author 장윤하
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public @ResponseBody Map<String, Object> getUserSearchKeyword(@RequestParam(value="pageNum", required = false) Integer pageNum ,@RequestParam(required = false) String userEmail, @RequestParam(required = false) String userName, @RequestParam(required = false) String codeName, @RequestParam(required = false) String type, @RequestParam(required = false) String teamId) throws Exception {
        try {
            return userService.getUserSearchKeyword(userEmail, userName, codeName, pageNum, type, teamId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString().substring(0, e.toString().indexOf(":")));
        }
    }

    @PostMapping("/user")
    public @ResponseBody Map<String, Object> insertUser(@RequestBody Map<String, Object> userMap) throws Exception {
        try {
            return userService.insertUser(userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString().substring(0, e.toString().indexOf(":")));
        }
    }

    @PutMapping("/user")
    public @ResponseBody Map<String, Object> updateUser(@RequestBody Map<String, Object> userMap) throws Exception {
        try {
            return userService.updateUser(userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString().substring(0, e.toString().indexOf(":")));
        }

    }

    @DeleteMapping("/user")
    public @ResponseBody Map<String, Object> deleteUser(@Param("userEmail") String userEmail,
                                                        @Param("authorityId") String authorityId,
                                                        @Param("login") String login) throws Exception {
        try {
            return  userService.deleteUser(userEmail,authorityId,login);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString().substring(0, e.toString().indexOf(":")));
        }
    }

    @PutMapping("/userPasswordReset")
    public @ResponseBody Map<String, Object> updateUserPasswordReset(@RequestBody Map<String, Object> userMap, @RequestHeader(value = "API_KEY") String apiKey) throws Exception {
        try {
            return userService.updateUserPasswordReset(userMap, apiKey);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.toString().substring(0, e.toString().indexOf(":")));
        }
    }

}