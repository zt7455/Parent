package com.shengliedu.parent.bean;

import org.jivesoftware.smack.RosterEntry;

public class BeanTXL {
	public RosterEntry rosterEntry;
	public String grade;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result
				+ ((rosterEntry == null) ? 0 : rosterEntry.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanTXL other = (BeanTXL) obj;
		if (rosterEntry == null) {
			if (other.rosterEntry != null)
				return false;
		} else if (!rosterEntry.equals(other.rosterEntry))
			return false;
		return true;
	}
	
	
}
