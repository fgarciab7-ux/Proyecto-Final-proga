document.addEventListener("DOMContentLoaded", async () => {
    const contenedor = document.getElementById("productos");

    try {
        const productos = await obtenerProductos();

        if (productos.length === 0) {
            contenedor.innerHTML = "<p>No hay productos disponibles.</p>";
            return;
        }

        contenedor.innerHTML = productos.map(p => `
            <div class="card">
                <img src="${p.imagen}" alt="${p.nombre}" onerror="this.src='https://via.placeholder.com/200x140?text=Sin+imagen'">
                <h3>${p.nombre}</h3>
                <p>Código: ${p.codigo}</p>
                <p class="precio">Q ${Number(p.precio).toFixed(2)}</p>
                <div class="cantidad-control">
    <button type="button" class="menos">−</button>
    <input type="number" class="cantidad" value="1" min="1" max="${MAX_POR_PRODUCTO}">
    <button type="button" class="mas">+</button>
</div>  
<button data-id="${p.idProducto}">Agregar al carrito</button>
            </div>
        `).join("");

        contenedor.querySelectorAll(".card").forEach(card => {
            const input = card.querySelector("input.cantidad");

            // Mantiene el valor entre 1 y MAX_POR_PRODUCTO
            const fijar = valor => {
                let n = parseInt(valor, 10);
                if (isNaN(n) || n < 1) n = 1;
                if (n > MAX_POR_PRODUCTO) n = MAX_POR_PRODUCTO;
                input.value = n;
                return n;
            };

            card.querySelector("button.menos").addEventListener("click", () => fijar(Number(input.value) - 1));
            card.querySelector("button.mas").addEventListener("click", () => fijar(Number(input.value) + 1));
            input.addEventListener("change", () => fijar(input.value));

            card.querySelector("button[data-id]").addEventListener("click", async e => {
                const cantidad = fijar(input.value);
                try {
                    await agregarAlCarrito(Number(e.currentTarget.dataset.id), cantidad);
                    mostrarMensaje(cantidad === 1
                        ? "Producto agregado al carrito"
                        : `${cantidad} unidades agregadas al carrito`);
                    input.value = 1;
                } catch (err) {
                    mostrarMensaje(err.message, "error");
                }
            });
        });
    } catch (e) {
        contenedor.innerHTML = `<p>Error al cargar los productos: ${e.message}</p>`;
    }
});

function mostrarMensaje(texto, tipo = "exito") {
    const div = document.getElementById("flash");
    div.className = `flash ${tipo}`;
    div.textContent = texto;
    div.style.display = "block";
    setTimeout(() => { div.style.display = "none"; }, 2500);
}
