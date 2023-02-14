/***
  Unify Program written



  変数:前に？をつける．  

  Examle:
  % Unify "Takayuki" "Takayuki"
  true

  % Unify "Takayuki" "Takoyuki"
  false

  % Unify "?x am Takayuki" "I am Takayuki"
  ?x = I .

  % Unify "?x is ?x" "a is b"
  false

  % Unify "?x is ?x" "a is a"
  ?x = a .

  % Unify "?x is a" "b is ?y"
  ?x = b.
  ?y = a.

  % Unify "?x is a" "?y is ?x"
  ?x = a.
  ?y = a.

  Unify は，ユニフィケーション照合アルゴリズムを実現し，
  パターン表現を比較して矛盾のない代入によって同一と判断
  できるかどうかを調べる．

  ポイント！
  ここでは，ストリング同士の単一化であるから，出現検査を行う必要はない．
  しかし，"?x is a"という表記を"is(?x,a)"とするなど，構造を使うならば，
  単一化において出現検査を行う必要がある．
  例えば，"a(?x)"と"?x"を単一化すると ?x = a(a(a(...))) となり，
  無限ループに陥ってしまう．

  ***/
/***
Unify Program written



変数:前に？をつける．  

Examle:
% Unify "Takayuki" "Takayuki"
true

% Unify "Takayuki" "Takoyuki"
false

% Unify "?x am Takayuki" "I am Takayuki"
?x = I .

% Unify "?x is ?x" "a is b"
false

% Unify "?x is ?x" "a is a"
?x = a .

% Unify "?x is a" "b is ?y"
?x = b.
?y = a.

% Unify "?x is a" "?y is ?x"
?x = a.
?y = a.

Unify は，ユニフィケーション照合アルゴリズムを実現し，
パターン表現を比較して矛盾のない代入によって同一と判断
できるかどうかを調べる．

ポイント！
ここでは，ストリング同士の単一化であるから，出現検査を行う必要はない．
しかし，"?x is a"という表記を"is(?x,a)"とするなど，構造を使うならば，
単一化において出現検査を行う必要がある．
例えば，"a(?x)"と"?x"を単一化すると ?x = a(a(a(...))) となり，
無限ループに陥ってしまう．

***/

import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class Unify {
    public static void main(String arg[]){

    String fileName =  "src/dataset.txt";
    ArrayList<String> querylist = new  ArrayList<String>();
    ArrayList<String> datasetlist = new  ArrayList<String>();
    HashMap<String,String> varsmap = new HashMap<String,String>();
    ArrayList<String> maplist = new ArrayList<String>();
    try {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            datasetlist.add(line);
            // System.out.println("dataset add sucsess");
        }
    in.close();
    } catch (IOException e) {
        e.printStackTrace();
	}



    if(arg.length >= 1) {
        for (int i = 0; i < arg.length; i++) {
        	querylist.add(arg[i]);
        	// System.out.println("query add success");
        }

        } else {
            System.out.println("Usgae : % Unify [string]");

    }

    for(String query:querylist) {
//    	System.out.println(query);
        for(String data:datasetlist) {
//        	System.out.println(data);
            Unifier unifier = new Unifier();
            unifier.unify(query, data);
            if(!unifier.vartoString.equals("")) {
                String temp = unifier.vartoString.replace("{", "").replace("}", "");

                maplist.add(temp);
            }
        //    System.out.println(unifier.vars.toString());
        }
    }

    for (int i=0; i<maplist.size();i++){
        for(int j=0;j<maplist.size();j++){
            if (i!=j){
                String str1=maplist.get(i);
                String str2=maplist.get(j);
                if(str1.contains(str2)){
                    System.out.println(str1);
                }
            }
        }
    }
    System.out.println();
    }
}
class returnValue {
	boolean b;
	String s;
	returnValue(boolean b){
		b = this.b;
		s = "";
	}
	
	returnValue(boolean b, String s){
		b = this.b;
		s = this.s;
	}
	
	public boolean getbool() {
		return b;
	}
	
	public String getStr() {
		return s;
	}
}
class Unifier {
    StringTokenizer st1;
    String buffer1[];    
    StringTokenizer st2;
    String buffer2[];
    HashMap<String,String> vars;
    String vartoString = "";
    ArrayList<String> maplist = new ArrayList<String>();
    Unifier(){
        vars = new HashMap<String,String>();
    }

    public String getvartoString() {
        // String fuga = vartoString.replaceAll("{", "").replaceAll("}", "");
    	return vartoString;
    }
    
    public boolean unify(String string1,String string2){
//        System.out.println(string1);
//        System.out.println(string2);
    
        // 同じなら成功
        if(string1.equals(string2)) return true;
    
        // 各々トークンに分ける
        st1 = new StringTokenizer(string1);
        st2 = new StringTokenizer(string2);
    
        // 数が異なったら失敗
        if(st1.countTokens() != st2.countTokens()) return false;
    
        // 定数同士
        int length = st1.countTokens();
        buffer1 = new String[length];
        buffer2 = new String[length];
        for(int i = 0 ; i < length; i++){
            buffer1[i] = st1.nextToken();
            buffer2[i] = st2.nextToken();
        }
        for(int i = 0 ; i < length ; i++){
            if(!tokenMatching(buffer1[i],buffer2[i])){
                return false;
            }
        }
    
    
        // 最後まで O.K. なら成功
        // System.out.println(vars.toString());
        vartoString = vars.toString();
//        System.out.println(vartoString);
        return true;
    }

    boolean tokenMatching(String token1,String token2){
        if(token1.equals(token2)) return true;
        if( var(token1) && !var(token2)) return varMatching(token1,token2);
        if(!var(token1) &&  var(token2)) return varMatching(token2,token1);
        if( var(token1) &&  var(token2)) return varMatching(token1,token2);
        return false;
    }

    boolean varMatching(String vartoken,String token){
        if(vars.containsKey(vartoken)){
            if(token.equals(vars.get(vartoken))){
                return true;
            } else {
                return false;
            }
        } else {
            replaceBuffer(vartoken,token);
            if(vars.containsValue(vartoken)){
                replaceBindings(vartoken,token);
            }
            vars.put(vartoken,token);
        }
        return true;
    }

    void replaceBuffer(String preString,String postString){
        for(int i = 0 ; i < buffer1.length ; i++){
            if(preString.equals(buffer1[i])){
                buffer1[i] = postString;
            }
            if(preString.equals(buffer2[i])){
                buffer2[i] = postString;
            }
        }
    }
    
    void replaceBindings(String preString,String postString){
    Iterator<String> keys;
    for(keys = vars.keySet().iterator(); keys.hasNext();){
        String key = (String)keys.next();
        if(preString.equals(vars.get(key))){
        vars.put(key,postString);
        }
    }
    }
    
    
    boolean var(String str1){
    // 先頭が ? なら変数
    return str1.startsWith("?");
    }

}


