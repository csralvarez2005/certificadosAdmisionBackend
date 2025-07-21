package certificadosAdmisionBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteDto {
    private String estudiante;
    private String codigo;
    private String email;
    private String programaTecnico;
    private int semestre;
    private String horario;
    private String fechaLiquidacion;
    private String referencia;
    private String estadoLiquidacion;
    private String conceptoFacturacion;
    private int conceptoId;
}
