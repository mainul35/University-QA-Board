package com.springprojects.config;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;


public class Utils {

	private static final Logger logger = Logger.getLogger(Utils.class.getName());

	public Timestamp convertDateTimeToTimestamp(String dateTime, String format) {
		Timestamp timestamp = null;
		try {
		    DateFormat dateFormat = new SimpleDateFormat(format);
		    Date parsedDate = dateFormat.parse(dateTime);
		    return timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch(Exception e) { //this generic but you can control another types of exception
		    // look the origin of excption 
			e.printStackTrace();
		}
		return timestamp;
	}

	public String readFile(String fileName) {
		BufferedReader br = null;
		String content = null;
		File dir = null;

		try {
			dir = Paths.get(Properties.TEMP_PATH).toFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			dir = new File(Properties.TEMP_PATH + fileName);
			
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

	public String writeFile(String fileName, String content) {
		File file = null;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;

		try {
			file = new File(Properties.TEMP_PATH + fileName);
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
}