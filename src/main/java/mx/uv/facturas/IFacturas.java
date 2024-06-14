package mx.uv.facturas;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface IFacturas extends CrudRepository <Facturadores,Integer>{
    Optional<Facturadores> findByUDDI(String UDDI);
}
