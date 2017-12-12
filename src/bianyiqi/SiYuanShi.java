package bianyiqi;
/** 
* @author hts
* @version date：2017年12月12日 下午12:36:14 
* 
*/
public class SiYuanShi {
public String getOne() {
		return one;
	}
	public void setOne(String one) {
		this.one = one;
	}
	public String getTwo() {
		return two;
	}
	public void setTwo(String two) {
		this.two = two;
	}
	public String getThree() {
		return three;
	}
	public void setThree(String three) {
		this.three = three;
	}
public SiYuanShi(String one, String two, String three, String four) {
		super();
		this.one = one;
		this.two = two;
		this.three = three;
		this.four = four;
	}
String one;
String two;
String three;
String four;
int nextOfchain=0;
public int getNextOfchain() {
	return nextOfchain;
}
public void setNextOfchain(int nextOfchain) {
	this.nextOfchain = nextOfchain;
}
public String getFour() {
	return four;
}
public void setFour(String four) {
	this.four = four;
}
}
