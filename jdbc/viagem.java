
class viagem {
    private int idsistema;
    private LocalTime hinicio;
    private LocalTime hfim;
    private LocalDate dtviagem;
    private int valestimado;
    private int valfinal;
    private int latinicio;
    private int longinicio;
    private int latfim;
    private int longfim;
    private int classificacao;
    private int veiculo;
    private int condutor;
    private LocalDate dtinicio;


    viagem(String values){
        String[] attributos = values.split(","); // ()
        idsistema = Integer.parseInt(attributos[0]);
        hinicio = LocalTime.parse(attributos[1]);
        hfim = LocalTime.parse(attributos[2]);
        dtviagem = LocalDate.parse(attributos[3]);
        valestimado = Integer.parseInt(attributos[4]);
        valfinal = Integer.parseInt(attributos[5]);
        latinicio = Integer.parseInt(attributos[6]);
        longinicio = Integer.parseInt(attributos[7]);
        latfim = Integer.parseInt(attributos[8]);
        longfim = Integer.parseInt(attributos[9]);
        classificacao = Integer.parseInt(attributos[10]);
        condutor = Integer.parseInt(attributos[11]);
        dtinicio = LocalDate.parse(attributos[12]);
    }

    public Integer getIdSistema() { return idsistema; }

    public LocalTime getHInicio() { return hinicio; }

    public LocalTime getHFim() { return hfim; }

    public LocalDate getDtViagem() { return dtviagem; }
    
    public Integer getValEstimado() { return valestimado; }
    
    public Integer getValFinal() { return valfinal; }

    public Integer getLatInicio() { return latinicio; }

    public Integer getLongInicio() { return longinicio; }

    public Integer getLatFim() { return latfim; }

    public Integer getLongFim() { return longfim; }

    public Integer getClassificacao() { return classificacao; }

    public Integer getCondutor() { return condutor; }

    public LocalDate getDtInicio() { return dtinicio; }

    public void setIdSistema(int idsistema) { this.idsistema = idsistema; }

    public void setHInicio(LocalTime hinicio) { this.hinicio = hinicio; }

    public void setHFim(LocalTime hfim) { this.hfim = hfim; }

    public void setDtViagem(LocalDate dtviagem) { this.dtviagem = dtviagem; }

    public void setValEstimado(int valestimado) { this.valestimado = valestimado; }
    
    public void setValFinal(int valfinal) { this.valfinal = valfinal; }

    public void setLatInicio(int latinicio) { this.latinicio = latinicio; }

    public void setLongInicio(int longinicio) { this.longinicio = longinicio; }

    public void setLatFim(int latfim) { this.latfim = latfim; }

    public void setLongFim(int longfim) { this.longfim = longfim; }
 
    public void setClassificacao(int classificacao) { this.classificacao = classificacao; }

    public void setCondutor(int condutor) { this.condutor = condutor; }

    public void setDtInicio(LocalDate dtinicio) { this.dtinicio = dtinicio; }
}