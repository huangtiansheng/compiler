package bianyiqi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/** 
* @author hts
* @version date：2017年12月10日 下午7:52:02 
* 
*/
public class GrammaAnalyze {
	public GrammaAnalyze(ResultCollector result, int index) {
		super();
		this.input = result.results;
		this.index = index;
	    this.varnameList=result.varname;
	}

	private List<Integer> input;
	private int index=0;
	List varnameList;
	Stack<String> varStack=new Stack<String>();
	//语义栈
	Stack<Integer> yuyiStack=new Stack<Integer>();
	//临时变量分配 T1 T2 T3...
	private int tempnum=1;
	//四元式序号（emit输出用到）
	private int nextstat=0;
	
	List<SiYuanShi> siyuanlist=new ArrayList();
public void prase(String nonEndMark) throws Exception{
	switch(nonEndMark){
	case "程序":
		switch(getNow()){
        
        case 23:
        	 MatchToken(23);
        	 MatchToken(36);
        	 String second=getPreviousVar();
        	 MatchToken(52);
        	 emit("programme",second,"-","-");
        	 prase("变量说明");
        	 prase("复合句");
        	 MatchToken(46);
        	 int chain=yuyiStack.pop();
        	 backpatch(chain,nextstat);
        	 emit("sys","-","-","-");
        	 outputResult();
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "变量说明":
		switch(getNow()){
        
        case 33:
        	 MatchToken(33);     	
        	 prase("变量定义");   
		break;
        case 3:  
		break;		
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "变量定义":
		switch(getNow()){
     
        case 36:
        	 prase("标识符表");
        	 MatchToken(50);
        	 prase("类型");
        	 MatchToken(52);       	
        	 prase("变量定义’");      	
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "变量定义’":
		switch(getNow()){
        
        case 36:       	 
        	 prase("变量定义");
		break;
        case 3:
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "标识符表":
		switch(getNow()){
        
        case 36:       	 
        	 MatchToken(36);
        	 prase("标识符表’");
        
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "标识符表’":
		switch(getNow()){
        
        case 44:
        	 MatchToken(44);
        	 MatchToken(36);
        	 prase("标识符表’");
        	
		break;
        case 50:
    		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "复合句":
		switch(getNow()){
        
        case 3:
        	 MatchToken(3);
        	 prase("语句表");
        	 MatchToken(12);	
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "语句表":
		switch(getNow()){
        
        case 15:
        case 34:
        case 26:
        case 3:
        case 36:
        	 prase("语句");
        	 prase("语句表’");
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "语句表’":
		switch(getNow()){
        
        case 52:
        	 MatchToken(52);
        	 prase("语句");
        	 prase("语句表’");
        		
		break;
        case 12:
        break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "语句":
		switch(getNow()){
        
        case 36:   
        	 prase("赋值句");
        	 yuyiStack.push(0);//压入0,赋值句无出口
   
		break;
        case 15:
        	prase("if句");      	   
    		break;	
        case 34:
        	prase("while句");      	   
    		break;	
        case 26:
        	prase("repeat句");      	   
    		break;
        case 3:
        	prase("复合句");      	   
    		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "if句":
		switch(getNow()){
        
        case 15:
        	 MatchToken(15);
        	 prase("布尔表达式");
        	 //then
        	 MatchToken(29);
        	 int codebegin=yuyiStack.pop();
        	 int zhen=yuyiStack.pop();
        	 int jia=yuyiStack.pop();
        	 backpatch ( zhen , nextstat ) ; 
        	 yuyiStack.push(jia);//压入chain 
        	 prase("语句");
        	 
        	 prase("if句’");	
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "if句’":
		switch(getNow()){
        
        case 11:
        	//else
        	 MatchToken(11);
        	 int schain= yuyiStack.pop(); //弹出 schain
        	 int echain= yuyiStack.pop();  //弹出echain
        	 int q=nextstat;      	 
        	 emit("j","-","-","0");
        	 backpatch ( echain, nextstat ) ;
        	 yuyiStack.push( merge (q ,schain)); //压入tp
        	 prase("语句"); 
        	 int tpchain= yuyiStack.pop();
        	 int s2chain= yuyiStack.pop();
        	 yuyiStack.push(merge ( tpchain, s2chain )); //压入Schain 
		break;
        case 12:
        case 52: 
        	int schain2=yuyiStack.pop();//弹出s1 chain
       	 int echain2=yuyiStack.pop();//弹出e chain  
       	 yuyiStack.push(merge(echain2,schain2));//压入Schain 
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "while句":
		switch(getNow()){
        //while
        case 34:
        	 MatchToken(34);
        	
        	 yuyiStack.push( nextstat); //压入w.codebegin       	
        	 prase("布尔表达式");
        	 yuyiStack.pop();//pop出codebegin
        	 int zhen=yuyiStack.pop();
        	 backpatch(zhen,nextstat);
        	 MatchToken(10);
        	 prase("语句");
        	 int s1chain=yuyiStack.pop();                             
        	 int jia= yuyiStack.pop();                                      
        	 int wcodebegin=yuyiStack.pop();                             
        	 backpatch(s1chain,wcodebegin);    
        	 emit("j","-","-",String.valueOf(wcodebegin));         	 
        	 yuyiStack.push(jia);

        	
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "repeat句":
		switch(getNow()){
        
        case 26:
        	 MatchToken(26);
        	 yuyiStack.push(nextstat);
        	 prase("语句");       	 
        	 MatchToken(32);
        	 int s1chain=yuyiStack.pop();//弹出是s1chain
        	 backpatch(s1chain,nextstat);
        	 prase("布尔表达式");
        	 int ecodebegin=yuyiStack.pop();//弹出ecodebegin
        	 int zhen=yuyiStack.pop();//弹出真出口
        	 int jia=yuyiStack.pop();//弹出假出口
        	 int wcodebegin=yuyiStack.pop();//弹出假出口
             backpatch(jia,wcodebegin);
             yuyiStack.push(zhen);//将真出口作为chain入栈

		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "赋值句":
		switch(getNow()){
        
        case 36:
        	 MatchToken(36);
        	 varStack.push(getPreviousVar());
        	 MatchToken(51);
        	 prase("算术表达式");
        	 String second=varStack.pop();
        	 String fourth=varStack.pop();
        	 emit(":=",second,"-",fourth);	 
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "算术表达式":
		switch(getNow()){
        
        case 37:
        case 36:
        case 39:
        case 45:
        	 prase("项");
        	 prase("算术表达式’");
       
        
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "算术表达式’":
		switch(getNow()){
        // +
        case 43:
        	 MatchToken(43);
        	 prase("项");
        	 String third=varStack.pop();
        	 String second=varStack.pop();
        	 String temp=getTemp();
        	 varStack.push(temp);
        	 emit("+",second,third,temp);
        	 prase("算术表达式’");
		break;
		//  -
        case 45:
       	 MatchToken(45);
       	 prase("项");
       	 String third1=varStack.pop();
    	 String second1=varStack.pop();
    	 String temp1=getTemp();
    	 varStack.push(temp1);
    	 emit("-",second1,third1,temp1);
       	 prase("算术表达式’");
		break;
        case 52:
        case 12:
        	
        case 53:
        case 54:
        case 55:
        case 56:
        case 57:
        case 58:
        case 1:
        case 20:
        case 10:
        case 29:
		case 11:
		case 32:
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "项":
		switch(getNow()){
        
		    case 37:
	        case 36:
	        case 39:
	        case 45:        
        	 prase("因子");
        	 prase("项’");
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "项’":
		switch(getNow()){
        // *
        case 41:
        	 MatchToken(41);
        	 prase("因子");
        	 String third=varStack.pop();
        	 String second=varStack.pop();
        	 String temp=getTemp();
        	 varStack.push(temp);
        	 emit("*",second,third,temp);
        	 prase("项’");	
		break;
		// /
        case 48:
        	 MatchToken(48);
        	 prase("因子");
        	 String third1=varStack.pop();
        	 String second1=varStack.pop();
        	 String temp1=getTemp();
        	 varStack.push(temp1);
        	 emit("/",second1,third1,temp1);
        	 prase("项’");	
		break;
        case 43:
        case 45:
        case 52:
        case 12:
        	
        case 53:
        case 54:
        case 55:
        case 56:
        case 57:
        case 58:
        case 1:
        case 20:
        case 10:
        case 29:
        	
        case 11:
		case 32:
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "因子":
		switch(getNow()){
		 case 37:
        case 36:
        case 39:
        	 prase("算术量");
        	 break;
        case 45:
        	 MatchToken(45);	
        	 prase("因子");
        	 String var=varStack.pop();
        	 varStack.push("-"+var);
        	 
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
		
	break;
	
	case "算术量":
		switch(getNow()){
        
        case 37:
        	 MatchToken(37);
        	 varStack.push(getPreviousVar());
		break;
        case 36:
       	 MatchToken(36);
       	 varStack.push(getPreviousVar());
       	break;
        case 39:
        	MatchToken(39);  
        	prase("算术表达式");
        	MatchToken(40);  
          	break;
       	
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔表达式":
		switch(getNow()){
        
        case 31:
        case 13:        	
        case 36:
        case 37:
        case 39:
        case 45:
        case 18:        	
        	 prase("布尔项");
        	 prase("布尔表达式’");        	 	
		break;
        
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔表达式’":
		switch(getNow()){
        // or
		case 20:
        	MatchToken(20);
        	 int codebegin1=yuyiStack.pop();//弹出codebegin   
             int zhen1=yuyiStack.pop();//弹出真出口
             int jia1=yuyiStack.pop();//弹出假出口
       	 prase("布尔项");
       	 int codebegin2=yuyiStack.pop();//弹出codebegin   
         int zhen2=yuyiStack.pop();//弹出真出口
         int jia2=yuyiStack.pop();//弹出假出口
         
         backpatch(jia1 , codebegin2) ;
         yuyiStack.push(jia2); //插入假出口
         yuyiStack.push(merge( zhen1 ,zhen2  )); //插入真出口
         yuyiStack.push(codebegin1); //插入codebegin
       	 prase("布尔表达式’");        	 	
		break;
		case 10:
		case 29:
		case 12:
		case 52:
		case 11:
		case 32:
			break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔项":
		switch(getNow()){
        
		 case 31:
	        case 13:        	
	        case 36:
	        case 37:
	        case 39:
	        case 45:
	        case 18:        	
	        	 prase("布因子");
	        	 prase("布尔项’");        	 	
			break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔项’":
		switch(getNow()){
        //and
        case 1:
        	 MatchToken(1);
        	  int codebegin1=yuyiStack.pop();//弹出codebegin   
              int zhen1=yuyiStack.pop();//弹出真出口
              int jia1=yuyiStack.pop();//弹出假出口
        	 prase("布因子");
        	  int codebegin2=yuyiStack.pop();//弹出codebegin   
              int zhen2=yuyiStack.pop();//弹出真出口
              int jia2=yuyiStack.pop();//弹出假出口
              int zhen=codebegin1;
              backpatch(zhen1 , codebegin2) ;
              yuyiStack.push(merge( jia1 ,jia2  )); //插入假出口
              yuyiStack.push(zhen2); //插入真出口
              yuyiStack.push(codebegin1); //插入codebegin
        	 prase("布尔项’");   	
		break;
        case 10:
        case 20:
        case 29:
        case 12:
		case 52:
		case 11:
		case 32:
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布因子":
		switch(getNow()){
		case 31:
		case 13:        	
        case 36:
        case 37:
        case 39:
        case 45:       	
        	 prase("布尔量");
        	
		break;
       //not
        case 18: 
        	MatchToken(18);
        	prase("布因子");
        int codebegin=yuyiStack.pop();//弹出codebegin   
        int zhen=yuyiStack.pop();//弹出真出口
        int jia=yuyiStack.pop();//弹出假出口
        yuyiStack.push(zhen); //插入假出口
        yuyiStack.push(jia); //插入真出口
        yuyiStack.push(codebegin); //插入codebegin
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔量":
		switch(getNow()){
        
        case 31:
        case 13:      	
        	 prase("布尔常量");        	
		break;
        case 39:
        	MatchToken(39);
        	 prase("布尔表达式");
        	 MatchToken(40);
		break;
        case 36:
        case 37:
        case 45: 
        
        	 prase("算术表达式");
        	 prase("布尔量’");
		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔量’":
		switch(getNow()){
        
        case 53:
        case 54:
        case 55:
        case 56:
        case 57:
        case 58:
        
        	 prase("关系符");
        	 prase("算术表达式");
        	 String third=varStack.pop();
        	 String first=varStack.pop();
        	 String second=varStack.pop(); 
        	
        	 emit(first,second,third,String.valueOf(nextstat+2));       	 
        	 emit("j","-","-","0");	
        	 yuyiStack.push(nextstat-1);//入栈假出口
        	 yuyiStack.push(nextstat-2);//入栈真出口
        	 yuyiStack.push(nextstat-2);//入栈真出口
		break;
        case 1:
        case 20:
        case 10:
        case 29:
        case 12:
		case 52:
		case 11:
		case 32:
        	break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "布尔常量":
		switch(getNow()){
        //true
        case 31:
        	 MatchToken(31);
        	 yuyiStack.push(0);//入栈假出口
        	 yuyiStack.push(nextstat);//入栈真出口      	
        	 yuyiStack.push(nextstat);//入栈codebegin      
        	emit("j" ,"－","－","0" );
		break;
		//false
        case 13:
       	 MatchToken(13);
       	yuyiStack.push(nextstat);//入栈假出口
   	 yuyiStack.push(0);//入栈真出口      
   	yuyiStack.push(nextstat);//入栈codebegin   
   	emit("j" ,"－","－","0" );
       	break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	
	case "关系符":
		switch(getNow()){
        
        case 53:
        	 MatchToken(53);
        	 varStack.push("j<");
		break;
        case 54:
       	 MatchToken(54);
       	varStack.push("j<=");
		break;
        case 55:
       	 MatchToken(55);
       	varStack.push("j<>");
		break;
        case 56:
       	 MatchToken(56);
       	varStack.push("j=");
		break;
        case 57:
       	 MatchToken(57);
       	varStack.push("j>");
		break;
        case 58:
          	 MatchToken(58);
          	varStack.push("j>=");
   		break;
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	case "类型":
		switch(getNow()){
        
        case 17:
        	 MatchToken(17);
		break;
        case 4:
       	 MatchToken(4);
		break;
        case 7:
       	 MatchToken(7);
		break;
       
		default: throw new Exception("解析【"+nonEndMark+"】时不应该有输入 【"+getNow()+"】");
		}
	break;
	default: throw new Exception("编程错误：没有"+nonEndMark+"对应的处理程序");
	
	}
	
	
}

private void outputResult() {
	// TODO Auto-generated method stub
	for(int i=0;i<siyuanlist.size();i++){
		SiYuanShi siyuanshi=siyuanlist.get(i);
		System.out.println("("+i+")\t"+"("+siyuanshi.getOne()+","+siyuanshi.getTwo()+","+siyuanshi.getThree()+","+siyuanshi.getFour()+")");
		
	}
}

private Integer merge(int a, int b) {
	// TODO Auto-generated method stub
	SiYuanShi siyuanshi=new SiYuanShi(null,null,null,null);
	if(b==0) return a;
	else{
		int temp=b;
		while(temp!=0){
			siyuanshi=siyuanlist.get(temp);
			temp=siyuanshi.getNextOfchain();
		}
	siyuanshi.setNextOfchain(a);
	return b;
	}
	
}

private void backpatch(int a, int b) {
	// TODO Auto-generated method stub
	int temp=a;
	SiYuanShi siyuanshi;
	while(temp!=0){
		
		siyuanshi=siyuanlist.get(temp);
		temp=siyuanshi.getNextOfchain();
		siyuanshi.setFour(String.valueOf(b));
		
	}
	
}

private String getTemp() {
	// TODO Auto-generated method stub
	this.tempnum++;
	
	return "T"+String.valueOf(tempnum-1);
}

private int getNow() throws Exception {
	// TODO Auto-generated method stub
	if(index>=input.size()) 
		throw new Exception("语法错误：程序缺少结束标记");
	return input.get(index);
	
}
/**
 * 获取上一个字符，如果没有上一个返回-1
 * @return
 * @throws Exception
 */
private String getPreviousVar() throws Exception {
	// TODO Auto-generated method stub

	if(index>=1)
	{ 
	return (String) varnameList.get(index-1);
	}
	else return "error";
	
}
private void MatchToken(int i) throws Exception {
	// TODO Auto-generated method stub
	if(getNow()!=i){
		throw new Exception("语法错误：输入"+getNow()+"错误，应该输入"+i);
	}
	else 
		//匹配下一个字符
		this.index++;
}
/**
 * 四元式输出函数
 * @param first
 * @param second
 * @param third
 * @param fourth
 * @throws Exception
 */
private void emit(String first,String second,String third,String fourth) throws Exception {
	// TODO Auto-generated method stub
	siyuanlist.add(new SiYuanShi(first,second,third,fourth));
	nextstat++;
}

}
