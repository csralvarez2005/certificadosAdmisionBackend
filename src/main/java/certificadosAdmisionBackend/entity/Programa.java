package certificadosAdmisionBackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "programa", schema = "dbo", catalog = "financiero")
@Data
public class Programa {
    @Id
    private Long id;
    private String nombre;


}
