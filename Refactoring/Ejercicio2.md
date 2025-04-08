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
### **Bad Smell:** Duplicated Code
Los tres tipos de empleados tienen atributos en común: nombre, apellido, sueldoBasico
### **Refactoring:**  Extract Superclass y Pull Up Method? o van por separado?

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
    
      public double sueldo() {
        return this.sueldoBasico 
        + (this.horasTrabajadas * 500) 
        - (this.cantidadHijos * 1000) 
        - (this.sueldoBasico * 0.13);
        }
}

public class EmpleadoPlanta extends Empleado
{
     public int cantidadHijos = 0;
     // ....

     public double sueldo() {
        return this.sueldoBasico 
            + (this.cantidadHijos * 2000)
            - (this.sueldoBasico * 0.13);
    }
}

public class EmpleadoPasante extends Empleado
{
    // ...
    
     public double sueldo() {
            return this.sueldoBasico - (this.sueldoBasico * 0.13);
        }
}
```

## Refactor 2 
###  **Bad Smell:** Atributos Públicos
Los empleados tienen declarados todos sus atributos públicos, lo cual rompe el encapsulamiento.
###  **Refactoring:**  Encapsulate Fields
Creo metodos getters y setters, encuentro todas las invocaciones de los atributos publicos, las reemplazamos con los getters/setters y cambio a privados.
```java
public abstract class Empleado
{
    private String nombre;
    private String apellido;
    private double sueldoBasico = 0;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public double getSueldoBasico() {
        return this.sueldoBasico;
    }

    public void setSueldoBasico(double sueldoBasico) {
        this.sueldoBasico = sueldoBasico;
    }
}

public class EmpleadoTemporario extends Empleado
{
        private double horasTrabajadas = 0;
        private int cantidadHijos = 0;
        // ....

       public double getHorasTrabajadas(){
         return this.horasTrabajadas;
       }

      public void setHorasTrabajadas(double horas)
      {
         this.horasTrabajadas = horas;
      }

      public int getCantidadHijos(){
         return this.cantidadHijos;
       }

      public void setCantidadHijos(int hijos)
      {
         this.cantidadHijos = hijos;
      }

      public double sueldo() {
        return this.getSueldoBasico 
        + (this.getHorasTrabajadas() * 500) 
        - (this.getCantidadHijos() * 1000) 
        - (this.getSueldoBasic()o * 0.13);
        }
}

public class EmpleadoPlanta extends Empleado
{
      private int cantidadHijos = 0;
      // ....

      public int getCantidadHijos()
       {
         return this.cantidadHijos;
       }

      public void setCantidadHijos(int hijos)
      {
         this.cantidadHijos = hijos;
      }

     public double sueldo() {
        return this.getSueldoBasico() 
            + (this.getCantidadHijos() * 2000)
            - (this.getSueldoBasico() * 0.13);
    }
}

public class EmpleadoPasante extends Empleado
{
    // ...
    
     public double sueldo() {
            return this.getSueldoBasico() - (this.getSueldoBasico() * 0.13);
        }
}
```
