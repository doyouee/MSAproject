package com.inzent.commonAPI.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.inzent.commonMethod.common.Json;
import com.inzent.commonAPI.mapper.MailMapper;
import com.inzent.commonAPI.vo.MailHistoryVO;
import com.inzent.commonAPI.vo.MailInfoVO;

import jakarta.mail.internet.MimeMessage;

/**
 * 메일 전송
 * @author 조유진
 */

@Service
public class MailService {

    @Autowired
    private MailMapper mailMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    private String sendTo = "";
    private String mailTitle = "";
    private String mailContent = "";
    private String mailStatus = "";

    public String sendMail(String apiKey, String sendInfo) {
        /*
         * sendInfo Sample Data
         * {"sendTo":"jyj0108@inzent.com,haein0322@inzent.com,sdk0723@inzent.com,ujine@inzent.com", "sendContent": ["반려", "사유 : 그냥"]}
         * */
        try {
            mailStatus = "F";
            Map<String, Object> apiKeyMap = new HashMap<String, Object>();
            apiKeyMap.put("apiKey", apiKey);
            MailInfoVO mailInfoVo = mailMapper.getMailTemplate(apiKeyMap); // 메일 템플릿

            Map<String, Object> sendInfoMap = new HashMap<String, Object>();
            sendInfoMap = Json.jsonToMap(sendInfo);

            sendTo = sendInfoMap.get("sendTo").toString(); // 수신자
            mailTitle = mailInfoVo.getMailTitle(); // 메일 제목
            mailContent = mailInfoVo.getMailTemplate(); // 메일 내용

            // 메일 내용 치환
            List<String> replaceList = (List<String>) sendInfoMap.get("sendContent");

            for (String replaceString : replaceList) {
                mailContent = mailContent.replaceFirst("%", replaceString);
            }

            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                @Override
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    message.setTo(sendTo.split(","));
                    message.setFrom("dmsSystemManager@inzent.com", "DMS System Manager");
                    message.setSubject(mailTitle);
                    message.setText(mailContent.replaceAll("%", ""), true);
                }
            };

            javaMailSender.send(preparator); // 메일 발송
            mailStatus = "S";

        } catch (Exception e) {
            e.printStackTrace();
            mailContent = e.getMessage();
        }

        // 메일 전송 실패 또는 성공 시 이력 저장
        MailHistoryVO mailHistoryVO = new MailHistoryVO("", mailTitle, sendTo, mailContent, mailStatus);
        mailMapper.setMailHistory(mailHistoryVO);

        return mailStatus;
    }

}
