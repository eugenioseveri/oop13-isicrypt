package gui.models;

import java.io.Serializable;

/**
 * Generic class used to store 3 objects together.
 * @author Eugenio Severi
 */
public class Triple<X,Y,Z> implements Serializable {
	
	private static final long serialVersionUID = -1366233459051462780L;
	private X first;
	private Y second;
	private Z third;
	
	public Triple(final X first, final Y second, final Z third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public X getFirst() {
		return first;
	}

	public void setFirst(final X first) {
		this.first = first;
	}

	public Y getSecond() {
		return second;
	}

	public void setSecond(final Y second) {
		this.second = second;
	}

	public Z getThird() {
		return third;
	}

	public void setThird(final Z third) {
		this.third = third;
	}
	
	@Override
	public String toString() {
		return "Triple [first=" + first + ", second=" + second + ", third="
				+ third + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		final Triple<X, Y, Z> other = (Triple<X, Y, Z>) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}
	
}
