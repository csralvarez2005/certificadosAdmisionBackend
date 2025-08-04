package certificadosAdmisionBackend.repository.sqlserver;

import certificadosAdmisionBackend.dto.EstudianteDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class EstudianteNotasRepository {

    @PersistenceContext(unitName = "sqlServer")
    private EntityManager entityManager;

    public List<EstudianteDto> buscarNotasPorEstudiante(String codigoEstudiante) {
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
                mat.id_nivel AS nivel,
                modu.nombre AS asignatura,
                mat.nota1,
                mat.nota2,
                mat.nota3,
                mat.nota_definitiva,
                est.nombre_lugar_expedicion_documento
            FROM financiero.dbo.estudiante est
            INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
            INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
            INNER JOIN financiero.dbo.liquidacion_concepto lc ON lc.id_estudiante = est.id
            INNER JOIN financiero.dbo.liquidacion_concepto_detalle lcd ON lc.id = lcd.id_liquidacion
            INNER JOIN financiero.dbo.concepto_facturacion cf ON lcd.id_concepto = cf.id
            INNER JOIN financiero.dbo.lista_valor lv2 ON est.id_tipo_identificacion = lv2.codigo
            INNER JOIN matricula.dbo.matricula_academica mat ON mat.id_estudiante = est.id
            INNER JOIN matricula.dbo.modulo modu ON modu.id = mat.id_modulo
            WHERE est.codigo = :codigoEstudiante
            ORDER BY lc.fecha_liquidacion DESC
        """)
                .setParameter("codigoEstudiante", codigoEstudiante)
                .getResultList();

        return resultados.stream()
                .map(this::mapearAFilaEstudianteConNotas)
                .toList();
    }

    private EstudianteDto mapearAFilaEstudianteConNotas(Object[] r) {
        return new EstudianteDto(
                ((Number) r[0]).intValue(),
                (String) r[4],
                (String) r[5],
                (String) r[6],
                (String) r[9],
                ((Number) r[7]).intValue(),
                (String) r[8],
                parsearFecha((String) r[1]),
                (String) r[2],
                (String) r[3],
                (String) r[10],
                ((Number) r[11]).intValue(),
                (String) r[12],
                ((Number) r[13]).intValue(),
                (String) r[14],
                r[15] != null ? ((Number) r[15]).doubleValue() : null,
                r[16] != null ? ((Number) r[16]).doubleValue() : null,
                r[17] != null ? ((Number) r[17]).doubleValue() : null,
                r[18] != null ? ((Number) r[18]).doubleValue() : null,
                (String) r[19]
        );
    }

    private Date parsearFecha(String fechaTexto) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fechaTexto);
        } catch (ParseException e) {
            return null;
        }
    }
}