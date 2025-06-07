# Enunciado
**Para cada una de las siguientes situaciones, realice en forma iterativa los siguientes pasos:**
1. indique el mal olor, <br/>
2. indique el refactoring que lo corrige, <br/> 
3. aplique el refactoring, mostrando el resultado final (código y/o diseño según corresponda). <br/>
> Si vuelve a encontrar un mal olor, retorne al paso (i).

public class Cliente {

    private String nombre;
    private String tipo;
    private List<Compra> compras;

    public Cliente(String unNombre) {
        this.nombre = unNombre;
        this.tipo = "basico";
        this.compras = new ArrayList<Compra>();
    }

    public Compra comprar(List<Producto> productos) {

        double temp1 = 0;

        // calcular descuento
        if (this.tipo.equals("basico")) {
            temp1 = 0.1;
        } else if (this.tipo.equals("premium")) {
            temp1 = 0.05;
        } else if (this.tipo.equals("advance")) {
            temp1 = 0;
        }

        // subtotal de la compra
        double subtotal = productos.stream().mapToDouble(p -> p.getPrecio()).sum();
        double costoEnvio = subtotal * temp1;

        // crear y agregar compra
        Compra n = new Compra(productos, subtotal, costoEnvio);
        this.compras.add(n);

        // definir tipo de cliente
        if (this.montoAcumuladoEnCompras() > 10000) {
            this.tipo = "advance";
        } else if (this.montoAcumuladoEnCompras() > 5000) {
            this.tipo = "premium";
        }

        return n;
    }

    public double montoAcumuladoEnCompras() {
        // implementación
    }
}

public class Compra {
    private List<Producto> productos;
    private double subtotal;
    private double envio;
    private String estado;

    // Constructor y métodos
}

public class Producto {
    private String descripcion;
    private double precio;

    // Constructor y métodos
}

## Refactor 1 
### **Bad Smell:** Non-descriptive names
La clase `Cliente` dentro del metodo `Comprar` tiene las variables temporales 'n' y 'temp1', que no son descriptivas
### **Refactoring:** Rename 
Renombro la variable 'n' a 'nuevaCompra' y 'temp1' a descuento ya que es lo que representan

public class Cliente {

    private String nombre;
    private String tipo;
    private List<Compra> compras;

    public Cliente(String unNombre) {
        this.nombre = unNombre;
        this.tipo = "basico";
        this.compras = new ArrayList<Compra>();
    }

    public Compra comprar(List<Producto> productos) {

        double descuento = 0;

        // calcular descuento
        if (this.tipo.equals("basico")) {
            descuento = 0.1;
        } else if (this.tipo.equals("premium")) {
            descuento = 0.05;
        } else if (this.tipo.equals("advance")) {
            descuento = 0;
        }

        // subtotal de la compra
        double subtotal = productos.stream().mapToDouble(p -> p.getPrecio()).sum();
        double costoEnvio = subtotal * descuento;

        // crear y agregar compra
        Compra nuevaCompra = new Compra(productos, subtotal, costoEnvio);
        this.compras.add(nuevaCompra);

        // definir tipo de cliente
        if (this.montoAcumuladoEnCompras() > 10000) {
            this.tipo = "advance";
        } else if (this.montoAcumuladoEnCompras() > 5000) {
            this.tipo = "premium";
        }

        return nuevaCompra;
    }

    public double montoAcumuladoEnCompras() {
        // implementación
    }
}

public class Compra {
    private List<Producto> productos;
    private double subtotal;
    private double envio;
    private String estado;

    // Constructor y métodos
}

public class Producto {
    private String descripcion;
    private double precio;

    // Constructor y métodos
}

## Refactor 2
### **Bad Smell:** Long Method
El metodo `Compra` esta haciendo muchas cosas al mismo tiempo y es muy largo, podria separarse en mas metodos y ser invocado
### **Refactoring:** Extract Method 
Extraemos el fragmento de codigo cuya logica se encarga de calcular el descuento a aplicar y creamo el metodo descuento() 
Extraemos el fragmento de codigo cuya logica se encarga de calcular el descuento a aplicar y creamo el metodo calcularSubtotal() 
Extraemos el fragmento de codigo cuya logica se encarga de calcular el descuento a aplicar y creamo el metodo actualizarTipoCliente()

public class Cliente {

    private String nombre;
    private String tipo;
    private List<Compra> compras;

    public Cliente(String unNombre) {
        this.nombre = unNombre;
        this.tipo = "basico";
        this.compras = new ArrayList<Compra>();
    }

    public Compra comprar(List<Producto> productos) {
        // calcular descuento
        double descuento = descuento(this);
        // subtotal de la compra
        double subtotal = subtotal(productos);
        double costoEnvio = subtotal * descuento;

        // crear y agregar compra
        Compra nuevaCompra = new Compra(productos, subtotal, costoEnvio);
        this.compras.add(nuevaCompra);

        // definir tipo de cliente
        definirTipoCliente(this);

        return nuevaCompra;
    }

    public void definirTipoCliente(Cliente cliente){
        if (cliente.montoAcumuladoEnCompras() > 10000) {
            cliente.tipo = "advance";
        } else if (cliente.montoAcumuladoEnCompras() > 5000) {
            cliente.tipo = "premium";
        }
    }

    public double descuento(Cliente cliente){
        
        if (cliente.tipo.equals("basico")) {
            return 0.1;
        } else if (cliente.tipo.equals("premium")) {
            return 0.05;
        } else if (cliente.tipo.equals("advance")) {
            return 0;
        }
    }

    public double subtotal(List<Producto> productos){
        return productos.stream().mapToDouble(p -> p.getPrecio()).sum();
    }

    public double montoAcumuladoEnCompras(Cliente cliente) {
        // implementación
    }
}

public class Compra {
    private List<Producto> productos;
    private double subtotal;
    private double envio;
    private String estado;

    // Constructor y métodos
}

public class Producto {
    private String descripcion;
    private double precio;

    // Constructor y métodos
}

## Refactor 3
### **Bad Smell:** 
La variable temporal 'descuento' es innecesaria, puedo sacarla y reemplazarla por el metodo
### **Refactoring:** Replace temporal with query 
Saco la variable temporal y utilizo el retorno del metodo como resultado para calcular el costo de envio

public class Cliente {

    private String nombre;
    private String tipo;
    private List<Compra> compras;

    public Cliente(String unNombre) {
        this.nombre = unNombre;
        this.tipo = "basico";
        this.compras = new ArrayList<Compra>();
    }

    public Compra comprar(List<Producto> productos) {
        // subtotal de la compra
        double subtotal = subtotal(productos);
        double costoEnvio = subtotal * descuento(cliente);

        // crear y agregar compra
        Compra nuevaCompra = new Compra(productos, subtotal, costoEnvio);
        this.compras.add(nuevaCompra);

        // definir tipo de cliente
        definirTipoCliente(this);

        return nuevaCompra;
    }

    public void definirTipoCliente(Cliente cliente){
        if (cliente.montoAcumuladoEnCompras() > 10000) {
            cliente.tipo = "advance";
        } else if (cliente.montoAcumuladoEnCompras() > 5000) {
            cliente.tipo = "premium";
        }
    }

    public double descuento(Cliente cliente){
        
        if (cliente.tipo.equals("basico")) {
            return 0.1;
        } else if (cliente.tipo.equals("premium")) {
            return 0.05;
        } else if (cliente.tipo.equals("advance")) {
            return 0;
        }
    }

    public double subtotal(List<Producto> productos){
        return productos.stream().mapToDouble(p -> p.getPrecio()).sum();
    }

    public double montoAcumuladoEnCompras(Cliente cliente) {
        // implementación
    }
}

public class Compra {
    private List<Producto> productos;
    private double subtotal;
    private double envio;
    private String estado;

    // Constructor y métodos
}

public class Producto {
    private String descripcion;
    private double precio;

    // Constructor y métodos
}

## Refactor 4
### **Bad Smell:** 
El metodo descuento usa un if 
### **Refactoring:** Replace conditional with Polymorphism
Creo una clase abstracta TipoCliente para reemplazar el if

