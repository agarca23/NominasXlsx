import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.OutputKeys;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Validador de NIF/NIE a partir de excell
 *
 * @author Andres Garcia Alvarez
 * @author Marco Garcia Gonzalez
 * @author Eduardo Juarez Robles
 * @version 1.0
 */

public class Main extends JFrame {

	private JPanel contentPane;
	private final Action action = new SwingAction();
	ArrayList<Persona> errores = new ArrayList<Persona>();
	private JSpinner spinner;
	private JSpinner spinner_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnCargar = new JMenu("Cargar");
		menuBar.add(mnCargar);

		JMenuItem mntmArchivoXlsx = new JMenuItem("Archivo xlsx");
		mntmArchivoXlsx.setAction(action);
		mnCargar.add(mntmArchivoXlsx);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnInfo = new JButton("Info");
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String cadena = "Introduzca la fecha antes de cargar el archivo";

				JOptionPane.showMessageDialog(null, cadena, "Información", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnInfo.setBounds(345, 217, 89, 23);
		contentPane.add(btnInfo);

		JLabel lblNewLabel = new JLabel("Mes");
		lblNewLabel.setBounds(126, 61, 25, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Año");
		lblNewLabel_1.setBounds(126, 103, 25, 14);
		contentPane.add(lblNewLabel_1);

		spinner = new JSpinner();
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 12, 1);
		spinner.setModel(model);
		spinner.setBounds(182, 58, 63, 20);
		contentPane.add(spinner);

		spinner_1 = new JSpinner();
		SpinnerNumberModel model1 = new SpinnerNumberModel(2000, 1900, 2299, 1);
		spinner_1.setModel(model1);
		spinner_1.setBounds(182, 97, 63, 20);
		contentPane.add(spinner_1);

	}

	public File buscarArchivo() {

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel file", "xlsx", "xls");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(this);
		return chooser.getSelectedFile()/* .getAbsolutePath() */;
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Archivo xlsx");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {

			int contador = 0;
			File archivo = buscarArchivo();
			long starttine1 = System.nanoTime();
			ArrayList<Persona> datos = readExcelFile(archivo);
			compruebaTodos(datos);
			generarCorreos(datos);
			compruebaCuenta(datos);
			actualizarHoja(datos, archivo);

			int mes = (int) spinner.getValue();
			int anio = (int) spinner_1.getValue();
			anio -= 1900;
			mes -= 1;
			DatosNominas prueba = new DatosNominas(archivo);
			Date fecha = new Date(anio, mes, 1);
			// Date fecha = new Date(116, 5, 1);
			Nomina pru = new Nomina(datos.get(78), prueba, fecha);
			Pdf pruebapdf = new Pdf(datos.get(78), fecha, pru);

			generarNominas(datos, prueba, fecha);
			
			long starttine = System.nanoTime();
			System.out.println(starttine - starttine1);
			System.exit(0);

		}
	}

	public void generarNominas(ArrayList<Persona> datos, DatosNominas datosnominas, Date fecha) {
		for (int i = 0; i < datos.size(); i++) {
			if (datos.get(i).getDni() != null || datos.get(i).getNIE() != null) {
				if (fecha.getTime() > DateUtil.getJavaDate(Double.parseDouble(datos.get(i).getFechaAltaEmpresa()))
						.getTime()) {
					Nomina nomina = new Nomina(datos.get(i), datosnominas, fecha);
					// hacer pdfs pasando nomina
					Pdf pruebapdf = new Pdf(datos.get(i), fecha, nomina);
				}
			}
		}
	}

	public ArrayList<Persona> readExcelFile(File excelFile) {
		ArrayList<Persona> data = new ArrayList<>();

		InputStream excelStream = null;
		try {
			excelStream = new FileInputStream(excelFile);
			// High level representation of a workbook.
			// Represents��n del m�s alto nivel de la hoja excel.
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelStream);
			// We chose the sheet is passed as parameter.
			// Elegimos la hoja que se pasa por par�metro.
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			// An object that allows us to read a row of the excel sheet, and
			// extract from it the cell contents.
			// Objeto que nos permite leer un fila de la hoja excel, y de aqu�
			// extraer el contenido de las celdas.
			XSSFRow xssfRow;
			// Initialize the object to read the value of the cell
			// Inicializo el objeto que leer� el valor de la celda
			XSSFCell cell;
			// I get the number of rows occupied on the sheet
			// Obtengo el n�mero de filas ocupadas en la hoja
			int rows = xssfSheet.getLastRowNum();
			// I get the number of columns occupied on the sheet
			// Obtengo el n�mero de columnas ocupadas en la hoja
			int cols = 0;
			// A string used to store the reading cell
			// Cadena que usamos para almacenar la lectura de la celda
			String cellValue;
			// For this example we'll loop through the rows getting the data we
			// want
			// Para este ejemplo vamos a recorrer las filas obteniendo los datos
			// que queremos
			for (int r = 1; r <= rows; r++) {
				Persona miPersona = new Persona();
				xssfRow = xssfSheet.getRow(r);
				if (xssfRow == null) {
					break;
				} else {
					// System.out.print("Row: " + r + " -> ");
					for (int c = 0; c < xssfRow.getLastCellNum(); c++) {
						/*
						 * We have those cell types (tenemos estos tipos de celda): CELL_TYPE_BLANK,
						 * CELL_TYPE_NUMERIC, CELL_TYPE_BLANK, CELL_TYPE_FORMULA, CELL_TYPE_BOOLEAN,
						 * CELL_TYPE_ERROR
						 */
						cellValue = xssfRow.getCell(c) == null ? ""
								: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_STRING)
										? xssfRow.getCell(c).getStringCellValue()
										: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_NUMERIC)
												? "" + xssfRow.getCell(c).getNumericCellValue()
												: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BOOLEAN)
														? "" + xssfRow.getCell(c).getBooleanCellValue()
														: (xssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BLANK)
																? ""
																: (xssfRow.getCell(c)
																		.getCellType() == Cell.CELL_TYPE_FORMULA)
																				? "FORMULA"
																				: (xssfRow.getCell(c)
																						.getCellType() == Cell.CELL_TYPE_ERROR)
																								? "ERROR"
																								: "";
						// System.out.print("[Column " + c + ": " + cellValue +
						// "] ");
						miPersona.setFilaExcel(r);
						switch (c) {
						case 0:
							if (cellValue.equals("") && cellValue == null) {
								errores.add(miPersona);
							} else {
								miPersona.setIdentificacion(cellValue);
							}
							break;
						case 1:
							miPersona.setApellido1(cellValue);
							break;
						case 2:
							miPersona.setApellido2(cellValue);
							break;
						case 3:
							miPersona.setNombre(cellValue);
							break;
						case 4:
							miPersona.setEmail(cellValue);
							break;
						case 5:
							miPersona.setCategoria(cellValue);
							break;
						case 6:
							miPersona.setEmpresaNombre(cellValue);
							break;
						case 7:
							miPersona.setEmpresaCif(cellValue);
							break;
						case 8:
							miPersona.setFechaAltaEmpresa(cellValue);
							break;
						case 9:
							miPersona.setFechaAltaLaboral(cellValue);
							break;
						case 10:
							miPersona.setFechaBajaLaboral(cellValue);
							break;
						case 11:
							if (!cellValue.equals(""))
								miPersona.setHorasExtrasForzadas(Double.parseDouble(cellValue));
							break;
						case 12:
							if (cellValue.equals("")) {
								miPersona.setHorasExtrasVoluntarias(0);

							} else {
								miPersona.setHorasExtrasVoluntarias(Double.parseDouble(cellValue));
							}
							break;
						case 13:
							if (cellValue.equals("SI")) {
								miPersona.setProrrateadasExtra(true);
							} else {
								miPersona.setProrrateadasExtra(false);
							}
							break;
						case 14:

							miPersona.setCodigoCuenta(cellValue);
							break;
						case 15:
							miPersona.setPaisCuenta(cellValue);
							break;
						case 16:
							miPersona.setIban(cellValue);
							break;

						}

					}
					data.add(miPersona);
					// System.out.println();
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists (No se encontr� el fichero): " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
		} finally {
			try {
				excelStream.close();
			} catch (IOException ex) {
				System.out.println(
						"Error in file processing after close it (Error al procesar el fichero despu�s de cerrarlo): "
								+ ex);
			}
		}
		return data;
	}

	public void compruebaTodos(ArrayList<Persona> datos) {
		try {

			Element trabajadores = new Element("Trabajadores");
			Document doc = new Document(trabajadores);
			doc.removeContent();
			doc.setRootElement(trabajadores);
			/* Comprobamos los NIF/NIE */
			for (int i = 0; i < datos.size(); i++) {
				if (datos.get(i).getDni() != null) {
					if (!datos.get(i).getDni().verificarNIF()) {
						/* Escribimos fichero xml */
						// System.out.println("NIF erroneo " + datos.get(i).toString());
						datos.get(i).setDni(null);
						errores.add(datos.get(i));

					}
				} else if (datos.get(i).getNIE() != null) {
					if (!datos.get(i).getNIE().verificarNIE()) {
						// System.out.println("NIE erroneo " + datos.get(i).toString());
						datos.get(i).setNIE(null);
						errores.add(datos.get(i));

					}
				}
			}
			/* Comprobamos los repetidos */
			for (int i = 0; i < datos.size(); i++) {
				for (int j = 0; j < datos.size() - 1; j++) {
					if (i != j) {
						if (datos.get(i).getDni() != null && datos.get(j).getDni() != null) {
							if (datos.get(i).getDni().toString().equals(datos.get(j).getDni().toString())) {
								// System.out.println("NIF repetido " + datos.get(i).toString());
								Element trabajador = new Element("Trabajador");
								String num = String.valueOf(i + 2);
								trabajador.setAttribute(new Attribute("id", num));
								trabajador.addContent(new Element("Nombre").setText(datos.get(i).getNombre()));
								trabajador
										.addContent(new Element("PrimerApellido").setText(datos.get(i).getApellido1()));
								trabajador.addContent(
										new Element("SegundoApellido").setText(datos.get(i).getApellido2()));
								trabajador.addContent(new Element("Categoria").setText(datos.get(i).getCategoria()));
								trabajador.addContent(new Element("Empresa").setText(datos.get(i).getEmpresaNombre()));
								doc.getRootElement().addContent(trabajador);
							}
						} else if (datos.get(i).getNIE() != null && datos.get(j).getNIE() != null) {
							if (datos.get(i).getNIE().toString().equals(datos.get(j).getNIE().toString())) {
								// System.out.println("NIE repetido " + datos.get(i).toString());
								Element trabajador = new Element("Trabajador");
								String num = String.valueOf(i + 2);
								trabajador.setAttribute(new Attribute("id", num));
								trabajador.addContent(new Element("Nombre").setText(datos.get(i).getNombre()));
								trabajador
										.addContent(new Element("PrimerApellido").setText(datos.get(i).getApellido1()));
								trabajador.addContent(
										new Element("SegundoApellido").setText(datos.get(i).getApellido2()));
								trabajador.addContent(new Element("Categoria").setText(datos.get(i).getCategoria()));
								trabajador.addContent(new Element("Empresa").setText(datos.get(i).getEmpresaNombre()));
								doc.getRootElement().addContent(trabajador);
							}
						}
					}
				}

			}

			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			File directorio = new File("resources");
			directorio.mkdirs();

			xmlOutput.output(doc, new FileWriter("resources\\Errores.xml"));

			System.out.println("File Saved!");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}

	}

	public void generarCorreos(ArrayList<Persona> datos) {
		for (int i = 0; i < datos.size(); i++) {
			if (datos.get(i).getEmail() == null || datos.get(i).getEmail() == "")
				crearEmail(datos.get(i), datos);
		}
	}

	public void crearEmail(Persona elemento, ArrayList<Persona> datos) {

		StringBuffer email = new StringBuffer();
		String nom = elemento.getNombre().toLowerCase();
		String ape1 = elemento.getApellido1().toLowerCase();
		String ape2 = elemento.getApellido2().toLowerCase();
		String nombre_empresa = elemento.getEmpresaNombre().toLowerCase();

		if (nom != "" && ape1 != "" && ape2 != "") {
			email.append(nom.substring(0, 3));
			email.append(ape1.substring(0, 2));
			email.append(ape2.substring(0, 2));
			email.append(validarEmail(elemento, datos, email.toString())).append("@").append(nombre_empresa)
					.append(".es");
		}
		if (nom != "" && ape1 != "" && ape2 == "") {
			email.append(nom.substring(0, 3));
			email.append(ape1.substring(0, 2));
			email.append(validarEmail(elemento, datos, email.toString())).append("@").append(nombre_empresa)
					.append(".es");
		}
		elemento.setEmail(remove1(email.toString()));
		System.out.println(elemento.getEmail());

	}
	
	
	public static String remove1(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}

	/*
	 * Comprueba que no haya ninguna otra persona con el mismo nombre apellidos y
	 * empresa, si lo hay asigna otro numero
	 */
	public String validarEmail(Persona entrada, ArrayList<Persona> datos, String emailparcial) {
		int cont = 0;
		StringBuffer numero = new StringBuffer();
		String letrasEmail;
		for (int i = 0; i < datos.size(); i++) {
			/*
			 * Comprobaremos si se aumenta el contador solo si la persona no es nula, ya
			 * tiene un email asignado, y la empresa es la misma
			 */
			if (datos.get(i) != null && datos.get(i).getEmail() != ""
					&& datos.get(i).getEmpresaNombre().equals(entrada.getEmpresaNombre()) && datos.get(i) != entrada) {
				String division[] = datos.get(i).getEmail().split("@");
				if (division[0].length() == 9) {
					letrasEmail = division[0].substring(0, 7);
				} else {
					letrasEmail = division[0].substring(0, 5);
				}
				if (letrasEmail.equals(emailparcial)) {
					cont++;
				}
			}
		}
		if (cont < 10) {
			numero.append(0).append(cont);
		} else {
			numero.append(cont);
		}
		return numero.toString();

	}

	public void compruebaCuenta(ArrayList<Persona> datos) {
		try {
			Element trabajadores = new Element("Trabajadores");
			Document doc = new Document(trabajadores);
			doc.removeContent();
			doc.setRootElement(trabajadores);
			for (int i = 0; i < datos.size(); i++) {
				// System.out.println(datos.get(i).getCodigoCuenta());
				// System.out.println(datos.get(i).getPaisCuenta());

				Cuenta cuenta = new Cuenta(datos.get(i).getCodigoCuenta(), datos.get(i).getPaisCuenta());

				// Solo hay 6 cuentas correctas
				// System.out.println(cuenta.isValidar());

				// Comprobamos las cuentas que son correctas y se las asignamos a la persona
				if (cuenta.isValidar()) {
					datos.get(i).setCuenta(cuenta);
					cuenta.setDigitoControl(
							String.valueOf(cuenta.calcularDigitoControl("00" + cuenta.getBanco() + cuenta.getOficina()))
									+ String.valueOf(cuenta.calcularDigitoControl(cuenta.getNumeroCuenta())));

					cuenta.cambiarNumeroCompleto(cuenta.getBanco(), cuenta.getOficina(), cuenta.getDigitoControl(),
							cuenta.getNumeroCuenta());

					datos.get(i).setIban(datos.get(i).getPaisCuenta()
							+ datos.get(i).getCuenta().calcularIBAN(datos.get(i).getCuenta().getBanco(),
									datos.get(i).getCuenta().getOficina(), datos.get(i).getCuenta().getDigitoControl(),
									datos.get(i).getCuenta().getNumeroCuenta(),
									datos.get(i).getCuenta().getCodigoPais())
							+ datos.get(i).getCuenta().getNumeroCuentaCompleto());
					// System.out.println(datos.get(i).getIban());

					// Las personas que tienen un numero de cuenta incorrecto, modificamos dicho
					// numero y le asignamos la nueva cuenta
				} else {

					Element trabajador = new Element("Trabajador");
					String num = String.valueOf(i + 2);
					trabajador.setAttribute(new Attribute("id", num));
					trabajador.addContent(new Element("Nombre").setText(datos.get(i).getNombre()));
					trabajador.addContent(new Element("PrimerApellido").setText(datos.get(i).getApellido1()));
					trabajador.addContent(new Element("SegundoApellido").setText(datos.get(i).getApellido2()));
					trabajador.addContent(new Element("Empresa").setText(datos.get(i).getEmpresaNombre()));

					trabajador.addContent(new Element("CodigoCuentaErroneo").setText(cuenta.getBanco()
							+ cuenta.getOficina() + cuenta.getDigitoControl() + cuenta.getNumeroCuenta()));
					cuenta.setDigitoControl(
							String.valueOf(cuenta.calcularDigitoControl("00" + cuenta.getBanco() + cuenta.getOficina()))
									+ String.valueOf(cuenta.calcularDigitoControl(cuenta.getNumeroCuenta())));

					cuenta.cambiarNumeroCompleto(cuenta.getBanco(), cuenta.getOficina(), cuenta.getDigitoControl(),
							cuenta.getNumeroCuenta());

					Cuenta cuentaAux = new Cuenta(cuenta.getBanco() + cuenta.getOficina() + cuenta.getDigitoControl()
							+ cuenta.getNumeroCuenta(), cuenta.getCodigoPais());
					datos.get(i).setCuenta(cuentaAux);
					datos.get(i)
							.setCodigoCuenta(datos.get(i).getCuenta().getBanco() + datos.get(i).getCuenta().getOficina()
									+ datos.get(i).getCuenta().getDigitoControl()
									+ datos.get(i).getCuenta().getNumeroCuenta());
					datos.get(i).setIban(datos.get(i).getPaisCuenta()
							+ datos.get(i).getCuenta().calcularIBAN(datos.get(i).getCuenta().getBanco(),
									datos.get(i).getCuenta().getOficina(), datos.get(i).getCuenta().getDigitoControl(),
									datos.get(i).getCuenta().getNumeroCuenta(),
									datos.get(i).getCuenta().getCodigoPais())
							+ datos.get(i).getCuenta().getNumeroCuentaCompleto());

					// System.out.println(datos.get(i).getIban());

					trabajador.addContent(new Element("IBAN").setText(datos.get(i).getIban()));
					doc.getRootElement().addContent(trabajador);

				}

			}
			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice

			xmlOutput.escapeAttributeEntities("ISO-8859-1");
			File directorio = new File("resources");
			directorio.mkdirs();

			xmlOutput.output(doc, new FileWriter("resources\\cuentasErroneas.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actualizarHoja(ArrayList<Persona> datos, File archivo) {

		InputStream excelStream = null;
		XSSFWorkbook xssfWorkbook = null;
		try {
			excelStream = new FileInputStream(archivo);
			// High level representation of a workbook.
			// Representaci�n del m�s alto nivel de la hoja excel.
			xssfWorkbook = new XSSFWorkbook(excelStream);
			// We chose the sheet is passed as parameter.
			// Elegimos la hoja que se pasa por par�metro.
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			// An object that allows us to read a row of the excel sheet, and
			// extract from it the cell contents.
			// Objeto que nos permite leer un fila de la hoja excel, y de aqu�
			// extraer el contenido de las celdas.
			XSSFRow xssfRow;
			// Initialize the object to read the value of the cell
			// Inicializo el objeto que leer� el valor de la celda
			XSSFCell cell;
			// I get the number of rows occupied on the sheet
			// Obtengo el n�mero de filas ocupadas en la hoja
			int rows = xssfSheet.getLastRowNum();
			// I get the number of columns occupied on the sheet
			// Obtengo el n�mero de columnas ocupadas en la hoja
			int cols = 0;
			// A string used to store the reading cell
			// Cadena que usamos para almacenar la lectura de la celda
			String cellValue;
			// For this example we'll loop through the rows getting the data we
			// want
			// Para este ejemplo vamos a recorrer las filas obteniendo los datos
			// que queremos
			for (int r = 1; r <= rows; r++) {
				Persona miPersona = new Persona();
				xssfRow = xssfSheet.getRow(r);
				if (xssfRow == null) {
					break;
				} else {
					// System.out.print("Row: " + r + " -> ");
					for (int c = 0; c <= xssfRow.getLastCellNum(); c++) {
						/*
						 * We have those cell types (tenemos estos tipos de celda): CELL_TYPE_BLANK,
						 * CELL_TYPE_NUMERIC, CELL_TYPE_BLANK, CELL_TYPE_FORMULA, CELL_TYPE_BOOLEAN,
						 * CELL_TYPE_ERROR
						 */
						// "] ");
						switch (c) {
						// Actualizamos DNI/NIE
						case 0:
							if (xssfRow.getCell(c) == null) {
								xssfRow.createCell(c).setCellValue(datos.get(r - 1).getIdentificacion());
							} else {
								xssfRow.getCell(c).setCellValue(datos.get(r - 1).getIdentificacion());

							}
							break;
						case 1:
							break;
						case 2:
							break;
						case 3:
							break;
						case 4:
							if (xssfRow.getCell(c) == null) {
								xssfRow.createCell(c).setCellValue(datos.get(r - 1).getEmail());
							} else {
								xssfRow.getCell(c).setCellValue(datos.get(r - 1).getEmail());
							}
							break;
						case 5:
							break;
						case 6:
							break;
						case 7:
							break;
						case 8:
							break;
						case 9:
							break;
						case 10:
							break;
						case 11:
							break;
						case 12:
							break;
						case 13:
							break;
						case 14:
							xssfRow.getCell(c).setCellValue(datos.get(r - 1).getCodigoCuenta());

							break;
						case 15:
							break;
						case 16:
							if (xssfRow.getCell(c) == null) {
								xssfRow.createCell(c).setCellValue(datos.get(r - 1).getIban());
							} else {
								xssfRow.getCell(c).setCellValue(datos.get(r - 1).getIban());

							}
							break;

						}

					}
					// System.out.println();
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists (No se encontr� el fichero): " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
		} finally {
			try {
				excelStream.close();

				FileOutputStream outputStream = new FileOutputStream(archivo);
				xssfWorkbook.write(outputStream);
				xssfWorkbook.close();
				outputStream.close();

			} catch (IOException ex) {
				System.out.println(
						"Error in file processing after close it (Error al procesar el fichero despu�s de cerrarlo): "
								+ ex);
			}
		}
	}
}
