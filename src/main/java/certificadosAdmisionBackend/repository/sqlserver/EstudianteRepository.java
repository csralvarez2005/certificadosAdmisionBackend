package certificadosAdmisionBackend.repository.sqlserver;


import certificadosAdmisionBackend.dto.EstudianteDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public class EstudianteRepository {

    @PersistenceContext(unitName = "sqlServer")
    private EntityManager entityManager;

    public Optional<EstudianteDto> buscarPorId(Integer estudianteId) {
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
            cf.id AS concepto_id,
            lv2.valor AS tipo_documento,
            est.nombre_lugar_expedicion_documento
        FROM financiero.dbo.estudiante est
        INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
        INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
        INNER JOIN financiero.dbo.liquidacion_concepto lc ON lc.id_estudiante = est.id
        INNER JOIN financiero.dbo.liquidacion_concepto_detalle lcd ON lc.id = lcd.id_liquidacion
        INNER JOIN financiero.dbo.concepto_facturacion cf ON lcd.id_concepto = cf.id
        INNER JOIN financiero.dbo.lista_valor lv2 ON est.id_tipo_identificacion = lv2.codigo
        WHERE est.id = :estudianteId
    """)
                .setParameter("estudianteId", estudianteId)
                .getResultList();

        if (resultados.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapearAFilaEstudiante(resultados.get(0)));
    }


    public List<EstudianteDto> buscarTodosPaginado(int page, int size) {
        int offset = page * size;

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
                cf.id AS concepto_id,
                lv2.valor AS tipo_documento,
                est.nombre_lugar_expedicion_documento
            FROM financiero.dbo.estudiante est
            INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
            INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
            INNER JOIN financiero.dbo.liquidacion_concepto lc ON lc.id_estudiante = est.id
            INNER JOIN financiero.dbo.liquidacion_concepto_detalle lcd ON lc.id = lcd.id_liquidacion
            INNER JOIN financiero.dbo.concepto_facturacion cf ON lcd.id_concepto = cf.id
            INNER JOIN financiero.dbo.lista_valor lv2 ON est.id_tipo_identificacion = lv2.codigo
            WHERE lcd.id_concepto IN (21, 22, 20)
            ORDER BY lc.fecha_liquidacion DESC
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
        """)
                .setParameter("offset", offset)
                .setParameter("size", size)
                .getResultList();

        return resultados.stream()
                .map(this::mapearAFilaEstudiante)
                .toList();
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

    private EstudianteDto mapearAFilaEstudiante(Object[] r) {
        return new EstudianteDto(
                ((Number) r[0]).intValue(),   // id
                (String) r[4],                // estudiante
                (String) r[5],                // codigo
                (String) r[6],                // email
                (String) r[9],                // programa
                ((Number) r[7]).intValue(),   // semestre
                (String) r[8],                // horario
                parsearFecha((String) r[1]),  // fecha_liquidacion
                (String) r[2],                // referencia
                (String) r[3],                // estado_liquidacion
                (String) r[10],               // concepto_facturacion
                ((Number) r[11]).intValue(),  // concepto_id
                (String) r[12],               // tipo_documento
                null,                         // nivel
                null,                         // asignatura
                null,                         // nota1
                null,                         // nota2
                null,                         // nota3
                null,                         // nota_definitiva
                (String) r[13]                // lugar_exp_documento
        );
    }

    private Date parsearFecha(String fechaTexto) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fechaTexto);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
