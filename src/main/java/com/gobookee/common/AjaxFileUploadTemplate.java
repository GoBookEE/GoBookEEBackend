package com.gobookee.common;

import com.gobookee.common.enums.FileType;
import com.oreilly.servlet.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 다수 파일을 업로드하는 경우 Ajax & Cos 라이브러리를 사용해서 파일업로드 처리하는 템플릿
 * 사용법 : 인자로 request, 파일타입(notice, book, place, user), 파일크기 넘겨주면 파일 업로드를 해준다.
 */
public class AjaxFileUploadTemplate {
    public static MultipartRequest uploadFiles(HttpServletRequest request, FileType fileType, int fileSize) throws IOException {
        String filePath = request.getServletContext().getRealPath(CommonPathTemplate.BASIC_UPLOAD_PATH + fileType);
        return new MultipartRequest(request, filePath, fileSize, "utf-8", new CustomFileRenamePolicy());
    }
}
