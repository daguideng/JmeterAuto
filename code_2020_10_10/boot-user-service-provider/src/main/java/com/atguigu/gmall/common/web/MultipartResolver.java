package com.atguigu.gmall.common.web;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


/**
 * 附件上传解析器
 */
public class MultipartResolver extends CommonsMultipartResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected FileUpload prepareFileUpload(String encoding) {
        FileUpload upload = super.prepareFileUpload(encoding);

        upload.setProgressListener(new ProgressListener() {
            private boolean started;
            private long start;

            @Override
            public void update(long pBytesRead, long pContentLength, int pItems) {
                if (!this.started) {
                    this.started = true;
                    this.start = System.currentTimeMillis();
                } else if (pContentLength == pBytesRead) { // finished
                    long duration = System.currentTimeMillis() - this.start;
                    long speed = duration == 0 ? 0L : pContentLength * 1000L
                            / 1024L / duration;
                    logger.info("[upload speed] " + pContentLength
                            / 1024 + " KB uploaded in " + duration + " ms, speed "
                            + speed + " KB/s");
                }
            }
        });

        return upload;
    }
}
