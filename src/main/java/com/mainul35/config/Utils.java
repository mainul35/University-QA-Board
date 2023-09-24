package com.mainul35.config;

import com.mainul35.service.AttachmentService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jakarta.servlet.ServletContext;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Configuration
public class Utils {

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private ServletContext servletContext;

	private String TEMP_PATH = System.getProperty("FILE_LOCATION") + "/qa-board/temp";
	private String READ_FROM_PATH = System.getProperty("FILE_LOCATION") + "/qa-board";

	private static final Logger logger = Logger.getLogger(Utils.class.getName());

	public Timestamp convertStringToTimestamp(String dateTime, String format) {
		Timestamp timestamp = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			Date parsedDate = dateFormat.parse(dateTime);
			return timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch (Exception e) { // this generic but you can control another types of exception
			// look the origin of excption
			e.printStackTrace();
		}
		return timestamp;
	}

	public String convertTimestampToString(Timestamp timestamp, String format) {
		Date date = new Date();
		date.setTime(timestamp.getTime());
		String formattedDate = new SimpleDateFormat(format).format(date);
		return formattedDate;
	}

	public String readFile(String fileName) {
		BufferedReader br = null;
		String content = null;
		File dir = null;

		try {
			dir = Paths.get(TEMP_PATH).toFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			dir = new File(TEMP_PATH + fileName);

			if (dir.isFile()) {

				br = new BufferedReader(new FileReader(dir));

				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}

				content = sb.toString();
				System.out.println(content);
				br.close();
			} else {
				content = "";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	@SuppressWarnings("resource")
	public byte[] readFile(Long fileId) throws IOException {
		String attachmentUrl = attachmentService.readAttachment(fileId).getFileURL();
		InputStream in = null;
		if (servletContext.getResourceAsStream(attachmentUrl) == null) {
			in = new FileInputStream (READ_FROM_PATH + attachmentUrl);
		} else {
			in = servletContext.getResourceAsStream(attachmentUrl);
		}

		return IOUtils.toByteArray(in);
	}

	public String writeFile(String fileName, String content) {
		File file = null;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;

		try {
			file = new File(TEMP_PATH + fileName);
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(content.getBytes());

			logger.info("File writing successful.");
			bufferedOutputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {

			e.printStackTrace();

		}
		return content;
	}

	public void createZip(String filename) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		File file = new File(TEMP_PATH +filename+".sql");
		FileReader fileReader = new FileReader(file);
		FileInputStream fileInputStream = new FileInputStream(file);
		String line = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}

		File f = new File(TEMP_PATH +filename+".zip");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		ZipEntry e = new ZipEntry(filename+".sql");
		out.putNextEntry(e);

		byte[] data = sb.toString().getBytes();
		out.write(data, 0, data.length);
		out.closeEntry();
		System.out.println("Zip file created successfully.");
		out.close();
	}

	
	public void exportSQL(String filename) throws IOException, InterruptedException {
		File dir = Paths.get(TEMP_PATH).toFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// TODO: Need to fix this
		ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "Start C:\\shared\\xampp\\mysql\\bin\\mysqldump -u root --password= ewsd -r c:\\temp\\"+filename+".sql");
		
		pb.directory(dir);
		Process p = pb.start();
		try {
			logger.info(Integer.toString(p.exitValue()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		createZip(filename);
	}
	
	@Bean
	@Autowired
	JdbcTemplate jdbcTemplate(DriverManagerDataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
