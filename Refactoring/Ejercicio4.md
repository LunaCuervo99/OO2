# Ejercicio 4
![uml4](../img/4.png)
**1. Dado el siguiente código, aplique únicamente los siguientes refactoring:**
    >Replace Loop with Pipeline (líneas 16 a 19)
    >Replace Conditional with Polymorphism (líneas 21 a 27)
    >Extract method y move method (línea 28)
    >Extract method y replace temp with query (líneas 28 a 33)
**2. Realice el diagrama de clases del codigo refactorizado**
```java
public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private String formaPago;
    
    public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
        if (!"efectivo".equals(formaPago) && !"6 cuotas".equals(formaPago)
            && !"12 cuotas".equals(formaPago)) {
                throw new Error("Forma de pago incorrecta");
            }
        this.cliente = cliente;
        this.productos = productos;
        this.formaPago = formaPago;
    }

    public double getCostoTotal() {
        double costoProductos = 0;
        for (Producto producto : this.productos) {
            costoProductos += producto.getPrecio();
        }
 
        double extraFormaPago = 0;
        if ("efectivo".equals(this.formaPago)) {
            extraFormaPago = 0;
        } else if ("6 cuotas".equals(this.formaPago)) {
            extraFormaPago = costoProductos * 0.2;
        } else if ("12 cuotas".equals(this.formaPago)) {
            extraFormaPago = costoProductos * 0.5;
        }
        int añosDesdeFechaAlta = Period.between (this.cliente.getFechaAlta(), LocalDate.now().getYears());

        // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
        if (añosDesdeFechaAlta > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        }
        return costoProductos + extraFormaPago;
    }

    public class Cliente {
        private LocalDate fechaAlta;
        public LocalDate getFechaAlta() {
            return this.fechaAlta;
        }
    }

    public class Producto {
        private double precio;
        public double getPrecio() {
            return this.precio;
        }
    }
}   
```
###  **Refactoring:**  Replace Loop With Pipeline
```java
public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private String formaPago;
    
    public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
        if (!"efectivo".equals(formaPago) && !"6 cuotas".equals(formaPago)
            && !"12 cuotas".equals(formaPago)) {
                throw new Error("Forma de pago incorrecta");
            }
        this.cliente = cliente;
        this.productos = productos;
        this.formaPago = formaPago;
    }

    public double getCostoTotal() {
        double costoProductos = this.productos.stream().mapToDouble(p -> p.getPrecio().sum());
 
        double extraFormaPago = 0;
        if ("efectivo".equals(this.formaPago)) {
            extraFormaPago = 0;
        } else if ("6 cuotas".equals(this.formaPago)) {
            extraFormaPago = costoProductos * 0.2;
        } else if ("12 cuotas".equals(this.formaPago)) {
            extraFormaPago = costoProductos * 0.5;
        }
        int añosDesdeFechaAlta = Period.between (this.cliente.getFechaAlta(), LocalDate.now().getYears());

        // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
        if (añosDesdeFechaAlta > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        }
        return costoProductos + extraFormaPago;
    }

    public class Cliente {
        private LocalDate fechaAlta;
        public LocalDate getFechaAlta() {
            return this.fechaAlta;
        }
    }

    public class Producto {
        private double precio;
        public double getPrecio() {
            return this.precio;
        }
    }
}   
```
###  **Refactoring 2:**  Replace Loop With Polymorphism
```java
public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private FormaDePago formaPago;
    
    public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
        if (!"efectivo".equals(formaPago) && !"6 cuotas".equals(formaPago)
            && !"12 cuotas".equals(formaPago)) {
                throw new Error("Forma de pago incorrecta");
            }
        this.cliente = cliente;
        this.productos = productos;
        this.formaPago = formaPago;
    }

    public double getCostoTotal() {
        double costoProductos = this.productos.stream().mapToDouble(p -> p.getPrecio().sum()); 
        double extraFormaPago = this.formaPago.calcularCostoExtra(costoProductos);

        int añosDesdeFechaAlta = Period.between (this.cliente.getFechaAlta(), LocalDate.now().getYears());
        // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
        if (añosDesdeFechaAlta > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        }
        return costoProductos + extraFormaPago;
    }

    public class Cliente {
        private LocalDate fechaAlta;
        public LocalDate getFechaAlta() {
            return this.fechaAlta;
        }
    }

    public class Producto {
        private double precio;
        public double getPrecio() {
            return this.precio;
        }
    }

    public interface FormaDePago
    {
        public double calcularCosto(double costo);
    }

    public class Efectivo implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return 0;
        }
    }

    public class SeisCuotas implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return costo * 0.2;
        }
    }

    public class DoceCuotas implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return costo * 0.5;
        }
    }
}   
```
###  **Refactoring 3: Extract Method y Move Method** 
```java
public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private FormaDePago formaPago;
    
    public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
        if (!"efectivo".equals(formaPago) && !"6 cuotas".equals(formaPago)
            && !"12 cuotas".equals(formaPago)) {
                throw new Error("Forma de pago incorrecta");
            }
        this.cliente = cliente;
        this.productos = productos;
        this.formaPago = formaPago;
    }

    public double getCostoTotal() {
        double costoProductos = this.productos.stream().mapToDouble(p -> p.getPrecio().sum()); 
        double extraFormaPago = this.formaPago.calcularCostoExtra(costoProductos);

        int añosDesdeFechaAlta = this.cliente.getAñosDesdeFechaAlta();
        // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
        if (añosDesdeFechaAlta > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        }
        return costoProductos + extraFormaPago;
    }

    public class Cliente {
        private LocalDate fechaAlta;

        public LocalDate getFechaAlta() {
            return this.fechaAlta;
        }

        public int getAñosDesdeFechaAlta(){
            return Period.between(this.fechaAlta, LocalDate.now().getYears());         
        }
    }

    public class Producto {
        private double precio;
        public double getPrecio() {
            return this.precio;
        }
    }

    public interface FormaDePago
    {
        public double calcularCosto(double costo);
    }

    public class Efectivo implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return 0;
        }
    }

    public class SeisCuotas implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return costo * 0.2;
        }
    }

    public class DoceCuotas implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return costo * 0.5;
        }
    }
}   
```
###  **Refactoring: 4: Extract Method y Replace Temp With Query** 
```java
public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;
    private FormaDePago formaPago;
    
    public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
        if (!"efectivo".equals(formaPago) && !"6 cuotas".equals(formaPago)
            && !"12 cuotas".equals(formaPago)) {
                throw new Error("Forma de pago incorrecta");
            }
        this.cliente = cliente;
        this.productos = productos;
        this.formaPago = formaPago;
    }

    public double getCostoTotal() {
        double costoProductos = this.productos.stream().mapToDouble(p -> p.getPrecio().sum()); 
        double extraFormaPago = this.formaPago.calcularCostoExtra(costoProductos);

        return this.calcularCostoFinal(costoProductos,extraFormaPago);
    }

    public double calcularCostoFinal(double costoProductos, double extraFormaPago)
    {
        // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
        if (this.cliente.getAñosDesdeFechaAlta() > 5) {
            return (costoProductos + extraFormaPago) * 0.9;
        } 
        return costoProductos + extraFormaPago;
    }

    public class Cliente {
        private LocalDate fechaAlta;

        public LocalDate getFechaAlta() {
            return this.fechaAlta;
        }

        public int getAñosDesdeFechaAlta(){
            return Period.between(this.fechaAlta, LocalDate.now().getYears());         
        }
    }

    public class Producto {
        private double precio;
        public double getPrecio() {
            return this.precio;
        }
    }

    public interface FormaDePago
    {
        public double calcularCosto(double costo);
    }

    public class Efectivo implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return 0;
        }
    }

    public class SeisCuotas implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return costo * 0.2;
        }
    }

    public class DoceCuotas implements FormaDePago
    {
        public double calcularCosto(double costo)
        {
            return costo * 0.5;
        }
    }
}   