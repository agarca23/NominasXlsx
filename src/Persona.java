import java.util.Date;

public class Persona {
	
	private int filaExcel;
	private  String identificacion;
	private NIF dni;
	private NIE nie;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private String categoria;
	private String empresaNombre;
	private String empresaCif;
	private String fechaAltaEmpresa;/*Excel trabaja las fechas como enteros*/
	private String fechaAltaLaboral;
	private String fechaBajaLaboral;
	private double horasExtrasForzadas;
	private double horasExtrasVoluntarias;
	private boolean prorrateadasExtra;
	private String codigoCuenta;
	private String paisCuenta;
	private String iban;
	private Cuenta cuenta;

	private Nomina nomina;
	
	public Persona(NIF dni, String nombre, String apellido1,String apellido2){
		this.dni=dni;
		this.nombre=nombre;
		this.apellido1=apellido1;
		this.apellido2=apellido2;
	}
	
	public Persona(){

	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public int getFilaExcel() {
		return filaExcel;
	}

	public void setFilaExcel(int filaExcel) {
		this.filaExcel = filaExcel;
	}

	public void setIdentidicacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public NIF getDni() {
		return dni;
	}
	public String getNombre() {
		return nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public String getEmail() {
		return email;
	}
	public String getCategoria() {
		return categoria;
	}
	public String getEmpresaNombre() {
		return empresaNombre;
	}
	public String getEmpresaCif() {
		return empresaCif;
	}
	public String getFechaAltaEmpresa() {
		return fechaAltaEmpresa;
	}
	public String getFechaAltaLaboral() {
		return fechaAltaLaboral;
	}
	public double getHorasExtrasForzadas() {
		return horasExtrasForzadas;
	}
	public double getHorasExtrasVoluntarias() {
		return horasExtrasVoluntarias;
	}
	public boolean isProrrateadasExtra() {
		return prorrateadasExtra;
	}
	public String getCodigoCuenta() {
		return codigoCuenta;
	}
	public String getPaisCuenta() {
		return paisCuenta;
	}
	public String getIban() {
		return iban;
	}
	public String getFechaBajaLaboral() {
		return fechaBajaLaboral;
	}

	public String getIdentificacion(){
		
		if(dni==null&&nie!=null){
			return nie.toString();
		}else if(nie==null&&dni!=null){
			return dni.toString();
		}else{
			return "";
		}
		
		
	}

	public void setIdentificacion(String id){
		this.identificacion=id;
		if(id!=null&&!id.equals("")){
			if (Character.isDigit(id.charAt(0))) {
				setDni(procesarNIF(id));
			}else{
				setNIE(procesarNIE(id));
			}
		}
	}
	public void setDni(NIF dni) {
		this.dni = dni;
	}
	public void setNIE(NIE id){
		this.nie=id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public void setEmpresaNombre(String empresaNombre) {
		this.empresaNombre = empresaNombre;
	}
	public void setEmpresaCif(String empresaCif) {
		this.empresaCif = empresaCif;
	}
	public void setFechaAltaEmpresa(String fechaAltaEmpresa) {
		this.fechaAltaEmpresa = fechaAltaEmpresa;
	}
	public void setFechaAltaLaboral(String fechaAltaLaboral) {
		this.fechaAltaLaboral = fechaAltaLaboral;
	}
	public void setFechaBajaLaboral(String fechaBaja){
		this.fechaBajaLaboral=fechaBaja;
	}
	public void setHorasExtrasForzadas(double horasExtrasForzadas) {
		this.horasExtrasForzadas = horasExtrasForzadas;
	}
	public void setHorasExtrasVoluntarias(double horasExtrasVoluntarias) {
		this.horasExtrasVoluntarias = horasExtrasVoluntarias;
	}
	public void setProrrateadasExtra(boolean prorrateadasExtra) {
		this.prorrateadasExtra = prorrateadasExtra;
	}
	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	public void setPaisCuenta(String paisCuenta) {
		this.paisCuenta = paisCuenta;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

	
	public NIF procesarNIF(String entrada) {
		NIF nuevoNIF = null;
		int numero = 0;
		boolean error = false;
		if (entrada.length() == 9) {
			String letra = entrada.substring(8);
			letra = letra.toUpperCase();
			char letraChar = letra.charAt(0);
			try {
				numero = Integer.parseInt(entrada.substring(0, 8));
			} catch (NumberFormatException e) {
				System.err.println("Entrada erronea");
				error = true;
			}
			if (!error)
				nuevoNIF = new NIF(numero, letraChar);
		}
		return nuevoNIF;
	}
	
	public NIE procesarNIE(String entrada){
		char letraInicio = 0;
		int numeroequivalente = 0;/*El numero lleva convertida la letra inicial*/
		NIE nuevoNIE=null;
		char letraFin = 0;
		if(entrada.toUpperCase().charAt(0)=='X'){
			letraInicio='X';
			numeroequivalente=Integer.parseInt('0'+entrada.substring(1, 8));
			letraFin=entrada.charAt(8);
		}else if(entrada.toUpperCase().charAt(0)=='Y'){
			letraInicio='Y';
			numeroequivalente=Integer.parseInt('1'+entrada.substring(1, 8));
			letraFin=entrada.charAt(8);
		}else if(entrada.toUpperCase().charAt(0)=='Z'){
			letraInicio='Z';
			numeroequivalente=Integer.parseInt('1'+entrada.substring(1, 8));
			letraFin=entrada.charAt(8);
		}
		nuevoNIE=new NIE(letraInicio, numeroequivalente, letraFin);
		return nuevoNIE;
	}
	
	public String toString(){
		StringBuffer miString=new StringBuffer();
		if(dni!=null){
			miString.append(dni.toString()+"  ");
		}else if(nie!=null){
			miString.append(nie.toString()+"  ");

		}
		miString.append(apellido1+"  ");
		miString.append(apellido2+"  ");
		miString.append(nombre+"  ");
		miString.append(categoria);
		miString.append(empresaNombre);

		
		return miString.toString();
	}

	public NIE getNIE() {
		// TODO Auto-generated method stub
		return this.nie;
	}

}

