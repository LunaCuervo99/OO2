# Ejercicio 9: 
Se tiene el siguiente modelo de un sistema de pedidos y la correspondiente implementación.
![Diagrama UML Ejercicio 9](../../uml9.png)
```java
01: public class Pedido {
02:  private Cliente cliente;
03:  private List<Producto> productos;
04:  private String formaPago;
05:  public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
06:     if (!"efectivo".equals(formaPago)
07:        && !"6 cuotas".equals(formaPago)
08:        && !"12 cuotas".equals(formaPago)) {
09:          throw new Error("Forma de pago incorrecta");
10:    }
11:    this.cliente = cliente;
12:    this.productos = productos;
13:    this.formaPago = formaPago;
14:   }
15:   public double getCostoTotal() {
16:     double costoProductos = 0;
17:     for (Producto producto : this.productos) {
18:       costoProductos += producto.getPrecio();
19:     }
20:     double extraFormaPago = 0;
21:     if ("efectivo".equals(this.formaPago)) {
22:       extraFormaPago = 0;
23:     } else if ("6 cuotas".equals(this.formaPago)) {
24:       extraFormaPago = costoProductos * 0.2;
25:     } else if ("12 cuotas".equals(this.formaPago)) {
26:       extraFormaPago = costoProductos * 0.5;
27:     }
28:     int añosDesdeFechaAlta = Period.between(this.cliente.getFechaAlta(), LocalDate.now()).getYears();
29:     // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
30:     if (añosDesdeFechaAlta > 5) {
31:       return (costoProductos + extraFormaPago) * 0.9;
32:     }
33:     return costoProductos + extraFormaPago;
34:   }
35: }
36: public class Cliente {
37:   private LocalDate fechaAlta;
38:   public LocalDate getFechaAlta() {
39:     return this.fechaAlta;
40:   }
41: }
42: public class Producto {
43:   private double precio;
44:   public double getPrecio() {
45:     return this.precio;
46:   }
47: }
```
Tareas:
Dado el código anterior, aplique únicamente los siguientes refactoring:
1) Replace Loop with Pipeline (líneas 16 a 19)
2) Replace Conditional with Polymorphism (líneas 21 a 27)
3) Extract method y move method (línea 28)
4) Extract method y replace temp with query (líneas 28 a 33)
5) Realice el diagrama de clases del código refactorizado.

### 1. Replace Loop with Pipeline (líneas 16 a 19)
```java
01: public class Pedido {
02:  private Cliente cliente;
03:  private List<Producto> productos;
04:  private String formaPago;
05:  public Pedido(Cliente cliente, List<Producto> productos, String formaPago) {
06:     if (!"efectivo".equals(formaPago)
07:        && !"6 cuotas".equals(formaPago)
08:        && !"12 cuotas".equals(formaPago)) {
09:          throw new Error("Forma de pago incorrecta");
10:    }
11:    this.cliente = cliente;
12:    this.productos = productos;
13:    this.formaPago = formaPago;
14:   }
15:   public double getCostoTotal() {
16:     double costoProductos = productos.stream().mapToInt(prod->prod.getPrecio()).sum(); 
20:     double extraFormaPago = 0;
21:     if ("efectivo".equals(this.formaPago)) {
22:       extraFormaPago = 0;
23:     } else if ("6 cuotas".equals(this.formaPago)) {
24:       extraFormaPago = costoProductos * 0.2;
25:     } else if ("12 cuotas".equals(this.formaPago)) {
26:       extraFormaPago = costoProductos * 0.5;
27:     }
28:     int añosDesdeFechaAlta = Period.between(this.cliente.getFechaAlta(), LocalDate.now()).getYears();
29:     // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
30:     if (añosDesdeFechaAlta > 5) {
31:       return (costoProductos + extraFormaPago) * 0.9;
32:     }
33:     return costoProductos + extraFormaPago;
34:   }
35: }
36: public class Cliente {
37:   private LocalDate fechaAlta;
38:   public LocalDate getFechaAlta() {
39:     return this.fechaAlta;
40:   }
41: }
42: public class Producto {
43:   private double precio;
44:   public double getPrecio() {
45:     return this.precio;
46:   }
47: }
```
### 2. Replace Conditional with Polymorphism (líneas 21 a 27)
```java
01: public class Pedido {
02:  private Cliente cliente;
03:  private List<Producto> productos;
04:  private FormaPago formaPago;
05:
      public Pedido(Cliente cliente, List<Producto> productos, FormaPago formaPago) {
06:    this.formaPago = formaPago;
11:    this.cliente = cliente;
12:    this.productos = productos;
13:    this.formaPago = formaPago;
14:   }
15:   public double getCostoTotal() {
16:     double costoProductos = productos.stream().mapToInt(prod->prod.getPrecio()).sum(); 
20:     double extraFormaPago = this.formaPago.calcularExtra(costoProductos);
28:     int añosDesdeFechaAlta = Period.between(this.cliente.getFechaAlta(), LocalDate.now()).getYears();

29:     // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
30:     if (añosDesdeFechaAlta > 5) {
31:       return (costoProductos + extraFormaPago) * 0.9;
32:     }
33:     return costoProductos + extraFormaPago;
34:   }
35: }
36: public class Cliente {
37:   private LocalDate fechaAlta;
38:   public LocalDate getFechaAlta() {
39:     return this.fechaAlta;
40:   }
41: }
42: public class Producto {
43:   private double precio;
44:   public double getPrecio() {
45:     return this.precio;
46:   }
47: }

  public interface FormaPago
  {
      double calcularExtra(double costoProductos);
  }

  public class Efectivo implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return 0;
      }
  }

  public class SeisCuotas implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return costoProductos * 0.2;
      }
  }

  public class DoceCuotas implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return costoProductos * 0.5;
      }
  }
```
### 3. Extract method y move method (línea 28)
```java
01: public class Pedido {
02:  private Cliente cliente;
03:  private List<Producto> productos;
04:  private FormaPago formaPago;

05:  public Pedido(Cliente cliente, List<Producto> productos, FormaPago formaPago) {
06:    this.formaPago = formaPago;
11:    this.cliente = cliente;
12:    this.productos = productos;
13:    this.formaPago = formaPago;
14:   }
15:   public double getCostoTotal() {
16:     double costoProductos = productos.stream().mapToInt(prod->prod.getPrecio()).sum(); 
20:     double extraFormaPago = this.formaPago.calcularExtra(costoProductos);
28:     int añosDesdeFechaAlta = this.cliente.añosDesdeFechaAlta();

29:     // Aplicar descuento del 10% si el cliente tiene más de 5 años de antiguedad
30:     if (añosDesdeFechaAlta > 5) {
31:       return (costoProductos + extraFormaPago) * 0.9;
32:     }
33:     return costoProductos + extraFormaPago;
34:   }
35: }
36: public class Cliente {
37:   private LocalDate fechaAlta;
38:   public LocalDate getFechaAlta() {
39:     return this.fechaAlta;
40:   }
      
      public int añosDesdeFechaAlta()
      {
          return Period.between(this.getFechaAlta(), LocalDate.now()).getYears();
      }
41: }
42: public class Producto {
43:   private double precio;
44:   public double getPrecio() {
45:     return this.precio;
46:   }
47: }

  public interface FormaPago
  {
      double calcularExtra(double costoProductos);
  }

  public class Efectivo implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return 0;
      }
  }

  public class SeisCuotas implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return costoProductos * 0.2;
      }
  }

  public class DoceCuotas implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return costoProductos * 0.5;
      }
  }
```

### Extract method y replace temp with query (líneas 28 a 33)
```java
01: public class Pedido {
02:  private Cliente cliente;
03:  private List<Producto> productos;
04:  private FormaPago formaPago;

05:  public Pedido(Cliente cliente, List<Producto> productos, FormaPago formaPago) {
06:    this.formaPago = formaPago;
11:    this.cliente = cliente;
12:    this.productos = productos;
13:    this.formaPago = formaPago;
14:   }
15:   public double getCostoTotal() {
16:     double costoProductos = productos.stream().mapToInt(prod->prod.getPrecio()).sum(); 
20:     double extraFormaPago = this.formaPago.calcularExtra(costoProductos);

33:     double subtotal = costoProductos + extraFormaPago;
        return this.aplicarDescuento(subtotal);
34:   }
      private double aplicarDescuento(double subtotal) {
      // La condición con la Query directa
      if (this.cliente.getAntiguedadEnAnios() > 5) {
          return subtotal * 0.9;
      }
      return subtotal;
}
35: }
36: public class Cliente {
37:   private LocalDate fechaAlta;
38:   public LocalDate getFechaAlta() {
39:     return this.fechaAlta;
40:   }
      
      public int añosDesdeFechaAlta()
      {
          return Period.between(this.getFechaAlta(), LocalDate.now()).getYears();
      }
41: }
42: public class Producto {
43:   private double precio;
44:   public double getPrecio() {
45:     return this.precio;
46:   }
47: }

  public interface FormaPago
  {
      double calcularExtra(double costoProductos);
  }

  public class Efectivo implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return 0;
      }
  }

  public class SeisCuotas implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return costoProductos * 0.2;
      }
  }

  public class DoceCuotas implements FormaPago
  {
      @Override
      public double calcularExtra(double costoProductos)
      {
        return costoProductos * 0.5;
      }
  }
```
