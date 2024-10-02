/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the Licens    e.
 */
package com.jhonattanApechakuchas.core.servlets;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import com.google.gson.Gson;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.servlets.annotations.SlingServletPaths;

@Component(service = { javax.servlet.Servlet.class })
@SlingServletPaths(value = { "/bin/my-simple-servlet" }) // Configuración de la ruta del servlet
@ServiceDescription("Servlet que maneja GET, POST, PUT y DELETE y retorna JSON")
public class SimpleServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    // Clase interna para representar al cliente
    class Client {
        private String name;
        private String city;
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    private Client client = new Client();
    private List<Client> clients = new ArrayList<>();


    // Método para manejar solicitudes GET
    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
        // Convertir el objeto Client a JSON usando Gson
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(clients);

        // Configurar la respuesta como JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Escribir la respuesta JSON
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse);
        out.flush();
    }

    // Método para manejar solicitudes POST
    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
        // Leer el cuerpo JSON de la solicitud
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        String requestBody = stringBuilder.toString();

        // Convertir el JSON a un objeto Client usando Gson
        Gson gson = new Gson();
        Client updatedClient = gson.fromJson(requestBody, Client.class);

        // Actualizar los datos del cliente con los datos recibidos en JSON
        clients.add(updatedClient);
        // Convertir el cliente actualizado de nuevo a JSON para la respuesta
        String jsonResponse = gson.toJson(clients);

        // Configurar la respuesta como JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Escribir la respuesta JSON
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse);
        out.flush();
    }

    // Método para manejar solicitudes PUT
// Método para manejar solicitudes PUT
@Override
protected void doPut(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
    // Leer el cuerpo JSON de la solicitud
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;
    try (BufferedReader reader = req.getReader()) {
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
    }

    String requestBody = stringBuilder.toString();

    // Convertir el JSON a un objeto Client usando Gson
    Gson gson = new Gson();
    Client updatedClient = gson.fromJson(requestBody, Client.class);

    // Verificar si la lista de clientes no está vacía
    if (!clients.isEmpty()) {
        // Buscar al cliente por el nombre
        for (Client existingClient : clients) {
            if (existingClient.getName().equals(updatedClient.getName())) {
                // Actualizar los datos del cliente si están presentes en la solicitud
                if (updatedClient.getCity() != null) {
                    existingClient.setCity(updatedClient.getCity());
                }
                if (updatedClient.getPhone() != null) {
                    existingClient.setPhone(updatedClient.getPhone());
                }

                // Cliente encontrado y actualizado, convertir la lista de clientes a JSON
                String jsonResponse = gson.toJson(clients);

                // Configurar la respuesta como JSON
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                // Escribir la respuesta JSON
                PrintWriter out = resp.getWriter();
                out.write(jsonResponse);
                out.flush();
                return;
            }
        }
    }

    // Si el cliente no se encontró, devolver un mensaje de error
    resp.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
    PrintWriter out = resp.getWriter();
    out.write("{\"error\": \"Client not found\"}");
    out.flush();
}

    // Método para manejar solicitudes DELETE
    @Override
    protected void doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
        // Eliminar (resetear) el objeto cliente
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        client = new Client(); // Crear un nuevo objeto Client vacío
        String requestBody = stringBuilder.toString();

        // Convertir el JSON a un objeto Client usando Gson
        Gson gson = new Gson();
        Client updatedClient = gson.fromJson(requestBody, Client.class);
        for (Client existingClient : clients) {
            if (existingClient.getName().equals( updatedClient.getName())){
                
                // Configurar la respuesta como JSON
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                clients.remove(existingClient);
                // Escribir la respuesta de eliminación exitosa
                PrintWriter out = resp.getWriter();
                out.write("{\"message\": \"Client data deleted successfully\"}");
                out.flush();  
            }
        
        }
    }
}
