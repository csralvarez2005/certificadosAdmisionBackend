package certificadosAdmisionBackend.repository.postgres;



import certificadosAdmisionBackend.model.postgres.ReportePdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportePdfRepository extends JpaRepository<ReportePdf, Long> {
}
