package com.gobookee.common;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MultipartRequest 객체 생성 시 필요한 FileRenamePolicy 템플릿
 * 사용법 : 파일 객체 넘겨주면 규칙에 맞게 파일명을 rename해서 반환
 */
public class CustomFileRenamePolicy implements FileRenamePolicy {
    @Override
    public File rename(File file) {
        String oriName = file.getName();
        File newFile = null;
        do {
            String prefix = "GOBOOKE_";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            int rnd = (int) (Math.random() * 1000) + 1;
            String ext = oriName.substring(oriName.lastIndexOf("."));
            String rename = prefix + sdf.format(new Date()) + "_" + rnd + ext;
            newFile = new File(file.getParent(), rename);
        } while (!createFile(newFile));

        return newFile;
    }

    private boolean createFile(File newFile) {
        try {
            return newFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }
}
