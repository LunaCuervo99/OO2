# Ejercicio 2: Iteradores circulares

```java
 1   public class CharRing extends Object {
 2      char[] source;
 3      int idx;
 4  
 5      public CharRing(String srcString) {
 6          char result;
 7          source = new char[srcString.length()];
 8          srcString.getChars(0, srcString.length(), source, 0);
 9          result = 0;
10          idx = result;
11      }
12  
13      public char next() {
14          int result;
15          if (idx >= source.length)
16              idx = 0;
17          result = idx++;
18          return source[result];
19      }
20  }
```

## Tareas:
Se quiere aplicar el refactoring Rename Variable sobre la variable result que se usa en la línea 18 con el nuevo nombre currentPosition.
¿Cómo queda el código final y qué inconveniente se podría encontrar?

### Solución

```java
 1   public class CharRing extends Object {
 2      char[] source;
 3      int idx;
 4  
 5      public CharRing(String srcString) {
 6          char result;
 7          source = new char[srcString.length()];
 8          srcString.getChars(0, srcString.length(), source, 0);
 9          result = 0;
10          idx = result;
11      }
12  
13      public char next() {
14          int currentPosition;
15          if (idx >= source.length)
16              idx = 0;
17          currentPosition = idx++;
18          return source[currentPosition];
19      }
20  }
```
El inconveniente que se puede presentar es el *alcance de las variables*. En el código original el nombre `result` está siendo utilizado para dos variables distintas en dos scopes separados. Hay que tener cuidado de reemplazarlo en el método correspondiente (*next()*)
