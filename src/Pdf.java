import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf {

	public Pdf(Persona trabajador, Date fecha, Nomina nomina) {
		// TODO Auto-generated constructor stub
		escribir_Pdf(trabajador, fecha, nomina);
	}

	public void escribir_Pdf(Persona trabajador, Date fecha, Nomina nomina) {
		String aux;
		File ficheroPDF = null;
		if (trabajador.getDni() != null) {
			ficheroPDF = new File("resources/" + trabajador.getDni() + "_" + trabajador.getNombre()
					+ trabajador.getApellido1() + trabajador.getApellido2() + "_" + (fecha.getMonth()+1) + "-"
					+ (fecha.getYear() + 1900) + ".pdf");
		} else {
			ficheroPDF = new File("resources/" + trabajador.getNIE() + "_" + trabajador.getNombre()
					+ trabajador.getApellido1() + trabajador.getApellido2() + "_" + fecha.getMonth() + "-"
					+ (fecha.getYear() + 1900) + ".pdf");
		}

		try {
			Document document = new Document();
			System.out.println(trabajador.getNombre());
			PdfWriter.getInstance(document, new FileOutputStream(ficheroPDF));

			document.open();

			document.add(new Paragraph("Empresa: " +	trabajador.getEmpresaNombre()));
			document.add(new Paragraph("CIF: " + trabajador.getEmpresaCif() + "\n\n"));
			document.add(new Paragraph("Destinatario: "));
			document.add(new Paragraph("		" + trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2()));
			if(trabajador.getDni()!=null) {
			document.add(new Paragraph("		" + "DNI : " + trabajador.getDni()));
			} else {
			document.add(new Paragraph("		" + "NIE : " + trabajador.getNIE()));
			}
			document.add(new Paragraph("		" + "Categoría : " + trabajador.getCategoria()));
			Date fechaaux = DateUtil.getJavaDate(Double.parseDouble(trabajador.getFechaAltaEmpresa()));
			document.add(new Paragraph("		" + "Antigüedad : " + fechaaux.getDay() + "/" + (fechaaux.getMonth()+1) + "/" + (fechaaux.getYear()+1900) + "\n\n"));
			document.add(new Paragraph("	" + "Periodo liquidacion : " + (fecha.getMonth()+1) + "/" + (fecha.getYear()+1900) + "\n\n"));
			document.add(new Paragraph("Salario base mes :                                  " + (double) (Math.round((nomina.getSalarioBase()/14) * 100d) / 100d)));
			document.add(new Paragraph("Complemento	 :                                       " +(double) (Math.round(nomina.getComplementosMes() * 100d) / 100d)));
			document.add(new Paragraph("Trienios     :                                              " + (double) (Math.round(nomina.getTrieniosMes() * 100d) / 100d)));
			document.add(new Paragraph("Prorrateo    :                                              " + (double) (Math.round(nomina.getProrrateoMes() * 100d) / 100d)));
			
			document.add(new Paragraph("Contingencias generales     "+ nomina.getCuotaObreraGeneralTrabajador()+ "% de : "+ (double) (Math.round(nomina.getBrutoMensual() * 100d) / 100d) + "          " + (double) (Math.round(nomina.getCosteCuotaObreraGeneralTrabajador() * 100d) / 100d)));
			document.add(new Paragraph("Desempleo                         " + nomina.getCuotaDesempleoTrabajador()+ " % de : " + (double) (Math.round(nomina.getBrutoMensual() * 100d) / 100d)+ "          " +(double) (Math.round(nomina.getCosteCuotaDesempleoTrabajador() * 100d) / 100d)));
			document.add(new Paragraph("Cuota formación                 " + nomina.getCuotaFormacionTrabajador()+ " % de : " + (double) (Math.round(nomina.getBrutoMensual() * 100d) / 100d)+ "          " +(double) (Math.round(nomina.getCosteCuotaFormacionTrabajador() * 100d) / 100d)));
			if (trabajador.isProrrateadasExtra()) {
			document.add(new Paragraph("IRPF                               " + nomina.getRetencion() + " % de : "	+ (double) (Math.round(nomina.getBrutoMensual() * 100d) / 100d)+ "          " +(double) (Math.round(nomina.getCosteRetencion() / 12 * 100d) / 100d) + "\n\n"));
			} else {
			document.add(new Paragraph("IRPF                               " + nomina.getRetencion() + " % de : "	+ (double) (Math.round(nomina.getBrutoMensual() * 100d) / 100d)+ "          " +(double) (Math.round(nomina.getCosteRetencion() / 14 * 100d) / 100d) + "\n\n"));
			}

			if (trabajador.isProrrateadasExtra()) {
				double fin1 = nomina.getCosteCuotaDesempleoTrabajador() + nomina.getCosteCuotaFormacionTrabajador()
						+ nomina.getCosteRetencion() / 12 + nomina.getCosteCuotaObreraGeneralTrabajador();
			document.add(new Paragraph("Total deducciones(desempleo, formacion, Irpf):                   "+ (double) (Math.round(fin1 * 100d) / 100d)));
			} else {
				double fin1 = nomina.getCosteCuotaDesempleoTrabajador() + nomina.getCosteCuotaFormacionTrabajador()
						+ nomina.getCosteRetencion() / 14 + nomina.getCosteCuotaObreraGeneralTrabajador();
			document.add(new Paragraph("Total deducciones(desempleo, formacion, Irpf):                    "+ (double) (Math.round(fin1 * 100d) / 100d)));
			}

			double fin2 = nomina.getBrutoMensual();
			document.add(new Paragraph("Total devengos(salario base, complementos y trienios):      " + (double) (Math.round(fin2 * 100d) / 100d) + "\n\n"));

			double total = (double) (Math.round(nomina.getLiquidoMensual() * 100d) / 100d);

			document.add(new Paragraph("Líquido a percibir :                                                           						" + total + "\n\n"));
			if ((fecha.getMonth()==5||fecha.getMonth()==11) && !nomina.isProrrateo()) {
				document.add(new Paragraph("La extra recibida este mes : " + (double) (Math.round(nomina.getExtra() * 100d) / 100d)+ "\n"));
			}
			document.add(new Paragraph(
					"A ingresar en cuenta :		" + trabajador.getIban() + "\n\n\n"));
			document.add(new Paragraph("COSTES EMPRESARIO" + "\n"));
			document.add(new Paragraph("Contingencias comunes empresario :               " + (double) (Math.round(nomina.getCosteContingenciasComunesEmpresario() * 100d) / 100d)));
			document.add(new Paragraph("Desempleo empresario :                                   " + (double) (Math.round(nomina.getCosteDesempleoEmpresario() * 100d) / 100d)));
			document.add(new Paragraph("Formación empresario :                                     " + (double) (Math.round(nomina.getCosteFormacionEpresario() * 100d) / 100d)));
			document.add(new Paragraph("Accidentes trabajo empresario :                        " + (double) (Math.round(nomina.getCosteAccidentesEmpresario() * 100d) / 100d)));
			document.add(new Paragraph("FOGASA empresario :                                        " + (double) (Math.round(nomina.getCosteFogasaEmpresario() * 100d) / 100d) + "\n\n"));
			double totEmp = nomina.getTotalEmpresario();

			document.add(new Paragraph("TOTAL empresario :                                         " + (Math.round(totEmp * 100d) / 100d)));
			document.add(new Paragraph("Coste total trabajador :                                     " +(double) (Math.round(nomina.getCosteTotalTrabajador() * 100d) / 100d)) );
			document.close();

		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
