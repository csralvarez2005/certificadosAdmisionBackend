package certificadosAdmisionBackend.repository;

import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    @Query(value = """
    SELECT top 100
        CONCAT(est.nombre_estudiante, ' ', est.apellido_estudiante) AS estudiante,
        est.codigo AS codigo,
        est.email AS email,
        pro.nombre AS programaTecnico,
        lv.valor AS horario
    FROM financiero.dbo.estudiante est
    INNER JOIN financiero.dbo.programa pro ON est.id_programa = pro.id
    INNER JOIN lista_valor lv ON est.id_horario = lv.codigo
    WHERE est.id_nivel_formacion = 2
    ORDER BY est.id DESC
    """, nativeQuery = true)
    List<EstudianteDto> buscarEstudiantesConNivelFormacionDos();
}
