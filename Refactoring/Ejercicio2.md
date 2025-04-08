# Ejercicio 2
## 2.1 Empleados

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
    - (this.cantidadHijos * 1000) 
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

**Para cada una de las siguientes situaciones, realice en forma iterativa los siguientes pasos:**
1. indique el mal olor, <br/>
2. indique el refactoring que lo corrige, <br/> 
3. aplique el refactoring, mostrando el resultado final (código y/o diseño según corresponda). <br/>
> Si vuelve a encontrar un mal olor, retorne al paso (i). 

## Refactor 1 
### 🔍 **Bad Smell:** Duplicated Code
Los tres tipos de empleados tienen atributos en común: nombre, apellido, sueldoBasico
### 🔧 **Refactoring:**  Extract Superclass y Pull Up Method? o van por separado?

```java
public abstract class Empleado
{
    public String nombre;
    public String apellido;
    public double sueldoBasico = 0;
}

public class EmpleadoTemporario extends Empleado
{
    public double horasTrabajadas = 0;
    public int cantidadHijos = 0;
    // ....
}

public class EmpleadoPlanta extends Empleado
{
     public int cantidadHijos = 0;
     // ....
}

public class EmpleadoPasante extends Empleado
{
    // ...
}
