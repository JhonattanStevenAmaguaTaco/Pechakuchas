(function() {
    "use strict";

    // Seleccionamos los elementos del DOM
    var form = document.getElementById('fetch-form');
    var urlInput = document.getElementById('url-input');
    var textArea = document.getElementById('textarea-result');
    var fetchButton = document.getElementById('fetch-button');

    // Función para manejar el clic en el botón de envío
    fetchButton.addEventListener('click', function(event) {
        event.preventDefault(); // Evitar que el botón realice el comportamiento por defecto

        var url = urlInput.value.trim(); // Obtener la URL ingresada

        if (url) {
            // Realizamos la solicitud GET a la URL ingresada
            fetch(url)
                .then(function(response) {
                    if (response.ok) {
                        return response.text(); // Si la respuesta es exitosa, retornar el texto
                    } else {
                        throw new Error('Error: ' + response.status);
                    }
                })
                .then(function(data) {
                    textArea.value = data; // Mostrar el resultado en el textarea
                })
                .catch(function(error) {
                    textArea.value = "Error fetching data: " + error.message; // Mostrar el error en el textarea
                });
        } else {
            textArea.value = "Please enter a valid URL."; // Mostrar un mensaje si la URL está vacía
        }
    });
})();
