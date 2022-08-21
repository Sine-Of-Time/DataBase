package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Book implements Serializable,Comparable<Book>,Cloneable{
		private long isbn;
		private int individualWordCount;
		private String title; 
		private String author;
		private String path; 
		//FYI, This class one works on Windows NT systems only.
		public Book() {
			isbn=1234;
			title="Blank";
			author="Unkown";
			path="n/a";
		}
 
		public Book(long isbn,String title,String author,String path) {
			if(isbn>0)this.isbn=isbn;
			else this.isbn =-1;
			if(title!=null)this.title=title;
			else this.title="Blank";
			if(author!=null)this.author=author;
			else this.author="Unkown";
			if(path!=null)this.path=path;
			else this.path="";
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public long getIsbn() {
			return isbn;
		}
		public void setIsbn(int isbn) {
			this.isbn = isbn;
		}
		
		public String getAuthor() {
			return author;
		}
		
		public void setAuthor(String author) {
			this.author = author;
		}
		
		public void setPath(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return path;
		}
 
		
		
		/**
		 * @param isbn the isbn to set
		 */
		public void setIsbn(long isbn) {
			this.isbn = isbn;
		}

		/**
		 * @param individualWordCount the individualWordCount to set
		 */
		public void setIndividualWordCount(int individualWordCount) {
			this.individualWordCount = individualWordCount;
		}

		
		
		public int getIndividualWordCount() {//Done as of 8/5/22
			File file=new File(path);
			if(!file.exists())return -1;
			if(!file.isFile())return -1;
 
			ArrayList<String> words=new ArrayList<>();
			try(Scanner sc=new Scanner(file);){
				while(sc.hasNext()) {
					String[] arr=sc.nextLine().replaceAll("[.,;:]","").split(" ");
					for(int i=0;i<arr.length;i++) {
						if(words.size()==0) {
							words.add(arr[i].toLowerCase());
						}
						if(!words.contains(arr[i].toLowerCase())&&!arr[i].isEmpty()&&!arr[i].isEmpty()) {
							words.add(arr[i].toLowerCase());
						}
					}	
				}
 
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(Exception e) {
				e.printStackTrace();
			}
			individualWordCount=words.size();
			return words.size();
		}
 
		public Node[] getTopTenWords() {
			File file=new File(path);
			if(!file.exists())return null;
			if(!file.isFile())return null;
			//ArrayList<String> words=new ArrayList<>();
			ArrayList<Node> nodes=new ArrayList<>();
			ArrayList<String> sList=new ArrayList<>();
			int cntr=0;
			try(Scanner sc=new Scanner(file);) {
				while(sc.hasNext()) {
					String[] tArr=sc.nextLine().replaceAll("[.,;:]","").split(" ");
					cntr+=tArr.length;
		
					ArrayList<Node> tList=new ArrayList<>();		
					for(int i=0;i<tArr.length;i++) {
						if(tArr[i].endsWith(".")||tArr[i].endsWith(",")||tArr[i].endsWith(":")||tArr[i].endsWith(";")||tArr[i].endsWith("?")||tArr[i].endsWith("!")) {
							tArr[i]=tArr[i].substring(0,tArr[i].length()-1);
						}
						
						if(tArr[i].isBlank())
						
						if(nodes.size()==0&&!tArr[0].equalsIgnoreCase("")&&!!tArr[0].equalsIgnoreCase(" ")) {
							sList.add(tArr[i].toLowerCase());
							nodes.add(new Node(tArr[i].toLowerCase()));
						}
						
						if(!sList.contains(tArr[i].toLowerCase())) {
							nodes.add(new Node(tArr[i].toLowerCase()));
							tList.add(new Node(tArr[i].toLowerCase()));
							sList.add(tArr[i].toLowerCase());
						}
					}
					
				}
				
				
				Scanner sc2=new Scanner(file);
			
				while(sc2.hasNext()) {//Checking each node occ
				try {
					String[] arrC=sc2.nextLine().replaceAll("[.,;:]","").split(" ");	
					for(int k=0;k<nodes.size();k++) {
						if(nodes.get(k)!=null&&nodes.get(k).getWord()!=null) {
							for(int j=0;j<arrC.length;j++) {
							  if(arrC[j]!=null) {	 
								if(nodes.get(k).getWord().equalsIgnoreCase(arrC[j])){
									nodes.get(k).incrementOccurance();
								}
							}
						  }
					  }
					}
						}catch(NullPointerException e) {
							e.printStackTrace();
						}
				}
				
				sc2.close();
				Collections.sort(nodes,Collections.reverseOrder());
				Node[] t10=new Node[10];
				for(int i=0;i<10&&i<nodes.size();i++) {
					if(nodes.get(i)!=null) {
						nodes.get(i).setPercent((nodes.get(i).getOccurances())/(double)cntr);
						t10[i]=nodes.get(i);
					}
				}
				return t10;
 
			}catch(FileNotFoundException e){
				e.printStackTrace();
				return null;
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			} 
		}
 
		@Override //this>o 1, o<this -1, else 0.
		public int compareTo(Book b) {
			return ((int)this.isbn-(int)b.getIsbn());
		}
 
		@Override
		public boolean equals(Object o){
			if(o==null)return false;
			if(this==o)return true;
			if(!(o instanceof Book)) return false;
			Book x=(Book)o;
			return x.getIsbn()==this.isbn;
		}
 
		@Override
		protected Object clone() {
			Book b=null;
			try {
				b=(Book)super.clone();
				b.setAuthor(new String(this.getAuthor()));
				b.setPath(new String(this.getPath()));
				b.setTitle(new String(this.getTitle()));
 		} catch (CloneNotSupportedException e) {
 			e.printStackTrace();
 		}
			return b;
 	}
 
 
		@Override
		public String toString() {
			return "Book [isbn=" + isbn + ", title=" + title + ", author=" + author + ", path=" + path + "]";
		}
}