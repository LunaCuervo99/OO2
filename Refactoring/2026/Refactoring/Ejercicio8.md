## Ejercicio 8: Documentos y estadísticas
### Enunciado
Dado el siguiente código implementado en la clase Document y que calcula algunas estadísticas del mismo:
```java
public class Document {
    List<String> words;
  
    public long characterCount() {
       	long count = this.words
      .stream()
      .mapToLong(w -> w.length())
      .sum();
          	return count;
	  }
    public long calculateAvg() {
    	long avgLength = this.words
      .stream()
      .mapToLong(w -> w.length())
      .sum() / this.words.size();
       	return avgLength;
	  }
  // Resto del código que no importa
}
```
Tareas:
1) Enumere los code smell y que refactorings utilizará para solucionarlos.
2) Aplique los refactorings encontrados, mostrando el código refactorizado luego de aplicar cada uno.
3) Analice el código original y detecte si existe un problema al calcular las estadísticas. Explique cuál es el error y en qué casos se da ¿El error identificado sigue presente luego de realizar los refactorings? En caso de que no esté presente, ¿en qué momento se resolvió? De acuerdo a lo visto en la teoría, ¿podemos considerar esto un refactoring?

### 1. Code Smells y Refactorings a utilizar
* **(i) Code Smell: Duplicated Code (Código Duplicado)**
    La misma expresión que calcula la suma de la longitud de las palabras está escrita tanto en el método `characterCount()` como en `calculateAvg()`.
    **Refactoring:** Como la lógica ya está encapsulada en el método `characterCount()`, simplemente debemos invocar ese método desde `calculateAvg()` en lugar de repetir el código (**Extract Method**?).
* **(ii) Code Smell: Variables Temporales Innecesarias**. 
    En ambos métodos se declaran variables temporales (`count` y `avgLength`) a las que se les asigna el resultado de una expresión una sola vez e inmediatamente se retornan.
    **Refactoring:** **Inline Temp**: Reemplazar las referencias a esas temporales con la expresión original y eliminar las variables.

### 2. Aplico los refactorings nombrados anteriormente: 
```java
public class Document {
    List<String> words;
  
    public long characterCount() {
        return this.words
            .stream()
            .mapToLong(w -> w.length())
            .sum();
	  }
    public long calculateAvg() {
       	return  this.characterCount() / this.words.size();
	  }
  // Resto del código que no importa
}
```

### 3. El error lógico ocurre en el método `calculateAvg()`, si la lista de palabras está vacía, el método this.words.size() devolverá 0. Al intentar dividir la cantidad de caracteres por 0, Java lanza un error.
*¿El error identificado sigue presente luego de realizar los refactorings?*
El error sigue estando presente luego del refactoring. El código refactorizado es más limpio y legible pero rompería de la misma manera que antes.
*En caso de que no esté presente, ¿en qué momento se resolvió? De acuerdo a lo visto en la teoría, ¿podemos considerar esto un refactoring?*
Según la definición, el refactoring es el proceso de modificar el software de tal manera que no altere el comportamiento del código, pero mejore su estructura interna. Ante la misma entrada, el sistema tiene que seguir produciendo la misma salida.
El refactoring no agrega funcionalidad ni arregla bugs, por lo tanto, si el comportamiento está roto (el código no funciona o tiene un bug), no se puede refactorizar, primero tiene que funcionar y pasar los tests.
