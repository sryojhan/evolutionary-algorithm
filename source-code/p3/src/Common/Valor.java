package Common;

import java.util.Random;

public class Valor {
	public enum Funcion {
		add, sub, mult
	}

	public enum Terminal {

		x, MenosDos, MenosUno, Cero, Uno, Dos
	}

	private boolean isFunction;
	Funcion funcion;
	Terminal terminal;

	public Valor() {

		Random rnd = new Random();

		if (rnd.nextBoolean()) {

			isFunction = true;

			var funciones = Funcion.values();
			funcion = funciones[rnd.nextInt(funciones.length)];

		} else {

			isFunction = false;

			var terminales = Terminal.values();
			terminal = terminales[rnd.nextInt(terminales.length)];
		}

	}

	public Valor(boolean bool) {

		Random rnd = new Random();
		if (bool) {

			isFunction = true;

			var funciones = Funcion.values();
			funcion = funciones[rnd.nextInt(funciones.length)];

		} else {

			isFunction = false;

			var terminales = Terminal.values();
			terminal = terminales[rnd.nextInt(terminales.length)];
		}

	}

	public Valor(Funcion f) {

		isFunction = true;
	}

	public Valor(Terminal t) {

		isFunction = false;
	}

	public Valor(Valor v) {

		isFunction = v.isFunction;

		funcion = v.funcion;
		terminal = v.terminal;
	}

	public boolean esFuncion() {
		return isFunction;
	}

	public Funcion GetFuncion() {
		return funcion;
	}

	public Terminal GetTerminal() {
		return terminal;
	}
	
}
