package certificadosAdmisionBackend.repository;

import certificadosAdmisionBackend.dto.EstudianteDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstudianteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<EstudianteDto> buscarEstudiantesPaginados(int pagina, int tamano) {
        List<Object[]> resultados = entityManager.createNativeQuery("""
        SELECT 
            FORMAT(lc.fecha_liquidacion, 'dd/MM/yyyy') AS fecha_liquidacion,
            lc.referencia,
            lc.estado_liquidacion,
            CONCAT(est.nombre_estudiante, ' ', est.apellido_estudiante) AS estudiante,
            est.codigo,
            est.email,
            est.semestre,
            lv.valor AS horario,
            pro.nombre AS programa,
            cf.nombre AS concepto_facturacion,
            cf.id AS concepto_id
        FROM financiero.dbo.estudiante est
        INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
        INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
        INNER JOIN financiero.dbo.liquidacion_concepto lc ON lc.id_estudiante = est.id
        INNER JOIN financiero.dbo.liquidacion_concepto_detalle lcd ON lc.id = lcd.id_liquidacion
        INNER JOIN financiero.dbo.concepto_facturacion cf ON lcd.id_concepto = cf.id
        WHERE lcd.id_concepto IN (22, 21,20)
        ORDER BY lc.fecha_liquidacion DESC
    """)
                .setFirstResult(pagina * tamano)
                .setMaxResults(tamano)
                .getResultList();

        return resultados.stream()
                .map(r -> new EstudianteDto(
                        (String) r[3],
                        (String) r[4],
                        (String) r[5],
                        (String) r[8],
                        ((Number) r[6]).intValue(),
                        (String) r[7],
                        (String) r[0],
                        (String) r[1],
                        (String) r[2],
                        (String) r[9],
                        ((Number) r[10]).intValue()
                ))
                .toList();
    }

    public long contarTotalEstudiantes() {
        Object result = entityManager.createNativeQuery("""
        SELECT COUNT(DISTINCT est.id)
        FROM financiero.dbo.estudiante est
        INNER JOIN financiero.dbo.liquidacion_concepto lc ON lc.id_estudiante = est.id
        INNER JOIN financiero.dbo.liquidacion_concepto_detalle lcd ON lc.id = lcd.id_liquidacion
        WHERE lcd.id_concepto IN (21, 22,20)
    """).getSingleResult();

        return ((Number) result).longValue();
    }
}

