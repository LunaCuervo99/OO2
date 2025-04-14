# Ejercicio 3

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

1. Enumere los code smell y que refactorings utilizará para solucionarlos.
2. Aplique los refactorings encontrados, mostrando el código refactorizado luego de aplicar cada uno.
3. Analice el código original y detecte si existe un problema al calcular las estadísticas. Explique cuál es el error y en qué casos se da ¿El error identificado sigue presente luego de realizar los refactorings? En caso de que no esté presente, ¿en qué momento se resolvió? De acuerdo a lo visto en la teoría, ¿podemos considerar esto un refactoring?

## Refactor 1 
### **Bad Smell:** Atributos Publicos
### **Refactoring:** Encapsulate Fields
```java
public class Document {
    private List<String> words;

    public List<String> getWords() {
        return this.words;
    }

    public void setWords(List<String> words){
        this.words = words;
    }

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

## Refactor 2
### **Bad Smell:** Duplicated Code
El metodo `calculateAvg()` esta utilizando el metodo `characterCount()`, por lo que se podria invocar en vez de reescribir codigo
### **Refactoring:** Extract Method 
```java
public class Document {
    private List<String> words;

    public List<String> getWords() {
        return this.words;
    }

    public void setWords(List<String> words){
        this.words = words;
    }

    public long characterCount() {
 	    long count = this.words
        .stream()
        .mapToLong(w -> w.length())
        .sum();
        return count;
    }

    public long calculateAvg() {
        long avgLength = this.characterCount() / this.words.size();
        return avgLength;
    }
    // Resto del código que no importa
}
```