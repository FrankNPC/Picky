package picky.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class LocalStorage {
	
	private static LocalStorage localStorage = new LocalStorage();

	public static LocalStorage getInstance() {
		return localStorage;
	}
	
	private int FileReadCacheSize = 1024*1024*16;
	public void setFileReadCacheSize(int size) {
		FileReadCacheSize = size;
	}
	
	public void append(String path, byte[] bytes) throws IOException {
		OutputStream output = new BufferedOutputStream(new FileOutputStream(path, true));
		try {
			output.write(bytes);
		}finally {
			output.close();
		}
	}
	public OutputStream append(String path) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(path));
	}
	public void delete(String path) {
		new File(path).delete();
	}
	
	public byte[] readAll(String path) throws IOException {
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			byte[] bytes = new byte[FileReadCacheSize];
			int offset = 0;
			while((offset=inputStream.read(bytes))!=-1) {
				byteArray.write(bytes, 0, offset);
			}
			return byteArray.toByteArray();
		}finally {
			inputStream.close();
		}
	}
	public byte[] read(String path, int size) throws IOException {
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
		try {
			byte[] bytes = new byte[size];
			int offset = 0, length = 0;
			while((offset=inputStream.read(bytes, 0, 0))!=-1&&length<size) {
				length += offset;
			}
			return length==size?bytes:Arrays.copyOfRange(bytes, 0, length);
		}finally {
			inputStream.close();
		}
	}
	public void write(String path, byte[] bytes, int start, int end) throws IOException {
		OutputStream outputStream = new FileOutputStream(path);
		try {
			outputStream.write(bytes, start, end-start);
		}finally {
			outputStream.flush();
			outputStream.close();
		}
	}
}
