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
## Refactor 2 
### **Bad Smell:** Nombre poco descriptivo
La clase `Juego` contiene métodos con nombres *poco descriptivos*, no es claro a que se refiere con el incrementar y decrementar
### **Refactoring:** Rename Method
Cambio el nombre del metodo *incrementar* a *incrementarPuntuacion* y *decrementar* a *decrementarPuntuacion* para que haya mas claridad
```java
public class Juego {
// ......
  public void incrementarPuntuacion(Jugador j) {
    j.puntuacion = j.puntuacion + 100;
  }
  public void decrementarPuntuacion(Jugador j) {
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
## Refactor 3 
### **Bad Smell:** Feature Envy
Los métodos *incrementarPuntuacion* y *decrementarPuntuacion* de la clase `Juego` corresponden mas a un comportamiento de la clase `Jugador` ya que utilizan y modifican sus atributos, lo cual sugiere mover esos métodos a la clase `Jugador`
### **Refactoring:** Move Method
 Traslado los métodos *incrementarPuntuacion* y *decrementarPuntuacion* de la clase `Juego` a la clase `Jugador`, y los que están en `Juego` hacen referencia a los de `Jugador`.
Acá deberia recibir en la clase jugador los puntos como parametro?
 
```java
  public class Juego {
    // ......
    public void incrementarPuntuacion(Jugador j) {
        j.incrementarPuntuacion();
    }
    public void decrementarPuntuacion(Jugador j) {
        j.decrementarPuntuacion();
    }
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

    public void incrementarPuntuacion() {
      this.puntuacion += 100;
    }
    public void decrementarPuntuacion() {
      this.puntuacion -= 50;
    }
  }
```
## Refactor 4 
### **Bad Smell:** Ausencia de Constructor
La clase `Juegador` no tiene un constructor que permita instanciar e inicializar los datos de un Jugador correctamente: *nombre, apellido* y *puntuacion* en 0.
### **Refactoring:** Crear un Constructor
```java
  public class Juego {
    // ......
    public void incrementarPuntuacion(Jugador j) {
        j.incrementarPuntuacion();
    }
    public void decrementarPuntuacion(Jugador j) {
        j.decrementarPuntuacion();
    }
  }

  public class Jugador {
    private String nombre;
    private String apellido;
    private int puntuacion = 0;

     public Jugador(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.puntuacion = 0;
    }

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

    public void incrementarPuntuacion() {
      this.puntuacion += 100;
    }
    public void decrementarPuntuacion() {
      this.puntuacion -= 50;
    }
  }

```
