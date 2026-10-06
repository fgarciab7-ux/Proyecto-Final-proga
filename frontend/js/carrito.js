document.addEventListener("DOMContentLoaded", async () => {
    await cargarCarrito();

    document.getElementById("vaciar").addEventListener("click", async () => {
        try {
            await vaciarCarrito();
            await cargarCarrito();
        } catch (e) {
            alert(e.message);
        }
    });
});

async function cargarCarrito() {
    const cuerpo = document.getElementById("cuerpo-carrito");
    const totalEl = document.getElementById("total");

    try {
        const items = await obtenerCarrito();

        if (items.length === 0) {
            cuerpo.innerHTML = `<tr><td colspan="5">El carrito está vacío</td></tr>`;
            totalEl.textContent = "Q 0.00";
            return;
        }

        let total = 0;
        cuerpo.innerHTML = items.map(item => {
            total += Number(item.subTotal);
            return `
                <tr>
                    <td>${item.nombre}</td>
                    <td>Q ${Number(item.precio).toFixed(2)}</td>
                    <td>
    <div class="cantidad-control">
        <button type="button" class="menos" data-id="${item.idShoppingCart}" data-cant="${item.cantidad}" ${item.cantidad <= 1 ? "disabled" : ""}>−</button>
        <span class="cantidad-valor">${item.cantidad}</span>
        <button type="button" class="mas" data-id="${item.idShoppingCart}" data-cant="${item.cantidad}">+</button>
    </div>
</td>
                    <td>Q ${Number(item.subTotal).toFixed(2)}</td>
                    <td><button class="eliminar" data-id="${item.idShoppingCart}">Eliminar</button></td>
                </tr>
            `;
        }).join("");

        totalEl.textContent = `Q ${total.toFixed(2)}`;
        cuerpo.querySelectorAll("button.menos, button.mas").forEach(btn => {
            btn.addEventListener("click", async () => {
                const delta = btn.classList.contains("mas") ? 1 : -1;
                try {
                    await actualizarCantidadItem(Number(btn.dataset.id), Number(btn.dataset.cant) + delta);
                    await cargarCarrito();
                } catch (e) {
                    alert(e.message);
                }
            });
        });
        cuerpo.querySelectorAll("button.eliminar").forEach(btn => {
            btn.addEventListener("click", async () => {
                await eliminarItemCarrito(Number(btn.dataset.id));
                await cargarCarrito();
            });
        });
    } catch (e) {
        cuerpo.innerHTML = `<tr><td colspan="5">Error al cargar el carrito: ${e.message}</td></tr>`;
    }
}
