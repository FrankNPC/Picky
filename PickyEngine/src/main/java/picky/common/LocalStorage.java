package picky.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public final class LocalStorage {
	
	private static LocalStorage localStorage;

	public static LocalStorage getInstance() {
		if (localStorage==null) {
			synchronized(LocalStorage.class) {
				if (localStorage==null) {
					localStorage = new LocalStorage();
				}
			}
		}
		return localStorage;
	}
	
	private int FileReadCacheSize = 1024*1024*16;
	public void setFileReadCacheSize(int size) {
		FileReadCacheSize = size/8*8;
	}
	public int getFileReadCacheSize() {
		return FileReadCacheSize;
	}
	
	public void append(String path, byte[] bytes) throws IOException {
		OutputStream output = new BufferedOutputStream(new FileOutputStream(path, true));
		try {
			output.write(bytes);
		}finally {
			output.close();
		}
	}
	
	public byte[] read(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
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
	public byte[] read(String path, int start, int size) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		try {
			inputStream.skip(start);
			byte[] bytes = new byte[size];
			int count = 0, length = 0;
			while(length<size && (count=inputStream.read(bytes, length, size-length))!=-1) {
				length += count;
			}
			return length==size?bytes:Arrays.copyOfRange(bytes, 0, length);
		}finally {
			inputStream.close();
		}
	}

	public void write(String path, byte[] bytes) throws IOException {
		OutputStream outputStream = new FileOutputStream(path);
		try {
			outputStream.write(bytes);
		}finally {
			outputStream.flush();
			outputStream.close();
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
