## Ejercicio 5: Productos

### Enunciado
Se cuenta con un sistema que maneja una jerarquía de productos que pueden ser estadías de hoteles o alquileres de autos. En el siguiente material adicional puede descargar un proyecto con las clases implementadas. Observe que el proyecto cuenta con test unitarios que verifican el funcionamiento correcto de la aplicación. A continuación se muestra un extracto del código:

```java
public class HotelStay extends Product {
    public double cost;
    private TimePeriod timePeriod;
    private Hotel hotel;

    public HotelStay(double cost, TimePeriod timePeriod, Hotel hotel) {
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.hotel = hotel;
    }

    public LocalDate startDate() {
        return this.timePeriod.start();
    }

    public LocalDate endDate() {
        return this.timePeriod.end();
    }

    public double priceFactor() {
        return this.cost / this.price();
    }

    public double price() {
        return this.timePeriod.duration() * this.hotel.nightPrice() * this.hotel.discountRate();
    }
}

public class CarRental extends Product {
    public double cost;
    private TimePeriod timePeriod;
    private Company company;

  public CarRental(double cost, TimePeriod timePeriod, Company company) {
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.company = company;
    }

    public LocalDate startDate() {
        return this.timePeriod.start();
    }

    public LocalDate endDate() {
        return this.timePeriod.end();
    }

    public double price() {
        return this.company.price() * this.company.promotionRate();
    }

    public double cost() {
        return this.cost;
    }
}
```
***Tareas solicitadas:***
1) La variable cost está declarada como pública, lo que rompe el encapsulamiento de la clase. Utilice el refactoring Encapsulate Field y describa brevemente los pasos llevados a cabo. Verifique que los tests provistos sigan funcionando. Discuta: ¿Es correcto modificar alguno de los tests para que el código refactorizado funcione? En caso de que el test falle, ¿qué situación está representando este test?
2) Utilice el refactoring Rename Field en el método priceFactor(), para que la variable cost se pase a llamar quote. Verifique que los tests provistos sigan funcionando. Discuta: ¿en este caso, es necesario modificar alguno de los tests para que el código refactorizado funcione?
3) Se quiere aplicar el refactoring Pull Up Method para subir los métodos startDate() y endDate() a la superclase Product. ¿Es posible hacerlo en el código anterior? Justifique su respuesta basándose en las precondiciones del refactoring vistas en la teoría y en el libro de Refactoring de Martin Fowler.
4) Mencione y aplique los refactorings necesarios para poder hacer Pull Up Method.
5) Aplique Pull Up Method para subir los métodos a la superclase Product.
6) Observe los métodos price() en CarRental.java y en HotelStay.java. Identifique los Code Smell presentes. Luego aplique los refactoring correspondientes para eliminarlos.

### Solución
#### 1) Encapsulate Field sobre la variable `cost`

**Pasos llevados a cabo:**
1. Se crearon los métodos getter (`getCost()`) y setter (`setCost(double cost)`) para el atributo `cost` en ambas clases.
2. Se buscaron todos los clientes fuera de la clase que referenciaban al atributo `cost` y se reemplazó esa referencia directa por una llamada a los nuevos métodos getter/setter.
3. Una vez que todos los clientes fueron cambiados, se declaró el atributo `cost` como `private`.
```java
public class HotelStay extends Product {
    private double cost; // 1. Cambiado a private
    private TimePeriod timePeriod;
    private Hotel hotel;

    public HotelStay(double cost, TimePeriod timePeriod, Hotel hotel) {
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.hotel = hotel;
    }

    // 2. Creación de métodos Getter y Setter
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public LocalDate startDate() {
        return this.timePeriod.start();
    }
    public LocalDate endDate() {
        return this.timePeriod.end();
    }
    public double priceFactor() {
        return this.cost / this.price(); 
    }
    public double price() {
        return this.timePeriod.duration() * this.hotel.nightPrice() * this.hotel.discountRate();
    }
}

public class CarRental extends Product {
    private double cost; // 1. Cambiado a private
    private TimePeriod timePeriod;
    private Company company;

    public CarRental(double cost, TimePeriod timePeriod, Company company) {
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.company = company;
    }

    // 2. Creación de métodos Getter y Setter
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public LocalDate startDate() {
        return this.timePeriod.start();
    }
    public LocalDate endDate() {
        return this.timePeriod.end();
    }
    public double price() {
        return this.company.price() * this.company.promotionRate();
    }

    /* Nota sobre este método original: 
       CarRental ya tenía un método llamado cost() que actuaba como un getter. 
       Al aplicar el refactoring y crear getCost(), este método antiguo puede 
       eliminarse (reemplazando sus llamadas) o dejarse transitoriamente 
       si no queremos romper otros clientes, pero lo ideal es unificar a getCost().
    */
    public double cost() {
        return this.cost;
    }
}
```
a- ¿Es correcto modificar los tests? 
Sí, es correcto en este caso. 
El refactoring dice que no debe cambiar el comportamiento observable, pero al encapsular un atributo público, estamos cambiando el contrato de la clase. Los clientes externos (incluidos los tests) deben actualizarse para usar el getCost() en lugar de leer el atributo directamente.

#### 2) Rename Field en `priceFactor()` para pasar de "cost" a "quote"
Preguntar, no estoy segura de si otra vez tengo que cambiar el atributo

#### 3) Se requiere aplicar el refactoring **Pull Up Method** para `startDate()` y `endDate()` a la superclase Product. Justificar si es posible.
No, no es posible. La precondición fundamental de Pull Up Method según Martin Fowler es que el método a subir solo debe referenciar a variables o métodos que ya existan o sean visibles en la superclase.
La variable timePeriod está declarada en las subclases (HotelStay y CarRental), por lo que si subimos el método a Product, el compilador arrojará un error porque Product no sabe qué es timePeriod.
En cambio, si primero hacemos Pull Up Field para timePeriod y luego Pull Up Method si seria posible.

#### 4) Mencione y aplique los refactorings necesarios para poder hacer Pull Up Method
Se debe hacer Pull Up Field de timePeriod, quedando de esta forma:
```java
public abstract class Product {
    protected TimePeriod timePeriod; 
}
public class HotelStay extends Product {
    private double quote; // Ya encapsulado y renombrado (Tareas 1 y 2)
    private Hotel hotel;
    //La declaración 'private TimePeriod timePeriod;' desapareció de acá

    public HotelStay(double cost, TimePeriod timePeriod, Hotel hotel) {
        this.quote = cost;
        this.timePeriod = timePeriod; // Ahora guarda el dato en el atributo heredado
        this.hotel = hotel;
    }

    public double getCost() { return this.quote; }
    public void setCost(double cost) { this.quote = cost; }

    public LocalDate startDate() {
        return this.timePeriod.start(); 
    }

    public LocalDate endDate() {
        return this.timePeriod.end();
    }

    public double priceFactor() {
        return this.quote / this.price();
    }

    public double price() {
        return this.timePeriod.duration() * this.hotel.nightPrice() * this.hotel.discountRate();
    }
}
```

#### 5) Aplicar Pull Up Method
```java
public abstract class Product {
    protected TimePeriod timePeriod; 

    public LocalDate startDate() {
        return this.timePeriod.start();
    }

    public LocalDate endDate() {
        return this.timePeriod.end();
    }
}

public class HotelStay extends Product {
    private double quote; 
    private Hotel hotel;

    public HotelStay(double cost, TimePeriod timePeriod, Hotel hotel) {
        this.quote = cost;
        this.timePeriod = timePeriod;
        this.hotel = hotel;
    }

    public double getCost() { return this.quote; }
    public void setCost(double cost) { this.quote = cost; }

    public double priceFactor() {
        return this.quote / this.price();
    }

    public double price() {
        return this.timePeriod.duration() * this.hotel.nightPrice() * this.hotel.discountRate();
    }
}

public class CarRental extends Product {
    private double cost; 
    private Company company;

    public CarRental(double cost, TimePeriod timePeriod, Company company) {
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.company = company;
    }

    public double getCost() { return this.cost; }
    public void setCost(double cost) { this.cost = cost; }

    public double price() {
        return this.company.price() * this.company.promotionRate();
    }

    public double cost() {
        return this.cost;
    }
}
```

#### 6) Observe los métodos price() en CarRental.java (línea 21) y en HotelStay.java (línea 25).  Identifique los Code Smell presentes. Luego aplique los refactoring correspondientes para eliminarlos. Verifique que los tests provistos sigan funcionando.
El metodo price() esta utilizando datos de la clase `Company` para hacer un calculo que deberia pertenecer a otra clase. 
El code smell es **Feature Envy**. Se puede aplicar Extract Method + Move Method

1- Seleccionamos la parte matemática que envidia a Hotel y la ponemos en un método privado auxiliar dentro de HotelStay:
En HotelStay.java (Paso Intermedio)
```java
// Extract Method. 
private double calcularPrecioNocheConDescuento() {
    return this.hotel.nightPrice() * this.hotel.discountRate();
}
//Además price quedaría:
public double price() {
    return this.timePeriod.duration() * this.calcularPrecioNocheConDescuento(); 
}
```

2- Move Method
Ahora que tenemos la lógica empaquetada en el método calcularPrecioNocheConDescuento() aplicamos Move Method: agarramos el paquete completo, lo borramos de HotelStay y lo pegamos adentro de la clase Hotel.
Al moverlo, el método pierde los prefijos this.hotel (porque ahora vive adentro del hotel).
El resultado del Move Method en Hotel.java:
```java
public class Hotel {
    // ... sus variables internas

    // pegamos el método movido
    public double getDiscountedNightPrice() {
        return this.nightPrice() * this.discountRate(); 
    }
}
```

La actualización final en el archivo original (HotelStay.java):
Como le borramos el método auxiliar que habíamos extraído en el Paso 1, ahora la clase tiene que delegar la llamada al objeto hotel:
```java
public abstract class Product {
    protected TimePeriod timePeriod; // Subido por Pull Up Field

    // Subidos por Pull Up Method
    public LocalDate startDate() {
        return this.timePeriod.start();
    }

    public LocalDate endDate() {
        return this.timePeriod.end();
    }
}

public class HotelStay extends Product {
    private double quote; // Encapsulado y Renombrado
    private Hotel hotel;

    public HotelStay(double cost, TimePeriod timePeriod, Hotel hotel) {
        this.quote = cost;
        this.timePeriod = timePeriod;
        this.hotel = hotel;
    }

    // Métodos de acceso creados por Encapsulate Field
    public double getCost() {
        return this.quote;
    }

    public void setCost(double cost) {
        this.quote = cost;
    }

    public double priceFactor() {
        return this.quote / this.price();
    }

    public double price() {
        // Envidia curada: se delega el cálculo al hotel
        return this.timePeriod.duration() * this.hotel.getDiscountedNightPrice();
    }
}

public class Hotel {
    // ... sus variables internas (precio base, tasas, etc.) ...

    // Creado por Extract Method + Move Method
    public double getDiscountedNightPrice() {
        return this.nightPrice() * this.discountRate();
    }

    // (Métodos originales que la clase ya tenía)
    public double nightPrice() { /* ... */ return 0; }
    public double discountRate() { /* ... */ return 0; }
}

public class CarRental extends Product {
    private double cost; // Encapsulado
    private Company company;

    public CarRental(double cost, TimePeriod timePeriod, Company company) {
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.company = company;
    }

    // Métodos de acceso creados por Encapsulate Field
    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double price() {
        // Envidia curada: se delega el cálculo a la compañía
        return this.company.getDiscountedPrice();
    }

    // Método original que actuaba como getter
    public double cost() {
        return this.cost;
    }
}

public class Company {
    // ... sus variables internas ...

    // Creado por Extract Method + Move Method
    public double getDiscountedPrice() {
        return this.price() * this.promotionRate();
    }

    // (Métodos originales que la clase ya tenía)
    public double price() { /* ... */ return 0; }
    public double promotionRate() { /* ... */ return 0; }
}
```
