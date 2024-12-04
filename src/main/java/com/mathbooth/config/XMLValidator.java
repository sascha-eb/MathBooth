 package com.mathbooth.config;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XMLValidator {
    public static void validate(String xmlFilePath, String xsdFilePath) {
        try (InputStream xsdStream = XMLValidator.class.getClassLoader().getResourceAsStream(xsdFilePath);
             InputStream xmlStream = XMLValidator.class.getClassLoader().getResourceAsStream(xmlFilePath)) {

            if (xsdStream == null) {
                throw new RuntimeException("XSD file not found: " + xsdFilePath);
            }
            if (xmlStream == null) {
                throw new RuntimeException("XML file not found: " + xmlFilePath);
            }

            byte[] xmlData = xmlStream.readAllBytes();
            XMLValidator.validateAgainstXSD(xmlData, xsdStream);

            //System.out.println("XML file is valid!");

        } catch (Exception e) {
            throw new RuntimeException("XML Validation failed: " + e.getMessage(), e);
        }
    }

    private static void validateAgainstXSD(byte[] xmlData, InputStream xsdStream) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(xsdStream));
        Validator validator = schema.newValidator();

        validator.validate(new StreamSource(new ByteArrayInputStream(xmlData)));
    }
}