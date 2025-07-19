package certificadosAdmisionBackend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstudianteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> buscarTodosLosEstudiantes() {
        return entityManager.createNativeQuery("""
            SELECT TOP 100
                CONCAT(est.nombre_estudiante, ' ', est.apellido_estudiante) AS estudiante,
                est.codigo AS codigo,
                est.email AS email,
                pro.nombre AS programaTecnico,
                lv.valor AS horario
            FROM financiero.dbo.estudiante est
            INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
            INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
            ORDER BY est.id DESC
        """).getResultList();
    }
}

