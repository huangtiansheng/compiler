package bianyiqi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @author hts
* @version date：2017年12月9日 上午12:29:11 
* 
*/
public class ResultCollector {
List results=new ArrayList();
List varname=new ArrayList();
Map map=new HashMap();
int increaseNum=0;
public void collect(String token,int key){
	//如果是字符常数或者标识符
	results.add( key);
	varname.add(token);
	if(key==36||key==37||key==38)
	{  
		int allocNum;
		if(map.get(token)!=null)
	    allocNum=(int)map.get(token);
		else {
			allocNum=++this.increaseNum;
			map.put(token, allocNum);
		}
		System.out.print("("+key+","+allocNum+")"+"\t");		
	}
	else System.out.print("("+key+",-)"+"\t");
}
}
