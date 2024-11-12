package trt;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.collections4.functors.ExceptionClosure;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class SheetExcel {

	private String sheetName;
	private HashMap<String, Integer> head = new HashMap<>(); // column , columnIndex

	public SheetExcel(String sheetName) {
		this.sheetName = sheetName;
	}

	private ArrayList<Field> checkMappingColumn(Object cls, HashMap<String, Integer> header) {

		ArrayList<Field> existFields = new ArrayList<>();
		Field[] fff = cls.getClass().getDeclaredFields();
		LinkedList<Field> fields = new LinkedList(Arrays.asList(fff)); // attribute java

		for (Entry<String, Integer> excelColumn : head.entrySet()) { // column in Excel

			String excelColumnName = excelColumn.getKey().trim();
			boolean isFound = false;

			for (Field field : fields) // fields in Java
			{
				String fieldName = field.getName();

				if (excelColumnName.equalsIgnoreCase(fieldName)) { // Found

					existFields.add(field);
					fields.remove(field);
					isFound = true;
					break;

				}

			}

			if (!isFound) {
				System.err.println("Are you sure column " + excelColumnName + " in Excel and not in Java/n");

			}

		}

		for (Field field : fields) { // rest fields are in Java
			String fieldName = field.getName();
			System.err.println("Are you sure column " + fieldName + " in Java and not in Excel/n");

		}

		return existFields;

	}

	public static void setFields(Object cls, Row row, ArrayList<Field> existingFields,
			HashMap<String, Integer> header) {

		if (row != null) { // cell is not null

			try {
				Field[] fff = cls.getClass().getDeclaredFields();
				LinkedList<Field> fields = new LinkedList(Arrays.asList(fff)); // attribute java

				for (Field field : existingFields) {

					String fieldName = field.getName().toLowerCase();
					Class<?> type = field.getType();

					Cell cell = row.getCell(header.get(fieldName));

					if (cell == null) {
						System.out.println("CELL is Null " + row.getRowNum() + " " + fieldName + " not in Excel");
						continue;
					}

					String value = cell.toString();

					if (value.equals("")) {
						field.set(cls, value);

					}

					field.setAccessible(true);

					if ((type.equals(int.class) || type.equals(double.class)) && value.matches("-?\\d+")) {
						field.setInt(cls, Integer.parseInt(value));
					}

					else if (type.equals(boolean.class)
							&& (value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE"))) {
						field.setBoolean(cls, Boolean.parseBoolean(value));
					}

					else if (type.equals(String.class) && value instanceof String) {
						field.set(cls, value);
					}

					else {
						System.out.print(
								"Please check unsupported type or incompatability between field and it's data  :");
					}

				}

			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}

		}

	}

	public static <T> List<T> toList(String json, Class<T> type) {

		ArrayList<T> objs = new ArrayList<>();
		return null;
	}

	public <T> ArrayList<T> readSheet(T a) {

		ArrayList<T> objs = new ArrayList<>();

		FileInputStream file;

		try {

			file = new FileInputStream(new File("Path"));
			System.out.println("The choosed file is " + file);
			XSSFWorkbook wb = new XSSFWorkbook(file);
			Sheet sheet = wb.getSheet(sheetName);

			Iterator<Row> row = sheet.rowIterator();
			Row header = row.next();

			Iterator<Cell> h = header.cellIterator();

			// header and index

			while (h.hasNext()) {
				Cell cell = h.next();
				if (cell.toString().isEmpty() || cell.toString().equals("")) {
					continue;

				}

				head.put(cell.toString().trim().toLowerCase(), cell.getColumnIndex());

			}

			// need to check mapping
			// checkMappingColumn (T) a.getClass().newInstance();

			T objectCheck = (T) a.getClass().newInstance();

			ArrayList<Field> existingFields = checkMappingColumn(objectCheck, head);

			// iterate over rows

			Row rowModel = null;

			while (row.hasNext()) {
				rowModel = row.next();
				// Empty cell is null

				if (rowModel.getCell(0).toString().equalsIgnoreCase("End of Table")) { // getCell error null
					break;
				}

				T obj = (T) a.getClass().newInstance();

				setFields(obj, rowModel, existingFields, head); // Model , ROW , Column in excel

				objs.add(obj);
			}
			return objs;

		} catch (InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return objs;
	}

}
