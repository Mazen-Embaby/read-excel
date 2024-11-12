package trt;

import java.util.ArrayList;

public class EX1 {

	private XsCell Defal;

	public ArrayList<EX1> EX1() {
		SheetExcel sheet = new SheetExcel("fff");
		ArrayList<EX1> ff = sheet.readSheet(this); // read this POJO
		return ff;
	}

}
