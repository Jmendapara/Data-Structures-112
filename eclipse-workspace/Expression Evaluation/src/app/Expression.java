package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	
    	String key = "";
    	
    	int i=0;
    	
    	while(i<expr.length()){

    		if(expr.charAt(i)==' ' ||expr.charAt(i)=='\t' ||Character.isDigit(expr.charAt(i))){
    			
    			if(i==expr.length()-1) {
    			expr=expr.substring(0, i+1);
    			}
    			
    			else {
    				expr= expr.substring(0, i) + expr.substring(i+1);
    			}
    		}
    		
    		i++;
    	
    	}
    
    	i=0;
    	
    	while(i<expr.length())
    	{
    		
    		 if(Character.isLetter(expr.charAt(i))) {
    			key=key+expr.charAt(i);
    			i++;
    			
    			while((i<expr.length())&&Character.isLetter(expr.charAt(i))){
    				key=key+expr.charAt(i);
    				i++;
    			}
    			
    			int yes = 0;
    			int yes1 = 0;
    	
    			for(int k=0;k<vars.size();k++) {
    				if(vars.get(k).name.equals(key)) {
    					yes=1;
    					yes1=0;
    				}
    			}	
    		
    		for(int k=0;k<arrays.size();k++) {
    			
    			if(arrays.get(k).name.equals(key)) {
    				yes1=1;
    				yes=0;
    			    }
    		 	}
    		
    			if(i<(expr.length())&&(expr.charAt(i))=='['&&yes1==0){
        			Array arr = new Array(key);
    			   	 arrays.add(arr);
    			
				}
    			
    			else if(i<(expr.length())&&(expr.charAt(i))=='['&&yes1==1){
    			
				}
				
    			else if(yes==0){
    				Variable var=new Variable(key);
    				vars.add(var);

    			}

    			
    			}
    			
     		i++;

			key="";

       	}
    	
    }
    	
    
    	
    
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    
 
    
    
    private static float evaluate3(ArrayList<String> expression,ArrayList<Array> arrays) {
    	//a*(a+b*arrayA[b*b]+c-b)+2
    	//a * (b+A[B[2]])*d + 3
    	        Stack<String> vals = new Stack<>();
    	        Stack<Character> opps = new Stack<>();
    	        
    	        int i = 0;
    	        while (i < expression.size()) {
    	        
    	            if ((expression.get(i).charAt(0) >= '0' && expression.get(i).charAt(0) <= '9')||Character.isLetter(expression.get(i).charAt(0))) {//if the element is a number
    	                vals.push((expression.get(i)));
    	            }
    	            
    	            
    	            else {

    	                if (expression.get(i).charAt(0) == ')'||expression.get(i).charAt(0) == ']') {
    	                	
    	                    while (opps.peek() != '('&& opps.peek() != '[') {
    	                        
    	                    	Character sign = opps.pop();
    	                    	String val2=vals.pop();
    	                    	String val1=vals.pop();

    	                    	String valf="";
    	                    	
    	                    	 if( sign == '+'){
    	                             valf= ""+(Float.valueOf(val1) + Float.valueOf(val2));}
    	                    	 else if(sign=='-') {
    	                             valf= ""+(Float.valueOf(val1) - Float.valueOf(val2));}
    	                             
    	                    	 else if(sign=='*') {
    	                             valf= ""+(Float.valueOf(val1) * Float.valueOf(val2));}

    	                    	 
    	                    	 else if(sign=='/') {
    	                             valf= ""+(Float.valueOf(val1) / Float.valueOf(val2));}
    	                 
    	                    	 vals.push(valf);

    	                         }
    	                    
    	                    if(opps.peek()=='(')
    	                    	{opps.pop();}
    	                   
    	                    else if(opps.peek() == '[') {
    	                    	
    	                    	opps.pop();
    	                    	String element = vals.pop();
    	                    	String names = vals.pop();

    	                    	String eval="";
    	                    	for(int e=0;e<arrays.size();e++){
    	                    		
    	                    		if(arrays.get(e).name.equals(names)) {
    	                    		
    	                    			int [] k = arrays.get(e).values;
    	                    			eval=""+k[(int)(Float.parseFloat(element))];
    	                    		}
    	                    	}
    	                             
    	                    vals.push(eval);
    	                    
    	                    }

    	                }
    	           
    	            else {

    	                    Character currentOperator = expression.get(i).charAt(0);
    	                    Character lastOperator = (opps.isEmpty() ? null : opps.peek());
    	                 
    	                    if (lastOperator != null && precedence(currentOperator, lastOperator)) {
    	                   
    	                    	vals.push(expression.get(i+1));
    	                    	char sign= currentOperator;
    	                    	String val2=vals.pop();
    	                    	String val1=vals.pop();
    	                    	
    	                   
    	                    	String valf="";
    	                    	
    	                    	 if( sign == '+'){
    	                             valf= ""+(Float.valueOf(val1) + Float.valueOf(val2));}
    	                    	 else if(sign=='-') {
    	                             valf= ""+(Float.valueOf(val1) - Float.valueOf(val2));}
    	                             
    	                    	 else if(sign=='*') {
    	                             valf= ""+(Float.valueOf(val1) * Float.valueOf(val2));}

    	                    	 
    	                    	 else if(sign=='/') {
    	                             valf= ""+(Float.valueOf(val1) / Float.valueOf(val2));}
    	                                  
    	                    	 vals.push(valf);
    	                    	
    	                  i++;
    	                  
    	                    }
    	                    else if(lastOperator==null){
    	                    	opps.push(currentOperator);
    	                    }
    	                    
    	                    else if(lastOperator=='['||lastOperator=='('){
    	                    	opps.push(currentOperator);
    	                    }
    	                    
    	                    else if(currentOperator=='['||currentOperator=='(') {
    	                    	opps.push(currentOperator);

    	                    }
    	                    
    	                    else if (lastOperator!='('&&lastOperator!='['){
    	                    	
    	                    char sign= lastOperator;
    	                    String val2=vals.pop();
	                    	String val1=vals.pop();
	                    	String valf="";
	                    	
	                    	 if( sign == '+'){
	                             valf= ""+(Float.valueOf(val1) + Float.valueOf(val2));}
	                    	 else if(sign=='-') {
	                             valf= ""+(Float.valueOf(val1) - Float.valueOf(val2));}
	                             
	                    	 else if(sign=='*') {
	                             valf= ""+(Float.valueOf(val1) * Float.valueOf(val2));}

	                    	 
	                    	 else if(sign=='/') {
	                             valf= ""+(Float.valueOf(val1) / Float.valueOf(val2));}
	                                  
	                    	 vals.push(valf);
	    	                 opps.pop();
	                    	 i--;
	                    	 
    	                    }
    	                    
    	                    else {
    	                    	opps.push(currentOperator);
    	                    }

    	                }
    	                
    	            }
    	            
    	            i++;
    	            

    	        }


    	        while (!opps.isEmpty()) {
    	        	
    	        	char sign= opps.pop();
    	        	String val2=vals.pop();
    	        	String val1=vals.pop();
    	        	String valf="";
    	        	 if( sign == '+'){
    	                 valf= ""+(Float.valueOf(val1) + Float.valueOf(val2));}
    	        	 else if(sign =='-') {
    	                 valf= ""+(Float.valueOf(val1) - Float.valueOf(val2));}
    	                 
    	        	 else if(sign=='*') {
    	                 valf= ""+(Float.valueOf(val1) * Float.valueOf(val2));}

    	        	 
    	        	 else if(sign=='/') {
    	                 valf= ""+(Float.valueOf(val1) / Float.valueOf(val2));}
    	     
    	        	 vals.push(valf);

    	             }
    	       
    	        return Float.valueOf(vals.pop());//last value in stack

    	    }


    
    private static boolean precedence(Character o1, Character o2) {

   	 List<Character> pL = new ArrayList<>();
        pL.add('(');
        pL.add(')');
        pL.add('[');
        pL.add(']');
        pL.add('/');
        pL.add('*');
        pL.add('%');
        pL.add('-');
        pL.add('+');


        if(o2 == '('||o2=='[' ){
            return false;
        }
        
       
        if((o1=='*'||o1=='/')&&(o2=='*'||o2=='/')) {
       	 return false;
        }
        if((o1=='+'||o1=='-')&&(o2=='+'||o2=='-')) {
       	 return false;
        }
        if((o1=='*'||o1=='/')&&(o2=='+'||o2=='-')) {
       	 return true;
        }
        
        if((o1=='+'||o1=='-')&&(o2=='*'||o2=='/')) {

       	 return false;
        }
        
        if (pL.indexOf(o1) > pL.indexOf(o2)) {
            return true;
        } 
        else {
            return false;
        }
        
   }

    
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	int space=0;
    	while(space<expr.length()){
    		
    		if(expr.charAt(space)==' ' ||expr.charAt(space)=='\t'){
    			if(space==expr.length()-1) {
    				expr=expr.substring(0, space);
    			}
    			
    		else {
    			expr= expr.substring(0, space) + expr.substring(space+1);
    		}
    			
    		}
    		
    		space++;
    		
    	}
    	    	
    	
    	StringTokenizer tokens =new StringTokenizer(expr,delims,true);
    	ArrayList<String> tokensarraylist =new ArrayList<String>();
    	
    	while (tokens.hasMoreTokens()) {
    		
            tokensarraylist.add(tokens.nextToken());
        }

    	
    	for(int k=0;k<vars.size();k++) {
    		for(int f=0;f<tokensarraylist.size();f++) {
    			
    			if(vars.get(k).name.equals(tokensarraylist.get(f))) {
    			
    				tokensarraylist.set(f, ""+vars.get(k).value);
    				
    			}
    		}
    	}


    	String g = "";
    	
    	for(int k=0;k<tokensarraylist.size();k++) {
    		g=g+tokensarraylist.get(k);
    	}
    	
    
    	
    	return evaluate3(tokensarraylist, arrays);
   
    
    
    
    }
    
    
    
    
    
    
    
}
