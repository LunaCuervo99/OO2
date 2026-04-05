## Ejercicio 4: Alcance en Redes Sociales

Una nueva red social está desarrollando un sistema para modelar perfiles y publicaciones, y medir su alcance como parte del algoritmo de relevancia. Cada publicación acumula reacciones de los usuarios, y cada perfil consolida el alcance de sus publicaciones amplificándolo según si está verificado o no. El código es el siguiente:

**Publicacion.java**
```java
public class Publicacion {
   private String texto;
   private int likes;
   public Publicacion(String texto) {
      this.texto = texto;
      this.likes = 0;
   }
   public void darLike() { likes++; }
   public void darDislike() { likes--; }
   private int procesar() {
       return likes * 3;
   }
   public int calcular() {
       return procesar() * 10;
   }
}

public class Perfil {
   private boolean verificado;
   private ArrayList<Publicacion> publicaciones;
   public Perfil(boolean verificado) {
        this.verificado = verificado;
        this.publicaciones = new ArrayList<>();
   }
   public void agregarPublicacion(Publicacion p) { publicaciones.add(p); }
   private int bonus() { return verificado ? 2 : 1; }
   private int alcanceDePublicaciones() {
       return publicaciones.stream().mapToInt(p -> p.calcular()).sum();
   }
   public int calcular() {
       return alcanceDePublicaciones() * bonus();
   }
}
```
## Tareas:
Liste cada uno de los cambios necesarios, indicando archivo y línea afectados, para cada uno de los siguientes refactorings:
1) Rename method: procesar (referenciado en línea 11 de Publicacion.java) por impacto
2) Rename method: calcular (referenciado en línea 14 de Publicacion.java) por alcance
3) Rename method: calcular (referenciado en línea 15 de Perfil.java) por alcance
4) Rename parameter: el parámetro “p” del método agregarPublicacion (línea 10 de Perfil.java) por “publicacion”

### Solución
1) Al ser un método privado, rename method solo afecta a Publicacion.java y las lineas:
11:
```diff
- public int calcular() {
+ public int impacto() {
```
15:
```diff
- return procesar() * 10;
+ return impacto() * 10;
```
2) calcular en Publicacion.java afecta tanto a Publicacion.java como a Perfil.java que lo llama. Modificaciones en las lineas:
14 de Publicacion.java:
```diff
- public int calcular() {
+ public int alcance() {
```
13 de Perfil.java
```diff
- return publicaciones.stream().mapToInt(p -> p.calcular()).sum();
+ return publicaciones.stream().mapToInt(p -> p.alcance()).sum();
```
3) calcular en Perfil.java afecta solo a Perfil.java, en la linea 15:
```diff
- public int calcular() {
+ public int alcance() {
```
4) renombrar el parametro "p" de agregarPublicacion afecta solo a Publicacion.java en las linea 10:
```diff
- public void agregarPublicacion(Publicacion p) { publicaciones.add(p); }
+ public void agregarPublicacion(Publicacion publicacion) { publicaciones.add(publicacion); }
```
La variable p de la linea 13 corresponde a una variable temporal del stream, no al parámetro.
