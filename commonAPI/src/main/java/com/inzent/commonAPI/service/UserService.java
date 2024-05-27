package com.inzent.commonAPI.service;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.commonAPI.common.Encrypt;
import com.inzent.commonAPI.mapper.LoginMapper;
import com.inzent.commonAPI.vo.UserVO;
import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.Pagination;
import com.inzent.commonMethod.common.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.inzent.commonAPI.mapper.UserMapper;

/**
 * 사용자 관리 서비스
 *
 * @author 장윤하
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private LoginMapper loginMapper;

    @Value("${API_KEY}")
    private String apiKey;

    //리스트 생성(비밀번호 초기화)->5분
    public static Map<String, Object> pwResetMap = new HashMap<String, Object>();
    private long resetDateValidTime = 5 * 60 * 1000L;    // 초기화 메일 발송 시간 5분: 5분 전에는 초기화를 또 진행할 수 없다.

    public ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 사용자 조회
    public Map<String, Object> getUserSearchKeyword(String userEmail, String userName, String codeName, Integer pageNum, String type, String teamId) {
        if (type != null && type.equals("email")) {
            UserVO userVO = new UserVO(null, null, userEmail, null, null, null, null, null, null,null,null);
            return ResponseMessage.setMessage(Consts.SUCCESS, userMapper.getSelectUserInfo(userVO, Pagination.setPage(null)));
        } else {
            UserVO userVO = new UserVO(null, userName, userEmail, teamId, null, null, null, codeName, null,null,null);
            return ResponseMessage.setMessage(Consts.SUCCESS, userMapper.getUserSearchKeyword(userVO, Pagination.setPage(pageNum)));
        }
    }

    // 사용자 등록
    public Map<String, Object> insertUser(Map<String, Object> userMap) throws Exception {
        UserVO userVO = objectMapper.convertValue(userMap.get(Consts.REQUEST_MESSAGE), UserVO.class);
        String userEmail = userVO.getUserEmail();//유저 이메일
        if (userMapper.getSelectUserName(userEmail,"Y") != null) {
            return ResponseMessage.setMessage(Consts.ERROR, "삭제된 사용자입니다. 관리자에게 문의해 주세요.");
        }
        if (userMapper.getSelectUserName(userEmail,"N") == null) {
            userMapper.insertUser(pwResetEmailSend(userEmail, userVO, apiKey));
            return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
        }else{
            return ResponseMessage.setMessage(Consts.ERROR, "이메일이 중복되었습니다.");
        }
    }

    //사용자 수정
    public Map<String, Object> updateUser(Map<String, Object> userMap) {
        Map<String,Object> getRequestMessage = (Map<String, Object>) userMap.get(Consts.REQUEST_MESSAGE);
        UserVO userVO = objectMapper.convertValue(userMap.get(Consts.REQUEST_MESSAGE), UserVO.class);

        if(Integer.parseInt(String.valueOf(getRequestMessage.get("login"))) <= Integer.parseInt(String.valueOf(getRequestMessage.get("preAuthorityId"))) ){
            if(userVO.getUserPassword() == null){
                userMapper.updateUser(userVO);
                return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
            }else{
                userVO.setUserPassword(Encrypt.encrypt(userVO.getUserPassword()));
                if(!loginMapper.loginConfirm(userVO).equals("1")){
                    return ResponseMessage.setMessage(Consts.ERROR, "비밀번호가 일치하지 않습니다.");
                }else{
                    userVO.setUserPassword(Encrypt.encrypt((String) getRequestMessage.get("newPassword")));
                    userMapper.updateUserPassword(userVO);
                    return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
                }
            }
        }else{
            return ResponseMessage.setMessage(Consts.ERROR,"상위권한의 사용자는 수정할 수 없습니다.");
        }

    }

    // 비밀번호 초기화
    public Map<String, Object> updateUserPasswordReset(Map<String, Object> userMap, String apiKey){
        UserVO userVO = objectMapper.convertValue(userMap.get(Consts.REQUEST_MESSAGE), UserVO.class);
        boolean resetBoolean = true;
        String responseCode = Consts.SUCCESS;
        String responseMessage = Consts.NO_ERROR;
        String userEmail = userVO.getUserEmail(); //이메일 중복 체크
        String userExistCheck = userMapper.getSelectUserName(userEmail,"N");

        try {
        if (userExistCheck != null) { //이메일이 DB data에 존재할 경우(회원이 맞다->초기화 진행->메일 발송)
            userVO.setUserName(userExistCheck);
            if (pwResetMap.get(userEmail) != null) {
                long expireDate = (long) pwResetMap.get(userVO.getUserEmail());
                long now = new Date().getTime(); //현재 시간
                if (now <= expireDate) {
                    resetBoolean = false;
                    responseCode = Consts.ERROR;
                    responseMessage = "5분이 지난 후 다시 초기화를 진행해 주세요.";
                }
            }
            if (resetBoolean) {
                pwResetEmailSend(userEmail, userVO, apiKey);//이메일 발송
                userMapper.updateUserPassword(userVO);
                pwResetMap.put(userVO.getUserEmail(), new Date().getTime() + resetDateValidTime);
            }

        } else {
            return ResponseMessage.setMessage(Consts.ERROR, "이메일이 존재하지 않습니다.");
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMessage.setMessage(responseCode, responseMessage);
    }

    //사용자 삭제
    public Map<String, Object> deleteUser(String userEmail, String authorityId, String login) {
        if(Integer.parseInt(login) <= Integer.parseInt(authorityId)){
            userMapper.deleteUser(userEmail);
            return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
        }else{
            return ResponseMessage.setMessage(Consts.ERROR,"상위권한의 사용자는 삭제할 수 없습니다.");
        }
    }

    //랜덤 비밀번호 생성
    public String pwRandom() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        String str = "";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }

        return str;
    }

    //이메일 발송
    public UserVO pwResetEmailSend(String userEmail, UserVO userVO, String apiKey) throws JsonProcessingException {
        String randomPassword = pwRandom(); // 랜덤 비밀번호 생성

        Map<String, Object> sendInfoMap = new HashMap<String, Object>();
        List<Object> addSendContent = new ArrayList<>();
        addSendContent.add(userVO.getUserName());
        addSendContent.add(randomPassword);
        sendInfoMap.put("sendTo", userEmail);
        sendInfoMap.put("sendContent", addSendContent);

        String sendInfoJSON = objectMapper.writeValueAsString(sendInfoMap);
        //{"sendTo":"user_email", "sendContent": [user_name, passeword]}
        mailService.sendMail(apiKey, sendInfoJSON);

        userVO.setUserPassword(Encrypt.encrypt(randomPassword)); // 비밀번호 암호화
        return userVO;

    }

    //이메일 삭제 스케줄러
    @Scheduled(cron = "0 0 2 * * *")
    public void removeTokenSchdule() {
        // 매일 2시 스케줄 실행하여 만료된 이메일 삭제
        List<String> removeList = new ArrayList<String>();

        long now = new Date().getTime();
        for (String key : pwResetMap.keySet()) {
            long expireDate = (long) pwResetMap.get(key);
            if (now > expireDate) {
                removeList.add(key);
            }
        }

        for (String removeEmailKey : removeList) {
            // pwResetMap 안에 5분이 지난 이메일 값 삭제
            if (pwResetMap.get(removeEmailKey) != null) {
                pwResetMap.remove(removeEmailKey);
            }
        }
        logger.debug("Remove Expiration Email Schedule pwResetMap Size : " + pwResetMap.size());
    }

}