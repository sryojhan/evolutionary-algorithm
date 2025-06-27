package Common;

import java.util.Random;

public class Gen {


	Nodo raiz;

	public Gen() {

		raiz = Nodo.ConstruirArbolAleatorio();
	}

	public Gen(Gen otro) {

		raiz = Nodo.Clone(otro.raiz);
	}

	public void Set(int lotus, Nodo alelo) {

		if(lotus == 0)
		{
			raiz = Nodo.Clone(alelo);
			return;
		}
		
		Nodo n = raiz.GetNodo(lotus);
		
		if(n.padre.izquierdo == n) {
			
			n.padre.izquierdo = alelo;
		}else {
			n.padre.derecho = alelo;
		}
		
	}
	
	public void Set(Nodo l) {

		raiz = Nodo.Clone(l);
	}
	

	public Nodo Get(int lotus) {

		return raiz.GetNodo(lotus);
	}

	public int Size() {
		
		return Nodo.NumeroNodos(raiz);
	}
	
	public int Profundidad() {
		
		return Nodo.Profundidad(raiz);
	}

	public Gen Clone() {
		
		return new Gen(this);
	}

	public Nodo Fenotype() {
		
		return raiz;
	}

}
