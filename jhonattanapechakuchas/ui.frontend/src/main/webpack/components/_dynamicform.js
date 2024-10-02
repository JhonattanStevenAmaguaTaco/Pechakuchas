(function() {
    "use strict";

    // Selectores del componente Dynamic Form
    var selectors = {
        self:      '[data-cmp-is="dynamicform"]', // Selector para el componente
        name:      '[data-cmp-hook-dynamicform="name"]', // Campo "Name"
        city:      '[data-cmp-hook-dynamicform="city"]', // Campo "City"
        phone:     '[data-cmp-hook-dynamicform="phone"]', // Campo "Phone"
        submit:    '[data-cmp-hook-dynamicform="submit"]' // Botón de envío
    };

    function DynamicForm(config) {

        function init(config) {
            config.element.removeAttribute("data-cmp-is");

            var nameField = config.element.querySelector(selectors.name);
            var cityField = config.element.querySelector(selectors.city);
            var phoneField = config.element.querySelector(selectors.phone);
            var submitButton = config.element.querySelector(selectors.submit);

            // Función para manejar el envío del formulario
            function handleSubmit() {
                var data = {
                    name: nameField.value,
                    city: cityField.value,
                    phone: phoneField.value
                };

                // Enviar la solicitud con el método especificado (POST, PUT, DELETE, etc.)
                fetch(config.element.getAttribute('action'), {
                    method: config.element.getAttribute('method'), // Obtener el método del formulario
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    console.log('Success:', data);
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
            }

            // Añadir evento de clic al botón de envío
            submitButton.addEventListener('click', handleSubmit);
        }

        if (config && config.element) {
            init(config);
        }
    }

    // Iniciar el componente Dynamic Form cuando el DOM esté listo
    function onDocumentReady() {
        var elements = document.querySelectorAll(selectors.self);
        for (var i = 0; i < elements.length; i++) {
            new DynamicForm({ element: elements[i] });
        }

        var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        var body             = document.querySelector("body");
        var observer         = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                var nodesArray = [].slice.call(mutation.addedNodes);
                if (nodesArray.length > 0) {
                    nodesArray.forEach(function(addedNode) {
                        if (addedNode.querySelectorAll) {
                            var elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                            elementsArray.forEach(function(element) {
                                new DynamicForm({ element: element });
                            });
                        }
                    });
                }
            });
        });

        observer.observe(body, {
            subtree: true,
            childList: true,
            characterData: true
        });
    }

    if (document.readyState !== "loading") {
        onDocumentReady();
    } else {
        document.addEventListener("DOMContentLoaded", onDocumentReady);
    }

}());
