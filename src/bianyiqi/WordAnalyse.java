package bianyiqi;

import java.util.HashMap;
import java.util.Map;

/**
 *  
 * 
 * @author hts
 * @version date：2017年12月8日 下午11:17:44 
 * 
 */
public class WordAnalyse {
	private char ch;
	private String token = "";
	public ResultCollector result = new ResultCollector();
	public ResultCollector getResult() {
		return result;
	}

	public void setResult(ResultCollector result) {
		this.result = result;
	}


	private int index = 0;
	private int rowNum=0;
	private int rowLength = 0;
	// 一行字符串
	private char[] strChar = new char[1000];
	// 保留字
	private Map keyword = new HashMap();
	// 界符键值对表
	private Map punc = new HashMap();

	public WordAnalyse() {
		// 初始化符号表
		punc.put("(", 39);
		punc.put(")", 40);
		punc.put("*", 41);
		punc.put("*/", 42);
		punc.put("+", 43);
		punc.put(",", 44);
		punc.put("-", 45);
		punc.put(".", 46);
		punc.put("..", 47);
		punc.put("/", 48);
		punc.put("/*", 49);
		punc.put(":", 50);
		punc.put(":=", 51);
		punc.put(";", 52);
		punc.put("<", 53);
		punc.put("<=", 54);
		punc.put("<>", 55);
		punc.put("=", 56);
		punc.put(">", 57);
		punc.put(">=", 58);
		punc.put("[", 59);
		punc.put("]", 60);
		// 初始化保留字
		keyword.put("and", 1);
		keyword.put("array", 2);
		keyword.put("begin", 3);
		keyword.put("bool", 4);
		keyword.put("call", 5);
		keyword.put("case", 6);
		keyword.put("char", 7);
		keyword.put("constant", 8);
		keyword.put("dim", 9);
		keyword.put("do", 10);
		keyword.put("else", 11);
		keyword.put("end", 12);
		keyword.put("false", 13);
		keyword.put("for", 14);
		keyword.put("if", 15);
		keyword.put("input", 16);
		keyword.put("integer", 17);
		keyword.put("not", 18);
		keyword.put("of", 19);
		keyword.put("or", 20);
		keyword.put("output", 21);
		keyword.put("procedure", 22);
		keyword.put("program", 23);
		keyword.put("read", 24);
		keyword.put("real", 25);
		keyword.put("repeat", 26);
		keyword.put("set", 27);
		keyword.put("stop", 28);
		keyword.put("then", 29);
		keyword.put("to", 30);
		keyword.put("true", 31);
		keyword.put("until", 32);
		keyword.put("var", 33);
		keyword.put("while", 34);
		keyword.put("write", 35);

	}

	public char getBC() {
		while (this.index < this.rowLength && (strChar[this.index]) == ' '||(strChar[this.index]) == '\t'||(strChar[this.index]) == '\n') {
			if(this.index==this.rowLength-1) break;
			this.index++;
		}
		this.index++;
		return strChar[this.index - 1];

	}

	public char getChar() {
		this.index++;
		return strChar[this.index - 1];
	}

	public void startAnalyse(String rowString,int rowNum) throws Exception {
		this.index=0;
		this.rowNum=rowNum;
		strChar = rowString.toCharArray();
		rowLength = rowString.length();
		while (this.index < rowLength) {
			this.ch = this.getBC();
			//如果是空格，证明已经结束
			if(this.ch==' '||this.ch=='\n'||this.ch=='\t') 
			{	System.out.println("\n");
				return;
				
			}
			// 判断首字符是否为字母
			if (Character.isLetter(ch)) {
				this.token = this.concat(token, ch);
				while (this.index < rowLength) {
					ch = getChar();
					if (Character.isLetter(ch) || Character.isDigit(ch))
						this.token = this.concat(token, ch);
					else {
						this.index--;
						break;
					}
				}
				// 判断是否为保留字
				if (this.keyword.containsKey(token)) {
					result.collect(token, (int) this.keyword.get(token));
				}
				// 不时地话就是标识符
				else {
					result.collect(token, 36);
				}
			}
			// 首字符是數字
			else if (Character.isDigit(ch)) {
				this.token = this.concat(token, ch);
				while (this.index < rowLength) {
					ch = getChar();
					if (Character.isDigit(ch))
						this.token = this.concat(token, ch);
					else {
						this.index--;
						break;
					}
				}
				result.collect(token, 37);
			}
			// 否则就是符号
			else {
				this.token = this.concat(token, ch);
				if(this.index<this.rowLength)
				ch = this.strChar[this.index];
				else ch='@';
				//判断是否为字符常量（不知道为什么从word复制来的'是‘这样的）
				if(token.equals("'")||token.equals("’")){
					int temp=this.index;
					boolean match=false;
					while(this.index < rowLength){
						ch = getChar();
						this.concat(token, ch);
						if(String.valueOf(ch).equals("'")||String.valueOf(ch).equals("’")) 
						{   match=true;
							break;
						}
					}
					//不match的话报错
					if(!match)
					throw new Exception("[']号没有成对匹配，第一个[']在"+rowNum+"行"+temp+"个字符\n");	
					result.collect(token,38 );
				}
				
				//判断是否为注释
				else if((token+String.valueOf(ch)).equals("/*")){
					int temp=this.index;
					this.index++;
					boolean match=false;
					while(this.index < rowLength-1){
						ch = getChar();
						if(String.valueOf(ch).equals("*")) 
						{   ch = getChar();
							if(String.valueOf(ch).equals("/"))					
							{match=true;		
							break;}
							//不是*的话，证明认错了。回滚
							else this.index=this.index-2;
						
						}
					}
					//不match的话报错
					if(!match)
					throw new Exception("[/**]号没有成对匹配，第一个[/*]在"+rowNum+"行"+temp+"个字符\n");
				}
				//判断是否为双节符
				else if (this.punc.containsKey(token+String.valueOf(ch))) {
					this.index++;
					result.collect(token+String.valueOf(ch), (int) this.punc.get(token+String.valueOf(ch)));
				}
				//不时地话再判断是否为单节符
				else if (this.punc.containsKey(token)) {
					result.collect(token, (int) this.punc.get(token));
				}
				//否则报错，出现未知字符
				else{
					throw new Exception("在"+rowNum+"行"+index+"处出现未知字符"+token+"\n");
					
				}
				
			}
			this.token = "";
		}
		System.out.println("\n");
		}
	

	public String concat(String token, char ch) {
		return token + String.valueOf(ch);
	}

}
