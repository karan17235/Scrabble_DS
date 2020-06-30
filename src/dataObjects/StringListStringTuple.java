package dataObjects;

import java.util.List;

public class StringListStringTuple  implements java.io.Serializable {

	public String string;
	public List<String> arrayList;
	
	public StringListStringTuple(String string, List<String> arrayList)
	{
		this.string = string;
		this.arrayList = arrayList;
	}
}
