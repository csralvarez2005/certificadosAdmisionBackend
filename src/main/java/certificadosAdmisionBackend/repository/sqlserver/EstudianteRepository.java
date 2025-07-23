package certificadosAdmisionBackend.repository.sqlserver;

import certificadosAdmisionBackend.dto.EstudianteDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EstudianteRepository {

    @PersistenceContext(unitName = "sqlServer") // 🔥 especificamos la unidad de persistencia
    private EntityManager entityManager;

    // ✅ ESTE método debe estar presente
    public Optional<EstudianteDto> buscarPorId(int estudianteId) {
        List<Object[]> resultados = entityManager.createNativeQuery("""
        SELECT 
            est.id,
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
        WHERE est.id = :estudianteId
        ORDER BY lc.fecha_liquidacion DESC
    """)
                .setParameter("estudianteId", estudianteId)
                .setMaxResults(1) // trae solo el último
                .getResultList();

        if (resultados.isEmpty()) return Optional.empty();

        Object[] r = resultados.get(0);
        EstudianteDto dto = new EstudianteDto(
                ((Number) r[0]).intValue(), // id
                (String) r[4],              // estudiante
                (String) r[5],              // codigo
                (String) r[6],              // email
                (String) r[9],              // programaTecnico
                ((Number) r[7]).intValue(), // semestre
                (String) r[8],              // horario
                (String) r[1],              // fechaLiquidacion
                (String) r[2],              // referencia
                (String) r[3],              // estadoLiquidacion
                (String) r[10],             // conceptoFacturacion
                ((Number) r[11]).intValue() // conceptoId
        );

        return Optional.of(dto);
    }


    public long contarTotalEstudiantes() {
        Object result = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT est.id)
            FROM financiero.dbo.estudiante est
            INNER JOIN financiero.dbo.liquidacion_concepto lc ON lc.id_estudiante = est.id
            INNER JOIN financiero.dbo.liquidacion_concepto_detalle lcd ON lc.id = lcd.id_liquidacion
            WHERE lcd.id_concepto IN (21, 22, 20)
        """).getSingleResult();

        return ((Number) result).longValue();
    }
}

