package certificadosAdmisionBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "estudiante", schema = "dbo", catalog = "financiero")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    @Id
    private Long id;

    private String apellido_estudiante;
    private String cargo;
    private String celular;
    private String codigo;
    private String direccion;
    private String email;
    private String empresa;
    private String estado_estudiante;
    private LocalDate fecha_nacimiento;
    private Long id_aspirante;
    private Long id_genero;
    private Long id_horario;
    private Long id_jornada;
    private Long id_modalida;
    private Long id_nivel_formacion;
    private Long id_periodo_academico;
    private Long id_seccional;
    private Long id_solicitud;
    private Long id_tipo_convenio;
    private Long id_tipo_identificacion;
    private String identificacion;
    private String nombre_estudiante;
    private String nombre_lugar_expedicion_documento;
    private String semestre;
    private String telefono;
    private String estado_activo_cartera;
    private String estado_credito;

    @ManyToOne
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    private Programa programa;

    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "codigo", insertable = false, updatable = false)
    private ListaValor horario;
}
