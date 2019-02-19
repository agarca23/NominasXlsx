import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DatosNominas {

	private String[] categoria = new String[14];
	private double[] salarioBase = new double[14];
	private int[] complementos = new int[14];
	private int[] codigoCotizacion = new int[14];

	private double cuotaObreraGeneralTrabajador;
	private double cuotaDesempleoTrabajador;
	private double cuotaFormacionTrabajador;
	
	private double contingenciasComunesEmpresario;
	private double fogasaEmpresario;
	private double desempleoEmpresario;
	private double formacionEpresario;
	private double accidentesEmpresario;

	private int[] brutoAnual = new int[49];
	private double[] retencion = new double[49];

	private int[] numeroTrienios = new int[18];
	private int[] importeBruto = new int[18];

	public DatosNominas(File excelFile) {
		InputStream excelStream = null;
		try {
			excelStream = new FileInputStream(excelFile);
			// High level representation of a workbook.
			// Represents¿½n del mï¿½s alto nivel de la hoja excel.
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelStream);
			// We chose the sheet is passed as parameter.
			// Elegimos la hoja que se pasa por parï¿½metro.
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(1);
			// An object that allows us to read a row of the excel sheet, and
			// extract from it the cell contents.
			// Objeto que nos permite leer un fila de la hoja excel, y de aquï¿½
			// extraer el contenido de las celdas.
			XSSFRow xssfRow;
			// Initialize the object to read the value of the cell
			// Inicializo el objeto que leerï¿½ el valor de la celda
			XSSFCell cell;
			// I get the number of rows occupied on the sheet
			// Obtengo el nï¿½mero de filas ocupadas en la hoja
			// int rows = xssfSheet.getLastRowNum();
			// I get the number of columns occupied on the sheet
			// Obtengo el nï¿½mero de columnas ocupadas en la hoja
			// int cols = 0;
			// A string used to store the reading cell
			// Cadena que usamos para almacenar la lectura de la celda
			String cellValue;
			// For this example we'll loop through the rows getting the data we
			// want
			// Para este ejemplo vamos a recorrer las filas obteniendo los datos
			// que queremos
			for (int r = 1; r < 15; r++) {
				xssfRow = xssfSheet.getRow(r);
				int c = 0;
				cellValue = xssfRow.getCell(c) == null ? ""
						: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(c).getStringCellValue()
								: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(c).getNumericCellValue()
										: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(c).getBooleanCellValue()
												: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(c)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.categoria[r - 1] = cellValue;
				int d = 1;
				cellValue = xssfRow.getCell(d) == null ? ""
						: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(d).getStringCellValue()
								: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(d).getNumericCellValue()
										: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(d).getBooleanCellValue()
												: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(d)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.salarioBase[r - 1] = Double.parseDouble(cellValue);
				int e = 2;
				cellValue = xssfRow.getCell(e) == null ? ""
						: (xssfRow.getCell(e).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(e).getStringCellValue()
								: (xssfRow.getCell(e).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(e).getNumericCellValue()
										: (xssfRow.getCell(e).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(e).getBooleanCellValue()
												: (xssfRow.getCell(e).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(e).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(e)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.complementos[r - 1] = (int) (Double.parseDouble(cellValue));
				int f = 3;
				cellValue = xssfRow.getCell(f) == null ? ""
						: (xssfRow.getCell(f).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(f).getStringCellValue()
								: (xssfRow.getCell(f).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(f).getNumericCellValue()
										: (xssfRow.getCell(f).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(f).getBooleanCellValue()
												: (xssfRow.getCell(f).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(f).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(f)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.codigoCotizacion[r - 1] = (int) (Double.parseDouble(cellValue));
				// System.out.println(cellValue);
			}
			for (int r = 1; r < 50; r++) {
				xssfRow = xssfSheet.getRow(r);
				int c = 5;
				cellValue = xssfRow.getCell(c) == null ? ""
						: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(c).getStringCellValue()
								: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(c).getNumericCellValue()
										: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(c).getBooleanCellValue()
												: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(c)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.brutoAnual[r - 1] = (int) (Double.parseDouble(cellValue));
				int d = 6;
				cellValue = xssfRow.getCell(d) == null ? ""
						: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(d).getStringCellValue()
								: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(d).getNumericCellValue()
										: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(d).getBooleanCellValue()
												: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(d)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.retencion[r - 1] = (Double.parseDouble(cellValue));
			}
			int contador = 0;
			for (int r = 18; r < 36; r++) {

				xssfRow = xssfSheet.getRow(r);
				int c = 3;
				cellValue = xssfRow.getCell(c) == null ? ""
						: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(c).getStringCellValue()
								: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(c).getNumericCellValue()
										: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(c).getBooleanCellValue()
												: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(c)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.numeroTrienios[contador] = (int) (Double.parseDouble(cellValue));
				int d = 4;
				cellValue = xssfRow.getCell(d) == null ? ""
						: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_STRING)
								? xssfRow.getCell(d).getStringCellValue()
								: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_NUMERIC)
										? "" + xssfRow.getCell(d).getNumericCellValue()
										: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_BOOLEAN)
												? "" + xssfRow.getCell(d).getBooleanCellValue()
												: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
														: (xssfRow.getCell(d).getCellType() == Cell.CELL_TYPE_FORMULA)
																? "FORMULA"
																: (xssfRow.getCell(d)
																		.getCellType() == Cell.CELL_TYPE_ERROR)
																				? "ERROR"
																				: "";
				this.importeBruto[contador] = (int) (Double.parseDouble(cellValue));
				contador++;
			}
			xssfRow = xssfSheet.getRow(17);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.cuotaObreraGeneralTrabajador = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(18);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.cuotaDesempleoTrabajador = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(19);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.cuotaFormacionTrabajador = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(20);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.contingenciasComunesEmpresario = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(21);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.fogasaEmpresario = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(22);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.desempleoEmpresario = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(23);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.formacionEpresario = (Double.parseDouble(cellValue));
			xssfRow = xssfSheet.getRow(24);
			cellValue = xssfRow.getCell(1) == null ? ""
					: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
							? xssfRow.getCell(1).getStringCellValue()
							: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
									? "" + xssfRow.getCell(1).getNumericCellValue()
									: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BOOLEAN)
											? "" + xssfRow.getCell(1).getBooleanCellValue()
											: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) ? "BLANK"
													: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_FORMULA)
															? "FORMULA"
															: (xssfRow.getCell(1).getCellType() == Cell.CELL_TYPE_ERROR)
																	? "ERROR"
																	: "";
			this.accidentesEmpresario = (Double.parseDouble(cellValue));
			//System.out.println(accidentesEmpresario);
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists (No se encontrï¿½ el fichero): " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
		} finally {
			try {
				excelStream.close();
			} catch (IOException ex) {
				System.out.println(
						"Error in file processing after close it (Error al procesar el fichero despuï¿½s de cerrarlo): "
								+ ex);
			}
		}

		// for (int i = 0; i < numeroTrienios.length; i++) {
		// System.out.println(importeBruto[i]);
		// }
	}

	public String[] getCategoria() {
		return categoria;
	}

	public double[] getSalarioBase() {
		return salarioBase;
	}

	public int[] getComplementos() {
		return complementos;
	}

	public int[] getCodigoCotizacion() {
		return codigoCotizacion;
	}

	public double getCuotaObreraGeneralTrabajador() {
		return cuotaObreraGeneralTrabajador;
	}

	public double getCuotaDesempleoTrabajador() {
		return cuotaDesempleoTrabajador;
	}

	public double getCuotaFormacionTrabajador() {
		return cuotaFormacionTrabajador;
	}

	public double getContingenciasComunesEmpresario() {
		return contingenciasComunesEmpresario;
	}

	public double getFogasaEmpresario() {
		return fogasaEmpresario;
	}

	public double getDesempleoEmpresario() {
		return desempleoEmpresario;
	}

	public double getFormacionEpresario() {
		return formacionEpresario;
	}

	public double getAccidentesEmpresario() {
		return accidentesEmpresario;
	}

	public int[] getBrutoAnual() {
		return brutoAnual;
	}

	public double[] getRetencion() {
		return retencion;
	}

	public int[] getNumeroTrienios() {
		return numeroTrienios;
	}

	public int[] getImporteBruto() {
		return importeBruto;
	}

}
