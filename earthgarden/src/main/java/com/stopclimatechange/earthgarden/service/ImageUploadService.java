package com.stopclimatechange.earthgarden.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;

@Service
public class ImageUploadService {

    /*localhost에서 실행시키면 C:\\upload 에 저장된다.*/
    private static final String SAVE_PATH = "/upload";

    public String restore(MultipartFile multipartFile) {
        String url = null;

        try {
            // 파일 정보
            String originFilename = multipartFile.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());    //확장자
            Long size = multipartFile.getSize();    //용량 제한 여부 결정

            // 서버에서 저장 할 파일 이름 생성
            String saveFileName = genSaveFileName(extName);

            //저장
            writeFile(multipartFile, saveFileName);
            url = SAVE_PATH + "/" + saveFileName;
        }
        catch (IOException e) {
            /*에러 수정*/
            throw new RuntimeException(e);
        }
        return url;
    }


    // 현재 시간을 기준으로 랜덤 파일 이름 생성
    private String genSaveFileName(String extName) {
        String fileName = "";
        String dir = SAVE_PATH + "/";

        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += setNameLength(calendar.get(Calendar.MONTH));

        //파일 정리용 폴더 생성
        dir += fileName;
        File folder = new File(dir);
        if (!folder.exists()) {
            try {
                Files.createDirectory(folder.toPath());
            } catch (Exception e) {
                /*에러 수정*/
                throw new RuntimeException(e);
            }
        }
        fileName += "/";
        RandomString randomString = new RandomString(16);

        fileName += randomString.nextString();
        fileName += extName;

        return fileName;
    }


    // 파일을 실제로 write 하는 메서드
    private void writeFile(MultipartFile multipartFile, String saveFileName) throws IOException {
        byte[] data = multipartFile.getBytes();
        FileOutputStream fos = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
        fos.write(data);
        fos.close();
    }

    private String setNameLength(int date){
        if(date < 10){
            return "0" + date;
        }
        return Integer.toString(date);
    }
}