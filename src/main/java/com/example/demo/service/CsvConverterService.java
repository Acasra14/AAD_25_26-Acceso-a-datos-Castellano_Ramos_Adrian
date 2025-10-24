package com.example.demo.service;

import com.example.demo.model.Alumn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para convertir archivos CSV a JSON y XML
 */
@Service
public class CsvConverterService {

    public void convertirCsv(String csvPath, String jsonPath, String xmlPath) throws Exception {
        List<Alumn> alumns = leerCsv(csvPath);
        escribirJson(alumns, jsonPath);
        escribirXml(alumns, xmlPath);
    }

    /**
     * Lee un archivo CSV y lo convierte a una lista de alumnos
     * @param csvPath Ruta del archivo CSV
     * @return Lista de alumnos
     * @throws Exception Si el archivo no existe o tiene formato incorrecto
     */
    private List<Alumn> leerCsv(String csvPath) throws Exception {
        List<Alumn> alumns = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(csvPath));

        // Saltar cabecera y procesar cada línea
        for (int i = 1; i < lineas.size(); i++) {
            String[] datos = lineas.get(i).split(",");
            if (datos.length == 3) {
                alumns.add(new Alumn(
                        Integer.parseInt(datos[0].trim()),
                        datos[1].trim(),
                        Double.parseDouble(datos[2].trim())
                ));
            }
        }
        return alumns;
    }

    /**
     * Escribe la lista de alumnos en formato JSON
     * @param alumns Lista de alumnos a escribir
     * @param jsonPath Ruta del archivo JSON de salida
     * @throws Exception Si hay error al escribir el archivo
     */
    private void escribirJson(List<Alumn> alumns, String jsonPath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(jsonPath), alumns);
    }

    /**
     * Escribe la lista de alumnos en formato XML
     * @param alumns Lista de alumnos a escribir
     * @param xmlPath Ruta del archivo XML de salida
     * @throws Exception Si hay error al escribir el archivo
     */
    private void escribirXml(List<Alumn> alumns, String xmlPath) throws Exception {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(xmlPath), alumns);
    }
}