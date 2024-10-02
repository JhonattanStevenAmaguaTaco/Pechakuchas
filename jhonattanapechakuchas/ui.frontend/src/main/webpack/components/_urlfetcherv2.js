(function() {
    "use strict";

    // Definimos los selectores basándonos en data attributes
    var selectors = {
        self:      '[data-cmp-is="urlfetcher-v2"]',
        urlInput:  '[data-cmp-hook-urlfetcher="url-input-v2"]',
        fetchButton: '[data-cmp-hook-urlfetcher="fetch-button-v2"]',
        responseTextArea: '[data-cmp-hook-urlfetcher="textarea-result-v2"]'
    };

    function UrlFetcherV2(config) {

        function init(config) {
            // Evitamos la inicialización múltiple quitando el atributo data-cmp-is
            config.element.removeAttribute("data-cmp-is");

            var urlInputElement = config.element.querySelector(selectors.urlInput);
            var fetchButtonElement = config.element.querySelector(selectors.fetchButton);
            var responseTextAreaElement = config.element.querySelector(selectors.responseTextArea);

            if (fetchButtonElement) {
                fetchButtonElement.addEventListener('click', function() {
                    var url = urlInputElement ? urlInputElement.value : '';
                    if (url) {
                        fetch(url)
                            .then(response => response.text())
                            .then(data => {
                                if (responseTextAreaElement) {
                                    responseTextAreaElement.value = data;
                                }
                            })
                            .catch(error => {
                                console.error('Error fetching URL:', error);
                                if (responseTextAreaElement) {
                                    responseTextAreaElement.value = 'Error fetching data';
                                }
                            });
                    }
                });
            }
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        var elements = document.querySelectorAll(selectors.self);
        for (var i = 0; i < elements.length; i++) {
            new UrlFetcherV2({ element: elements[i] });
        }

        var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        var body = document.querySelector("body");
        var observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                var nodesArray = [].slice.call(mutation.addedNodes);
                if (nodesArray.length > 0) {
                    nodesArray.forEach(function(addedNode) {
                        if (addedNode.querySelectorAll) {
                            var elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                            elementsArray.forEach(function(element) {
                                new UrlFetcherV2({ element: element });
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
