package certificadosAdmisionBackend.dto;


import java.util.Date;

public class EstudianteDto {
    private int id;
    private String estudiante;
    private String codigo;
    private String email;
    private String programaTecnico;
    private int semestre;
    private String horario;
    private Date fechaLiquidacion;
    private String referencia;
    private String estadoLiquidacion;
    private String conceptoFacturacion;
    private int conceptoId;
    private String tipoDocumento;
    private Integer nivel;
    private String asignatura;
    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double notaDefinitiva;
    private String lugarExpedicionDocumento;


    public EstudianteDto() {
    }

    public EstudianteDto(int id, String estudiante, String codigo, String email, String programaTecnico, int semestre, String horario, Date fechaLiquidacion, String referencia, String estadoLiquidacion, String conceptoFacturacion, int conceptoId, String tipoDocumento, Integer nivel, String asignatura, Double nota1, Double nota2, Double nota3, Double notaDefinitiva, String lugarExpedicionDocumento) {
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
        this.nivel = nivel;
        this.asignatura = asignatura;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
        this.notaDefinitiva = notaDefinitiva;
        this.lugarExpedicionDocumento = lugarExpedicionDocumento;
    }

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

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Date fechaLiquidacion) {
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

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public Double getNota3() {
        return nota3;
    }

    public void setNota3(Double nota3) {
        this.nota3 = nota3;
    }

    public Double getNotaDefinitiva() {
        return notaDefinitiva;
    }

    public void setNotaDefinitiva(Double notaDefinitiva) {
        this.notaDefinitiva = notaDefinitiva;
    }

    public String getLugarExpedicionDocumento() {
        return lugarExpedicionDocumento;
    }

    public void setLugarExpedicionDocumento(String lugarExpedicionDocumento) {
        this.lugarExpedicionDocumento = lugarExpedicionDocumento;
    }
}
