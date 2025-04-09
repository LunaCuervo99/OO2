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
