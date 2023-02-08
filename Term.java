//Thang Tran 
//tdt200004

public class Term implements Comparable<Term>{
	private int coeff;
	private int exp;
	//constructor
	public Term (int coeff, int exp) {
	    this.coeff = coeff;
	    this.exp = exp;
	}
	//accessor 
	public int getCoeff() {
	    return this.coeff;
	}
	public int getExp() {
	    return this.exp;
	}
	//mutator
	public void setCoeff(int coeff) {
	    this.coeff = coeff;
	}
	public void setExp(int exp) {
	    this.exp = exp;
	}
	//comparator
	public int compareTo(Term t) {
	    return (this.exp - t.getExp());
	}
	//merge this term with an equivalent term
	public void merge(Term rightTerm) {
	    if (this.exp == rightTerm.getExp()) {
	        this.coeff += rightTerm.getCoeff();
	    }
	}
}