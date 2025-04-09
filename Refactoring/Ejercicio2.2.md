# Ejercicio 2
**Para cada una de las siguientes situaciones, realice en forma iterativa los siguientes pasos:**
1. indique el mal olor, <br/>
2. indique el refactoring que lo corrige, <br/> 
3. aplique el refactoring, mostrando el resultado final (código y/o diseño según corresponda). <br/>
> Si vuelve a encontrar un mal olor, retorne al paso (i).

## 2.2 Juego
```java
public class Juego {
// ......
  public void incrementar(Jugador j) {
    j.puntuacion = j.puntuacion + 100;
  }
  public void decrementar(Jugador j) {
    j.puntuacion = j.puntuacion - 50;
  }

  public class Jugador {
    public String nombre;
    public String apellido;
    public int puntuacion = 0;
  }

}
```
- - - 
## Refactor 1 
### **Bad Smell:** Atributos Públicos
La clase `Jugador` tiene todos sus atributos públicos, lo cual *rompe el encapsulamiento*
### **Refactoring:** Encapsulate Field
Creo metodos getters y setters para los campos publicos, encuentro todas las invocaciones y las reemplazo con los metodos y luego hago los campos *nombre, apellido, puntuacion* privados.

```java
public class Juego {
// ......
  public void incrementar(Jugador j) {
    j.puntuacion = j.puntuacion + 100;
  }
  public void decrementar(Jugador j) {
    j.puntuacion = j.puntuacion - 50;
  }

  public class Jugador {
    private String nombre;
    private String apellido;
    private int puntuacion = 0;

    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public int getPuntuacion() {
        return puntuacion;
    }
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
  }

}
```


