package certificadosAdmisionBackend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstudianteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> buscarEstudiantesPaginados(int pagina, int tamano) {
        return entityManager.createNativeQuery("""
            SELECT 
                CONCAT(est.nombre_estudiante, ' ', est.apellido_estudiante) AS estudiante,
                est.codigo,
                est.email,
                pro.nombre,
                est.semestre,
                lv.valor
            FROM financiero.dbo.estudiante est
            INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
            INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
            ORDER BY est.id DESC
        """)
                .setFirstResult(pagina * tamano)
                .setMaxResults(tamano)
                .getResultList();
    }

    public long contarTotalEstudiantes() {
        Object result = entityManager.createNativeQuery("""
            SELECT COUNT(*)
            FROM financiero.dbo.estudiante
        """).getSingleResult();
        return ((Number) result).longValue();
    }
}

