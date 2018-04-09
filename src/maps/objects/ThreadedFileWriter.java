package maps.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ThreadedFileWriter extends Thread {
	private ArrayList<ArrayList<Double>> innerPointsToWriteTofile = null;
	private String dataToWrite = null;
	private String folder;
	private String fileName;
	private String format;
	private String endFormat;

	String userHomeFolder = System.getProperty("user.home");

	public ThreadedFileWriter(String folder, String fileName) {
		new ThreadedFileWriter(folder, fileName, "", "");
	}

	public ThreadedFileWriter(String folder, String fileName,
			String dataToWrite, String format) {
		this.folder = folder;
		this.fileName = fileName;
		this.dataToWrite = dataToWrite;
		this.format = format;
		format();
	}

	public ThreadedFileWriter(String folder, String fileName,
			ArrayList<ArrayList<Double>> latLngDatas, String format) {
		this.folder = folder;
		this.fileName = fileName;
		this.innerPointsToWriteTofile = latLngDatas;
		this.format = format;
		format();
	}

	@Override
	public void run() {
		File outFile = new File(userHomeFolder + "/Desktop/" + folder);
		outFile.mkdirs();
		File file=new File(outFile,fileName);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			if (dataToWrite != null) {
				out.write(dataToWrite);
			}
			if (innerPointsToWriteTofile != null) {
				for (int i = 0; i < innerPointsToWriteTofile.size(); i++) {
					out.write(format + innerPointsToWriteTofile.get(i).get(0)
							+ "," + innerPointsToWriteTofile.get(i).get(1)
							+ endFormat + ",\n");
				}
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void format() {
		if (format.length() > 0) {
			char c = format.charAt(format.length() - 1);
			if (c == '(') {
				endFormat = ")";
			} else {
				endFormat = "";
			}
		}else{
			format="";
			endFormat="";
		}
	}
}
