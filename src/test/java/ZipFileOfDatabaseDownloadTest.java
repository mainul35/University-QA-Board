import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileOfDatabaseDownloadTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			exportSQLTest();
			createZip();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createZip() throws IOException {
		StringBuilder sb = new StringBuilder();
		File file = new File("c:\\temp\\ewsd.sql");
		FileReader fileReader = new FileReader(file);
		FileInputStream fileInputStream = new FileInputStream(file);
		String line = "";
		BufferedReader br =
		           new BufferedReader( new InputStreamReader(fileInputStream, "UTF-8" ));
		while ((line = br.readLine())!=null) {
			sb.append(line+"\n");		
		}

		
		File f = new File("c:\\temp\\ewsd.zip");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		ZipEntry e = new ZipEntry("ewsd.sql");
		out.putNextEntry(e);
		
		byte[] data = sb.toString().getBytes();
		out.write(data, 0, data.length);
		out.closeEntry();
		System.out.println("Zip file created successfully.");
		out.close();
	}

	public static void exportSQLTest() throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "C:\\shared\\xampp\\mysql\\bin\\mysqldump -u root --password= ewsd -r c:\\temp\\ewsd.sql");
		File dir = new File("C:\\temp\\");
		pb.directory(dir);
		Process p = pb.start();
	}
}
