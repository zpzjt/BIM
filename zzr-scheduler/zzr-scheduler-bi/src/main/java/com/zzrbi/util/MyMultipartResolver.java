package com.zzrbi.util;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MyMultipartResolver extends CommonsMultipartResolver {
	public MultipartParsingResult parseRequest(HttpServletRequest request) {
		String encoding = "UTF-8";
		FileUpload fileUpload = prepareFileUpload(encoding);
		final HttpSession session = request.getSession();
		fileUpload.setProgressListener(new ProgressListener() {

			public void update(long pBytesRead, long pContentLength, int pItems) {
				int percent = (int) ((float) pBytesRead / (float) pContentLength * 100.0F);
				session.setAttribute("percent", percent + "%");
			}
		});
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}
}
