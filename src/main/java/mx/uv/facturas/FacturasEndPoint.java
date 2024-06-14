package mx.uv.facturas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import mx.uv.t4is.saludos.GenerarFacturaRequest;
import mx.uv.t4is.saludos.GenerarFacturaResponse;
import mx.uv.t4is.saludos.RecuperarFacturarRequest;
import mx.uv.t4is.saludos.RecuperarFacturarResponse;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Endpoint
public class FacturasEndPoint {

    ArrayList<GenerarFacturaRequest> facturasGeneradas = new ArrayList<>();
    ArrayList<GenerarFacturaResponse> facturasGenerada = new ArrayList<>();

    @Autowired
    private IFacturas ifacturadores;

    int contadorFactura = 1;

    @PayloadRoot(localPart = "GenerarFacturaRequest", namespace = "t4is.uv.mx/saludos")
    @ResponsePayload
    public GenerarFacturaResponse crearFactura(@RequestPayload GenerarFacturaRequest peticion) {
        GenerarFacturaResponse respuesta = new GenerarFacturaResponse();
        
        // Simulando generación de datos UDDI, sello y certificado
        String UDDI = UUID.randomUUID().toString();
        String sello = "d0SQvkr48CwmQIYq/EirTTkUwzBdSgNLgFYZSHjKPGs+lQDZnvHVAU/gkvs2PoCsniSW4NHi42l8TRP6oRhXbeO6N3evg/Mr+9+0PdqyaAUuLAXNoPFadMF3prahU7UwEcUrBoJNa5yZQzQZbbNoBJM";
        String certificado = "MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVE";
        
        respuesta.setUDDI(UDDI);
        respuesta.setVersion(4.0);
        respuesta.setSerie(1.0);
        respuesta.setTipoDeComprobante("I");    
        respuesta.setExportacion("01");
        respuesta.setLugarExpedicion("Xalapa, Veracruz");
        respuesta.setCertificado(certificado);
        respuesta.setSello(sello);
        respuesta.setRegimenFiscalEmisor("625");
        respuesta.setRfcEmisor("EKU9003173C9");
        respuesta.setRegimenFiscalReceptor(peticion.getRegimenFiscalReceptor());
        respuesta.setDomicilioFiscalReceptor(peticion.getDomicilioFiscalReceptor());
        respuesta.setFecha(peticion.getFecha());
        respuesta.setPrecioUnitario(peticion.getPrecioUnitario());
        respuesta.setCantidad(peticion.getCantidad());
        respuesta.setSubtotal(peticion.getSubtotal());
        respuesta.setTotal(peticion.getSubtotal() + (peticion.getSubtotal() * 0.16)); // ejemplo de cálculo de total con IVA
        respuesta.setNombre(peticion.getNombre());
        respuesta.setFormaDePago(peticion.getFormaDePago());
        respuesta.setRfc(peticion.getRfc());
        respuesta.setDescripcion(peticion.getDescripcion());

        //Datos a pedir para guardar en la base de datos
        Facturadores facturas =new Facturadores();
        facturas.setId(contadorFactura);
        facturas.setUDDI(UDDI);
        facturas.setVersion(4.0);
        facturas.setSerie(1.0);
        facturas.setTipo_de_comprobante("I");
        facturas.setExportacion("01");
        facturas.setLugarExpedicion("Xalapa, Veracruz");
        facturas.setCertificado(certificado);
        facturas.setSello(sello);
        facturas.setRegimenFiscalEmisor("625");
        facturas.setRfcEmisor("EKU9003173C9");
        facturas.setRegimenFiscalReceptor(peticion.getRegimenFiscalReceptor());
        facturas.setDomicilioFiscalReceptor(peticion.getDomicilioFiscalReceptor());
        facturas.setFecha(peticion.getFecha());
        facturas.setPrecio_unitario(peticion.getPrecioUnitario());
        facturas.setCantidad(peticion.getCantidad());
        facturas.setSubtotal(peticion.getSubtotal());
        facturas.setTotal(peticion.getSubtotal() + (peticion.getSubtotal() * 0.16)); // ejemplo de cálculo de total con IVA
        facturas.setNombre(peticion.getNombre());
        facturas.setForma_de_pago(peticion.getFormaDePago());
        facturas.setRfc(peticion.getRfc());
        facturas.setDescripcion(peticion.getDescripcion());

        ifacturadores.save(facturas);
        facturasGenerada.add(respuesta);
        contadorFactura++;

        return respuesta;
    }

    @PayloadRoot(localPart = "RecuperarFacturarRequest", namespace = "t4is.uv.mx/saludos")
    @ResponsePayload
    public RecuperarFacturarResponse recuperarFactura(@RequestPayload RecuperarFacturarRequest peticion) {
        String uudi = peticion.getUDDI();
        RecuperarFacturarResponse respuesta = new RecuperarFacturarResponse();
        
        // Consulta en la base de datos
        Optional<Facturadores> optionalFactura = ifacturadores.findByUDDI(uudi);
        

         if (optionalFactura.isPresent()) {
            Facturadores factura = optionalFactura.get();
            respuesta.setUDDI(factura.getUDDI());
            respuesta.setVersion(factura.getVersion());
            respuesta.setSerie(factura.getSerie());
            respuesta.setTipoDeComprobante(factura.getTipo_de_comprobante());
            respuesta.setExportacion(factura.getExportacion());
            respuesta.setLugarExpedicion(factura.getLugarExpedicion());
            respuesta.setCertificado(factura.getCertificado());
            respuesta.setSello(factura.getSello());
            respuesta.setRegimenFiscalEmisor(factura.getRegimenFiscalEmisor());
            respuesta.setRfcEmisor(factura.getRfcEmisor());
            respuesta.setRegimenFiscalReceptor(factura.getRegimenFiscalReceptor());
            respuesta.setDomicilioFiscalReceptor(factura.getDomicilioFiscalReceptor());
            respuesta.setFecha(factura.getFecha());
            respuesta.setPrecioUnitario(factura.getPrecio_unitario());
            respuesta.setCantidad(factura.getCantidad());
            respuesta.setSubtotal(factura.getSubtotal());
            respuesta.setTotal(factura.getTotal());
            respuesta.setNombre(factura.getNombre());
            respuesta.setFormaDePago(factura.getForma_de_pago());
            respuesta.setRfc(factura.getRfc());
            respuesta.setDescripcion(factura.getDescripcion());
        } else {
            System.out.println("Error");
        }
        return respuesta;
    }
}
