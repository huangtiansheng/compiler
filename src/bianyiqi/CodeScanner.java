package bianyiqi;

import java.util.Scanner;

/** 
* @author hts
* @version date：2017年12月9日 下午12:04:59 
* 
*/
public class CodeScanner {
private Scanner scanner=new Scanner(System.in);
private WordAnalyse analyser=new WordAnalyse();
private ResultCollector collector;
public void startScan() throws Exception{
	int linenum=0;
	while(scanner.hasNext()){
		String line=scanner.nextLine();
		if(line.equals("EOF")) break; 
		analyser.startAnalyse(line,linenum);
		linenum++;
	}
	collector=analyser.getResult();
	GrammaAnalyze grammar=new GrammaAnalyze(collector,0);
	grammar.prase("程序");
	System.out.println("语法检测无误");
}

public static void main(String args[]) throws Exception{
	System.out.println("黄天晟           201530731079 15计科2班");
	System.out.println("请输入代码，最后一行以EOF结束！");
	CodeScanner codeScanner=new CodeScanner();
	codeScanner.startScan();
	
}
}
