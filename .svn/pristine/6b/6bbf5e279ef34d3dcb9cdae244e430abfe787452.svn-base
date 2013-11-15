package com.ginwave.smshelper;

public class ZipUtils {
	public static void unZipFolder(String zipFileString, String outPathString)
			throws Exception {
		android.util.Log.v("XZip", "UnZipFolder(String, String)");
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
				new java.io.FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";

		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {

				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(outPathString
						+ java.io.File.separator + szName);
				folder.mkdirs();

			} else {

				java.io.File file = new java.io.File(outPathString
						+ java.io.File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				java.io.FileOutputStream out = new java.io.FileOutputStream(
						file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}// end of func
}
