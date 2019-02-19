import java.math.BigInteger;

public class Cuenta {

	private static String numeroCuentaCompleto;
	private String codigoPais;
	private String iban;
	private boolean validar;
	private String banco;
	private String oficina;
	private String digitoControl;
	private String numeroCuenta;

	public Cuenta(String cuenta, String codigoPais) {
		this.codigoPais = codigoPais;
		this.numeroCuentaCompleto = cuenta;
		banco = cuenta.substring(0, 4);
		oficina = cuenta.substring(4, 8);
		digitoControl = cuenta.substring(8, 10);
		numeroCuenta = cuenta.substring(10, 20);
		validar = validarCuenta(banco, oficina, digitoControl, numeroCuenta);

	}

	public Cuenta(String banco, String oficina, String digitoControl, String numeroCuenta, String codigoPais) {
		this.codigoPais = codigoPais;
		this.banco = banco;
		this.digitoControl = digitoControl;
		this.numeroCuenta = numeroCuenta;
		this.codigoPais = codigoPais;
		this.numeroCuentaCompleto = banco + oficina + digitoControl + numeroCuenta;
		validar = validarCuenta(banco, oficina, digitoControl, numeroCuenta);
	}

	public void cambiarNumeroCompleto(String banco, String oficina, String digitoControl, String numeroCuenta) {
		numeroCuentaCompleto = banco + oficina + digitoControl + numeroCuenta;
	}

	public static boolean validarCuenta(String cuenta) {

		if (cuenta.length() != 20) {
			return false;
		} else {
			return true;
		}
	}

	public static int calcularDigitoControl(String cuenta) {
		int[] factores = { 1, 2, 4, 8, 5, 10, 9, 7, 3, 6 };
		int suma = 0;
		int resto;

		for (int i = 0; i < factores.length; i++) {
			suma += factores[i] * Integer.parseInt(cuenta.substring(i, i + 1));
		}
		resto = 11 - (suma % 11);
		if (resto == 10) {
			resto = 1;
		}
		if (resto == 11) {
			resto = 0;
		}
		return resto;
	}

	private static boolean validarCuenta(String banco, String oficina, String digitoControl, String numeroCuenta) {
		// System.out.println(calcularDigitoControl("00" + banco + oficina));
		// System.out.println(calcularDigitoControl(numeroCuenta));
		// System.out.println(digitoControl);

		return calcularDigitoControl("00" + banco + oficina) * 10 + calcularDigitoControl(numeroCuenta) == Integer
				.parseInt(digitoControl);
	}

	public String calcularIBAN(String banco, String oficina, String digitoControl, String numeroCuenta,
			String codigoPais) {
		String cuentaConIBAN = banco + oficina + digitoControl + numeroCuenta + valorLetraIBAN(codigoPais.charAt(0))
				+ valorLetraIBAN(codigoPais.charAt(1)) + "00";
		BigInteger ccc = new BigInteger(cuentaConIBAN);
		BigInteger nueveSiete = new BigInteger("97");
		ccc = ccc.mod(nueveSiete);
		int aux = ccc.intValue();
		aux = 98 - aux;
		return rellenarConCeros(Integer.toString(aux), 2);
	}

	public static String rellenarConCeros(String cuenta, int longitud) {
		String ceros = "";
		if (cuenta.length() < longitud) {
			for (int i = 0; i < (longitud - cuenta.length()); i++) {
				ceros += "0";
			}
			cuenta = ceros + cuenta;
		}
		return cuenta;
	}

	public static String valorLetraIBAN(char letra) {
		String valor = "";
		letra = Character.toUpperCase(letra);
		switch (letra) {
		case 'A':
			valor = "10";
			break;
		case 'B':
			valor = "11";
			break;
		case 'C':
			valor = "12";
			break;
		case 'D':
			valor = "13";
			break;
		case 'E':
			valor = "14";
			break;
		case 'F':
			valor = "15";
			break;
		case 'G':
			valor = "16";
			break;
		case 'H':
			valor = "17";
			break;
		case 'I':
			valor = "18";
			break;
		case 'J':
			valor = "19";
			break;
		case 'K':
			valor = "20";
			break;
		case 'L':
			valor = "21";
			break;
		case 'M':
			valor = "22";
			break;
		case 'N':
			valor = "23";
			break;
		case 'O':
			valor = "24";
			break;
		case 'P':
			valor = "25";
			break;
		case 'Q':
			valor = "26";
			break;
		case 'R':
			valor = "27";
			break;
		case 'S':
			valor = "28";
			break;
		case 'T':
			valor = "29";
			break;
		case 'U':
			valor = "30";
			break;
		case 'V':
			valor = "31";
			break;
		case 'W':
			valor = "32";
			break;
		case 'X':
			valor = "33";
			break;
		case 'Y':
			valor = "34";
			break;
		case 'Z':
			valor = "35";
			break;
		}
		return valor;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuentaC(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public boolean isValidar() {
		return validar;
	}

	public void setValidar(boolean validar) {
		this.validar = validar;
	}

	public String getNumeroCuentaCompleto() {
		return numeroCuentaCompleto;
	}

	public void setNumeroCuentaCompleto(String numeroCuentaCompleto) {
		this.numeroCuentaCompleto = numeroCuentaCompleto;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getDigitoControl() {
		return digitoControl;
	}

	public void setDigitoControl(String digitoControl) {
		this.digitoControl = digitoControl;
	}

}

