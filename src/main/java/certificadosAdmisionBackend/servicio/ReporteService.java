package certificadosAdmisionBackend.servicio;

import java.util.Optional;

public interface ReporteService {

    Optional<byte[]> obtenerReportePdfPorId(Long id);
    Long generarConstanciaEstudioPorId(Integer estudianteId);
    Long generarConstanciaNotasPorCodigoYNivel(String codigoEstudiante, Integer nivel, String cuerpo,String infoPrograma);
    Long generarCertificadoBuenaConductaPorId(Integer estudianteId);
}
