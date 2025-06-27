package Common;

import java.util.ArrayList;

public class Nodo {

	public static final int profundidadMaxima = 5;

	
	
	public Nodo(Nodo padre) {

		izquierdo = null;
		derecho = null;

		this.padre = padre;

		valor = new Valor();
	}
	
	public Nodo(Nodo padre, boolean funcion) {

		izquierdo = null;
		derecho = null;

		this.padre = padre;

		valor = new Valor(funcion);
	}
	
	public Nodo() {
		
	}
	
	
	public static Nodo Clone(Nodo original) {
		
		if(original == null)
			return null;
		
		Nodo n = new Nodo();
		
		n.valor = new Valor(original.valor);
		
		n.izquierdo = Clone(original.izquierdo);
		n.derecho = Clone(original.derecho);
		
		if(n.izquierdo != null) {
			n.izquierdo.padre = n;
		}
		
		if(n.derecho != null) {
			n.derecho.padre = n;
		}
		
		return n;
	}

	Nodo padre;

	Nodo izquierdo;
	Nodo derecho;

	Valor valor;

	
	public Valor GetValor() {
		return valor;
	}
	
	public void SetValor(Valor v) {
		valor = v;
	}
	
	public Nodo GetNodo(int n) {
		
		
		int i = 0;
		ArrayList<Nodo> nodos = new ArrayList<>();
		
		nodos.add(this);
		
		while(!nodos.isEmpty())
		{	
			Nodo nodo = nodos.get(0);
			
			if(i == n)
				return nodo;
			
			if(nodo.izquierdo != null)
				nodos.add(nodo.izquierdo);
			if(nodo.derecho != null)
				nodos.add(nodo.derecho);
			
			nodos.remove(0);
			i++;
		}
		
		
		return null;
	}
	

	public void ContruirHijos(int p) {

		if(p == 1) {
			
			izquierdo = new Nodo(this, false);
			derecho = new Nodo(this, false);
			return;
		}
		
		izquierdo = new Nodo(this);

		if (izquierdo.valor.esFuncion())
			izquierdo.ContruirHijos(p - 1);

		derecho = new Nodo(this);
		if (derecho.valor.esFuncion())
			derecho.ContruirHijos(p - 1);
	}

	public Nodo Izquierdo() {
		return izquierdo;
	}

	public Nodo Derecho() {
		return derecho;
	}
	
	
	public static int Profundidad(Nodo n) {
		
		if(n == null)
			return 0;
		
		return 1 + Math.max(Profundidad(n.izquierdo), Profundidad(n.derecho));
	}
	
	public static int NumeroNodos(Nodo n) {
		
		if(n == null)
			return 0;
		
		return 1 + NumeroNodos(n.izquierdo) + NumeroNodos(n.derecho);
	}
	

	public static Nodo ConstruirArbolAleatorio() {

		Nodo raiz = new Nodo(null, true);
		raiz.ContruirHijos(profundidadMaxima - 1);

		return raiz;
	}


}
