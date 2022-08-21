package application;

import java.io.File;

public class FileWrapper extends File implements Cloneable{
	private File file;
	private String[] arr; 
	
	public FileWrapper(File parent, String child) {
		super(parent, child);
	}

	public FileWrapper(File file,String[] arr) {
		super(file.getAbsolutePath());
		if(file!=null)this.file=file;
		if(arr!=null)this.arr=arr;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		if(file!=null)this.file = file;
	}
	
	public String getArrIndex(int idx) {
		if(idx<0)return null;
		return arr[idx];
	}	
	
	public String[] getArr() {
		return arr;
	}

	public void setArr(String[] arr) {
		this.arr = arr;
	}

	public void setArrIndex(int idx,String x) {
		if(idx>=0&&idx<arr.length)return;
		for(int i =0;i<arr.length;i++) {
			if(arr[i]==null) arr[i]=x;
		}
	}	

	public int compareTo(FileWrapper f) {
		return f.arr.length-this.arr.length;
	}
	
	@Override
	public boolean equals(Object o){
		if(o==null)return false;
		if(this==o)return true;
		if(!(o instanceof FileWrapper)) return false;
		FileWrapper f=(FileWrapper)o;
		return file.getAbsolutePath().equals(f.getAbsolutePath());
	}
	
	@Override
	protected Object clone() {
		FileWrapper f=null;
		try {
			f=(FileWrapper)super.clone();
			f.file=new File(this.file.getAbsolutePath());
			f.setArr(new String[arr.length]);
			for(int i=0;i<arr.length;i++) {
				f.setArrIndex(i,new String(this.getArrIndex(i)));
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	@Override
	public String toString() {
		return "FileWrapper [file=" + file.getAbsolutePath() + ", arr size=" + arr.length + "]";
	}	
}