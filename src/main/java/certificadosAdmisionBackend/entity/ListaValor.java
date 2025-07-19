package certificadosAdmisionBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "lista_valor", schema = "dbo", catalog = "financiero")
@Data
public class ListaValor {

    @Id
    private Long codigo;
    private String categoria;
    private String estado;
    private String referencia;
    private String valor;
}
