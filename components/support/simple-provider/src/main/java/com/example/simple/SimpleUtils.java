package com.example.simple;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public class SimpleUtils {

    private static char[] charSet = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '!', '@', '#', '$', '%'
    };

    private static final String[] PERMISSION_FILE_MIME_TYPES = {
            "image/jpeg", "image/png", "image/bmp"
    };

    /**
     * 넘겨받은 길이만큼의 랜덤한 문자열을 반환
     * 대문자, 소문자, 특수문자, 숫자를 조합
     * @param Length
     * @return
     */
    public static String randomString(int Length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Length; ++i) {
            double rNum = Math.random();
            int idx = (int)(charSet.length * rNum);
            builder.append(charSet[idx]);
        }
        return builder.toString();
    }

    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }
        return ip;
    }

    public static boolean validateMimeType(MultipartFile file) throws Exception {
//
//
//        String mimeType = new Tika().detect(file.getInputStream());
//
//        for (int i = 0; i < Utils.PERMISSION_FILE_MIME_TYPES.length; ++i) {
//            if (mimeType.equals(Utils.PERMISSION_FILE_MIME_TYPES[i])) {
//                return true;
//            }
//        }
        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
