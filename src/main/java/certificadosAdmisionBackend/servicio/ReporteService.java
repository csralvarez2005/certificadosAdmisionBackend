package certificadosAdmisionBackend.servicio;

import java.util.Optional;

public interface ReporteService {

    Optional<byte[]> obtenerReportePdfPorId(Long id);
    Long generarConstanciaEstudioPorId(Integer estudianteId);
}
