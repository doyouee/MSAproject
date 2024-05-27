package com.inzent.deliverablesAPI.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import com.inzent.commonMethod.common.Consts;
import com.inzent.commonMethod.common.ResponseMessage;
import com.inzent.deliverablesAPI.mapper.DeliverablesReasonMapper;
import com.inzent.deliverablesAPI.mapper.DeliverablesRegMapper;
import com.inzent.deliverablesAPI.vo.DeliverablesListVO;
import com.inzent.deliverablesAPI.vo.DeliverablesRegVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 산출물 등록 CRUD 서비스 @author 박주한
 * zip 다운로드 @author 이도영
 * */
@Service
public class DeliverablesRegService {

    @Autowired
    private DeliverablesRegMapper deliverablesRegMapper;
    
    @Autowired
    private DeliverablesReasonMapper deliverablesReasonMapper;

    @Autowired
    private DeliverablesListService deliverablesListService;

    @Value("${FILEPATH}")
    private String filePath;
    @Value("${API_URL}")
    private String apiUrl ;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Map<String,Object> getReg(String deliverablesId, String projectId) {
        return ResponseMessage.setMessage(Consts.SUCCESS, deliverablesRegMapper.getReg(deliverablesId, projectId));
    }

    public List<DeliverablesRegVO> getRegOne(String id) {
        return deliverablesRegMapper.getRegOne(id);
    }

    public void insertRegInfo(DeliverablesRegVO deliverablesRegVO, String deliverName, String filename, String filePath) {
        List<DeliverablesRegVO> reasonList = deliverablesReasonMapper.getDeliverIdReason(deliverablesRegVO.getDeliverablesId(), deliverablesRegVO.getProjectId());
        if(!reasonList.isEmpty()) {
        	deliverablesRegMapper.deleteReason(reasonList.get(0).getRegistrationId(), deliverablesRegVO.getRegistrationUserEmail(), deliverablesRegVO.getRegistrationUserName());
        }
        deliverablesRegMapper.insertReg(deliverablesRegVO, deliverName, filename, filePath);
    }

    public Map<String,Object> fileTransfer(List<MultipartFile> filelist, DeliverablesRegVO deliverablesRegVO, String path, String uploadSubFolderName) throws Exception {
        for (int i = 0; i < filelist.size(); i++) {
            // 저장된 파일에 등록하는 파일명과 동일한 파일명이 있는지 확인
            File dir = new File(path);

            String[] filenames = dir.list();
            List<String> strList = new ArrayList<>(Arrays.asList(filenames));

            if(strList.contains(filelist.get(i).getOriginalFilename())) {
                return ResponseMessage.setMessage(Consts.ERROR, "중복된 파일이 존재합니다.");
            }
        }

        for (int i = 0; i < filelist.size(); i++) {
            // DELIVERABLES_TITLE가 NULL값일 때,
            String deliverablesTitle = "";
            if (deliverablesRegVO.getDeliverablesTitle() == null) {
                String tempName = filelist.get(0).getOriginalFilename();
                String tempName2 = tempName.substring(0, tempName.indexOf("."));
                deliverablesTitle = tempName2 + " 외 " + filelist.size();
            }
            // DELIVERABLES_TITLE가 값을 가지고 있을 때,
            else {
                deliverablesTitle = deliverablesRegVO.getDeliverablesTitle();
            }

            // 파일 전송
            filelist.get(i).transferTo(new File(path + File.separator + filelist.get(i).getOriginalFilename()));
            insertRegInfo(deliverablesRegVO, deliverablesTitle, filelist.get(i).getOriginalFilename(), path);

        }
        return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
    }

    public Map<String,Object> insertReg(List<MultipartFile> filelist, DeliverablesRegVO deliverablesRegVO) throws Exception {
        List<DeliverablesListVO> list = deliverablesListService.getDeliverId(deliverablesRegVO.getDeliverablesId());
        String deliverId = list.get(0).getDeliverablesId();
        StringBuilder saveFilepath = new StringBuilder();               // 파일 저장 경로명

        for(int i=0; i<list.get(0).getDeliverablesId().length(); i++) {
            List<DeliverablesListVO> pathList = deliverablesListService.getPathInfo(deliverId);

            // 마지막 상위 폴더 filepath 앞에 붙이기
            if(pathList.get(0).getDeliverablesTopId() == null) {
                saveFilepath.insert(0, pathList.get(0).getDeliverablesName());
                break;
            }

            // 상위 폴더명 불러와 filepath 앞에 붙이기
            saveFilepath.insert(0, File.separator + pathList.get(0).getDeliverablesName());
            deliverId = pathList.get(0).getDeliverablesTopId();

        }
        // 최종으로 프로젝트ID, filepath 앞에 붙이기
        saveFilepath.insert(0, deliverablesRegVO.getProjectId() + File.separator);

        // 텍스트 입력 시
        if (filelist == null) {
            String path = filePath + saveFilepath;

            insertRegInfo(deliverablesRegVO, deliverablesRegVO.getDeliverablesTitle(), null, path);

            return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
        }
        // 파일 입력 시
        else {
            String uploadSubFolderName = "";
            if (deliverablesRegVO.getDeliverablesTitle() != null) {
                uploadSubFolderName = deliverablesRegVO.getDeliverablesTitle();
            } else {
                String tempName = filelist.get(0).getOriginalFilename();
                String tempName2 = tempName.substring(0, tempName.indexOf("."));
                uploadSubFolderName = tempName2 + " 외 " + filelist.size();
            }

            String path = filePath + saveFilepath;
            File folder = new File(path);

            // 산출물별 폴더가 없다면
            if (!folder.exists()) {
                // 산출물별 폴더 생성
                if (folder.mkdirs()) {
                    return fileTransfer(filelist, deliverablesRegVO, path, uploadSubFolderName);
                }
            }
            // 산출물별 폴더가 있다면
            else {
                return fileTransfer(filelist, deliverablesRegVO, path, uploadSubFolderName);
            }
            return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
        }
    }

	public List<HashMap<String, Object>> getFileInfo(String deliverablesTitle) {
		return deliverablesRegMapper.getFileInfo(deliverablesTitle);
	}

    public void deleteReg(String registrationId, String deleteUserEmail, String deleteUserName) {
        deliverablesRegMapper.deleteReg(registrationId, deleteUserEmail, deleteUserName);
    }

    public void deleteFileInServer(String registrationId) {
        List<HashMap<String, Object>> list = getFileInfo(registrationId);
        String fileName = (String) list.get(0).get("filename");
        String deleteFilePath = list.get(0).get("filepath").toString();

        try {
            File file = new File(deleteFilePath + File.separator + fileName);
            if(file.exists()) {
                file.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResponseEntity<InputStreamResource> getProjectDeliverablesDownload(HttpServletRequest request, HttpServletResponse response, String projectId, String projectName) {
        try {
        	File zipFilePath = new File(filePath + ".." + File.separator + "Downloads"  + File.separator + projectName + ".zip");
	        
	        FileSystemResource fileSystemResource = new FileSystemResource(zipFilePath);
	        File file = fileSystemResource.getFile();
	        String fileName = file.getName();

	        HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(fileSystemResource.contentLength());
            headers.add("Content-FileName", Base64.getEncoder().encodeToString(fileName.getBytes(Charset.forName("UTF-8"))));
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                      .filename(fileName, Charset.forName("UTF-8"))
                      .build();
            headers.setContentDisposition(contentDisposition);

            return ResponseEntity.ok()
                    .headers(headers)
                    .lastModified(fileSystemResource.lastModified())
                    .contentLength(fileSystemResource.contentLength())
                    .body(new InputStreamResource(fileSystemResource.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
    }

    public void getTxtFile(String path, String contents, String title) {
    	File file = new File(path);
        Path directoryPath = Paths.get(path);
        if (!file.isDirectory()) {
            try {
                Files.createDirectories(directoryPath);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
        }
        
        String filePath = path +  File.separator + title + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            contents = contents.replaceAll("<br>","\n");
            fileWriter.write(contents);
            fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public Map<String, Object> getDeliverablesCreateFile(HttpServletRequest request, HttpServletResponse response, String projectId, String projectName) {
		try {
			// 미등록 사유 파일, 텍스트 파일 생성
			String rootPathList = "";
			List<DeliverablesRegVO> list = deliverablesRegMapper.getDownloadReg(projectId);
			for(DeliverablesRegVO d : list) {
				if(d.getContentType().equals("1")) { // txt 타입일 때 .txt 다운로드
					if(d.getNotRegistrationReason() != null) { // 미등록 사유
						getTxtFile(d.getFilePath().toString(), d.getNotRegistrationReason().toString(), d.getDeliverablesTitle()+"_"+"미등록"); // 경로에 [산출물제목_미등록.txt]로 저장
						logger.debug("미등록 사유로 .txt 파일 생성");
					} else if(d.getTextContents() != null){ // 텍스트 내용
						getTxtFile(d.getFilePath().toString(), d.getTextContents().toString(), d.getDeliverablesTitle().toString());
						logger.debug("텍스트 컨텐츠로 .txt 파일 생성");
					}
				}
				rootPathList = d.getFilePath().toString();
			}
			
			// zip 파일 생성
			File zipFilePath = new File(filePath + ".." + File.separator + "Downloads"  + File.separator + projectName + ".zip");
			ZipUtil.pack(new File(rootPathList.substring(0, rootPathList.indexOf(projectId)) + projectId), zipFilePath);
			logger.debug("zip 파일 생성 완료");
			return ResponseMessage.setMessage(Consts.SUCCESS, Consts.NO_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.setMessage(Consts.EXCEPTION_ERROR, e.getMessage());
		}
	}
}
