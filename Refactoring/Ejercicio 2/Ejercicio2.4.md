# Ejercicio 2
**Para cada una de las siguientes situaciones, realice en forma iterativa los siguientes pasos:**
1. indique el mal olor, <br/>
2. indique el refactoring que lo corrige, <br/> 
3. aplique el refactoring, mostrando el resultado final (código y/o diseño según corresponda). <br/>
> Si vuelve a encontrar un mal olor, retorne al paso (i).

![uml2.4](../img/uml2.4.png)
## 2.4 Carrito de compras
```java
public class Producto {
    private String nombre;
    private double precio;
    
    public double getPrecio() {
        return this.precio;
    }
}

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
        
    public Producto getProducto() {
        return this.producto;
    }
    
    public int getCantidad() {
        return this.cantidad;
    }

}

public class Carrito {
    private List<ItemCarrito> items;
    
    public double total() {
        return this.items.stream()
        .mapToDouble(item -> 
        item.getProducto().getPrecio() * item.getCantidad())
        .sum();
    }
}
```
- - - 
## Refactor 1 
### **Bad Smell:** Feature Envy
La clase `Carrito` esta designando mal las tareas, utilizando los atributos de `itemCarrito` para calcular el total. Por lo que ese metodo `total()` deberia estar en la Clase `Carrito` 
### **Refactoring:** Extract Method y Move Method
Extraigo la parte del metodo `total()` que utiliza los atributos de `itemCarrito` a otro metodo llamado `totalItemCarrito()`, y luego lo invoco en el metodo original `total.
Luego aplico Move Method moviendo el metodo `totalItemCarrito()` a la clase `itemCarrito` que es quien le corresponde hacer ese calculo.
```java
public class Producto {
    private String nombre;
    private double precio;
    
    public double getPrecio() {
        return this.precio;
    }
}

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
        
    public Producto getProducto() {
        return this.producto;
    }
    
    public int getCantidad() {
        return this.cantidad;
    }

    public double totalItemCarrito() {
        return this.getProducto().getPrecio() * this.getCantidad();
    }
}

public class Carrito {
    private List<ItemCarrito> items;
    
    public double total() {
        return this.items.stream()
        .mapToDouble(item -> item.totalItemCarrito())
        .sum();
    }
}
```