
public class NIE {
	private char letraInicio;
	private int numero;
	private char letra;
	
	public NIE(char letraI, int numero, char letraF){
		this.letraInicio=letraI;
		this.numero=numero;
		this.letra=letraF;
	}
	
	public char generarLetra(){
		char letraCalculada = 0;
		int resto=this.numero%23;
		
		switch(resto){
		case 0: letraCalculada='T';
		break;
		case 1: letraCalculada='R';
		break;
		case 2: letraCalculada='W';
		break;
		case 3: letraCalculada='A';
		break;
		case 4: letraCalculada='G';
		break;
		case 5: letraCalculada='M';
		break;
		case 6: letraCalculada='Y';
		break;
		case 7: letraCalculada='F';
		break;
		case 8: letraCalculada='P';
		break;
		case 9: letraCalculada='D';
		break;
		case 10: letraCalculada='X';
		break;
		case 11: letraCalculada='B';
		break;
		case 12: letraCalculada='N';
		break;
		case 13: letraCalculada='J';
		break;
		case 14: letraCalculada='Z';
		break;
		case 15: letraCalculada='S';
		break;
		case 16: letraCalculada='Q';
		break;
		case 17: letraCalculada='V';
		break;
		case 18: letraCalculada='H';
		break;
		case 19: letraCalculada='L';
		break;
		case 20: letraCalculada='C';
		break;
		case 21: letraCalculada='K';
		break;
		case 22: letraCalculada='E';
		break;
		}
		return letraCalculada;
	}
	
	public boolean verificarNIE(){
		if(this.letra==generarLetra()){
			return true;
		}else{
			this.letra=generarLetra();
		}
		return false;
	}
	
	public String toString(){
		StringBuffer miDni=new StringBuffer();
		miDni.append(this.letraInicio);
		String conversionnumero=String.valueOf(this.numero);
		if(letraInicio=='X'){
			miDni.append(conversionnumero);
		}else{
			miDni.append(conversionnumero.substring(1));
		}
		miDni.append(this.letra);

		return miDni.toString();
	}
}
