# Enunciado
**Para cada una de las siguientes situaciones, realice en forma iterativa los siguientes pasos:**
1. indique el mal olor, <br/>
2. indique el refactoring que lo corrige, <br/> 
3. aplique el refactoring, mostrando el resultado final (código y/o diseño según corresponda). <br/>
> Si vuelve a encontrar un mal olor, retorne al paso (i).

public class Pago {
    private List<Producto> productos;
    private String tipo;
    private static final double ADICIONAL_TARJETA = 1000.0;
    private static final double DESCUENTO_EFECTIVO = 2000.0;

    public Pago(String tipo, List<Producto> productos) {
        this.productos = productos;
        this.tipo = tipo;
    }

    public double calcularMontoFinal() {
        double total = 0.0;
        if (this.tipo == "EFECTIVO") {
            for (Producto producto : this.productos) {
                total = total + producto.getPrecio() + (producto.getPrecio() * producto.getIVA());
            }
            if (total > 100000) {
                total = total - DESCUENTO_EFECTIVO;
            }
        }
        else if (this.tipo == "TARJETA") {
            for (Producto producto : this.productos) {
                total = total + producto.getPrecio() + (producto.getPrecio() * producto.getIVA());
            }
            total = total + ADICIONAL_TARJETA;
        }
        return total;
    }
}

public class Producto {
    private double precio;
    private double IVA;

    public Producto(double precio, double IVA) {
        this.precio = precio;
        this.IVA = IVA;
    }

    public double getPrecio() {
        return this.precio;
    }

    public double getIVA() {
        return this.IVA;
    }
}

### **Bad Smell:** Switch Statement 
### **Refactoring:** Replace conditional with Polymorphism
Reemplazo el if anidado con una clase abstracta para simplificar el codigo y el metodo

public class Pago {
    private List<Producto> productos;
    private TipoPago tipo;

    public Pago(TipoPago tipo, List<Producto> productos) {
        this.productos = productos;
        this.tipo = tipo;
    }

    public double calcularMontoFinal() {
        double total = 0.0;
        for (Producto producto : this.productos) {
            total = total + producto.calcularTotal(producto);
        }
        return total;
    }
}

public abstract class TipoPago {
    public double calcularTotal(Producto producto) {
        return producto.getPrecio() + (producto.getPrecio() * producto.getIVA());
    }
}

public class TipoEfectivo extends TipoPago{
    private static final double DESCUENTO_EFECTIVO = 2000.0;

    @Override
    public double calcularTotal(Producto){
        double total = super.calcularTotal(producto);
        return (total > 100000 ? total - DESCUENTO_EFECTIVO : total);
    }
}

public class TipoTarjeta extends TipoPago{
    private static final double ADICIONAL_TARJETA = 1000.0;

    @Override
    public double calcularTotal(Producto){
        return super.calcularTotal(producto) + ADICIONAL_TARJETA;
    }
}

public class Producto {
    private double precio;
    private double IVA;

    public Producto(double precio, double IVA) {
        this.precio = precio;
        this.IVA = IVA;
    }

    public double getPrecio() {
        return this.precio;
    }

    public double getIVA() {
        return this.IVA;
    }
}

### **Bad Smell 2:** ###  Reinventa la rueda
el for dentro del metodo calcularMontoFinal podria ser mas eficiente con las herramientas que nos brinda el lenguaje
### **Refactoring:** ###  Replace loop with Pipeline
Aplico streams en el for, ademas elimino la variable temporal total. 

public class Pago {
    private List<Producto> productos;
    private TipoPago tipo;

    public Pago(TipoPago tipo, List<Producto> productos) {
        this.productos = productos;
        this.tipo = tipo;
    }

    public double calcularMontoFinal() {
        return this.productos.stream()
        .mapToDouble(producto -> producto.calcularTotal(producto)).sum();
    }
}

public abstract class TipoPago {
    public double calcularTotal(Producto producto) {
        return producto.getPrecio() + (producto.getPrecio() * producto.getIVA());
    }
}

public class TipoEfectivo extends TipoPago{
    private static final double DESCUENTO_EFECTIVO = 2000.0;

    @Override
    public double calcularTotal(Producto){
        double total = super.calcularTotal(producto);
        return (total > 100000 ? total - DESCUENTO_EFECTIVO : total);
    }
}

public class TipoTarjeta extends TipoPago{
    private static final double ADICIONAL_TARJETA = 1000.0;

    @Override
    public double calcularTotal(Producto){
        return super.calcularTotal(producto) + ADICIONAL_TARJETA;
    }
}

public class Producto {
    private double precio;
    private double IVA;

    public Producto(double precio, double IVA) {
        this.precio = precio;
        this.IVA = IVA;
    }

    public double getPrecio() {
        return this.precio;
    }

    public double getIVA() {
        return this.IVA;
    }
    
}

### **Bad Smell 3:** ###  Feature Envy
el metodo calcularTotal tiene envidia de atributos al momento de sumar el iva, deberia ser una responsabilidad del producto y no del TipoPago
### **Refactoring:** ###  Extract Method y Move Method
Hago el calculo del precio con iva en otro metodo, y lo muevo a la clase que corresponde
public class Pago {
    private List<Producto> productos;
    private TipoPago tipo;

    public Pago(TipoPago tipo, List<Producto> productos) {
        this.productos = productos;
        this.tipo = tipo;
    }

    public double calcularMontoFinal() {
        return this.productos.stream()
        .mapToDouble(producto -> producto.calcularTotal(producto)).sum();
    }
}

public abstract class TipoPago {
    public double calcularTotal(Producto producto) {
        return producto.getPrecioConIVA();
    }
}

public class TipoEfectivo extends TipoPago{
    private static final double DESCUENTO_EFECTIVO = 2000.0;

    @Override
    public double calcularTotal(Producto){
        double total = super.calcularTotal(producto);
        return (total > 100000 ? total - DESCUENTO_EFECTIVO : total);
    }
}

public class TipoTarjeta extends TipoPago{
    private static final double ADICIONAL_TARJETA = 1000.0;

    @Override
    public double calcularTotal(Producto){
        return super.calcularTotal(producto) + ADICIONAL_TARJETA;
    }
}

public class Producto {
    private double precio;
    private double IVA;

    public Producto(double precio, double IVA) {
        this.precio = precio;
        this.IVA = IVA;
    }

    public double getPrecio() {
        return this.precio;
    }

    public double getIVA() {
        return this.IVA;
    }

     public double getPrecioConIVA() {
        return this.precio + this.precio * this.IVA;
    }
}