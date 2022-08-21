package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataCenter {//In main you will call DataCenter.getInstance();
	private static DataCenter x=null;
	private ArrayList<Book> list=new ArrayList<Book>(); //<--Switch back to public.
	private File dir;
	
	private DataCenter() {//Can not create data center if private.
		loadData();
	}
	
	public static DataCenter getInstance() {
		if(x==null) {
			x=new DataCenter();
		}
		return x;
	}
	
	public ArrayList<Book> getAllBooks() {
		return list;
	}
	
	public boolean add(File f) {
		if(f==null)return false;
		Book b=null;
		
		
		if(f instanceof FileWrapper) {
			FileWrapper x=(FileWrapper)f;
			long n=-39909;
			if(x.getArrIndex(2).strip().length()<19) {
				n=Long.valueOf(x.getArrIndex(2).strip());
			}
			b=new Book(n,x.getArrIndex(0),x.getArrIndex(1),dir.getAbsolutePath()+"\\"+x.getArrIndex(0)+".txt");
		}else{
			b=new Book((int)(Math.random()*10000),f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\"),f.getAbsolutePath().lastIndexOf(".")),"Unkown",f.getAbsolutePath());
		}
		
		//This loop checks if a book with same ISBN already exits
		for(int i=0;i<list.size();i++) {
			if(b.getIsbn()==list.get(i).getIsbn())return false;
		}
			
		String pat=dir.getAbsolutePath();
		String pop=pat+"\\"+b.getTitle()+".txt";
		if(f.renameTo(new File(pat+"\\"+b.getTitle()+".txt"))) {
			f.delete();
		}else {
			return false;
		}
		list.add(b);
		return true;
	}
	
	public boolean remove(Book b){//This works -8/13/22
		if(b==null)return false;
		for(int i=0;i<list.size();i++) {
			if(b.equals(list.get(i))) {
				dir=new File("Books");
				File[] x=dir.listFiles();
				int j=0;
				for(j=0;j<x.length;j++) {
					if(x[j]!=null) {
						String absPath=x[j].getAbsolutePath();
						String relPath4I=list.get(i).getPath();
						if(x[j].getAbsolutePath().contains(list.get(i).getPath())) {
							System.out.println(x[j].delete());
							x[j]=null;
							list.remove(i);
							break;
						}
					}
				}
				if(j<x.length&&x[j]==null)return true;
			}
		}
		return false;
	}
	
	
	private boolean saveData(){
		try {
			FileOutputStream fileOut=new FileOutputStream("BookObjs.ser");
			ObjectOutputStream out=new ObjectOutputStream(fileOut);	
			
			fileOut.write(list.size());
			for(int i=0;i<list.size();i++) {
				out.writeObject(list.get(i));
			}
			
			fileOut.close();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void dump() {
		saveData();
	}
	
	
	private boolean loadData() {
	  dir=new File("Books");
		if(dir.exists()&&dir.isDirectory()) {//Deser if it exists, otherwise just create the directory.
			if(dir.listFiles().length<=0)return false;
			File file=new File("BookObjs.ser");
			if(!file.exists())return false;
				try {
					if(!file.exists())throw new FileNotFoundException();
					FileInputStream fileIn=new FileInputStream("BookObjs.ser");
					ObjectInputStream in=new ObjectInputStream(fileIn);
					int cntr=fileIn.read();
					if(cntr!=dir.list().length)return false;
					for(int i=0;i<cntr;i++){
						list.add((Book)in.readObject());
					}
					return true;
				}catch(FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			
		}else {//Create case for only ser file exist.
			dir.mkdir();
			return true;
		}
		return false;
	}

	//equals,comparTo,clone are redundant due to singleton pattern.
	
	@Override
	public String toString() {
		return "DataCenter [list size=" + list.size() + ", dir=" + dir.getAbsolutePath() + "]";
	}
}