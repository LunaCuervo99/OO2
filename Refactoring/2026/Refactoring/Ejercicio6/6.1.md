# Ejercicio 6: 

Para cada una de las siguientes situaciones, realice en forma iterativa los siguientes pasos:
(i) indique el mal olor,
(ii) indique el refactoring que lo corrige, 
(iii) aplique el refactoring, mostrando el resultado final (código y/o diseño según corresponda). 
Si vuelve a encontrar un mal olor, retorne al paso (i). 

## 6.1 Empleados
```java
public class EmpleadoTemporario {
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;
    public double horasTrabajadas = 0;
    public int cantidadHijos = 0;
    // ......
    
    public double sueldo() {
      return this.sueldoBasico
      + (this.horasTrabajadas * 500) 
      + (this.cantidadHijos * 1000) 
      - (this.sueldoBasico * 0.13);
    }
}
public class EmpleadoPlanta {
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;
    public int cantidadHijos = 0;
    // ......
    
    public double sueldo() {
        return this.sueldoBasico 
        + (this.cantidadHijos * 2000)
        - (this.sueldoBasico * 0.13);
    }
}

public class EmpleadoPasante {
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;
    // ......
    
    public double sueldo() {
        return this.sueldoBasico - (this.sueldoBasico * 0.13);
    }
}
```

### Solución:
* **(i) Mal olor:** **Duplicated Code**: Las tres clases comparten los mismos atributos básicos (`nombre`, `apellido`, `sueldoBasico`) y representan el mismo concepto.
* **(ii) Refactoring:** **Extract Superclass**: Se crea una nueva superclase `Empleado` con los atributos comunes, seguido de  **Pull Up Field** y **Pull Up Method** para mover los campos repetidos y sus metodos.
* **(iii) Resultado:**
```java
public abstract class Empleado {
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;
    
    // Firma común para garantizar polimorfismo
    public abstract double sueldo(); 
}

public class EmpleadoTemporario extends Empleado {
    public double horasTrabajadas = 0;
    public int cantidadHijos = 0;
    
    public double sueldo() {
        return this.sueldoBasico + (this.horasTrabajadas * 500) + (this.cantidadHijos * 1000) - (this.sueldoBasico * 0.13);
    }
}

public class EmpleadoPlanta extends Empleado {
    public int cantidadHijos = 0;
    
    public double sueldo() {
        return this.sueldoBasico + (this.cantidadHijos * 2000) - (this.sueldoBasico * 0.13);
    }
}

public class EmpleadoPasante extends Empleado {
    public double sueldo() {
        return this.sueldoBasico - (this.sueldoBasico * 0.13);
    }
}
```
* **(i) Mal olor:** **Magic Numbers**: Los valores 500, 1000, 2000 y 0.13 están fijos en el código sin explicación de qué representan.
* **(ii) Refactoring:** **Replace Magic Number with Symbolic Constant**
* **(iii) Resultado:**
```java
  public abstract class Empleado {
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;

    // El descuento es común a todos, lo definimos en el padre
    public static final double PORCENTAJE_RETENCIONES = 0.13;
  
    public abstract double sueldo(); 
}

public class EmpleadoTemporario extends Empleado {
    public double horasTrabajadas = 0;
    public int cantidadHijos = 0;
    public static final double PAGO_POR_HORA = 500;
    public static final double ASIGNACION_POR_HIJO = 1000;

    public double sueldo() {
        return this.sueldoBasico + (this.horasTrabajadas * PAGO_POR_HORA) + (this.cantidadHijos * ASIGNACION_POR_HIJO) - (this.sueldoBasico * PORCENTAJE_RETENCIONES);
    }
}

public class EmpleadoPlanta extends Empleado {
    public int cantidadHijos = 0;
    public static final double ASIGNACION_POR_HIJO = 2000.0;

    public double sueldo() {
        return this.sueldoBasico + (this.cantidadHijos * ASIGNACION_POR_HIJO) - (this.sueldoBasico * PORCENTAJE_RETENCIONES);
    }
}

public class EmpleadoPasante extends Empleado {
    
    public double sueldo() {
        return this.sueldoBasico - (this.sueldoBasico * PORCENTAJE_RETENCIONES);
    }
}
```
* **(i) Mal olor:** **Duplicated Code**: Todos los tipos de empleado comparten en su método `sueldo()` la accion de multiplicar el sueldo por las retenciones.
* **(ii) Refactoring:** **Extract Method**: Extraigo las lineas repetidas en un nuevo método llamado `sueldoBasicoConRetenciones()` , y luego aplico **Pull Up Method** para pasarlo a la superclase
* **(iii) Resultado:**
```java
  public abstract class Empleado {
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;
    public static final double PORCENTAJE_RETENCIONES = 0.13;

    // Método extraído
    public double sueldoBasicoConRetenciones() {
        return this.sueldoBasico - (this.sueldoBasico * PORCENTAJE_RETENCIONES)
    }
    public abstract double sueldo(); 
}

public class EmpleadoTemporario extends Empleado {
    public double horasTrabajadas = 0;
    public int cantidadHijos = 0;
    public static final double PAGO_POR_HORA = 500;
    public static final double ASIGNACION_POR_HIJO = 1000;

    public double sueldo() {
        // Al básico neto, le sumamos sus adicionales
        return this.sueldoBasicoConRetenciones() + (this.horasTrabajadas * PAGO_POR_HORA) + (this.cantidadHijos * ASIGNACION_POR_HIJO);
    }
}

public class EmpleadoPlanta extends Empleado {
    public int cantidadHijos = 0;
    public static final double ASIGNACION_POR_HIJO = 2000.0;

    public double sueldo() {
        return this.sueldoBasicoConRetenciones() + (this.cantidadHijos * ASIGNACION_POR_HIJO);
    }
}

public class EmpleadoPasante extends Empleado {
    public double sueldo() {
        return this.sueldoBasicoConRetenciones();
    }
}
```

* **(i) Mal olor:** **Public Field** (Atributo Público). Todos los atributos en la superclase y en las subclases quedaron expuestos, lo que rompe el encapsulamiento.
* **(ii) Refactoring:** **Encapsulate Field** (Encapsular Atributo). Se cambia la visibilidad de los atributos a `private` y se proveen métodos de acceso (getters/setters) solo si son necesarios para los clientes externos.
* **(iii) Resultado:**

```java
public abstract class Empleado {
    private String nombre;
    private String apellido;
    private double sueldoBasico = 0;

    public static final double PORCENTAJE_RETENCIONES = 0.13;

    protected double sueldoBasicoConRetenciones() {
        return this.sueldoBasico - (this.sueldoBasico * PORCENTAJE_RETENCIONES);
    }
    
    public abstract double sueldo(); 
}

public class EmpleadoTemporario extends Empleado {
    public static final double PAGO_POR_HORA = 500.0;
    public static final double ASIGNACION_POR_HIJO = 1000.0;
    
    private double horasTrabajadas = 0;
    private int cantidadHijos = 0;

    public double sueldo() {
        return this.sueldoBasicoConRetenciones() + (this.horasTrabajadas * PAGO_POR_HORA) + (this.cantidadHijos * ASIGNACION_POR_HIJO);
    }
}

public class EmpleadoPlanta extends Empleado {
    public static final double ASIGNACION_POR_HIJO = 2000.0;

    private int cantidadHijos = 0;

    public double sueldo() {
        return this.sueldoBasicoConRetenciones() + (this.cantidadHijos * ASIGNACION_POR_HIJO);
    }
}

public class EmpleadoPasante extends Empleado {
    public double sueldo() {
        return this.sueldoBasicoConRetenciones();
    }
}
```
