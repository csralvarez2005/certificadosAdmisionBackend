package certificadosAdmisionBackend.dto;


public class EstudianteDto {
    private int id;
    private String estudiante;
    private String codigo;
    private String email;
    private String programaTecnico;
    private int semestre;
    private String horario;
    private String fechaLiquidacion;
    private String referencia;
    private String estadoLiquidacion;
    private String conceptoFacturacion;
    private int conceptoId;
    private String tipoDocumento;


    public EstudianteDto() {
    }

    public EstudianteDto(int id, String estudiante, String codigo, String email, String programaTecnico,
                         int semestre, String horario, String fechaLiquidacion, String referencia,
                         String estadoLiquidacion, String conceptoFacturacion, int conceptoId,
                         String tipoDocumento) {
        this.id = id;
        this.estudiante = estudiante;
        this.codigo = codigo;
        this.email = email;
        this.programaTecnico = programaTecnico;
        this.semestre = semestre;
        this.horario = horario;
        this.fechaLiquidacion = fechaLiquidacion;
        this.referencia = referencia;
        this.estadoLiquidacion = estadoLiquidacion;
        this.conceptoFacturacion = conceptoFacturacion;
        this.conceptoId = conceptoId;
        this.tipoDocumento = tipoDocumento;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProgramaTecnico() {
        return programaTecnico;
    }

    public void setProgramaTecnico(String programaTecnico) {
        this.programaTecnico = programaTecnico;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(String fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(String estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public String getConceptoFacturacion() {
        return conceptoFacturacion;
    }

    public void setConceptoFacturacion(String conceptoFacturacion) {
        this.conceptoFacturacion = conceptoFacturacion;
    }

    public int getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(int conceptoId) {
        this.conceptoId = conceptoId;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
