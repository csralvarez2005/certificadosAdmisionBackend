package certificadosAdmisionBackend.servicio.impl;


import certificadosAdmisionBackend.dto.EstudianteDto;
import certificadosAdmisionBackend.dto.EstudiantePageResponse;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteRepository;
import certificadosAdmisionBackend.repository.sqlserver.EstudianteNotasRepository;
import certificadosAdmisionBackend.servicio.EstudianteService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteNotasRepository estudianteNotasRepository;

    // Constructor con ambos repositorios inyectados
    public EstudianteServiceImpl(
            EstudianteRepository estudianteRepository,
            EstudianteNotasRepository estudianteNotasRepository
    ) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteNotasRepository = estudianteNotasRepository;
    }

    @Override
    public EstudiantePageResponse listarTodosPaginado(int page, int size) {
        List<EstudianteDto> estudiantes = estudianteRepository.buscarTodosPaginado(page, size);

        long totalRegistros = estudianteRepository.contarTotalEstudiantes();
        int totalPages = (int) Math.ceil((double) totalRegistros / size);

        return new EstudiantePageResponse(
                estudiantes,
                page,
                totalPages,
                totalRegistros
        );
    }

    @Override
    public List<EstudianteDto> obtenerNotasPorCodigo(String codigo) {
        List<EstudianteDto> lista = estudianteNotasRepository.buscarNotasPorEstudiante(codigo);

        System.out.println("Notas encontradas: " + lista.size());
        lista.forEach(nota -> {
            System.out.println("Nivel: " + nota.getNivel() +
                    " | Asignatura: " + nota.getAsignatura() +
                    " | Nota: " + nota.getNotaDefinitiva());
        });

        // Filtrar duplicados
        Set<String> modulosUnicos = new LinkedHashSet<>();
        List<EstudianteDto> filtradas = lista.stream()
                .filter(n -> n.getAsignatura() != null && n.getNotaDefinitiva() != null)
                .filter(n -> modulosUnicos.add(n.getAsignatura().trim() + "|" + n.getNotaDefinitiva()))
                .toList();

        System.out.println("Notas después de filtrar duplicados: " + filtradas.size());

        return filtradas;
    }

    @Override
    public List<EstudianteDto> obtenerNotasPorIdYNivel(Long id, int nivel) {
        // obtener codigo del estudiante
        EstudianteDto est = estudianteRepository.buscarPorId(id.intValue())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado id=" + id));

        List<EstudianteDto> todas = estudianteNotasRepository.buscarNotasPorEstudiante(est.getCodigo());

        // filtrar por nivel y eliminar duplicados por (asignatura|notaDefinitiva)
        Set<String> vistos = new LinkedHashSet<>();
        return todas.stream()
                .filter(n -> Objects.equals(n.getNivel(), nivel))
                .filter(n -> n.getAsignatura() != null && n.getNotaDefinitiva() != null)
                .filter(n -> vistos.add(n.getAsignatura().trim().toUpperCase() + "|" + n.getNotaDefinitiva()))
                .toList();
    }


}