package application;
public class Node implements Comparable<Node>,Cloneable{//Updated and working 8/6/22
	private String word;
	private int occurances;
	private double percent;

	public Node(){ 
		word="empty";
	}
	
	public Node(String word) {
		if(word!=null&&(!word.isBlank()||!word.isEmpty())) {
			this.word=word.toLowerCase();
		}else {
			word="Err";
		}
	}

	public void setWord(String word) {
		if(word!=null)this.word = word.toLowerCase();
	}

	public String getWord() {
		return word;
	}
	
	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		if(percent>=0.00000000000001)this.percent = percent;
	}
	
	public int getIntpercent() {
		double y=percent*100;
		int x=(int)((y%1)*10);
		if(x>=5)y=y+1-(x/10.0);
		else y=(int)y;
		return (int)y;
	}

	public void incrementOccurance() {
		occurances++;
	}
	
	public int getOccurances() {
		return occurances;
	}
	
	@Override //1 this>o,o<this, 0 =.
	public int compareTo(Node o) {
		if(o==this)return 0;//Correction
		return this.getOccurances()-o.getOccurances(); 
	}
	
	@Override
	public boolean equals(Object obj){
		if(this==null||this.getWord()==null||this.getWord().equals(null))return false;
		if(obj==null)return false;
		if(this==obj)return true;
		if(!(obj instanceof Node))return false;
		Node n=(Node)obj;
		return this.word.equals(n.getWord());
	}
	
	@Override
	protected Object clone() {
		Node n=null;
		try {
			n=(Node)super.clone();
			n.setWord(new String(this.word));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return n;
	}

	@Override
	public String toString() {
		return "Node [word=" + word + ", occurances=" + occurances + ", percent=" + percent + "]";
	}
}