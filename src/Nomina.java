import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;

public class Nomina {

	private double salarioBase;
	private double complementos;
	private double complementosMes;
	private int codigoCotizacion;
	private int numeroTrienios;
	private int importeBrutoTrienios;
	private double trieniosMes;
	
	private double prorrateoMes;

	private double brutoAnual;
	private double brutoMensual;
	private double brutoAnualTrieniosExtra;

	private boolean prorrateo;

	private double salarioNeto;

	private double extra;
	
	private double cuotaObreraGeneralTrabajador;
	private double costeCuotaObreraGeneralTrabajador;
	private double cuotaDesempleoTrabajador;
	private double costeCuotaDesempleoTrabajador;
	private double cuotaFormacionTrabajador;
	private double costeCuotaFormacionTrabajador;

	private double retencion;
	private double costeRetencion;

	private double liquidoMensual;

	private double contingenciasComunesEmpresario;
	private double fogasaEmpresario;
	private double desempleoEmpresario;
	private double formacionEpresario;
	private double accidentesEmpresario;

	private double costeContingenciasComunesEmpresario;
	private double costeFogasaEmpresario;
	private double costeDesempleoEmpresario;
	private double costeFormacionEpresario;
	private double costeAccidentesEmpresario;

	private double totalEmpresario;
	private double costeTotalTrabajador;
	private Date fechanomina;

	public Nomina(Persona persona, DatosNominas datos, Date fecha) {
		this.fechanomina = fecha;
		Date fechaaux = null;
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(persona.getNombre());
		System.out.println(persona.getCategoria());
		if (persona.getFechaBajaLaboral() != ""/* || persona.getFechaBajaLaboral()!="BLANK" */) {
			System.out.println(persona.getNombre());
			fechaaux = DateUtil.getJavaDate(Double.parseDouble(persona.getFechaBajaLaboral()));
		} else {
			fechaaux = fecha;
		}

		if (fechaaux.getTime() >= fecha.getTime()) {
			// Salario base que obtiene el trabajador por su trabajo
			this.salarioBase = getSalarioBase(persona, datos);
			System.out.println("Salario base: " + salarioBase);
			// Complementos extra que recibe el trabajador
			this.complementos = getComplementos(persona, datos);
			System.out.println("Complementos: " + complementos);
			// Codigo de cotizacion correspondiente al trabajador
			this.codigoCotizacion = getCodigoCotizacion(persona, datos);
			System.out.println(codigoCotizacion);
			// Calculo del numero de trienios que lleva el trabajador en la empresa
			this.numeroTrienios = getNumeroTrienios(fecha,
					DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa())));
			System.out.println("Numero trienios: " + numeroTrienios);
			// Extra que recibe el trabajador por los trienios trabajados
			this.importeBrutoTrienios = getImporteBrutoTrienios(datos, numeroTrienios);
			System.out.println("Importe bruto trienios: " + importeBrutoTrienios);

			// Suma de los correspondientes trienios del año para calcular las 2 nominas
			// restantes
			int sumaAnualDeTrienios = 0;
			int auxTrienios = 0;
			for (int i = 0; i < 12; i++) {
				Date fechaAux = new Date(fecha.getYear(), i, fecha.getDay());
				auxTrienios = getNumeroTrienios(fechaAux,
						DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa())));
				System.out.print(auxTrienios);
				if (i == 5 | i == 11) {
					sumaAnualDeTrienios += 2 * getImporteBrutoTrienios(datos, auxTrienios);

				} else {
					sumaAnualDeTrienios += getImporteBrutoTrienios(datos, auxTrienios);
				} // System.out.println(sumaAnualDeTrienios);
			}
			System.out.println();
			System.out.println("Suma total de todos los trienios del año: " + sumaAnualDeTrienios);

			// Bruto anual = salarioBase + complementos + sumaAnualDeTrienios
			this.prorrateo = persona.isProrrateadasExtra();

			if (!prorrateo) {
				Date junio = new Date(fecha.getYear(), 5, fecha.getDay());
				int trienioJunio = getNumeroTrienios(junio,
						DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa())));
				double extraJunio = getImporteBrutoTrienios(datos, trienioJunio);
				Date diciembre = new Date(fecha.getYear(), 11, fecha.getDay());
				int trienioDiciembre = getNumeroTrienios(diciembre,
						DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa())));
				double extraDiciembre = getImporteBrutoTrienios(datos, trienioDiciembre);

				this.brutoAnualTrieniosExtra = salarioBase + complementos + sumaAnualDeTrienios + extraJunio
						+ extraDiciembre;
				this.brutoAnual = brutoAnualTrieniosExtra;
			} else {
				// El trabajador recibira ademas cada mes un sexto del mes
				Date aux = new Date(fecha.getYear(), fecha.getMonth(), fecha.getDay());
				int trienioMes = getNumeroTrienios(aux,
						DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa())));
				double auss = (double) trienioMes;
				this.brutoAnual = salarioBase + complementos + sumaAnualDeTrienios + auss / 6;
			}

			this.trieniosMes = getImporteBrutoTrienios(datos,getNumeroTrienios(fecha,
					DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa()))));
			System.out.println("Bruto anual: " + brutoAnual);

			// Calculamos el salario de un mes de trabajo en ambos casos
			if (persona.isProrrateadasExtra()) {
				double aux = brutoAnual / 14;
				System.out.println(aux);
				double amayores = aux / 6;
				this.prorrateoMes  = amayores;
				System.out.println(amayores);
				this.brutoMensual = aux + amayores;

				System.out.println(brutoMensual + "----------");
			} else {
				this.brutoMensual = brutoAnual / 14;
			}
			System.out.println("prorrateo: " + prorrateo);
			this.complementosMes = complementos / 14;
			// Impuestos que recibe el trabajador
			this.cuotaObreraGeneralTrabajador = datos.getCuotaObreraGeneralTrabajador();
			costeCuotaObreraGeneralTrabajador = brutoAnual * (cuotaObreraGeneralTrabajador / 100) / 12;
			System.out.println("Coste cuota obrera general trabajador: " + costeCuotaObreraGeneralTrabajador);

			this.cuotaDesempleoTrabajador = datos.getCuotaDesempleoTrabajador();
			costeCuotaDesempleoTrabajador = brutoAnual * (cuotaDesempleoTrabajador / 100) / 12;
			System.out.println("Coste cuota desempleo trabajador: " + costeCuotaDesempleoTrabajador);

			this.cuotaFormacionTrabajador = datos.getCuotaFormacionTrabajador();
			costeCuotaFormacionTrabajador = brutoAnual * (cuotaFormacionTrabajador / 100) / 12;
			System.out.println("Coste cuota formacion trabajador: " + costeCuotaFormacionTrabajador);

			for (int i = 0; i < datos.getRetencion().length; i++) {
				if (brutoAnual > datos.getBrutoAnual()[i] && brutoAnual <= datos.getBrutoAnual()[i + 1]) {
					this.retencion = datos.getRetencion()[i + 1];
					this.costeRetencion = brutoAnual * (retencion / 100);
				}
			}
			System.out.println("Retencion porcentaje : " + retencion);
			System.out.println("Coste retencion al año: " + costeRetencion);
			if (isProrrateo()) {
				this.liquidoMensual = brutoMensual - costeCuotaObreraGeneralTrabajador - costeCuotaDesempleoTrabajador
						- costeCuotaFormacionTrabajador - costeRetencion / 12;
			} else {
				this.liquidoMensual = brutoMensual - costeCuotaObreraGeneralTrabajador - costeCuotaDesempleoTrabajador
						- costeCuotaFormacionTrabajador - costeRetencion / 14;
			}
			System.out.println("Liquido mensual: " + liquidoMensual);

			System.out.println("Mes " + fecha.getMonth());
			if ((fecha.getMonth() == 5 || fecha.getMonth() == 11) && !prorrateo) {
				if (fecha.getYear() == (DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa()))
						.getYear())) {
					double numeroMeses = fecha.getMonth()
							- (DateUtil.getJavaDate(Double.parseDouble(persona.getFechaAltaEmpresa())).getMonth()) - 1;
					this.extra = (salarioBase / 14 + complementos / 14) * (numeroMeses / 6) * (1 - retencion / 100);
				} else {

					this.extra = (salarioBase / 14 + complementos / 14 + importeBrutoTrienios) * (1 - retencion / 100);

				}
			}
			System.out.println("La extra que recibe el trabajador seria: " + extra);

			this.contingenciasComunesEmpresario = datos.getContingenciasComunesEmpresario();
			System.out.println("Porcentaje contiengencias comunes: " + contingenciasComunesEmpresario);
			this.fogasaEmpresario = datos.getFogasaEmpresario();
			System.out.println("Porcentaje fogasa: " + fogasaEmpresario);
			this.desempleoEmpresario = datos.getDesempleoEmpresario();
			System.out.println("Porcentaje desempleo: " + desempleoEmpresario);
			this.accidentesEmpresario = datos.getAccidentesEmpresario();
			System.out.println("Porcentaje accidentes: " + accidentesEmpresario);
			this.formacionEpresario = datos.getFormacionEpresario();
			System.out.println("Porcentaje formacion: " + formacionEpresario);

			this.costeContingenciasComunesEmpresario = brutoAnual * (contingenciasComunesEmpresario / 100) / 12;
			this.costeFogasaEmpresario = brutoAnual * (fogasaEmpresario / 100) / 12;
			this.costeDesempleoEmpresario = brutoAnual * (desempleoEmpresario / 100) / 12;
			this.costeAccidentesEmpresario = brutoAnual * (accidentesEmpresario / 100) / 12;
			this.costeFormacionEpresario = brutoAnual * (formacionEpresario / 100) / 12;
			System.out.println("Coste contingencias: " + costeContingenciasComunesEmpresario);
			System.out.println("Fogasa: " + costeFogasaEmpresario);
			System.out.println("Desempleo : " + costeDesempleoEmpresario);
			System.out.println("Accidentes : " + costeAccidentesEmpresario);
			System.out.println("Formacion: " + costeFormacionEpresario);

			this.totalEmpresario = costeContingenciasComunesEmpresario + costeFogasaEmpresario
					+ costeDesempleoEmpresario + costeAccidentesEmpresario + costeFormacionEpresario;
			System.out.println("Coste impuestos empresario: " + totalEmpresario);
			this.costeTotalTrabajador = brutoMensual + totalEmpresario;
			System.out.println("Coste total del trabajador: " + costeTotalTrabajador);
		}
	}

	public int getImporteBrutoTrienios(DatosNominas datos, int numeroTrienios) {

		for (int i = 0; i < datos.getImporteBruto().length; i++) {
			if (numeroTrienios == datos.getNumeroTrienios()[i]) {
				return datos.getImporteBruto()[i];
			}
		}
		return 0;
	}

	public double getSalarioBase(Persona persona, DatosNominas datos) {

		double salarioBase = 0;
		for (int i = 0; i < datos.getCategoria().length; i++) {
			if (persona.getCategoria().equals(datos.getCategoria()[i])) {
				salarioBase = datos.getSalarioBase()[i];
				return salarioBase;
			}
		}
		return salarioBase;
	}

	public double getComplementos(Persona persona, DatosNominas datos) {
		double complementos = 0;
		for (int i = 0; i < datos.getComplementos().length; i++) {
			if (persona.getCategoria().equals(datos.getCategoria()[i])) {
				complementos = datos.getComplementos()[i];
				break;
			}
		}
		return complementos;
	}

	public int getCodigoCotizacion(Persona persona, DatosNominas datos) {
		int codigoCotizacion = 0;
		for (int i = 0; i < datos.getCodigoCotizacion().length; i++) {
			if (persona.getCategoria().equals(datos.getCategoria()[i])) {
				codigoCotizacion = datos.getCodigoCotizacion()[i];
				break;
			}
		}
		return codigoCotizacion;
	}

	// public double getRetencion(int salario)

	public int getNumeroTrienios(Date fecha, Date alta) {
		if (alta != null) {
			int years = 0;
			int months = 0;
			if (fecha.getTime() < alta.getTime()) {
				return 0;
			} else {
				months = (fecha.getYear() - alta.getYear()) * 12 + (fecha.getMonth() - alta.getMonth());
				years = ((fecha.getYear() - alta.getYear()) * 12 + (fecha.getMonth() - alta.getMonth())) / 36;
				if (months % 36 == 0) {
					if (years == 0) {
						return 0;
					}
					return years - 1;
				} else {
					return years;
				}
			}
		} else {
			return 0;
		}
	}

	public static Date ParseFecha(String fecha) {
		System.out.println(fecha);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fecha);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return fechaDate;
	}

	public final Date fechaDeStringADate(String strFecha) throws ParseException {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");

		Date fecha = null;
		try {
			fecha = formatoDelTexto.parse(strFecha);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fecha;

	}

	public double getSalarioBase() {
		return salarioBase;
	}

	public double getComplementos() {
		return complementos;
	}

	public int getCodigoCotizacion() {
		return codigoCotizacion;
	}

	public int getNumeroTrienios() {
		return numeroTrienios;
	}

	public int getImporteBrutoTrienios() {
		return importeBrutoTrienios;
	}

	public double getBrutoAnual() {
		return brutoAnual;
	}

	public double getBrutoMensual() {
		return brutoMensual;
	}

	public double getBrutoAnualTrieniosExtra() {
		return brutoAnualTrieniosExtra;
	}

	public boolean isProrrateo() {
		return prorrateo;
	}

	public double getSalarioNeto() {
		return salarioNeto;
	}

	public double getCuotaObreraGeneralTrabajador() {
		return cuotaObreraGeneralTrabajador;
	}

	public double getCosteCuotaObreraGeneralTrabajador() {
		return costeCuotaObreraGeneralTrabajador;
	}

	public double getCuotaDesempleoTrabajador() {
		return cuotaDesempleoTrabajador;
	}

	public double getCosteCuotaDesempleoTrabajador() {
		return costeCuotaDesempleoTrabajador;
	}

	public double getCuotaFormacionTrabajador() {
		return cuotaFormacionTrabajador;
	}

	public double getCosteCuotaFormacionTrabajador() {
		return costeCuotaFormacionTrabajador;
	}

	public double getRetencion() {
		return retencion;
	}

	public double getCosteRetencion() {
		return costeRetencion;
	}

	public double getLiquidoMensual() {
		return liquidoMensual;
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

	public double getCosteContingenciasComunesEmpresario() {
		return costeContingenciasComunesEmpresario;
	}

	public double getCosteFogasaEmpresario() {
		return costeFogasaEmpresario;
	}

	public double getCosteDesempleoEmpresario() {
		return costeDesempleoEmpresario;
	}

	public double getCosteFormacionEpresario() {
		return costeFormacionEpresario;
	}

	public double getCosteAccidentesEmpresario() {
		return costeAccidentesEmpresario;
	}

	public double getTotalEmpresario() {
		return totalEmpresario;
	}

	public double getCosteTotalTrabajador() {
		return costeTotalTrabajador;
	}

	public Date getFechanomina() {
		return fechanomina;
	}

	public double getComplementosMes() {
		return complementosMes;
	}

	public void setComplementosMes(double complementosMes) {
		this.complementosMes = complementosMes;
	}

	public double getTrieniosMes() {
		return trieniosMes;
	}

	public void setTrieniosMes(double trieniosMes) {
		this.trieniosMes = trieniosMes;
	}

	public double getProrrateoMes() {
		return prorrateoMes;
	}

	public void setProrrateoMes(double prorrateoMes) {
		this.prorrateoMes = prorrateoMes;
	}

	public double getExtra() {
		return extra;
	}

	public void setExtra(double extra) {
		this.extra = extra;
	}
	
	
}

