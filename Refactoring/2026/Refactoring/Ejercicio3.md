# Ejercicio 3: Iteradores circulares bis
Se cuenta con las siguientes implementaciones de iteradores circulares, las cuales presentan implementaciones similares.
```java
CharRing.java
1 public class CharRing {
2    private char[] source;
3    private int idx;
4
5    public CharRing(String src) {
6        source = src.toCharArray();
7        idx = 0;
8    }
9
10    public char next() {
11        if (idx >= source.length)
12            idx = 0;
13        return source[idx++];
14    }
15 }

IntRing.java
1 public class IntRing {
2    private int[] source;
3    private int idx;
4
5    public IntRing(int[] src) {
6        source = src;
7       idx = 0;
8    }
9
10    public int next() {
11        if (idx >= source.length)
12            idx = 0;
13        return source[idx++];
14    }
15 }
```

## Tareas:
1) Diseñe e implemente Test de Unidad para las clases CharRing e IntRing. Asegúrese de que los test pasen.
2) Aplique el refactoring Extract Superclass. Detalle cada uno de los pasos intermedios que son necesarios para poder aplicar correctamente este refactoring.
3) Verifique que los tests definidos en el paso 1 sigan funcionando correctamente.
4) Realice un diagrama de clases UML con el diseño refactorizado.

### Solución

1) Para los tests de unidad voy a verificar 3 cosas: que devuelvan el primer elemento, que avanzan correctamente, que vuelven a empezar ya que es comportamiento circular

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IteradoresCircularesTest {
    @Test
    public void testCharRingNext() {
        CharRing charRing = new CharRing("abc");
        
        assertEquals('a', charRing.next()); // Índice 0
        assertEquals('b', charRing.next()); // Índice 1
        assertEquals('c', charRing.next()); // Índice 2
        assertEquals('a', charRing.next()); // Tendría que volver a empezar y no pasarse
    }

    @Test
    public void testIntRingNext() {
        int[] numeros = {10, 20};
        IntRing intRing = new IntRing(numeros);
        
        assertEquals(10, intRing.next()); // Índice 0
        assertEquals(20, intRing.next()); // Índice 1
        assertEquals(10, intRing.next()); // Volver a empezar
    }
}
```

2) Aplicar Extract Superclass con los pasos individuales.
Las clases `CharRing` e `IntRing` presentan **código duplicado** en la linea 3, al definir su variable idx, y en las lineas 10 a 14 para el método `next()`.
Al ser tipos distintos char e int, no se puede extraer el metodo next() completo, pero si la logica.

#### Pasos intermedios 
1. **Crear una superclase abstracta en blanco y hacer que las clases originales sean subclases de esta superclase**
```java
Ring.java
public abstract class Ring {
}

CharRing.java
public class CharRing extends Ring { ... }

IntRing.java
public class IntRing extends Ring { ... }
```
2. **Usar Pull Up Field, Pull Up Method y Pull Up Constructor Body para mover los elementos comunes a la clase**
```java
Ring.java
public abstract class Ring {
  private int idx;

  public Ring(){
    this.idx = 0;
  }
}

CharRing.java
public class CharRing extends Ring {
   private char[] source;

   public CharRing(String src) {
        super();
        source = src.toCharArray();
   }

   public char next() {
      if (idx >= source.length) 
          idx = 0;
      return source[idx++];
   }
}

IntRing.java
public class IntRing extends Ring {
    private int[] source;

    public IntRing(int[] src) {
        super();
        source = src;
    }

    public int next() {
       if (idx >= source.length)
           idx = 0;
       return source[idx++];
   }
}
```
2. ** Examina los métodos que quedaron en las subclases. Fíjate si hay partes comunes, si las hay podés usar Extract Method seguido de Pull Up Method en las partes comunes**
`*Extract Method*` de la parte común de ambos métodos:
private int calcularIndice(int longitud) {
    if (idx >= longitud)
        idx = 0;
    return idx++;
}
`*Pull Up Method*` a la superclase y Código resultante
```java
Ring.java
public abstract class Ring {
  private int idx;

  public Ring(){
    this.idx = 0;
  }

  private int calcularIndice(int longitud) {
    if (idx >= longitud)
        idx = 0;
    return idx++;
  }
}

CharRing.java
public class CharRing extends Ring {
   private char[] source;

   public CharRing(String src) {
        source = src.toCharArray();
   }

   public char next() {
      return source[this.calcularIndice(source.length)];
   }
}

IntRing.java
public class IntRing extends Ring {
    private int[] source;

    public IntRing(int[] src) {
        source = src;
    }

    public int next() {
       return source[this.calcularIndice(source.length)];
   }
}
```

4) Diagrama UML
![Diagrama UML ejercicio 2.4](/uml2026_ej2.4.png)
