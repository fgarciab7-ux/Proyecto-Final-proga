// URL base de la API REST (backend Spring Boot)
const API_BASE = "http://localhost:8080/api";
const MAX_POR_PRODUCTO = 10;

function getCodUsuario() {
    let cod = localStorage.getItem("codUsuario");
    if (!cod) {
        cod = crypto.randomUUID();
        localStorage.setItem("codUsuario", cod);
    }
    return cod;
}

async function obtenerProductos() {
    const res = await fetch(`${API_BASE}/productos`);
    if (!res.ok) throw new Error("Error al obtener productos");
    return res.json();
}

async function obtenerCarrito() {
    const cod = getCodUsuario();
    const res = await fetch(`${API_BASE}/carrito/${cod}`);
    if (!res.ok) throw new Error("Error al obtener el carrito");
    return res.json();
}

async function agregarAlCarrito(idProducto, cantidad = 1) {
    const cod = getCodUsuario();
    const res = await fetch(`${API_BASE}/carrito/${cod}/items`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ idProducto, cantidad })
    });
    if (!res.ok) throw new Error("Error al agregar producto al carrito");
    return res.json();
}

async function eliminarItemCarrito(idShoppingCart) {
    const res = await fetch(`${API_BASE}/carrito/items/${idShoppingCart}`, {
        method: "DELETE"
    });
    if (!res.ok) throw new Error("Error al eliminar el item");
    return res.json();
}

async function vaciarCarrito() {
    const cod = getCodUsuario();
    const res = await fetch(`${API_BASE}/carrito/${cod}`, {
        method: "DELETE"
    });
    if (!res.ok) throw new Error("Error al vaciar el carrito");
    return res.json();
}
async function actualizarCantidadItem(idShoppingCart, cantidad) {
    const res = await fetch(`${API_BASE}/carrito/items/${idShoppingCart}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cantidad })
    });
    if (!res.ok) throw new Error("Error al actualizar la cantidad");
    return res.json();
}
