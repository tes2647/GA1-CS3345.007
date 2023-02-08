//Thang Tran 
//tdt200004

import java.util.*;
import java.io.*;

public class Main
{
	public static void main(String[] args) throws FileNotFoundException, NullPointerException {
	    //prompt for input file name
	    String fName = "";
	    Scanner consoleIn = new Scanner(System.in);
	    System.out.print("Enter input file name: ");
	    fName = consoleIn.next();
	    consoleIn.close();
	    //======================================================================
	    //open and parse input from file
	    File inFile = new File(fName);
	    //check for file validity
	    if (!inFile.exists()) {
		    System.out.println("File not exist");
		    System.exit(0);
		}
		if (inFile.length() == 0 ) {
		    System.out.println("Empty file");
		    System.exit(0);
		}
		//----------------------------------------------------------------------
		//read and process data from file stream
		Scanner inStream = new Scanner(inFile);
		
		while (inStream.hasNextLine()) {
		    String line = inStream.nextLine();
		    //split the polynomial string by space character into integ array 
		    //format: array = {integral symnbol, term 1, term 2,...,term n}
		    ArrayList<String> integ = integSplit(line);
		    String integBound = "false";
		    BinTree<Term> binaryTree = new BinTree<Term>(null);
		    //handle integral type and bounds
		    if (!integ.get(0).equals("|")) {
		        //parse for bounds
		        integBound = integ.get(0);
		    }
		    
	        for (int i = 1; i < integ.size(); i++) {
	            //create term object from term String
	            Term integTerm = termCreate(integ.get(i));
	            //check for repeated terms and update accordingly
	            if (binaryTree.Search(integTerm) != null) {
	                Term holder = binaryTree.Search(integTerm);
	                binaryTree.Remove(integTerm);
	                integTerm.merge(holder);
	            }
	            //store terms 
	            binaryTree.Insert(integTerm);
	        }
	        //outputting data
		    printAntiDeriv(binaryTree, integBound);
		}
	}
	//==========================================================================
	//functions for splitting polynomial string into terms
	public static ArrayList<String> integSplit(String polyStr) {
	    ArrayList<String> integ = new ArrayList<String>();
	    //add integral symbol to the list
	    integ.add(polyStr.substring(0, polyStr.indexOf(" ")));
	    //get list of terms of the polynomial
	    termSplit(polyStr.substring(polyStr.indexOf(" ") + 1, polyStr.lastIndexOf("dx")).replaceAll("\\s", ""), integ);
	    
	    return integ;
	}
	//split the polynomial string for terms and store them in a list
	public static void termSplit(String termStr, ArrayList<String> integ) {
	    int termInd = 0;
	    for (int i = 1; i < termStr.length(); i++) {
	        //parse for terms
	        if (Character.compare(termStr.charAt(i), '+') == 0 || Character.compare(termStr.charAt(i), '-') == 0 && Character.compare(termStr.charAt(i - 1), '^') != 0) {
                integ.add(termStr.substring(termInd, i));
                termInd = i;
	        }
	    }
	    //store the parsed terms
	    integ.add(termStr.substring(termInd));
	}
	//--------------------------------------------------------------------------
	//functions for term creation and integration
	//create term from a string of term
	public static Term termCreate(String termStr) {
	    int coeff = 1;
	    int exp = 1;
	    //check if exponent is not 0
	    if (termStr.indexOf("x") != -1) {
	        //parse for coefficient
	        if (termStr.indexOf("x") != 0) {
	            if (termStr.indexOf("x") != 1)
	                coeff = Integer.parseInt(termStr.substring(0, termStr.indexOf("x")));
	            else {
	                if (Character.compare(termStr.charAt(0), '-') == 0)
	                    coeff = -1;
	                else if (Character.compare(termStr.charAt(0), '+') != 0)
	                    coeff = Integer.parseInt(termStr.substring(0, termStr.indexOf("x")));
	            }
	                
	        }
	        //parse for exponent
            if (termStr.indexOf("^") != -1)
	            exp = Integer.parseInt(termStr.substring(termStr.indexOf("^") + 1));
	    }
	    //get coefficient in case of zero exponent
	    else {
	        exp = 0;
	        coeff = Integer.parseInt(termStr);
	    }
	    return new Term(coeff, exp);
	}
	//convert integral term into anti anti-derivative
	//find the greatest common divisor of 2 int
	public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
    //integrate and return a string of the antiDerv
	public static String termIntegrate(Term term, boolean firstEntry) {
	    String antiDerv = "";
	    //normal integration
	    double antiDervCoeff = (double) term.getCoeff() / (double) (term.getExp() + 1);
	    //add +/- notation depend on if the term is the first term of the polynomial or not 
	    if (firstEntry != true) {
	        if (antiDervCoeff >= 0)
    	        antiDerv += "+ ";
    	    else if (antiDervCoeff < 0)
    	        antiDerv += "- ";
	    }
	    //check for case of 0 coeff of anti-derivative
	    if (antiDervCoeff == 0) {
	        antiDerv += "0";
	        return antiDerv;
	    }
	        
	    //print coeff of anti-derivative
	    if (Math.abs(antiDervCoeff) != 1) {
	        //check for case of integer coefficient after integration
	        if (term.getCoeff() % (term.getExp() + 1) == 0) {
	            if (firstEntry == true)
        	        antiDerv += (int) antiDervCoeff;
        	    else 
        	        antiDerv += (int) Math.abs(antiDervCoeff);
        	}
        	//if the coefficient after integration is a rational number
        	else {
        	    int d = gcd(term.getCoeff(), term.getExp() + 1);
        	    //simplify fraction
        	    int numerator = term.getCoeff()/d;
        	    int denominator = (term.getExp() + 1)/d;
        	    if (denominator < 0) {
    	            numerator *= -1;
    	            denominator *= -1;
        	    }
        	    if (firstEntry == true)
        	        antiDerv += "(" + numerator + "/" + denominator + ")";
        	    else
        	        antiDerv += "(" + Math.abs(numerator) + "/" + Math.abs(denominator) + ")";
        	}
	    }
	    
	    //print x and exponent
	    if (term.getExp() + 1 == 1) {
            antiDerv += "x";
        }
        else {
            antiDerv += "x^" + (term.getExp() + 1);
        }
        return antiDerv;
        //--------------------------------------------------------------------------
        //cos sin integration
	    /*
	    *cry*
	    */
	}
	//calculate definite anti-derivative term
	public static double finiteIntegCal(Term term, String integBound) {
	    //parse for upper and lower bounds
	    double upper = Integer.parseInt(integBound.substring(integBound.indexOf("|") + 1));
	    double lower = Integer.parseInt(integBound.substring(0, integBound.indexOf("|")));
	    //calculate coefficient and exponent of anti-derivative
	    double antiDervExp = term.getExp() + 1;
	    double antiDervCoeff = term.getCoeff()/antiDervExp;
	    //calculate term from bounds
	    return antiDervCoeff*(Math.pow(upper, antiDervExp) - Math.pow(lower, antiDervExp));
	}
	//--------------------------------------------------------------------------
	//functions for outputting terms 
	public static void printAntiDeriv(BinTree<Term> binaryTree, String integBound) {
	    ArrayList<Term> termList = new ArrayList<Term>();
        termList = binaryTree.GetContent();
        double integSum = 0;
        for (int j = 0; j < termList.size(); j++) {
            //print coeff of anti-derivative
            if (j == 0) {
                System.out.print(termIntegrate(termList.get(j), true));
            }
            else {
                System.out.print(termIntegrate(termList.get(j), false));
            }
            //check for definite polynomial and calculate accordingly
            if (!integBound.equals("false"))
                integSum += finiteIntegCal(termList.get(j), integBound);
            if (j != termList.size() - 1)
                System.out.print(" ");
        }
        //check for definity of polynomial
        if (!integBound.equals("false"))
            System.out.print(", " + integBound + " = " + String.format("%.3f", integSum));
        else 
            System.out.print(" + C");
        System.out.println();
	}
}
