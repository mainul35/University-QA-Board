package com.springprojects.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springprojects.config.Properties;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Idea;
import com.springprojects.repository.AttachmentRepository;

@Service
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;

	// Save the uploaded file to this folder
	private static final String UPLOADED_FOLDER = "C://temp//";

	private Logger logger = Logger.getLogger(AttachmentService.class.getName());

	public Attachment save(Attachment attachment, MultipartFile file, Long userId) {
		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();

			File dir = Paths.get(Properties.TEMP_PATH + userId + "//").toFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String extension = "";
			StringTokenizer tokenizer = new StringTokenizer(file.getOriginalFilename(), ".");
			while (tokenizer.hasMoreTokens()) {
				extension = tokenizer.nextToken();
			}
			String url = Properties.TEMP_PATH + userId + "//" + attachment.getAttachmentId() + "." + extension;
			File serverFile = new File(url);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			logger.info("File written successfully.");
			url = "/temp/" + userId + "//" + attachment.getAttachmentId() + "." + extension;
			attachment.setFileURL(url);
			attachment.setFileType(Files.probeContentType(Paths.get(url)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		attachmentRepository.save(attachment);
		return attachment;
	}

	public void save(Attachment attachment) {
		attachmentRepository.save(attachment);
	}

	public Attachment readAttachment(Long id) {
		return attachmentRepository.findOne(id);
	}

}
