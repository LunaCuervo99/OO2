## Ejercicio 7: Etiquetas
### Enunciado
Observe el siguiente código:
```java
abstract class Etiqueta {
    protected String nombreProducto;
    protected double precio;

    public Etiqueta(String nombre, double precio) {
        this.nombreProducto = nombre;
        this.precio = precio;
    }
}

class EtiquetaSimple extends Etiqueta {
    public EtiquetaSimple(String nombre, double precio) {
        super(nombre, precio);
    }

    public void generar() {
        System.out.println("--- ETIQUETA BÁSICA ---");
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Precio: $" + precio);
        System.out.println("-----------------------");
    }
}

class EtiquetaDetalle extends Etiqueta {
    public EtiquetaDetalle(String nombre, double precio) {
        super(nombre, precio);
    }

    public void generar() {
        System.out.println("--- ETIQUETA DETALLE ---");
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Precio sin imp.: $" + (precio * 0.79));
        System.out.println("Precio final: $" + precio);
        System.out.println("-----------------------");
    }
}
```
1) ¿Hay código duplicado? Indique claramente en qué líneas se encuentra.
2) Se quiere aplicar el refactoring Pull Up Method para subir el método generar() a la superclase Etiqueta. 
  ¿Es posible hacerlo en el código anterior? Justifique su respuesta basándose en las precondiciones del refactoring vistas en la teoría y en el libro de Refactoring de Martin Fowler.
3) Mencione los refactorings previos necesarios para que sea posible aplicar Pull Up Method.
4) Aplique Pull Up Method para subir el método generar() a la superclase Etiqueta.

### Solución:
1- Hay código duplicado en el método generar() en las clases `Etiqueta` y `EtiquetaDetalle`, ambos métodos comparten la misma lógica de imprimir cabecera, imprimir producto, imprimir precio, imprimir pie y repiten textualmente las siguientes líneas:
Impresión del producto: Línea 18 (EtiquetaSimple) y Línea 30 (EtiquetaDetalle). Impresión del pie de etiqueta: Línea 20 (EtiquetaSimple) y Línea 33 (EtiquetaDetalle).

2- No, no es posible aplicar Pull Up Method directamente. Según Fowler, la precondición para aplicar Pull Up Method es que los cuerpos de los métodos en las subclases deben ser idénticos. En el código, aunque el propósito general es el mismo (generar una etiqueta), los métodos hacen cosas distintas: imprimen diferentes encabezados y formatean los precios de forma diferente. 

3- Refactorings previos necesarios
Para lograr que los métodos generar() sean idénticos, aplicaría los siguientes refactorings:
   * **Extract Method**: En cada subclase, extraigo las líneas de código que varían en nuevos métodos (extraer la línea del encabezado a un método imprimirEncabezado() y las líneas del precio a un método imprimirPrecio()).
   * Declarar **métodos abstractos**: Como dice el libro de Fowler: si el método a subir "llama a otro método que está presente en ambas subclases pero no en la superclase, declará un método abstracto en la superclase". 
    Por lo tanto, declarO las firmas abstractas de imprimirEncabezado() e imprimirPrecio() en la clase padre

```java
abstract class Etiqueta {
    protected String nombreProducto;
    protected double precio;
    
    public Etiqueta(String nombre, double precio) {
        this.nombreProducto = nombre;
        this.precio = precio;
    }

    protected abstract void imprimirEncabezado();
    protected abstract void imprimirPrecio();
}

class EtiquetaSimple extends Etiqueta {
    public EtiquetaSimple(String nombre, double precio) {
        super(nombre, precio);
    }

    protected void imprimirEncabezado() {
        System.out.println("--- ETIQUETA BÁSICA ---");
    }

    protected void imprimirPrecio() {
        System.out.println("Precio: $" + precio);
    }

    public void generar() {
        this.imprimirEncabezado();
        System.out.println("Producto: " + nombreProducto);
        this.imprimirPrecio();
        System.out.println("-----------------------");
    }
}

class EtiquetaDetalle extends Etiqueta {
    public EtiquetaDetalle(String nombre, double precio) {
        super(nombre, precio);
    }
    
    public void generar() {
        this.imprimirEncabezado();
        System.out.println("Producto: " + nombreProducto);
        this.imprimirPrecio();
        System.out.println("-----------------------");
    }

    protected void imprimirEncabezado() {
        System.out.println("--- ETIQUETA DETALLE ---");
    }

    protected void imprimirPrecio() {
        System.out.println("Precio sin imp.: $" + (precio * 0.79));
        System.out.println("Precio final: $" + precio);
    }
}
```
4- Aplicación de Pull Up Method y resultado final: al tener los cuerpos idénticos, finalmente aplicamos Pull Up Method  para subir generar() a la superclase.
```java
abstract class Etiqueta {
    protected String nombreProducto;
    protected double precio;
    
    public Etiqueta(String nombre, double precio) {
        this.nombreProducto = nombre;
        this.precio = precio;
    }

    public void generar() {
        this.imprimirEncabezado();
        System.out.println("Producto: " + nombreProducto);
        this.imprimirPrecio();
        System.out.println("-----------------------");
    }

    protected abstract void imprimirEncabezado();
    protected abstract void imprimirPrecio();
}

class EtiquetaSimple extends Etiqueta {
    public EtiquetaSimple(String nombre, double precio) {
        super(nombre, precio);
    }

    protected void imprimirEncabezado() {
        System.out.println("--- ETIQUETA BÁSICA ---");
    }

    protected void imprimirPrecio() {
        System.out.println("Precio: $" + precio);
    }
}

class EtiquetaDetalle extends Etiqueta {
    public EtiquetaDetalle(String nombre, double precio) {
        super(nombre, precio);
    }

    protected void imprimirEncabezado() {
        System.out.println("--- ETIQUETA DETALLE ---");
    }

    protected void imprimirPrecio() {
        System.out.println("Precio sin imp.: $" + (precio * 0.79));
        System.out.println("Precio final: $" + precio);
    }
}
```
